package com.quizlab.dto;

import com.quizlab.model.DifficultyLevel;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.locks.LockSupport;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizAttemptRequest {
    @NotNull(message = "ID Pengguna tidak boleh Kosong")
    private UUID UserId;

    @NotNull(message = "ID Kategori tidak boleh Kosong")
    private UUID categoryId;

    @NotNull(message = "Score tidak boleh kosong")
    @Min(value = 0, message = "Skor minimal adalah 0")
    private Integer score;

    @NotNull(message = "Total pertanyaan tidak boleh kosong")
    @Min(value = 1, message = "Total pertanyaan minimal adalah 1")
    private Integer totalQuestions;

    private DifficultyLevel difficultyLevel;

    @NotNull(message = "Waktu Mulai kuis tidak Boleh Kosong")
    private LocalDateTime startTime;

    @NotNull(message = "Waktu Selesai Kuis Tidak Boleh Kosong")
    private LocalDateTime endTime;
}
