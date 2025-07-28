package com.quizlab.service;

import com.quizlab.dto.AnswerOptionRequest;  // DTO input untuk pilihan jawaban
import com.quizlab.dto.AnswerOptionResponse; // DTO output untuk pilihan jawaban
import com.quizlab.model.AnswerOption;      // Entitas AnswerOption
import com.quizlab.model.Question;          // Entitas Question (induknya)
import com.quizlab.repository.AnswerOptionRepository; // Repositori untuk AnswerOption
import com.quizlab.repository.QuestionRepository;    // Repositori untuk Question (untuk validasi FK)
import jakarta.persistence.EntityNotFoundException; // Exception jika entitas tidak ditemukan
import lombok.RequiredArgsConstructor;       // Lombok untuk konstruktor
import org.springframework.stereotype.Service; // Anotasi @Service
import org.springframework.transaction.annotation.Transactional; // Untuk manajemen transaksi

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AnswerOptionService {

    private final AnswerOptionRepository answerOptionRepository;
    private final QuestionRepository questionRepository;

    /**
     * Membuat pilihan jawaban baru untuk sebuah pertanyaan.
     * @param request DTO yang berisi detail pilihan jawaban.
     * @return AnswerOptionResponse DTO dari pilihan jawaban yang berhasil dibuat.
     * @throws EntityNotFoundException jika pertanyaan (questionId) yang diacu tidak ditemukan.
     */
    public AnswerOptionResponse createAnswerOption(AnswerOptionRequest request) {
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new EntityNotFoundException("Pertanyaan dengan ID " + request.getQuestionId() + " tidak ditemukan."));

        AnswerOption newAnswerOption = new AnswerOption();
        newAnswerOption.setText(request.getText());
        newAnswerOption.setQuestion(question); // Set objek Question yang sudah ditemukan
        newAnswerOption.setCorrect(request.getIsCorrect()); // Set apakah ini jawaban benar
        newAnswerOption.setDisplayOrder(request.getDisplayOrder()); // Set urutan tampilan

        AnswerOption savedAnswerOption = answerOptionRepository.save(newAnswerOption);

        return mapToAnswerOptionResponse(savedAnswerOption);
    }

    /**
     * Mengambil daftar semua pilihan jawaban.
     * @return List<AnswerOptionResponse> daftar pilihan jawaban.
     */
    @Transactional(readOnly = true)
    public List<AnswerOptionResponse> getAllAnswerOptions() {
        return answerOptionRepository.findAll().stream()
                .map(this::mapToAnswerOptionResponse)
                .collect(Collectors.toList());
    }

    /**
     * Mengambil pilihan jawaban berdasarkan ID.
     * @param id ID pilihan jawaban.
     * @return Optional<AnswerOptionResponse> DTO pilihan jawaban jika ditemukan.
     */
    @Transactional(readOnly = true)
    public Optional<AnswerOptionResponse> getAnswerOptionById(UUID id) {
        return answerOptionRepository.findById(id)
                .map(this::mapToAnswerOptionResponse);
    }

    /**
     * Mengambil daftar pilihan jawaban untuk pertanyaan tertentu.
     * @param questionId ID pertanyaan.
     * @return List<AnswerOptionResponse> daftar pilihan jawaban untuk pertanyaan tersebut.
     * @throws EntityNotFoundException jika pertanyaan (questionId) yang diacu tidak ditemukan.
     */
    @Transactional(readOnly = true)
    public List<AnswerOptionResponse> getAnswerOptionsByQuestionId(UUID questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Pertanyaan dengan ID " + questionId + " tidak ditemukan."));

        return answerOptionRepository.findByQuestion(question).stream()
                .map(this::mapToAnswerOptionResponse)
                .collect(Collectors.toList());
    }

    /**
     * Memperbarui pilihan jawaban yang sudah ada.
     * @param id ID pilihan jawaban yang akan diperbarui.
     * @param request DTO dengan data pilihan jawaban yang baru.
     * @return Optional<AnswerOptionResponse> DTO pilihan jawaban yang diperbarui jika ditemukan.
     * @throws EntityNotFoundException jika pilihan jawaban (ID) atau pertanyaan (questionId) yang diacu tidak ditemukan.
     */
    public Optional<AnswerOptionResponse> updateAnswerOption(UUID id, AnswerOptionRequest request) {
        return answerOptionRepository.findById(id).map(existingOption -> {
            // Validasi: Pastikan pertanyaan (Question) yang diacu ada
            Question question = questionRepository.findById(request.getQuestionId())
                    .orElseThrow(() -> new EntityNotFoundException("Pertanyaan dengan ID " + request.getQuestionId() + " tidak ditemukan."));

            existingOption.setText(request.getText());
            existingOption.setQuestion(question);
            existingOption.setCorrect(request.getIsCorrect());
            existingOption.setDisplayOrder(request.getDisplayOrder());

            AnswerOption updatedOption = answerOptionRepository.save(existingOption);
            return mapToAnswerOptionResponse(updatedOption);
        });
    }

    /**
     * Menghapus pilihan jawaban berdasarkan ID.
     * @param id ID pilihan jawaban yang akan dihapus.
     * @return true jika berhasil dihapus, false jika pilihan jawaban tidak ditemukan.
     */
    public boolean deleteAnswerOption(UUID id) {
        if (answerOptionRepository.existsById(id)) {
            answerOptionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Metode helper untuk mengkonversi entitas AnswerOption ke AnswerOptionResponse DTO.
     * @param answerOption Entitas AnswerOption.
     * @return AnswerOptionResponse DTO.
     */
    private AnswerOptionResponse mapToAnswerOptionResponse(AnswerOption answerOption) {
        return AnswerOptionResponse.builder()
                .id(answerOption.getId())
                .text(answerOption.getText())
                .isCorrect(answerOption.isCorrect())
                .displayOrder(answerOption.getDisplayOrder())
                // Informasi Question terkait
                .questionId(answerOption.getQuestion().getId())
                .questionText(answerOption.getQuestion().getText())
                .build();
    }
}