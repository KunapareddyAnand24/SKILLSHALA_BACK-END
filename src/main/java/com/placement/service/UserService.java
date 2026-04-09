package com.placement.service;

import com.placement.dto.UserDTO;
import com.placement.model.User;
import com.placement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return convertToDTO(user);
    }

    public UserDTO updateUser(Long id, UserDTO userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        if (userDetails.getName() != null && !userDetails.getName().trim().isEmpty()) {
            user.setName(userDetails.getName().trim());
        }
        if (userDetails.getEmail() != null && !userDetails.getEmail().trim().isEmpty()) {
            user.setEmail(userDetails.getEmail().trim().toLowerCase());
        }
        if (userDetails.getRole() != null && !userDetails.getRole().trim().isEmpty()) {
            user.setRole(userDetails.getRole().trim().toUpperCase());
        }
        
        if (userDetails.getAvatar() != null) user.setAvatar(userDetails.getAvatar());
        if (userDetails.getCompanyName() != null) user.setCompanyName(userDetails.getCompanyName());
        if (userDetails.getStatus() != null) user.setStatus(userDetails.getStatus());
        if (userDetails.getCgpa() != null) user.setCgpa(userDetails.getCgpa());
        if (userDetails.getSkills() != null) user.setSkills(userDetails.getSkills());
        if (userDetails.getResumeUrl() != null) user.setResumeUrl(userDetails.getResumeUrl());
        if (userDetails.getUniversity() != null) user.setUniversity(userDetails.getUniversity());
        if (userDetails.getDepartment() != null) user.setDepartment(userDetails.getDepartment());
        if (userDetails.getGraduationYear() != null) user.setGraduationYear(userDetails.getGraduationYear());
        
        System.out.println("[DEBUG] User updated successfully: " + user.getEmail() + " | Role: " + user.getRole());
        return convertToDTO(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setAvatar(user.getAvatar());
        dto.setStatus(user.getStatus());
        dto.setCompanyName(user.getCompanyName());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setCgpa(user.getCgpa());
        dto.setSkills(user.getSkills());
        dto.setResumeUrl(user.getResumeUrl());
        dto.setUniversity(user.getUniversity());
        dto.setDepartment(user.getDepartment());
        dto.setGraduationYear(user.getGraduationYear());
        return dto;
    }
}
 
