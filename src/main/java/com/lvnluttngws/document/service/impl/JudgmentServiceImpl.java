package com.lvnluttngws.document.service.impl;

import com.lvnluttngws.document.model.*;
import com.lvnluttngws.document.repository.JudgmentEsRepository;
import com.lvnluttngws.document.repository.JudgmentRepository;
import com.lvnluttngws.document.service.JudgmentService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.naming.directory.SearchResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
public class JudgmentServiceImpl implements JudgmentService {
    private static final Logger logger = LoggerFactory.getLogger(JudgmentServiceImpl.class);

    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Autowired
    private JudgmentEsRepository judgmentEsRepository;

    @Autowired
    private JudgmentRepository judgmentRepository;

    @Autowired
    private ElasticsearchOperations es;

    @Override
    public void reindexDB() {
        // 1. Delete old index
        esTemplate.deleteIndex(JudgmentEs.class);
        esTemplate.createIndex(JudgmentEs.class);
        // 2. Get all record form DB -> indexing
        List<Judgment> judgments = judgmentRepository.findAll();
        if (!CollectionUtils.isEmpty(judgments)) {
            for (Judgment judgment : judgments) {
                indexing(judgment);
            }
        }
    }

    private void indexing(Judgment judgment) {
        JudgmentEs judgmentEs = new JudgmentEs();
        judgmentEs.setId(judgment.getId() + "");
        judgmentEs.setFileName(judgment.getFileName());
        judgmentEs.setContent(Arrays.toString(judgment.getContent()));
        judgmentEsRepository.save(judgmentEs);
    }

    @Override
    public Judgment addRecord(Judgment judgment) {
        logger.debug("### addRecord: START ###");
        // 1.0 Add to DB
        Judgment judg = judgmentRepository.save(judgment);

        // 2.0 Add to Elastic
        indexing(judg);
        logger.debug("### addRecord: END ###");
        return judgment;
    }

    @Override
    public ResultContainer search(SearchInput searchInput) {

        ResultContainer resultContainer = new ResultContainer();
        List<Result> results = new ArrayList<>();
        if (Strings.isEmpty(searchInput.getInputText())) {
            logger.debug("Keyword list is blank");
            return resultContainer;
        }

        // TODO: using custom tokenizer to analyze input text
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices("lvnluttngidx")
                .withTypes("judgmenttype")
                .withQuery(QueryBuilders.matchQuery("content", searchInput.getInputText()))
                .withHighlightFields(new HighlightBuilder.Field("content")
                        .preTags("<span style='background-color: #FFFF00'>")
                        .postTags("</span>"))
                .withPageable(new PageRequest(searchInput.getIndexFrom(), searchInput.getIndexTo()))
                .build();
        SearchResult result = es.query(searchQuery, new ResultsExtractor<SearchResult>() {
            @Override
            public SearchResult extract(SearchResponse response) {
                long totalHits = response.getHits().totalHits();
                for (SearchHit hit : response.getHits()) {
                    if (hit != null) {
                        Result res = new Result();
                        logger.info("Found ID: " + hit.getId());
                        res.setId(hit.getId());
                        res.setHighLight(getHighLight(hit));
                        float documentScore = hit.getScore();
                        res.setScore(documentScore);
                        results.add(res);
                        System.out.println("documentScore: " + documentScore);
                    }
                }
                return new SearchResult(null, totalHits, null);
            }
        });
        resultContainer.setResult(results);
        return resultContainer;
    }

    private List<String> getHighLight(SearchHit hit) {
        List<String> highLight = new ArrayList<>();
        Collection<HighlightField> highlightFields = hit.getHighlightFields().values();
        for (final HighlightField entry : highlightFields) {
            Text[] frags = entry.fragments();
            for (final Text t : frags) {
                highLight.add(t.toString());
            }
        }
        return highLight;
    }
}
