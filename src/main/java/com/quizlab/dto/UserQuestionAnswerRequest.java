package com.quizlab.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserQuestionAnswerRequest {
    @NotNull(message = "ID Quiz Attempt tidak boleh kosong")
    private UUID quizAttemptId;

    @NotNull(message = "ID Pertanyaan tidak boleh kosong")
    private UUID questionId;

    @NotNull(message = "ID Pilihan Jawaban tidak boleh kosong")
    private UUID selectedAnswerOptionId;

    @NotNull(message = "Status benar/salah jawaban tidak boleh kosong")
    private Boolean isCorrect;

    @Min(value = 0, message = "Waktu yang dibutuhkan minimal 0 detik") // Minimal 0 detik
    private Integer timeTakenSeconds;
}
