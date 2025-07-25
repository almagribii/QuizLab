package com.quizlab.service;


import com.quizlab.dto.UserLoginRequest;
import com.quizlab.dto.UserRegisterRequest;
import com.quizlab.dto.UserResponse;
import com.quizlab.model.User;
import com.quizlab.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public Optional<User> findUserById(UUID id) {
        return userRepository.findById(id);
    }

    public UserResponse registerUser(UserRegisterRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username '" + request.getUsername());
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPasswordHash(hashedPassword);
        newUser.setCreateAt(LocalDateTime.now());

        User savedUser = userRepository.save(newUser);

        return UserResponse.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .displayName(savedUser.getDisplayName())
                .bio(savedUser.getBio())
                .profilePictureUrl(savedUser.getProfilePictureUrl())
                .createdAt(savedUser.getCreateAt())
                .updateAt(savedUser.getUpdateAt())
                .build();
    }

    public Optional<UserResponse> authenticateUser(UserLoginRequest request) {
        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
                return Optional.of(UserResponse.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .displayName(user.getDisplayName())
                        .bio(user.getBio())
                        .profilePictureUrl(user.getProfilePictureUrl())
                        .createdAt(user.getCreateAt())
                        .updateAt(user.getUpdateAt())
                        .build());
            }
        }
        return Optional.empty();
    }
}
