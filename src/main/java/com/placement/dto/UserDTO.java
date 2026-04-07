package com.placement.dto;

import java.time.LocalDate;

public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String avatar;
    private String resumeUrl;
    private String skills;
    private String status;
    private String companyName;
    private LocalDate createdAt;
    private LocalDate lastLoginAt;

    public UserDTO() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public String getResumeUrl() { return resumeUrl; }
    public void setResumeUrl(String resumeUrl) { this.resumeUrl = resumeUrl; }
    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
    public LocalDate getLastLoginAt() { return lastLoginAt; }
    public void setLastLoginAt(LocalDate lastLoginAt) { this.lastLoginAt = lastLoginAt; }
}
 
