package com.placement.service;

import com.placement.dto.LoginRequest;
import com.placement.dto.LoginResponse;
import jakarta.annotation.PostConstruct;
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

    @PostConstruct
    public void repairCorruptedAccounts() {
        System.out.println("[REPAIR] [STATUS] Starting scan for corrupted accounts...");
        userRepository.findAll().forEach(user -> {
            boolean needsUpdate = false;
            if (user.getEmail() == null || user.getEmail().trim().isEmpty() || user.getEmail().equalsIgnoreCase("null")) {
                String fallbackEmail = (user.getName() != null && !user.getName().trim().isEmpty() ? 
                    user.getName().toLowerCase().replaceAll("\\s+", "") + user.getId() : "user" + user.getId()) + "@gmail.com";
                System.out.println("[REPAIR] [ACTION] User ID " + user.getId() + " was corrupted. Restoring email to: [" + fallbackEmail + "]");
                user.setEmail(fallbackEmail);
                needsUpdate = true;
            }
            if (user.getRole() == null || user.getRole().trim().isEmpty()) {
                System.out.println("[REPAIR] [ACTION] User ID " + user.getId() + " had no role. Setting default to STUDENT.");
                user.setRole("STUDENT");
                needsUpdate = true;
            }
            if (needsUpdate) {
                userRepository.save(user);
            }
        });
        System.out.println("[REPAIR] [SUCCESS] Scan and repair complete.");
    }

    public LoginResponse login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail() != null ? loginRequest.getEmail().trim().toLowerCase() : "";
        System.out.println("[DEBUG] [LOGIN_START] Attempting login for email: [" + email + "]");
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    System.out.println("[DEBUG] [LOGIN_FAIL] User not found in database for email: [" + email + "]");
                    return new RuntimeException("User not found");
                });

        System.out.println("[DEBUG] [LOGIN_STEP] User found: " + user.getName() + " (ID: " + user.getId() + ")");

        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            System.out.println("[DEBUG] [LOGIN_SUCCESS] Password matched for " + email);
            
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
            System.out.println("[DEBUG] [LOGIN_COMPLETE] Token generated for role: " + role);
            return new LoginResponse(token, user);
        } else {
            System.out.println("[DEBUG] [LOGIN_FAIL] Password mismatch for user: [" + email + "]");
            throw new RuntimeException("Incorrect password");
        }
    }

    public void register(com.placement.dto.RegisterRequest request) {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email is required for registration.");
        }
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new RuntimeException("Full name is required.");
        }
        System.out.println("[DEBUG] Processing registration for: " + request.getEmail());
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setName(request.getName().trim());
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole().toUpperCase() : "STUDENT");
        
        // Explicitly set extra fields based on role
        if ("STUDENT".equalsIgnoreCase(user.getRole())) {
            // If we have university in request, we could store it in a generic field or future column.
            // For now, let's keep it simple or use a placeholder if needed.
        } else if ("EMPLOYER".equalsIgnoreCase(user.getRole())) {
            user.setCompanyName(request.getCompanyName());
        }

        // Generate avatar initials if none provided
        String initials = "";
        String[] parts = user.getName().split("\\s+");
        for (String part : parts) {
            if (!part.isEmpty()) initials += part.substring(0, 1).toUpperCase();
        }
        user.setAvatar(initials.length() > 2 ? initials.substring(0, 2) : initials);

        User savedUser = userRepository.save(user);
        System.out.println("[DEBUG] User registered successfully with ID: " + savedUser.getId() + " and Role: " + savedUser.getRole());
    }
}
 
