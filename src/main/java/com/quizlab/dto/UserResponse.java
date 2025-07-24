package com.quizlab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.sql.rowset.serial.SerialStruct;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private UUID id;
    private String username;
    private String email;
    private String displayName;
    private String bio;
    private String profilePictureUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;

    // Field tambahan yang mungkin dikirim setelah login berhasil (misalnya JWT token)
    // Akan ditambahkan nanti di DTO terpisah jika kita ingin respons login lebih spesifik,
    // atau bisa juga di sini jika kita ingin UserResponse selalu membawa token setelah login.
    // private String jwtToken;
}
