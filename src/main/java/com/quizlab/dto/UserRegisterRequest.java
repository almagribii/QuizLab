package com.quizlab.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {
    @NotBlank(message = "Username Tidak Boleh Kosong Akhi..")
    @Size(min = 3, max = 20, message = "Username harus antara 3 dan 20 karakter")
    private String username;

    @NotBlank(message = "Password Tidak Boleh Kosong Akhi..")
    @Size(min = 3, message = "Username harus antara 3 dan 20 karakter")
    private String password;
}
