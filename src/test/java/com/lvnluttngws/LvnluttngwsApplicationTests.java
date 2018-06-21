package com.lvnluttngws;

import com.lvnluttngws.document.model.JudgmentEs;
import com.lvnluttngws.document.model.ResultContainer;
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
        System.out.println("### Before test: START ###");
        esTemplate.deleteIndex(JudgmentEs.class);
        esTemplate.createIndex(JudgmentEs.class);
        esTemplate.putMapping(JudgmentEs.class);
        esTemplate.refresh(JudgmentEs.class);
        System.out.println("### Before test: END ###");
    }

    @Test
    public void testFindDocumentNameOrContent() {
        System.out.println("### testFindDocumentNameOrContent: START ###");
        JudgmentEs judgmentEs = new JudgmentEs(1 + "", "Việt Nam document.pdf", "Việt Nam là một nước nông nghiệp");
        judgmentEsRepository.save(judgmentEs);
        JudgmentEs judgmentEs1 = new JudgmentEs(2 + "", "Document1.pdf", "Việt Nam là một nước nông nghiệp");
        judgmentEsRepository.save(judgmentEs1);
        JudgmentEs judgmentEs2 = new JudgmentEs(3 + "", "Document1.pdf", "Nội dung");
        judgmentEsRepository.save(judgmentEs2);

        List<JudgmentEs> list = judgmentEsRepository.findByFileNameOrContent("Việt", "Việt",
                new PageRequest(0, 10));
        System.out.println("Total records: " + list.size());
        System.out.println("### testFindDocumentNameOrContent: END ###");
    }

    @Test
    public void testSearch() {
        System.out.println("### testSearch: START ###");
        JudgmentEs judgmentEs = new JudgmentEs(1 + "", "Việt Nam document.pdf", "Việt Nam là một nước nông nghiệp");
        judgmentEsRepository.save(judgmentEs);
        JudgmentEs judgmentEs1 = new JudgmentEs(2 + "", "Document1.pdf", "Việt Nam là một nước nông nghiệp");
        judgmentEsRepository.save(judgmentEs1);
        JudgmentEs judgmentEs2 = new JudgmentEs(3 + "", "Document1.pdf", "Nội dung");
        judgmentEsRepository.save(judgmentEs2);

        SearchInput searchInput = new SearchInput();
        searchInput.setInputText("Việt Nam");
        searchInput.setIndexFrom(0);
        searchInput.setIndexTo(10);
        ResultContainer res = judgmentService.search(searchInput);
        System.out.println("Found: " + res.getResult().size());
        System.out.println("### testSearch: END ###");
    }
}
