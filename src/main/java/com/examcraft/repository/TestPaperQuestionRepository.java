package com.examcraft.repository;

import com.examcraft.entity.TestPaperQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestPaperQuestionRepository
        extends JpaRepository<TestPaperQuestion, Long> {
}