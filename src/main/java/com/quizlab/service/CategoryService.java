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
    
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.findByName(request.getName()).isPresent()) {
            throw new RuntimeException("Nama kategori '" + request.getName() + "' sudah ada.");
        }

        Category newCategory = new Category();
        newCategory.setName(request.getName());
        newCategory.setDescription(request.getDescription());

        Category savedCategory = categoryRepository.save(newCategory);

        return mapToCategoryResponse(savedCategory);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapToCategoryResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<CategoryResponse> getCategoryById(UUID id) {
        return categoryRepository.findById(id)
                .map(this::mapToCategoryResponse);
    }

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

    public boolean deleteCategory(UUID id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }

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