package com.examcraft.service;

import com.examcraft.entity.Attempt;
import com.examcraft.repository.AttemptRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttemptService {

    private final AttemptRepository repository;

    public AttemptService(AttemptRepository repository) {
        this.repository = repository;
    }

    public List<Attempt> getAllAttempts() {
        return repository.findAll();
    }

    public Attempt getAttemptById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attempt not found"));
    }

    public Attempt addAttempt(Attempt attempt) {
        return repository.save(attempt);
    }
}