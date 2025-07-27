package com.quizlab.dto;

import com.quizlab.model.DifficultyLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequest {
    @NotBlank(message = "Teks Pertanyaan tidak boleh kosong")
    @Size(min = 5, max = 500, message = "Teks pertanyaan harus antara 5 sampai 500 karakter")
    private String text;

    @NotNull(message = "ID kategori tidak boleh kosong")
    private UUID categoryId;

    @NotNull(message = "Tingkat Kesulita tidak boleh kosong")
    private DifficultyLevel difficultyLevel;

    @Size(max = 1000, message = "Penjelasan maksimal 1000 karakter")
    private String explanation;
}
