package com.quizlab.service;

import com.quizlab.dto.QuestionRequest;
import com.quizlab.dto.QuestionResponse;
import com.quizlab.model.Category;
import com.quizlab.model.Question;
import com.quizlab.repository.CategoryRepository;
import com.quizlab.repository.QuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.Size;
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
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;

    public QuestionResponse createQuestion(QuestionRequest request){
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Kategori dengan Id:" + request.getCategoryId() + "tidak ditemukan"));

        Question newQuestion = new Question();
        newQuestion.setText(request.getText());
        newQuestion.setCategory(category);
        newQuestion.setDifficultyLevel(request.getDifficultyLevel());
        newQuestion.setExplanation(request.getExplanation());
        newQuestion.setActive(true);

        Question savedQuestion = questionRepository.save(newQuestion);
        return mapToQuestionResponse(savedQuestion);
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> getAllQuestions() {
        return questionRepository.findAll().stream()
                .map(this::mapToQuestionResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<QuestionResponse> getQuestionById(UUID id){
        return questionRepository.findById(id)
                .map(this::mapToQuestionResponse);
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> getQuestionsByCategoryId(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Kategori dengan ID " + categoryId + " tidak ditemukan."));

        return questionRepository.findByCategory(category).stream()
                .map(this::mapToQuestionResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> getQuestionsByDifficultyLevel(com.quizlab.model.DifficultyLevel difficultyLevel) {
        return questionRepository.findByDifficultyLevel(difficultyLevel).stream()
                .map(this::mapToQuestionResponse)
                .collect(Collectors.toList());
    }
    public Optional<QuestionResponse> updateQuestion(UUID id, QuestionRequest request) {
        return questionRepository.findById(id).map(existingQuestion -> {
            // Validasi: Pastikan kategori yang diacu ada (jika categoryId berubah atau selalu dikirim)
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Kategori dengan ID " + request.getCategoryId() + " tidak ditemukan."));

            existingQuestion.setText(request.getText());
            existingQuestion.setCategory(category);
            existingQuestion.setDifficultyLevel(request.getDifficultyLevel());
            existingQuestion.setExplanation(request.getExplanation());
            // isActive bisa diupdate melalui endpoint terpisah atau disertakan di request jika diinginkan
            // existingQuestion.setActive(request.isActive());

            Question updatedQuestion = questionRepository.save(existingQuestion);
            return mapToQuestionResponse(updatedQuestion);
        });
    }
    public boolean deleteQuestion(UUID id) {
        if (questionRepository.existsById(id)) {
            questionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<QuestionResponse> updateQuestionActiveStatus(UUID id, boolean isActive) {
        return questionRepository.findById(id).map(question -> {
            question.setActive(isActive);
            Question updatedQuestion = questionRepository.save(question);
            return mapToQuestionResponse(updatedQuestion);
        });
    }

    private QuestionResponse mapToQuestionResponse(Question question) {
        return QuestionResponse.builder()
                .id(question.getId())
                .text(question.getText())
                .categoryId(question.getCategory().getId()) // Ambil ID kategori dari objek Category
                .categoryName(question.getCategory().getName()) // Ambil Nama kategori dari objek Category
                .difficultyLevel(question.getDifficultyLevel())
                .explanation(question.getExplanation())
                .isActive(question.isActive())
                .createdAt(question.getCreatedAt())
                .updatedAt(question.getUpdatedAt())
                .build();
    }
}
