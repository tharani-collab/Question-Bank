package com.examcraft.controller;

import com.examcraft.entity.Attempt;
import com.examcraft.service.AttemptService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attempts")
public class AttemptController {

    private final AttemptService service;

    public AttemptController(AttemptService service) {
        this.service = service;
    }

    @GetMapping
    public List<Attempt> getAll() {
        return service.getAllAttempts();
    }

    @GetMapping("/{id}")
    public Attempt getById(@PathVariable Long id) {
        return service.getAttemptById(id);
    }

    @PostMapping
    public Attempt add(@RequestBody Attempt attempt) {
        return service.addAttempt(attempt);
    }
}