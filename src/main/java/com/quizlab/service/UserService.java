package com.quizlab.service;


import com.quizlab.dto.UserRegisterRequest;
import com.quizlab.dto.UserResponse;
import com.quizlab.model.User;
import com.quizlab.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> findUserById(UUID id) {
        return userRepository.findById(id);
    }

//    public UserResponse registerUser(UserRegisterRequest request) {
//        if (userRepository.findByUsername(request.getUsername().isPresent())) {
//            throw new RuntimeException("Username '" + request.getUsername())
//        }
//    }
}
