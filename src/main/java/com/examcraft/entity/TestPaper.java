package com.examcraft.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "test_papers")
public class TestPaper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private int totalQuestions;
    private int easyPercentage;
    private int mediumPercentage;
    private int hardPercentage;

    @OneToMany(mappedBy = "testPaper", cascade = CascadeType.ALL)
    private List<TestPaperQuestion> questions;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getEasyPercentage() {
        return easyPercentage;
    }

    public void setEasyPercentage(int easyPercentage) {
        this.easyPercentage = easyPercentage;
    }

    public int getMediumPercentage() {
        return mediumPercentage;
    }

    public void setMediumPercentage(int mediumPercentage) {
        this.mediumPercentage = mediumPercentage;
    }

    public int getHardPercentage() {
        return hardPercentage;
    }

    public void setHardPercentage(int hardPercentage) {
        this.hardPercentage = hardPercentage;
    }

    public List<TestPaperQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<TestPaperQuestion> questions) {
        this.questions = questions;
    }
}