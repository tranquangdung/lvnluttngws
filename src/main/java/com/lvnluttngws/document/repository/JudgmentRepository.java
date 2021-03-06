package com.lvnluttngws.document.repository;

import com.lvnluttngws.document.model.Judgment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JudgmentRepository extends JpaRepository<Judgment, Long> {
}