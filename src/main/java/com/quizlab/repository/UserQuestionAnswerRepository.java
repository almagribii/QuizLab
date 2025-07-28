package com.quizlab.repository;

import com.quizlab.model.Question;
import com.quizlab.model.QuizAttempt;
import com.quizlab.model.UserQuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserQuestionAnswerRepository extends JpaRepository<UserQuestionAnswer, UUID> {
    List<UserQuestionAnswer> findByQuizAttempt(QuizAttempt quizAttempt);
    Optional<UserQuestionAnswer> findByQuizAttemptAndQuestion(QuizAttempt quizAttempt, Question question);
    List<UserQuestionAnswer> findByQuizAttemptAndIsCorrect(QuizAttempt quizAttempt, boolean isCorrect);
    List<UserQuestionAnswer> findByQuestion(Question question);
}
