package com.examcraft.controller;

import com.examcraft.entity.Question;
import com.examcraft.service.QuestionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService service;

    public QuestionController(QuestionService service) {
        this.service = service;
    }

    @GetMapping
    public List<Question> getAll() {
        return service.getAllQuestions();
    }

    @GetMapping("/{id}")
    public Question getById(@PathVariable Long id) {
        return service.getQuestionById(id);
    }

    @PostMapping
    public Question add(@RequestBody Question question) {
        return service.addQuestion(question);
    }

    @PutMapping("/{id}")
    public Question update(
            @PathVariable Long id,
            @RequestBody Question question) {

        return service.updateQuestion(id, question);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteQuestion(id);
        return "Question deleted successfully";
    }
}