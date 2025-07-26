// src/main/java/com/quizlab/service/CategoryService.java
package com.quizlab.service;

import com.quizlab.dto.CategoryRequest;
import com.quizlab.dto.CategoryResponse;
import com.quizlab.model.Category;
import com.quizlab.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * Membuat kategori baru.
     * @param request DTO yang berisi nama dan deskripsi kategori.
     * @return CategoryResponse DTO dari kategori yang berhasil dibuat.
     * @throws RuntimeException jika nama kategori sudah ada.
     */
    public CategoryResponse createCategory(CategoryRequest request) {
        // 1. Cek apakah nama kategori sudah ada
        if (categoryRepository.findByName(request.getName()).isPresent()) { // <--- getName() dari Lombok
            throw new RuntimeException("Nama kategori '" + request.getName() + "' sudah ada.");
        }

        // 2. Buat objek Category dari DTO Request
        Category newCategory = new Category();
        newCategory.setName(request.getName()); // <--- getName() dari Lombok
        newCategory.setDescription(request.getDescription()); // <--- getDescription() dari Lombok

        // 3. Simpan kategori ke database
        Category savedCategory = categoryRepository.save(newCategory);

        // 4. Konversi entitas Category yang disimpan ke DTO Response dan kembalikan
        return mapToCategoryResponse(savedCategory); // <--- INI ADALAH STATEMENT RETURN YANG DICARI COMPILER
    }

    /**
     * Mengambil daftar semua kategori.
     * @return List<CategoryResponse> daftar kategori.
     */
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapToCategoryResponse)
                .collect(Collectors.toList());
    }

    /**
     * Mengambil kategori berdasarkan ID.
     * @param id ID kategori.
     * @return Optional<CategoryResponse> DTO kategori jika ditemukan.
     */
    @Transactional(readOnly = true)
    public Optional<CategoryResponse> getCategoryById(UUID id) {
        return categoryRepository.findById(id)
                .map(this::mapToCategoryResponse);
    }

    /**
     * Memperbarui kategori yang sudah ada.
     * @param id ID kategori yang akan diperbarui.
     * @param request DTO dengan data kategori yang baru.
     * @return Optional<CategoryResponse> DTO kategori yang diperbarui jika ditemukan.
     * @throws RuntimeException jika kategori tidak ditemukan atau nama kategori duplikat.
     */
    public Optional<CategoryResponse> updateCategory(UUID id, CategoryRequest request) {
        return categoryRepository.findById(id).map(existingCategory -> {
            // Cek duplikasi nama jika nama berubah dan sudah ada di kategori lain
            if (!existingCategory.getName().equals(request.getName())) {
                if (categoryRepository.findByName(request.getName()).isPresent()) {
                    throw new RuntimeException("Nama kategori '" + request.getName() + "' sudah ada.");
                }
            }

            existingCategory.setName(request.getName());
            existingCategory.setDescription(request.getDescription());

            Category updatedCategory = categoryRepository.save(existingCategory);
            return mapToCategoryResponse(updatedCategory);
        });
    }

    /**
     * Menghapus kategori berdasarkan ID.
     * @param id ID kategori yang akan dihapus.
     * @return true jika berhasil dihapus, false jika kategori tidak ditemukan.
     */
    public boolean deleteCategory(UUID id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Metode helper untuk mengkonversi entitas Category ke CategoryResponse DTO.
     * @param category Entitas Category.
     * @return CategoryResponse DTO.
     */
    private CategoryResponse mapToCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}