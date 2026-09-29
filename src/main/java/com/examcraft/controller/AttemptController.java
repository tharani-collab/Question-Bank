package com.examcraft.controller;

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

import com.examcraft.dto.SubmitAttemptRequest;
import com.examcraft.entity.Attempt;
import com.examcraft.service.AttemptService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/attempts")
public class AttemptController {

    private final AttemptService service;

    public AttemptController(AttemptService service) {
        this.service = service;
    }

    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submit(
            @Valid @RequestBody SubmitAttemptRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.submitAttempt(request));
    }

    @GetMapping
    public List<Attempt> getAll() {
        return service.getAllAttempts();
    }

    @GetMapping("/{id}")
    public Attempt getById(
            @PathVariable Long id) {

        return service.getAttemptById(id);
    }
}