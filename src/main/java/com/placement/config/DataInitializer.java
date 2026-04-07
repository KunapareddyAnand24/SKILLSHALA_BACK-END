package com.placement.config;

import com.placement.model.User;
import com.placement.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // 1. ADMIN
            if (userRepository.findByEmail("demo@admin.com").isEmpty()) {
                User admin = new User();
                admin.setName("Admin User");
                admin.setEmail("demo@admin.com");
                admin.setPassword(passwordEncoder.encode("password123"));
                admin.setRole("ADMIN");
                admin.setAvatar("A");
                userRepository.save(admin);
                System.out.println("[DATA] Created demo admin user");
            }

            // 2. STUDENT
            if (userRepository.findByEmail("demo@student.com").isEmpty()) {
                User student = new User();
                student.setName("Student User");
                student.setEmail("demo@student.com");
                student.setPassword(passwordEncoder.encode("password123"));
                student.setRole("STUDENT");
                student.setAvatar("S");
                student.setSkills("Java, React, SQL");
                userRepository.save(student);
                System.out.println("[DATA] Created demo student user");
            }

            // 3. EMPLOYER
            if (userRepository.findByEmail("demo@employer.com").isEmpty()) {
                User employer = new User();
                employer.setName("Employer User");
                employer.setEmail("demo@employer.com");
                employer.setPassword(passwordEncoder.encode("password123"));
                employer.setRole("EMPLOYER");
                employer.setCompanyName("TechCorp");
                employer.setAvatar("E");
                userRepository.save(employer);
                System.out.println("[DATA] Created demo employer user");
            }

            // 4. OFFICER
            if (userRepository.findByEmail("demo@officer.com").isEmpty()) {
                User officer = new User();
                officer.setName("Officer User");
                officer.setEmail("demo@officer.com");
                officer.setPassword(passwordEncoder.encode("password123"));
                officer.setRole("OFFICER");
                officer.setAvatar("O");
                userRepository.save(officer);
                System.out.println("[DATA] Created demo officer user");
            }
        };
    }
}
