package com.examcraft.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.examcraft.dto.GeneratePaperRequest;
import com.examcraft.entity.TestPaper;
import com.examcraft.service.TestPaperService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/test-papers")
public class TestPaperController {

    private final TestPaperService service;

    public TestPaperController(
            TestPaperService service) {

        this.service = service;
    }

    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generate(
            @Valid @RequestBody GeneratePaperRequest request) {

        TestPaper paper =
                service.generate(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(paper));
    }

    @GetMapping
    public List<TestPaper> getAll() {

        return service.findAll();
    }

    @GetMapping("/{id}")
    public Map<String, Object> getById(
            @PathVariable Long id) {

        return toResponse(
                service.findById(id)
        );
    }

    private Map<String, Object> toResponse(
            TestPaper paper) {

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put(
                "id",
                paper.getId()
        );

        response.put(
                "title",
                paper.getTitle()
        );

        response.put(
                "totalQuestions",
                paper.getTotalQuestions()
        );

        response.put(
                "easyPercentage",
                paper.getEasyPercentage()
        );

        response.put(
                "mediumPercentage",
                paper.getMediumPercentage()
        );

        response.put(
                "hardPercentage",
                paper.getHardPercentage()
        );

        response.put(
                "generatedAt",
                paper.getGeneratedAt()
        );

        response.put(
                "questions",
                paper.getQuestions()
                        .stream()
                        .map(item -> {

                            Map<String, Object> q =
                                    new LinkedHashMap<>();

                            q.put(
                                    "questionOrder",
                                    item.getQuestionOrder()
                            );

                            q.put(
                                    "id",
                                    item.getQuestion().getId()
                            );

                            q.put(
                                    "questionText",
                                    item.getQuestion()
                                            .getQuestionText()
                            );

                            q.put(
                                    "optionA",
                                    item.getQuestion().getOptionA()
                            );

                            q.put(
                                    "optionB",
                                    item.getQuestion().getOptionB()
                            );

                            q.put(
                                    "optionC",
                                    item.getQuestion().getOptionC()
                            );

                            q.put(
                                    "optionD",
                                    item.getQuestion().getOptionD()
                            );

                            q.put(
                                    "difficulty",
                                    item.getQuestion().getDifficulty()
                            );

                            q.put(
                                    "topic",
                                    item.getQuestion().getTopic()
                            );

                            return q;
                        })
                        .toList()
        );

        return response;
    }
}