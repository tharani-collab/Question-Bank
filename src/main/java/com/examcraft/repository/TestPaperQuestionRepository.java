package com.examcraft.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.examcraft.entity.TestPaperQuestion;

public interface TestPaperQuestionRepository
        extends JpaRepository<TestPaperQuestion, Long> {

    boolean existsByTestPaper_IdAndQuestion_Id(
            Long testPaperId,
            Long questionId
    );

    long countByQuestion_Id(Long questionId);
}