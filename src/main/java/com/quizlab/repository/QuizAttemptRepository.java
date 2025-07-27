package com.quizlab.repository;

import com.quizlab.model.Category;
import com.quizlab.model.DifficultyLevel;
import com.quizlab.model.QuizAttempt;
import com.quizlab.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, UUID> {
    List<QuizAttempt> findByUser(User user);
    List<QuizAttempt> findByCategory(Category category);
    List<QuizAttempt> findByUserAndCategory(User user, Category category);
    List<QuizAttempt> findByDifficultyLevel(DifficultyLevel difficultyLevel);
    List<QuizAttempt> findByUserOrderByStartTimeDesc(User user);
    List<QuizAttempt> findByCategoryOrderByScoreDesc(Category category);
    List<QuizAttempt> findByUserOrderByScoreDesc(User user);
}
