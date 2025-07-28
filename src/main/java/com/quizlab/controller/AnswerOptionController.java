package com.quizlab.controller;

import com.quizlab.dto.AnswerOptionRequest;
import com.quizlab.dto.AnswerOptionResponse;
import com.quizlab.service.AnswerOptionService;
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
@RequestMapping("/api/v1/answer-options")
@RequiredArgsConstructor
public class AnswerOptionController {

    private final AnswerOptionService answerOptionService;

    /**
     * Endpoint untuk membuat pilihan jawaban baru untuk sebuah pertanyaan.
     * POST /api/v1/answer-options
     * @param request DTO yang berisi detail pilihan jawaban.
     * @return ResponseEntity<AnswerOptionResponse> dengan status 201 Created jika sukses,
     * atau 400 Bad Request / 404 Not Found jika gagal.
     */
    @PostMapping
    public ResponseEntity<AnswerOptionResponse> createAnswerOption(@Valid @RequestBody AnswerOptionRequest request) {
        try {
            AnswerOptionResponse newAnswerOption = answerOptionService.createAnswerOption(request);
            return new ResponseEntity<>(newAnswerOption, HttpStatus.CREATED); // Status 201 Created
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint untuk mengambil daftar semua pilihan jawaban, atau memfilter berdasarkan questionId.
     * GET /api/v1/answer-options
     * @param questionId (Opsional) ID pertanyaan untuk memfilter pilihan jawaban.
     * @return ResponseEntity<List<AnswerOptionResponse>> daftar pilihan jawaban.
     */
    @GetMapping
    public ResponseEntity<List<AnswerOptionResponse>> getAllAnswerOptions(
            @RequestParam(required = false) UUID questionId) {
        List<AnswerOptionResponse> answerOptions;

        if (questionId != null) {
            try {
                answerOptions = answerOptionService.getAnswerOptionsByQuestionId(questionId);
            } catch (EntityNotFoundException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            answerOptions = answerOptionService.getAllAnswerOptions();
        }

        return new ResponseEntity<>(answerOptions, HttpStatus.OK);
    }

    /**
     * Endpoint untuk mengambil pilihan jawaban berdasarkan ID.
     * GET /api/v1/answer-options/{id}
     * @param id ID pilihan jawaban (diambil dari URL path).
     * @return ResponseEntity<AnswerOptionResponse> dengan status 200 OK jika ditemukan,
     * atau 404 Not Found jika tidak.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AnswerOptionResponse> getAnswerOptionById(@PathVariable UUID id) {
        Optional<AnswerOptionResponse> answerOption = answerOptionService.getAnswerOptionById(id);
        return answerOption.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Endpoint untuk memperbarui pilihan jawaban yang sudah ada.
     * PUT /api/v1/answer-options/{id}
     * @param id ID pilihan jawaban yang akan diperbarui (diambil dari URL path).
     * @param request DTO dengan data pilihan jawaban yang baru.
     * @return ResponseEntity<AnswerOptionResponse> dengan status 200 OK jika sukses,
     * atau 404 Not Found / 400 Bad Request jika gagal.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AnswerOptionResponse> updateAnswerOption(@PathVariable UUID id, @Valid @RequestBody AnswerOptionRequest request) {
        try {
            Optional<AnswerOptionResponse> updatedAnswerOption = answerOptionService.updateAnswerOption(id, request);
            return updatedAnswerOption.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint untuk menghapus pilihan jawaban.
     * DELETE /api/v1/answer-options/{id}
     * @param id ID pilihan jawaban yang akan dihapus (diambil dari URL path).
     * @return ResponseEntity<Void> dengan status 204 No Content jika sukses,
     * atau 404 Not Found jika tidak.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnswerOption(@PathVariable UUID id) {
        boolean deleted = answerOptionService.deleteAnswerOption(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}