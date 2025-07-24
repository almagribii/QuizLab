// src/main/java/com/quizlab/api/controller/HealthCheckController.java
package com.quizlab.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Anotasi ini menandakan bahwa kelas ini adalah REST Controller
@RequestMapping("/api/health") // Menentukan base path untuk semua endpoint di controller ini
public class HealthCheckController {

    @GetMapping("/status") // Menentukan bahwa metode ini akan merespons permintaan GET ke /api/health/status
    public String checkStatus() {
        return "QuizLab API is Up and Running!";
    }

    @GetMapping("/hello") // Endpoint lain untuk menyapa
    public String sayHello() {
        return "Hello from QuizLab API!";
    }
}