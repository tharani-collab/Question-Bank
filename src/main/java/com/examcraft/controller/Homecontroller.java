package com.examcraft.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Homecontroller {

    @GetMapping("/")
    public String home() {
        return "ExamCraft Backend is Running Successfully!";
    }
}