package com.quizlab.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerOptionRequest {

    @NotNull(message = "ID Pertanyaan tidak boleh kosong")
    private UUID questionId;

    @NotBlank(message = "Teks pilihan jawaban tidak boleh kosong")
    private String text;

    @NotNull(message = "Indikator benar/salah tidak boleh kosong")
    private Boolean isCorrect;

    private Integer displayOrder;
}