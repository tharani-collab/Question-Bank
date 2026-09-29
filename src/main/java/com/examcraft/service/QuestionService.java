package com.examcraft.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.examcraft.entity.Question;
import com.examcraft.exception.ResourceNotFoundException;
import com.examcraft.repository.QuestionRepository;

@Service
public class QuestionService {

    private final QuestionRepository repository;

    public QuestionService(QuestionRepository repository) {
        this.repository = repository;
    }

    public List<Question> getAllQuestions() {
        return repository.findAll();
    }

    public Question getQuestionById(Long id) {
        return repository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Question not found: " + id
                        )
                );
    }

    public Question addQuestion(Question question) {
        return repository.save(question);
    }

    public Question updateQuestion(
            Long id,
            Question question) {

        Question existing = getQuestionById(id);

        existing.setQuestionText(
                question.getQuestionText()
        );

        existing.setTopic(
                question.getTopic()
        );

        existing.setDifficulty(
                question.getDifficulty()
        );

        existing.setQuestionType(
                question.getQuestionType()
        );

        existing.setOptionA(
                question.getOptionA()
        );

        existing.setOptionB(
                question.getOptionB()
        );

        existing.setOptionC(
                question.getOptionC()
        );

        existing.setOptionD(
                question.getOptionD()
        );

        existing.setCorrectAnswer(
                question.getCorrectAnswer()
        );

        existing.setUnit(
                question.getUnit()
        );

        return repository.save(existing);
    }

    public void deleteQuestion(Long id) {

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Question not found: " + id
            );
        }

        repository.deleteById(id);
    }

    /*
     * Question usage frequency
     */
    public List<Map<String, Object>> getQuestionUsage() {

        List<Question> questions =
                new ArrayList<>(repository.findAll());

        /*
         * Highest usage first.
         * Null usageCount is treated as 0.
         */
        questions.sort(
                (question1, question2) ->
                        Integer.compare(
                                getUsageCount(question2),
                                getUsageCount(question1)
                        )
        );

        List<Map<String, Object>> result =
                new ArrayList<>();

        for (Question question : questions) {

            Map<String, Object> data =
                    new LinkedHashMap<>();

            data.put(
                    "questionId",
                    question.getId()
            );

            data.put(
                    "question",
                    question.getQuestionText()
            );

            data.put(
                    "topic",
                    question.getTopic()
            );

            data.put(
                    "difficulty",
                    question.getDifficulty()
            );

            data.put(
                    "usageCount",
                    getUsageCount(question)
            );

            result.add(data);
        }

        return result;
    }

    private int getUsageCount(Question question) {

        Integer usage = question.getUsageCount();

        if (usage == null) {
            return 0;
        }

        return usage;
    }
}