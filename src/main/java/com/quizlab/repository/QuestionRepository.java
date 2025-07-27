package com.quizlab.repository;

import com.quizlab.model.Category;
import com.quizlab.model.DifficultyLevel;
import com.quizlab.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {
    List<Question> findByCategory(Category category);
    List<Question> findByDifficultyLevel(DifficultyLevel difficultyLevel);
    List<Question> findByCategoryAndDifficultyLevel(Category category, DifficultyLevel difficultyLevel);
    List<Question> findByTextContainingIgnoreCase(String text);
    List<Question> findByIsActive(boolean isActive);
}
