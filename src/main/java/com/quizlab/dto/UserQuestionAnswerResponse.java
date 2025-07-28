package com.quizlab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserQuestionAnswerResponse {
    private UUID id;

    private UUID quizAttemptId;

    private UUID questionId;
    private String questionText;

    private UUID selectedAnswerOptionId;
    private String selectedAnswerOptionText;

    private boolean isCorrect;
    private Integer timeTakenSeconds;
}
