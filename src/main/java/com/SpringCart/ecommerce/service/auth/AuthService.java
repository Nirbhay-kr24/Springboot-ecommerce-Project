package com.SpringCart.ecommerce.service.auth;

import com.SpringCart.ecommerce.enums.Role;
import com.SpringCart.ecommerce.exceptions.AlreadyExistsException;
import com.SpringCart.ecommerce.model.User;
import com.SpringCart.ecommerce.repository.UserRepository;
import com.SpringCart.ecommerce.request.AdminRegisterRequest;
import com.SpringCart.ecommerce.request.LoginRequest;
import com.SpringCart.ecommerce.request.RegisterRequest;
import com.SpringCart.ecommerce.service.cart.ICartService;
import com.SpringCart.ecommerce.service.email.EmailService;
import com.SpringCart.ecommerce.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final ICartService cartService; // ✅ Injected to link carts

    @Value("${admin.secret}")
    private String adminSecret;

    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("Email already registered");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.CUSTOMER);

        // 1. Save the User
        User savedUser = userRepository.save(user);

        // 2. ✅ Initialize a cart linked to this user
        cartService.initializeNewCart(savedUser);

        emailService.sendEmail(
                user.getEmail(),
                "Welcome to SpringCart 🎉",
                "Hi " + user.getFirstName() + ",\n\nYour account is ready."
        );
    }

    @Transactional
    public void registerAdmin(AdminRegisterRequest request) {
        if (!adminSecret.equals(request.getAdminSecret())) {
            throw new IllegalArgumentException("Invalid admin secret");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("Email already registered");
        }

        User admin = new User();
        admin.setFirstName(request.getFirstName());
        admin.setLastName(request.getLastName());
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setRole(Role.ADMIN);

        // 1. Save the Admin
        User savedAdmin = userRepository.save(admin);

        // 2. ✅ Initialize a cart for Admin (optional, but prevents errors)
        cartService.initializeNewCart(savedAdmin);

        emailService.sendEmail(
                admin.getEmail(),
                "Admin Access Granted 🔐",
                "Hi " + admin.getFirstName() + ",\nYou are now an ADMIN."
        );
    }

    public String login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtUtil.generateToken(user.getEmail(), user.getRole().name());
    }
}