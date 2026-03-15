package com.chatapp.auth.service;

import com.chatapp.auth.dto.AuthResponse;
import com.chatapp.auth.dto.LoginRequest;
import com.chatapp.auth.dto.SignUpRequest;
import com.chatapp.auth.dto.UpdateProfileRequest;
import com.chatapp.auth.model.User;
import com.chatapp.auth.repository.UserRepository;
import com.chatapp.auth.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse signup(SignUpRequest request) {
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            return AuthResponse.builder()
                    .message("Email already exists")
                    .build();
        }

        // Create new user
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);
        String token = jwtUtil.generateToken(savedUser.getId());

        // Clear password before returning
        savedUser.setPassword(null);

        return AuthResponse.builder()
                .token(token)
                .user(savedUser)
                .message("User registered successfully")
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return AuthResponse.builder()
                    .message("Invalid email or password")
                    .build();
        }

        String token = jwtUtil.generateToken(user.getId());

        // Clear password before returning
        user.setPassword(null);

        return AuthResponse.builder()
                .token(token)
                .user(user)
                .message("Login successful")
                .build();
    }

    public User getUserById(String userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public String verifyToken(String token) {
        if (!jwtUtil.validateToken(token)) {
            throw new RuntimeException("Invalid or expired token");
        }
        return jwtUtil.extractUserId(token);
    }

    public User updateProfile(String userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getFullName() != null && !request.getFullName().isEmpty()) {
            user.setFullName(request.getFullName());
        }

        if (request.getProfilePic() != null && !request.getProfilePic().isEmpty()) {
            user.setProfilePic(request.getProfilePic());
        }

        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }
}
