package com.quizlab.service;

import com.quizlab.dto.UserQuestionAnswerRequest;
import com.quizlab.dto.UserQuestionAnswerResponse;
import com.quizlab.model.AnswerOption;
import com.quizlab.model.Question;
import com.quizlab.model.QuizAttempt;
import com.quizlab.model.UserQuestionAnswer;
import com.quizlab.repository.AnswerOptionRepository;
import com.quizlab.repository.QuestionRepository;
import com.quizlab.repository.QuizAttemptRepository;
import com.quizlab.repository.UserQuestionAnswerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserQuestionAnswerService {

    private final UserQuestionAnswerRepository userQuestionAnswerRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final QuestionRepository questionRepository;
    private final AnswerOptionRepository answerOptionRepository;

    /**
     * Mencatat jawaban pengguna untuk satu pertanyaan dalam sebuah sesi kuis.
     * @param request DTO yang berisi detail jawaban.
     * @return UserQuestionAnswerResponse DTO dari jawaban yang berhasil dicatat.
     * @throws EntityNotFoundException jika QuizAttempt, Question, atau AnswerOption tidak ditemukan.
     * @throws IllegalArgumentException jika pilihan jawaban yang dipilih bukan milik pertanyaan yang benar.
     */
    public UserQuestionAnswerResponse recordUserAnswer(UserQuestionAnswerRequest request) {
        QuizAttempt quizAttempt = quizAttemptRepository.findById(request.getQuizAttemptId())
                .orElseThrow(() -> new EntityNotFoundException("Quiz Attempt dengan ID " + request.getQuizAttemptId() + " tidak ditemukan."));

        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new EntityNotFoundException("Pertanyaan dengan ID " + request.getQuestionId() + " tidak ditemukan."));

        AnswerOption selectedAnswerOption = answerOptionRepository.findById(request.getSelectedAnswerOptionId())
                .orElseThrow(() -> new EntityNotFoundException("Pilihan Jawaban dengan ID " + request.getSelectedAnswerOptionId() + " tidak ditemukan."));

        if (!selectedAnswerOption.getQuestion().getId().equals(question.getId())) {
            throw new IllegalArgumentException("Pilihan jawaban (ID: " + request.getSelectedAnswerOptionId() + ") bukan milik pertanyaan (ID: " + request.getQuestionId() + ").");
        }



        UserQuestionAnswer newAnswer = new UserQuestionAnswer();
        newAnswer.setQuizAttempt(quizAttempt);
        newAnswer.setQuestion(question);
        newAnswer.setSelectedAnswerOption(selectedAnswerOption);
        newAnswer.setCorrect(request.getIsCorrect()); // Status benar/salah dari request (asumsi klien sudah tahu)
        newAnswer.setTimeTakenSeconds(request.getTimeTakenSeconds());

        UserQuestionAnswer savedAnswer = userQuestionAnswerRepository.save(newAnswer);

        return mapToUserQuestionAnswerResponse(savedAnswer);
    }

    /**
     * Mengambil jawaban pengguna per pertanyaan berdasarkan ID.
     * @param id ID jawaban pengguna.
     * @return Optional<UserQuestionAnswerResponse> DTO jika ditemukan.
     */
    @Transactional(readOnly = true)
    public Optional<UserQuestionAnswerResponse> getUserQuestionAnswerById(UUID id) {
        return userQuestionAnswerRepository.findById(id)
                .map(this::mapToUserQuestionAnswerResponse);
    }

    /**
     * Mengambil semua jawaban pengguna untuk sebuah sesi kuis tertentu.
     * @param quizAttemptId ID sesi kuis.
     * @return List<UserQuestionAnswerResponse> daftar jawaban.
     * @throws EntityNotFoundException jika QuizAttempt yang diacu tidak ditemukan.
     */
    @Transactional(readOnly = true)
    public List<UserQuestionAnswerResponse> getAnswersByQuizAttemptId(UUID quizAttemptId) {
        QuizAttempt quizAttempt = quizAttemptRepository.findById(quizAttemptId)
                .orElseThrow(() -> new EntityNotFoundException("Quiz Attempt dengan ID " + quizAttemptId + " tidak ditemukan."));

        return userQuestionAnswerRepository.findByQuizAttempt(quizAttempt).stream()
                .map(this::mapToUserQuestionAnswerResponse)
                .collect(Collectors.toList());
    }

    /**
     * Mengambil semua jawaban pengguna untuk pertanyaan tertentu.
     * @param questionId ID pertanyaan.
     * @return List<UserQuestionAnswerResponse> daftar jawaban.
     * @throws EntityNotFoundException jika Pertanyaan yang diacu tidak ditemukan.
     */
    @Transactional(readOnly = true)
    public List<UserQuestionAnswerResponse> getAnswersByQuestionId(UUID questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Pertanyaan dengan ID " + questionId + " tidak ditemukan."));

        return userQuestionAnswerRepository.findByQuestion(question).stream()
                .map(this::mapToUserQuestionAnswerResponse)
                .collect(Collectors.toList());
    }

    /**
     * Metode helper untuk mengkonversi entitas UserQuestionAnswer ke UserQuestionAnswerResponse DTO.
     * @param answer Entitas UserQuestionAnswer.
     * @return UserQuestionAnswerResponse DTO.
     */
    private UserQuestionAnswerResponse mapToUserQuestionAnswerResponse(UserQuestionAnswer answer) {
        return UserQuestionAnswerResponse.builder()
                .id(answer.getId())
                .quizAttemptId(answer.getQuizAttempt().getId())
                .questionId(answer.getQuestion().getId())
                .questionText(answer.getQuestion().getText())
                .selectedAnswerOptionId(answer.getSelectedAnswerOption().getId())
                .selectedAnswerOptionText(answer.getSelectedAnswerOption().getText())
                .isCorrect(answer.isCorrect())
                .timeTakenSeconds(answer.getTimeTakenSeconds())
                .build();
    }
}