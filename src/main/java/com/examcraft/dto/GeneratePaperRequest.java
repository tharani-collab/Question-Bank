package com.examcraft.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class GeneratePaperRequest {

    @NotBlank(message = "Paper title is required")
    private String title;

    @NotNull(message = "Total questions is required")
    @Positive(message = "Total questions must be greater than 0")
    private Integer totalQuestions;

    @NotNull(message = "Easy percentage is required")
    @Min(value = 0, message = "Easy percentage cannot be negative")
    @Max(value = 100, message = "Easy percentage cannot exceed 100")
    private Integer easyPercent;

    @NotNull(message = "Medium percentage is required")
    @Min(value = 0, message = "Medium percentage cannot be negative")
    @Max(value = 100, message = "Medium percentage cannot exceed 100")
    private Integer mediumPercent;

    @NotNull(message = "Hard percentage is required")
    @Min(value = 0, message = "Hard percentage cannot be negative")
    @Max(value = 100, message = "Hard percentage cannot exceed 100")
    private Integer hardPercent;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Integer getEasyPercent() {
        return easyPercent;
    }

    public void setEasyPercent(Integer easyPercent) {
        this.easyPercent = easyPercent;
    }

    public Integer getMediumPercent() {
        return mediumPercent;
    }

    public void setMediumPercent(Integer mediumPercent) {
        this.mediumPercent = mediumPercent;
    }

    public Integer getHardPercent() {
        return hardPercent;
    }

    public void setHardPercent(Integer hardPercent) {
        this.hardPercent = hardPercent;
    }
}