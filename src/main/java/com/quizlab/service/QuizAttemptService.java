
package com.quizlab.service;

import com.quizlab.dto.QuizAttemptRequest;
import com.quizlab.dto.QuizAttemptResponse;
import com.quizlab.model.Category;
import com.quizlab.model.QuizAttempt;
import com.quizlab.model.User;
import com.quizlab.repository.CategoryRepository;
import com.quizlab.repository.QuizAttemptRepository;
import com.quizlab.repository.UserRepository;
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
public class QuizAttemptService {

    private final QuizAttemptRepository quizAttemptRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Mencatat percobaan kuis baru setelah selesai.
     * @param request DTO yang berisi detail percobaan kuis.
     * @return QuizAttemptResponse DTO dari percobaan kuis yang berhasil dicatat.
     * @throws EntityNotFoundException jika User atau Category yang diacu tidak ditemukan.
     */
    public QuizAttemptResponse createQuizAttempt(QuizAttemptRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Pengguna dengan ID " + request.getUserId() + " tidak ditemukan."));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Kategori dengan ID " + request.getCategoryId() + " tidak ditemukan."));

        QuizAttempt newAttempt = new QuizAttempt();
        newAttempt.setUser(user);
        newAttempt.setCategory(category);
        newAttempt.setScore(request.getScore());
        newAttempt.setTotalQuestions(request.getTotalQuestions());
        newAttempt.setDifficultyLevel(request.getDifficultyLevel());
        newAttempt.setStartTime(request.getStartTime());
        newAttempt.setEndTime(request.getEndTime());

        QuizAttempt savedAttempt = quizAttemptRepository.save(newAttempt);

        return mapToQuizAttemptResponse(savedAttempt);
    }

    /**
     * Mengambil daftar semua percobaan kuis.
     * @return List<QuizAttemptResponse> daftar percobaan kuis.
     */
    @Transactional(readOnly = true)
    public List<QuizAttemptResponse> getAllQuizAttempts() {
        return quizAttemptRepository.findAll().stream()
                .map(this::mapToQuizAttemptResponse)
                .collect(Collectors.toList());
    }

    /**
     * Mengambil percobaan kuis berdasarkan ID.
     * @param id ID percobaan kuis.
     * @return Optional<QuizAttemptResponse> DTO percobaan kuis jika ditemukan.
     */
    @Transactional(readOnly = true)
    public Optional<QuizAttemptResponse> getQuizAttemptById(UUID id) {
        return quizAttemptRepository.findById(id)
                .map(this::mapToQuizAttemptResponse);
    }

    /**
     * Mengambil daftar percobaan kuis yang dilakukan oleh user tertentu.
     * @param userId ID pengguna.
     * @return List<QuizAttemptResponse> daftar percobaan kuis oleh user tersebut.
     * @throws EntityNotFoundException jika User yang diacu tidak ditemukan.
     */
    @Transactional(readOnly = true)
    public List<QuizAttemptResponse> getQuizAttemptsByUserId(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Pengguna dengan ID " + userId + " tidak ditemukan."));

        return quizAttemptRepository.findByUser(user).stream()
                .map(this::mapToQuizAttemptResponse)
                .collect(Collectors.toList());
    }

    /**
     * Mengambil daftar percobaan kuis untuk kategori tertentu.
     * @param categoryId ID kategori.
     * @return List<QuizAttemptResponse> daftar percobaan kuis untuk kategori tersebut.
     * @throws EntityNotFoundException jika Kategori yang diacu tidak ditemukan.
     */
    @Transactional(readOnly = true)
    public List<QuizAttemptResponse> getQuizAttemptsByCategoryId(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Kategori dengan ID " + categoryId + " tidak ditemukan."));

        return quizAttemptRepository.findByCategory(category).stream()
                .map(this::mapToQuizAttemptResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<QuizAttemptResponse> getQuizAttemptsByUserIdAndCategoryId(UUID userId, UUID categoryId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Pengguna dengan ID " + userId + " tidak ditemukan."));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Kategori dengan ID " + categoryId + " tidak ditemukan."));

        return quizAttemptRepository.findByUserAndCategory(user, category).stream()
                .map(this::mapToQuizAttemptResponse)
                .collect(Collectors.toList());
    }

    /**
     * Metode helper untuk mengkonversi entitas QuizAttempt ke QuizAttemptResponse DTO.
     * @param quizAttempt Entitas QuizAttempt.
     * @return QuizAttemptResponse DTO.
     */
    private QuizAttemptResponse mapToQuizAttemptResponse(QuizAttempt quizAttempt) {
        return QuizAttemptResponse.builder()
                .id(quizAttempt.getId())
                .userId(quizAttempt.getUser().getId())
                .username(quizAttempt.getUser().getUsername())
                .categoryId(quizAttempt.getCategory().getId())
                .categoryName(quizAttempt.getCategory().getName())
                .score(quizAttempt.getScore())
                .totalQuestions(quizAttempt.getTotalQuestions())
                .difficultyLevel(quizAttempt.getDifficultyLevel())
                .startTime(quizAttempt.getStartTime())
                .endTime(quizAttempt.getEndTime())
                .build();
    }
}