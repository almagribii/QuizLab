package com.quizlab.controller;

import com.quizlab.dto.QuestionRequest;
import com.quizlab.dto.QuestionResponse;
import com.quizlab.model.DifficultyLevel;
import com.quizlab.service.QuestionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping
    public ResponseEntity<QuestionResponse> createQuestion(@Valid @RequestBody QuestionRequest request) {
        try {
            QuestionResponse newQuestion = questionService.createQuestion(request);
            return new ResponseEntity<>(newQuestion, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<QuestionResponse>> getAllQuestions(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) DifficultyLevel difficultyLevel) {
        List<QuestionResponse> questions;

        if (categoryId != null && difficultyLevel != null) {
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        } else if (categoryId != null) {
            try {
                questions = questionService.getQuestionsByCategoryId(categoryId);
            } catch (EntityNotFoundException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Kategori tidak ditemukan
            }
        } else if (difficultyLevel != null) {
            questions = questionService.getQuestionsByDifficultyLevel(difficultyLevel);
        } else {
            questions = questionService.getAllQuestions();
        }

        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    @GetMapping("/{id}") // Memetakan permintaan HTTP GET ke /api/v1/questions/{id}
    public ResponseEntity<QuestionResponse> getQuestionById(@PathVariable UUID id) {
        Optional<QuestionResponse> question = questionService.getQuestionById(id);
        return question.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PutMapping("/{id}")
    public ResponseEntity<QuestionResponse> updateQuestion(@PathVariable UUID id, @Valid @RequestBody QuestionRequest request) {
        try {
            Optional<QuestionResponse> updatedQuestion = questionService.updateQuestion(id, request);
            return updatedQuestion.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // Kategori atau Pertanyaan tidak ditemukan
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable UUID id) {
        boolean deleted = questionService.deleteQuestion(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Status 204 No Content
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Status 404 Not Found
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<QuestionResponse> updateQuestionStatus(@PathVariable UUID id, @RequestParam boolean isActive) {
        Optional<QuestionResponse> updatedQuestion = questionService.updateQuestionActiveStatus(id, isActive);
        return updatedQuestion.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
