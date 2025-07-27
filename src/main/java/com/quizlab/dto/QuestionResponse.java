package com.quizlab.dto;

import com.quizlab.model.DifficultyLevel;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionResponse{
    private UUID id;
    private String text;

    private UUID categoryId;
    private String categoryName;

    private DifficultyLevel difficultyLevel;
    private String explanation;

    private boolean isActive;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
