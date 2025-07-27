package com.quizlab.repository;

import com.quizlab.model.AnswerOption;
import com.quizlab.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AnswerOptionRepository extends JpaRepository<AnswerOption, UUID> {
    List<AnswerOption> findByQuestion(Question question);
    List<AnswerOption> findByQuestionAndIsCorrect(Question question, boolean isCorrect);
    Optional<AnswerOption> findByQuestionAndText(Question question, String text);
    Optional<AnswerOption> findByQuestionAndDisplayOrder(Question question, Integer displayOrder);
}
