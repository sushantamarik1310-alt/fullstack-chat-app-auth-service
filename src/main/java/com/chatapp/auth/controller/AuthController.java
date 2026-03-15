package com.chatapp.auth.controller;

import com.chatapp.auth.dto.AuthResponse;
import com.chatapp.auth.dto.LoginRequest;
import com.chatapp.auth.dto.SignUpRequest;
import com.chatapp.auth.dto.UpdateProfileRequest;
import com.chatapp.auth.model.User;
import com.chatapp.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignUpRequest request) {
        AuthResponse response = authService.signup(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@RequestHeader("Authorization") String token) {
        try {
            // Extract token from "Bearer <token>"
            String jwt = token.replace("Bearer ", "");
            String userId = authService.verifyToken(jwt);
            
            User user = authService.getUserById(userId);
            if (user != null) {
                user.setPassword(null); // Never return password
                return ResponseEntity.ok(user);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<User> getUser(@PathVariable String userId) {
        User user = authService.getUserById(userId);
        if (user != null) {
            user.setPassword(null); // Never return password
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/verify-token")
    public ResponseEntity<String> verifyToken(@RequestHeader("Authorization") String token) {
        try {
            String jwt = token.replace("Bearer ", "");
            authService.verifyToken(jwt);
            return ResponseEntity.ok("Token is valid");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        try {
            String jwt = token.replace("Bearer ", "");
            authService.verifyToken(jwt);
            // In a real app, you'd add the token to a blacklist here
            return ResponseEntity.ok("Logout successful. Please clear your token on client side.");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token");
        }
    }

    @PutMapping("/update-profile")
    public ResponseEntity<User> updateProfile(@RequestHeader("Authorization") String token,
                                               @RequestBody UpdateProfileRequest request) {
        try {
            String jwt = token.replace("Bearer ", "");
            String userId = authService.verifyToken(jwt);
            
            User updatedUser = authService.updateProfile(userId, request);
            updatedUser.setPassword(null); // Never return password
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(null);
        }
    }
}
