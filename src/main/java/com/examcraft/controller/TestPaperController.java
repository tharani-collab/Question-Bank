package com.examcraft.controller;

import com.examcraft.entity.TestPaper;
import com.examcraft.service.TestPaperService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test-papers")
public class TestPaperController {

    private final TestPaperService service;

    public TestPaperController(TestPaperService service) {
        this.service = service;
    }

    @GetMapping
    public List<TestPaper> getAll() {
        return service.getAllTestPapers();
    }

    @GetMapping("/{id}")
    public TestPaper getById(@PathVariable Long id) {
        return service.getTestPaperById(id);
    }

    @PostMapping
    public TestPaper add(@RequestBody TestPaper testPaper) {
        return service.addTestPaper(testPaper);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteTestPaper(id);
        return "Test paper deleted successfully";
    }
}