// src/main/java/com/quizlab/controller/AnswerOptionController.java
package com.quizlab.controller; // Pastikan ini sesuai dengan struktur paket Anda

import com.quizlab.dto.AnswerOptionRequest;   // DTO input untuk pilihan jawaban
import com.quizlab.dto.AnswerOptionResponse;  // DTO output untuk pilihan jawaban
import com.quizlab.service.AnswerOptionService; // Service layer yang akan kita panggil
import jakarta.persistence.EntityNotFoundException; // Untuk menangani exception EntityNotFoundException
import jakarta.validation.Valid;           // Untuk mengaktifkan validasi DTO
import lombok.RequiredArgsConstructor;     // Lombok untuk Dependency Injection
import org.springframework.http.HttpStatus; // Untuk kode status HTTP
import org.springframework.http.ResponseEntity; // Untuk membangun respons HTTP
import org.springframework.web.bind.annotation.*; // Anotasi untuk REST Controller

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController // Menandakan bahwa kelas ini adalah REST Controller
@RequestMapping("/api/v1/answer-options") // Base path untuk semua endpoint pilihan jawaban
@RequiredArgsConstructor // Lombok: Membuat konstruktor untuk AnswerOptionService
public class AnswerOptionController {

    private final AnswerOptionService answerOptionService; // Injeksi AnswerOptionService

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
            // Jika questionId tidak ditemukan
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // Status 404 Not Found (karena relasi ke pertanyaan)
        } catch (RuntimeException e) {
            // Untuk error umum lainnya (misal validasi lebih lanjut)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); // Status 400 Bad Request
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
            @RequestParam(required = false) UUID questionId) { // Parameter query opsional untuk filtering
        List<AnswerOptionResponse> answerOptions;

        if (questionId != null) {
            try {
                answerOptions = answerOptionService.getAnswerOptionsByQuestionId(questionId);
            } catch (EntityNotFoundException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Pertanyaan tidak ditemukan
            }
        } else {
            answerOptions = answerOptionService.getAllAnswerOptions(); // Ambil semua jika tanpa filter
        }

        return new ResponseEntity<>(answerOptions, HttpStatus.OK); // Status 200 OK
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
        return answerOption.map(value -> new ResponseEntity<>(value, HttpStatus.OK)) // Jika ditemukan, return 200 OK
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Jika tidak ditemukan, return 404 Not Found
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
            return updatedAnswerOption.map(value -> new ResponseEntity<>(value, HttpStatus.OK)) // Jika update sukses, return 200 OK
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Jika pilihan jawaban tidak ditemukan, return 404 Not Found
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // Jika pertanyaan induk tidak ditemukan
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); // Error umum (misal validasi)
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
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Status 204 No Content
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Status 404 Not Found
        }
    }
}