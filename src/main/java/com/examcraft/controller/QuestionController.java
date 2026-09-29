package com.examcraft.controller;

import com.examcraft.entity.Question;
import com.examcraft.service.QuestionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    /*
     * Usage frequency endpoint
     */
    @GetMapping("/usage")
    public List<Map<String, Object>> getQuestionUsage() {
        return service.getQuestionUsage();
    }

    /*
     * \d+ means only numeric IDs are accepted.
     * So /usage will not be treated as an ID.
     */
    @GetMapping("/{id:\\d+}")
    public Question getById(@PathVariable Long id) {
        return service.getQuestionById(id);
    }

    @PostMapping
    public Question add(@RequestBody Question question) {
        return service.addQuestion(question);
    }

    @PutMapping("/{id:\\d+}")
    public Question update(
            @PathVariable Long id,
            @RequestBody Question question) {
        return service.updateQuestion(id, question);
    }

    @DeleteMapping("/{id:\\d+}")
    public String delete(@PathVariable Long id) {
        service.deleteQuestion(id);
        return "Question deleted successfully";
    }
}