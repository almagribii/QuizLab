package com.quizlab.repository;

import com.quizlab.model.Category;
import com.quizlab.model.DifficultyLevel;
import com.quizlab.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public interface QuestionRepository extends JpaRepository<Question, UUID> {
    List<Question> findByCategory(Category category);
    List<Question> findByDifficulty(DifficultyLevel difficultyLevel);
    List<Question> findByCategoryAndDifficultyLevel(Category category, DifficultyLevel difficultyLevel);
    List<Question> findByTextContainingIgnoreCase(String text);
    List<Question> findByIsActive(boolean isActive);
}
