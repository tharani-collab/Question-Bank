package com.examcraft.service;

import com.examcraft.entity.TestPaper;
import com.examcraft.repository.TestPaperRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestPaperService {

    private final TestPaperRepository repository;

    public TestPaperService(TestPaperRepository repository) {
        this.repository = repository;
    }

    public List<TestPaper> getAllTestPapers() {
        return repository.findAll();
    }

    public TestPaper getTestPaperById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Test paper not found"));
    }

    public TestPaper addTestPaper(TestPaper testPaper) {
        return repository.save(testPaper);
    }

    public void deleteTestPaper(Long id) {
        repository.deleteById(id);
    }
}