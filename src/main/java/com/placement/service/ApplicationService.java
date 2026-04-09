package com.placement.service;

import com.placement.dto.ApplicationDTO;
import com.placement.model.Application;
import com.placement.model.Job;
import com.placement.repository.ApplicationRepository;
import com.placement.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private InterviewService interviewService;

    public List<ApplicationDTO> getAllApplications() {
        return applicationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ApplicationDTO> getApplicationsByStudent(Long studentId) {
        return applicationRepository.findByStudentId(studentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ApplicationDTO createApplication(ApplicationDTO dto) {
        Application app = new Application();
        app.setJobId(dto.getJobId());
        app.setStudentId(dto.getStudentId());
        app.setStudentName(dto.getStudentName());
        app.setJobTitle(dto.getJobTitle());
        app.setCompanyName(dto.getCompanyName());
        app.setStatus("applied");
        app.setResumeUrl(dto.getResumeUrl()); // Added to fix blank resume issue
        app.setAppliedAt(LocalDate.now());
        
        Application savedApp = applicationRepository.save(app);

        // Notify employer
        Job job = jobRepository.findById(dto.getJobId()).orElse(null);
        if (job != null) {
            notificationService.createNotification(
                job.getEmployerId(),
                "APPLICATION",
                "New application from " + dto.getStudentName() + " for " + job.getTitle(),
                savedApp.getId()
            );

            // Notify student
            notificationService.createNotification(
                dto.getStudentId(),
                "APPLICATION_SUCCESS",
                "Successfully applied for " + job.getTitle() + " at " + job.getCompanyName(),
                savedApp.getId()
            );
        }
        
        return convertToDTO(savedApp);
    }

    @Transactional
    public ApplicationDTO updateStatus(Long id, String status) {
        Application app = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + id));
        app.setStatus(status);
        Application updatedApp = applicationRepository.save(app);

        // Notify student with detailed message
        String message;
        switch (status.toLowerCase()) {
            case "shortlisted":
                message = "Congratulations! You have been shortlisted for " + updatedApp.getJobTitle() + " at " + updatedApp.getCompanyName() + ". Keep an eye out for interview details.";
                break;
            case "selected":
                message = "Great news! You have been SELECTED for " + updatedApp.getJobTitle() + " at " + updatedApp.getCompanyName() + ". Welcome to the team!";
                break;
            case "rejected":
                message = "Thank you for your interest in " + updatedApp.getJobTitle() + " at " + updatedApp.getCompanyName() + ". Unfortunately, we have decided to move forward with other candidates at this time.";
                break;
            case "interview":
                message = "You have been invited for an INTERVIEW for " + updatedApp.getJobTitle() + " at " + updatedApp.getCompanyName() + ". Please check your dashboard for the schedule.";
                break;
            default:
                message = "Your application for " + updatedApp.getJobTitle() + " at " + updatedApp.getCompanyName() + " has been updated to: " + status;
        }

        notificationService.createNotification(
            updatedApp.getStudentId(),
            "STATUS_UPDATE",
            message,
            updatedApp.getId()
        );

        // Auto-schedule interview record if status is interview
        if ("interview".equalsIgnoreCase(status)) {
            Job job = jobRepository.findById(updatedApp.getJobId()).orElse(null);
            if (job != null) {
                com.placement.dto.InterviewDTO interviewDTO = new com.placement.dto.InterviewDTO();
                interviewDTO.setApplicationId(updatedApp.getId());
                interviewDTO.setStudentId(updatedApp.getStudentId());
                interviewDTO.setEmployerId(job.getEmployerId());
                interviewDTO.setJobTitle(updatedApp.getJobTitle());
                interviewDTO.setScheduledAt(java.time.LocalDateTime.now().plusDays(2)); // Default to 2 days later
                interviewDTO.setLocation("Online - Google Meet");
                interviewService.scheduleInterview(interviewDTO);
            }
        }

        return convertToDTO(updatedApp);
    }

    private ApplicationDTO convertToDTO(Application app) {
        ApplicationDTO dto = new ApplicationDTO();
        dto.setId(app.getId());
        dto.setJobId(app.getJobId());
        dto.setStudentId(app.getStudentId());
        dto.setStudentName(app.getStudentName());
        dto.setJobTitle(app.getJobTitle());
        dto.setCompanyName(app.getCompanyName());
        dto.setStatus(app.getStatus());
        dto.setResumeUrl(app.getResumeUrl());
        dto.setAppliedAt(app.getAppliedAt());
        return dto;
    }
}
 
