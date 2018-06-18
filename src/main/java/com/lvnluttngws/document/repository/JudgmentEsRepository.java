package com.lvnluttngws.document.repository;

import com.lvnluttngws.document.model.JudgmentEs;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface JudgmentEsRepository extends ElasticsearchRepository<JudgmentEs, String> {
    List<JudgmentEs> findByFileNameOrContent(String fileName, String content, Pageable pageable);
}