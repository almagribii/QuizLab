package com.quizlab.controller;

import com.quizlab.dto.QuizAttemptRequest;
import com.quizlab.dto.QuizAttemptResponse;
import com.quizlab.service.QuizAttemptService;
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
@RequestMapping("/api/v1/quiz-attempts")
@RequiredArgsConstructor
public class QuizAttemptController {

    private final QuizAttemptService quizAttemptService;

    /**
     * Endpoint untuk mencatat percobaan kuis baru.
     * POST /api/v1/quiz-attempts
     * @param request DTO yang berisi detail percobaan kuis yang sudah selesai.
     * @return ResponseEntity<QuizAttemptResponse> dengan status 201 Created jika sukses,
     * atau 400 Bad Request / 404 Not Found jika gagal.
     */
    @PostMapping
    public ResponseEntity<QuizAttemptResponse> createQuizAttempt(@Valid @RequestBody QuizAttemptRequest request) {
        try {
            QuizAttemptResponse newAttempt = quizAttemptService.createQuizAttempt(request);
            return new ResponseEntity<>(newAttempt, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint untuk mengambil daftar semua percobaan kuis.
     * GET /api/v1/quiz-attempts
     * Dapat difilter berdasarkan userId atau categoryId.
     * @param userId (Opsional) ID pengguna untuk memfilter percobaan kuis.
     * @param categoryId (Opsional) ID kategori untuk memfilter percobaan kuis.
     * @return ResponseEntity<List<QuizAttemptResponse>> daftar percobaan kuis.
     */
    @GetMapping
    public ResponseEntity<List<QuizAttemptResponse>> getAllQuizAttempts(
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) UUID categoryId) {
        List<QuizAttemptResponse> attempts;

        if (userId != null && categoryId != null) {
            attempts = quizAttemptService.getQuizAttemptsByUserIdAndCategoryId(userId, categoryId);
        } else if (userId != null) {
            try {
                attempts = quizAttemptService.getQuizAttemptsByUserId(userId);
            } catch (EntityNotFoundException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else if (categoryId != null) {
            try {
                attempts = quizAttemptService.getQuizAttemptsByCategoryId(categoryId);
            } catch (EntityNotFoundException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            attempts = quizAttemptService.getAllQuizAttempts();
        }

        return new ResponseEntity<>(attempts, HttpStatus.OK);
    }

    /**
     * Endpoint untuk mengambil percobaan kuis berdasarkan ID.
     * GET /api/v1/quiz-attempts/{id}
     * @param id ID percobaan kuis (diambil dari URL path).
     * @return ResponseEntity<QuizAttemptResponse> dengan status 200 OK jika ditemukan,
     * atau 404 Not Found jika tidak.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuizAttemptResponse> getQuizAttemptById(@PathVariable UUID id) {
        Optional<QuizAttemptResponse> attempt = quizAttemptService.getQuizAttemptById(id);
        return attempt.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}