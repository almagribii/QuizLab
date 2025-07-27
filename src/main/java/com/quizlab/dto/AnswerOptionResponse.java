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
public class AnswerOptionResponse {

    private UUID id;
    private String text;

    private UUID questionId;
    private String questionText;

    private boolean isCorrect;
    private Integer displayOrder;

}