//// src/main/java/com/quizlab/api/service/UserService.java
//package com.quizlab;
//
//import com.quizlab.dto.UserRegisterRequest; // Import DTO input register
//import com.quizlab.dto.UserLoginRequest;   // Import DTO input login
//import com.quizlab.dto.UserResponse;      // Import DTO output
//import com.quizlab.model.User;           // Import entitas User
//import com.quizlab.repository.UserRepository; // Import UserRepository
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Import BCryptPasswordEncoder
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//@Transactional // Semua method publik di sini akan dalam transaksi DB
//public class UserService {
//
//    private final UserRepository userRepository;
//    private final BCryptPasswordEncoder passwordEncoder; // Injeksi BCryptPasswordEncoder
//
//    // Contoh method sederhana untuk mencari user berdasarkan ID
//    public Optional<User> findUserById(UUID id) {
//        return userRepository.findById(id);
//    }
//
//    /**
//     * Mendaftarkan user baru ke sistem.
//     * Melakukan hashing password dan menyimpan user ke database.
//     * @param request DTO yang berisi data username dan password untuk registrasi.
//     * @return UserResponse DTO dari user yang berhasil didaftarkan.
//     * @throws RuntimeException jika username atau email sudah terdaftar.
//     */
//    public UserResponse registerUser(UserRegisterRequest request) {
//        // 1. Cek apakah username sudah terdaftar
//        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
//            throw new RuntimeException("Username '" + request.getUsername() + "' sudah terdaftar.");
//        }
//        // Jika Anda punya email di UserRegisterRequest dan ingin unik:
//        // if (userRepository.findByEmail(request.getEmail()).isPresent()) {
//        //     throw new RuntimeException("Email '" + request.getEmail() + "' sudah terdaftar.");
//        // }
//
//        // 2. Hash password
//        String hashedPassword = passwordEncoder.encode(request.getPassword());
//
//        // 3. Buat objek User dari DTO Request
//        User newUser = new User();
//        newUser.setUsername(request.getUsername());
//        newUser.setPasswordHash(hashedPassword);
//        // Field lain (email, displayName, bio, profilePictureUrl) bisa diatur di sini
//        // jika ada di UserRegisterRequest atau punya nilai default.
//        // Untuk sekarang, mereka akan null kecuali diset.
//        newUser.setCreatedAt(LocalDateTime.now()); // @PrePersist akan mengisi ini juga, tapi tidak masalah diset di sini.
//        // @PrePersist akan memastikan ia TIDAK NULL jika kita lupa set di sini.
//
//        // 4. Simpan user ke database
//        User savedUser = userRepository.save(newUser);
//
//        // 5. Konversi entitas User yang disimpan ke DTO Response dan kembalikan
//        return UserResponse.builder()
//                .id(savedUser.getId())
//                .username(savedUser.getUsername())
//                .email(savedUser.getEmail()) // Akan null jika tidak ada di request atau tidak diset
//                .displayName(savedUser.getDisplayName()) // Akan null jika tidak diset
//                .bio(savedUser.getBio()) // Akan null jika tidak diset
//                .profilePictureUrl(savedUser.getProfilePictureUrl()) // Akan null jika tidak diset
//                .createdAt(savedUser.getCreatedAt())
//                .updatedAt(savedUser.getUpdatedAt())
//                .build();
//    }
//
//    /**
//     * Melakukan otentikasi user.
//     * Memverifikasi username dan password.
//     * @param request DTO yang berisi data username dan password untuk login.
//     * @return Optional<UserResponse> DTO dari user yang berhasil diotentikasi, atau Optional.empty() jika gagal.
//     */
//    public Optional<UserResponse> authenticateUser(UserLoginRequest request) {
//        // 1. Cari user berdasarkan username
//        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());
//
//        // 2. Jika user ditemukan, verifikasi password
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//            // passwordEncoder.matches(rawPassword, encodedPassword) membandingkan password mentah dengan hash
//            if (passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
//                // Password cocok, user berhasil diotentikasi
//                return Optional.of(UserResponse.builder()
//                        .id(user.getId())
//                        .username(user.getUsername())
//                        .email(user.getEmail())
//                        .displayName(user.getDisplayName())
//                        .bio(user.getBio())
//                        .profilePictureUrl(user.getProfilePictureUrl())
//                        .createdAt(user.getCreatedAt())
//                        .updatedAt(user.getUpdatedAt())
//                        .build());
//            }
//        }
//        // Jika user tidak ditemukan atau password tidak cocok
//        return Optional.empty();
//    }
//}