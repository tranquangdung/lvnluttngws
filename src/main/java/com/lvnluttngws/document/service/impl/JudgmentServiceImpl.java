package com.lvnluttngws.document.service.impl;

import com.lvnluttngws.document.common.ESConstant;
import com.lvnluttngws.document.model.*;
import com.lvnluttngws.document.repository.JudgmentEsRepository;
import com.lvnluttngws.document.repository.JudgmentRepository;
import com.lvnluttngws.document.service.JudgmentService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/*import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.highlight.HighlightField;*/

@Service
public class JudgmentServiceImpl implements JudgmentService {
    private static final Logger logger = LoggerFactory.getLogger(JudgmentServiceImpl.class);

    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Autowired
    private JudgmentEsRepository judgmentEsRepository;

    @Autowired
    private JudgmentRepository judgmentRepository;

    @Override
    public void reindexDB() {
        logger.debug("### reindexDB: START ###");
        // 1. Delete old index
        esTemplate.deleteIndex(JudgmentEs.class);
        esTemplate.createIndex(JudgmentEs.class);
        esTemplate.putMapping(JudgmentEs.class);
        esTemplate.refresh(JudgmentEs.class);
        // 2. Get all record form DB -> indexing
        List<Judgment> judgments = judgmentRepository.findAll();
        if (!CollectionUtils.isEmpty(judgments)) {
            for (Judgment judgment : judgments) {
                indexing(judgment);
            }
        }
        esTemplate.refresh(JudgmentEs.class);
        logger.debug("### reindexDB: END ###");
    }

    private void checkIndex() {
        if (!esTemplate.indexExists(JudgmentEs.class)) {
            esTemplate.createIndex(JudgmentEs.class);
            esTemplate.putMapping(JudgmentEs.class);
            esTemplate.refresh(JudgmentEs.class);
        }
    }

    private void indexing(Judgment judgment) {
        JudgmentEs judgmentEs = new JudgmentEs();
        judgmentEs.setId(judgment.getId() + "");
        judgmentEs.setFileName(judgment.getFileName());
        judgmentEs.setContent(new String(judgment.getContent(), StandardCharsets.UTF_8));
        judgmentEsRepository.save(judgmentEs);
    }

    @Override
    public Judgment addRecord(Judgment judgment) {
        logger.debug("### addRecord: START ###");
        // 1.0 Add to DB
        Judgment judg = judgmentRepository.save(judgment);

        // 2.0 Add to Elastic
        checkIndex();
        indexing(judg);
        esTemplate.refresh(JudgmentEs.class);
        logger.debug("### addRecord: END ###");
        return judgment;
    }

    @Override
    public ResultContainer search(SearchInput searchInput) {
        logger.debug("### search: START ###");
        logger.info("Search with keyword: " + searchInput.getInputText());
        ResultContainer resultContainer = new ResultContainer();
        List<Result> results = new ArrayList<>();
        if (Strings.isEmpty(searchInput.getInputText())) {
            logger.debug("Keyword list is blank");
            return resultContainer;
        }

        SearchResponse searchResponse = esTemplate.getClient().prepareSearch(ESConstant.ES_INDEX_NAME)
                .setTypes(ESConstant.ES_TYPE).setQuery(QueryBuilders.matchAllQuery())
                .highlighter(new HighlightBuilder().field("content")
                        .preTags("<span style='background-color: #FFFF00'>").postTags("</span>"))
                .execute().actionGet();
        SearchHits hits = searchResponse.getHits();
        logger.info("Total hits: " + hits.totalHits);
        for (SearchHit hit : hits.getHits()) {
            if (hit != null) {
                Result res = new Result();
                logger.info("Found ID: " + hit.getId());
                res.setId(hit.getId());
                res.setHighLight(getHighLight(hit));
                float documentScore = hit.getScore();
                res.setScore(documentScore);
                logger.info("documentScore: " + documentScore);
                results.add(res);
            }
        }

        /*// TODO: using custom tokenizer to analyze input text



        /*SearchResponse searchResponse = esTemplate.getClient().prepareSearch("test").setTypes("test")
                .setQuery(QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery()).should(QueryBuilders
                        .termQuery("name", "arbitrary")))
                .highlighter(
                        new HighlightBuilder().highlighterType("test-custom").field("name").field("other_name").field("other_other_name")
                                .useExplicitFieldOrder(true))
                .get();*/

        /*// TODO: using custom tokenizer to analyze input text
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(ESConstant.ES_INDEX_NAME)
                .withTypes(ESConstant.ES_TYPE)
                .withQuery(QueryBuilders.matchQuery("content", searchInput.getInputText()))
                .withHighlightFields(new HighlightBuilder.Field("content")
                        .preTags("<span style='background-color: #FFFF00'>")
                        .postTags("</span>"))
                .withPageable(new PageRequest(searchInput.getIndexFrom(), searchInput.getIndexTo()))
                // .withMinScore(ESConstant.ES_MIN_RESULT_THRESOLD)
                .build();
        SearchResult result = esTemplate.query(searchQuery, new ResultsExtractor<SearchResult>() {
            @Override
            public SearchResult extract(SearchResponse response) {
                long totalHits = response.getHits().totalHits();
                logger.info("Total records: " + totalHits);
                resultContainer.setTotalHits(totalHits);
                for (SearchHit hit : response.getHits()) {
                    if (hit != null) {
                        Result res = new Result();
                        logger.info("Found ID: " + hit.getId());
                        res.setId(hit.getId());
                        res.setHighLight(getHighLight(hit));
                        float documentScore = hit.getScore();
                        res.setScore(documentScore);
                        logger.info("documentScore: " + documentScore);
                        results.add(res);
                    }
                }
                return new SearchResult(null, totalHits, null);
            }
        });*/
        resultContainer.setResult(results);
        logger.debug("### search: END ###");
        return resultContainer;
    }

    private List<String> getHighLight(SearchHit hit) {
        List<String> highLight = new ArrayList<>();
        Collection<HighlightField> highlightFields = hit.getHighlightFields().values();
        for (final HighlightField entry : highlightFields) {
            Text[] frags = entry.fragments();
            for (final Text t : frags) {
                highLight.add(t.toString());
                System.out.println(t.toString());
            }
        }
        return highLight;
    }
}
