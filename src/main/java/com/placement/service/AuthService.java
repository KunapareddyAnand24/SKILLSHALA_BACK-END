package com.placement.service;

import com.placement.dto.LoginRequest;
import com.placement.dto.LoginResponse;
import com.placement.model.User;
import com.placement.model.ActivityLog;
import com.placement.repository.UserRepository;
import com.placement.repository.ActivityLogRepository;
import com.placement.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private ActivityLogRepository activityLogRepository;

    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            // Log Login Activity
            user.setLastLoginAt(LocalDate.now());
            userRepository.save(user);

            ActivityLog log = new ActivityLog();
            log.setUserId(user.getId());
            log.setUserEmail(user.getEmail());
            log.setAction("LOGIN");
            log.setDescription("User logged in at " + LocalDateTime.now());
            activityLogRepository.save(log);

            // FALLBACK TO STUDENT IF ROLE IS NULL
            String role = (user.getRole() != null && !user.getRole().isEmpty()) ? user.getRole() : "STUDENT";
            String token = jwtUtil.generateToken(user.getEmail(), role);
            return new LoginResponse(token, user);
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    public void register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
 
