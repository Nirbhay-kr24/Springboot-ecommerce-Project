package com.SpringCart.ecommerce.controller;

import com.SpringCart.ecommerce.enums.Role;
import com.SpringCart.ecommerce.model.User;
import com.SpringCart.ecommerce.repository.UserRepository;
import com.SpringCart.ecommerce.response.ApiResponse;
import com.SpringCart.ecommerce.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${admin.secret}")
    private String adminSecret;

    // ================= USER REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse("Email already exists", null));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);

        return ResponseEntity.ok(
                new ApiResponse("User registered successfully", null)
        );
    }

    // ================= ADMIN REGISTER =================
    @PostMapping("/register/admin")
    public ResponseEntity<ApiResponse> registerAdmin(
            @RequestBody User user,
            @RequestParam String secret
    ) {

        if (!adminSecret.equals(secret)) {
            return ResponseEntity.status(403)
                    .body(new ApiResponse("Invalid admin secret", null));
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse("Email already exists", null));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ADMIN);

        userRepository.save(user);

        return ResponseEntity.ok(
                new ApiResponse("Admin registered successfully", null)
        );
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody User user) {

        User dbUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email"));

        if (!passwordEncoder.matches(
                user.getPassword(),
                dbUser.getPassword()
        )) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(
                dbUser.getEmail(),
                dbUser.getRole().name()
        );

        return ResponseEntity.ok(
                new ApiResponse("Login successful", token)
        );
    }
}
