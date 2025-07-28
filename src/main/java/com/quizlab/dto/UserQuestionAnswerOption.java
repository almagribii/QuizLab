package com.quizlab.dto;

import java.util.UUID;

public class UserQuestionAnswerOption {
    private UUID id;

    private UUID quizAttemptId;

    private UUID questionId;
    private String questionText;

    private UUID selectedAnswerOptionId;
    private String selectedAnswerOptionText;

    private boolean isCorrect;
    private Integer timeTakenSeconds;
}
