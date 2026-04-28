package com.placement.controller;

import com.placement.model.User;
import com.placement.repository.UserRepository;
import com.placement.repository.JobRepository;
import com.placement.repository.ApplicationRepository;
import com.placement.repository.PlacementRepository;
import com.placement.model.Job;
import com.placement.model.Application;
import com.placement.model.Placement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/debug")
@CrossOrigin(origins = {"http://localhost:5173", "https://skill-shala.netlify.app"})
public class DebugController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private PlacementRepository placementRepository;

    @GetMapping("/users")
    public List<String> getAllUsers() {
        return userRepository.findAll().stream()
                .map(u -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append("ID: ").append(u.getId())
                      .append(" | Name: ").append(u.getName())
                      .append(" | Email: ").append(u.getEmail())
                      .append(" | Role: ").append(u.getRole())
                      .append(" | PwdHashed: ").append(u.getPassword() != null && u.getPassword().startsWith("$2a$"))
                      .append(" | Uni: ").append(u.getUniversity())
                      .append(" | Dept: ").append(u.getDepartment())
                      .append(" | Grad: ").append(u.getGraduationYear());
                    return sb.toString();
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/jobs")
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @GetMapping("/applications")
    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    @GetMapping("/placements")
    public List<Placement> getAllPlacements() {
        return placementRepository.findAll();
    }

    @GetMapping("/repair-status")
    public String getRepairStatus() {
        long count = userRepository.findAll().stream()
            .filter(u -> u.getEmail() == null || u.getEmail().trim().isEmpty())
            .count();
        return "Users with corrupted emails: " + count;
    }

    @PostMapping("/reset-password/{id}")
    public String resetPassword(@PathVariable Long id, @org.springframework.web.bind.annotation.RequestBody String newPassword) {
        User u = userRepository.findById(id).orElseThrow();
        u.setPassword(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode(newPassword));
        userRepository.save(u);
        return "Password reset successful for user " + id;
    }
}
