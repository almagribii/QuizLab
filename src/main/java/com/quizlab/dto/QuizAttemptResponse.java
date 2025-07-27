package com.quizlab.dto;

import com.quizlab.model.DifficultyLevel;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.locks.LockSupport;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizAttemptResponse {
    private UUID id;
    private UUID userId;
    private String username;
    private UUID categoryId;
    private String categoryName;
    private Integer score;
    private Integer totalQuestion;
    private DifficultyLevel difficultyLevel;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
