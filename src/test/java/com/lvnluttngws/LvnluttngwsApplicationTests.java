package com.lvnluttngws;

import com.lvnluttngws.document.model.JudgmentEs;
import com.lvnluttngws.document.model.SearchInput;
import com.lvnluttngws.document.repository.JudgmentEsRepository;
import com.lvnluttngws.document.service.JudgmentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LvnluttngwsApplicationTests {
    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Autowired
    private JudgmentEsRepository judgmentEsRepository;

    @Autowired
    private JudgmentService judgmentService;

    @Before
    public void before() {

        esTemplate.deleteIndex(JudgmentEs.class);
        esTemplate.createIndex(JudgmentEs.class);
        esTemplate.putMapping(JudgmentEs.class);
        esTemplate.refresh(JudgmentEs.class);
    }

    @Test
    public void testFindDocumentNameOrContent() {
        JudgmentEs judgmentEs = new JudgmentEs(1 + "", "Việt Nam document.pdf", "Việt Nam là một nước nông nghiệp");
        judgmentEsRepository.save(judgmentEs);
        JudgmentEs judgmentEs1 = new JudgmentEs(2 + "", "Document1.pdf", "Việt Nam là một nước nông nghiệp");
        judgmentEsRepository.save(judgmentEs1);
        JudgmentEs judgmentEs2 = new JudgmentEs(3 + "", "Document1.pdf", "Nội dung");
        judgmentEsRepository.save(judgmentEs2);

        List<JudgmentEs> list = judgmentEsRepository.findByFileNameOrContent("Việt", "Việt",
                new PageRequest(0, 10));
        System.out.println("Total records: " + list.size());
    }

    @Test
    public void testSearch() {
        JudgmentEs judgmentEs = new JudgmentEs(1 + "", "Việt Nam document.pdf", "Việt Nam là một nước nông nghiệp");
        judgmentEsRepository.save(judgmentEs);
        JudgmentEs judgmentEs1 = new JudgmentEs(2 + "", "Document1.pdf", "Việt Nam là một nước nông nghiệp");
        judgmentEsRepository.save(judgmentEs1);
        JudgmentEs judgmentEs2 = new JudgmentEs(3 + "", "Document1.pdf", "Nội dung");
        judgmentEsRepository.save(judgmentEs2);

        List<String> keywords = new ArrayList<>();
        keywords.add("Việt Nam");
        keywords.add("Châu Á");
        SearchInput searchInput = new SearchInput();
        searchInput.setKeywords(keywords);
        searchInput.setIndexFrom(0);
        searchInput.setIndexTo(10);
        judgmentService.search(searchInput);
    }
}
