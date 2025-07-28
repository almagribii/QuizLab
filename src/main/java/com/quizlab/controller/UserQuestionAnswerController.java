package com.quizlab.controller;

import com.quizlab.dto.UserQuestionAnswerRequest;
import com.quizlab.dto.UserQuestionAnswerResponse;
import com.quizlab.service.UserQuestionAnswerService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController // Menandakan bahwa kelas ini adalah REST Controller
@RequestMapping("/api/v1/user-answers") // Base path untuk semua endpoint jawaban pengguna
@RequiredArgsConstructor // Lombok: Membuat konstruktor untuk UserQuestionAnswerService
public class UserQuestionAnswerController {

    private final UserQuestionAnswerService userQuestionAnswerService; // Injeksi UserQuestionAnswerService

    /**
     * Endpoint untuk mencatat jawaban pengguna untuk satu pertanyaan dalam sesi kuis.
     * POST /api/v1/user-answers
     * @param request DTO yang berisi detail jawaban pengguna.
     * @return ResponseEntity<UserQuestionAnswerResponse> dengan status 201 Created jika sukses,
     * atau 400 Bad Request / 404 Not Found jika gagal.
     */
    @PostMapping
    public ResponseEntity<UserQuestionAnswerResponse> recordUserAnswer(@Valid @RequestBody UserQuestionAnswerRequest request) {
        try {
            UserQuestionAnswerResponse newAnswer = userQuestionAnswerService.recordUserAnswer(request);
            return new ResponseEntity<>(newAnswer, HttpStatus.CREATED); // Status 201 Created
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // Status 404 Not Found
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); // Status 400 Bad Request
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); // Status 400 Bad Request
        }
    }

    /**
     * Endpoint untuk mengambil jawaban pengguna per pertanyaan berdasarkan ID.
     * GET /api/v1/user-answers/{id}
     * @param id ID jawaban pengguna (diambil dari URL path).
     * @return ResponseEntity<UserQuestionAnswerResponse> dengan status 200 OK jika ditemukan,
     * atau 404 Not Found jika tidak.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserQuestionAnswerResponse> getUserQuestionAnswerById(@PathVariable UUID id) {
        Optional<UserQuestionAnswerResponse> answer = userQuestionAnswerService.getUserQuestionAnswerById(id);
        return answer.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Endpoint untuk mengambil semua jawaban pengguna untuk sebuah sesi kuis tertentu.
     * GET /api/v1/user-answers/by-attempt/{quizAttemptId}
     * @param quizAttemptId ID sesi kuis untuk memfilter jawaban.
     * @return ResponseEntity<List<UserQuestionAnswerResponse>> daftar jawaban.
     */
    @GetMapping("/by-attempt/{quizAttemptId}")
    public ResponseEntity<List<UserQuestionAnswerResponse>> getAnswersByQuizAttemptId(@PathVariable UUID quizAttemptId) {
        try {
            List<UserQuestionAnswerResponse> answers = userQuestionAnswerService.getAnswersByQuizAttemptId(quizAttemptId);
            return new ResponseEntity<>(answers, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // QuizAttempt tidak ditemukan
        }
    }

    /**
     * Endpoint untuk mengambil semua jawaban pengguna untuk sebuah pertanyaan tertentu.
     * GET /api/v1/user-answers/by-question/{questionId}
     * @param questionId ID pertanyaan untuk memfilter jawaban.
     * @return ResponseEntity<List<UserQuestionAnswerResponse>> daftar jawaban.
     */
    @GetMapping("/by-question/{questionId}")
    public ResponseEntity<List<UserQuestionAnswerResponse>> getAnswersByQuestionId(@PathVariable UUID questionId) {
        try {
            List<UserQuestionAnswerResponse> answers = userQuestionAnswerService.getAnswersByQuestionId(questionId);
            return new ResponseEntity<>(answers, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Pertanyaan tidak ditemukan
        }
    }

}