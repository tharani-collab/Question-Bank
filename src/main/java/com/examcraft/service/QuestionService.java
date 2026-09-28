package com.examcraft.service;

import com.examcraft.entity.Question;
import com.examcraft.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
                .orElseThrow(() -> new RuntimeException("Question not found"));
    }

    public Question addQuestion(Question question) {
        return repository.save(question);
    }

    public Question updateQuestion(Long id, Question question) {

        Question existing = getQuestionById(id);

        existing.setQuestionText(question.getQuestionText());
        existing.setTopic(question.getTopic());
        existing.setDifficulty(question.getDifficulty());
        existing.setQuestionType(question.getQuestionType());
        existing.setOptionA(question.getOptionA());
        existing.setOptionB(question.getOptionB());
        existing.setOptionC(question.getOptionC());
        existing.setOptionD(question.getOptionD());
        existing.setCorrectAnswer(question.getCorrectAnswer());
        existing.setUnit(question.getUnit());

        return repository.save(existing);
    }

    public void deleteQuestion(Long id) {
        repository.deleteById(id);
    }
}