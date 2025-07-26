package com.quizlab.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {
    @NotBlank(message = "Nama Kategori tidak boleh kosong")
    @Size(min = 2 ,max = 50, message = "Nama Kategori harus antara 2 sampai 50 karakter")
    private String name;

    @Size(max = 255, message = "Deskripsi kategory maksimal 255 karakter")
    private String description;

//    public String getName(){return name;}
}