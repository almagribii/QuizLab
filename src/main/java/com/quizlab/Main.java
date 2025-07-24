// src/main/java/com/quizlab/api/QuizlabApplication.java
package com.quizlab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // Ini adalah anotasi utama Spring Boot
public class Main {

    public static void main(String[] args) { // Ini adalah metode utama (entry point) aplikasi Java
        SpringApplication.run(Main.class, args);
    }

}