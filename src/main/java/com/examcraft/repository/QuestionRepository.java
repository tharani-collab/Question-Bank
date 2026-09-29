package com.examcraft.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.examcraft.entity.Question;

public interface QuestionRepository
        extends JpaRepository<Question, Long> {

    List<Question> findByDifficultyIgnoreCase(
            String difficulty
    );
}