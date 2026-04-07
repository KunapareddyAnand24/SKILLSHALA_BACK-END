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

    public List<ApplicationDTO> getAllApplications() {
        return applicationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ApplicationDTO> getApplicationsByStudent(Long studentId) {
        // Assume repository has this, otherwise we add it.
        return applicationRepository.findAll().stream()
                .filter(a -> a.getStudentId().equals(studentId))
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
        }
        
        return convertToDTO(savedApp);
    }

    @Transactional
    public ApplicationDTO updateStatus(Long id, String status) {
        Application app = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + id));
        app.setStatus(status);
        Application updatedApp = applicationRepository.save(app);

        // Notify student
        notificationService.createNotification(
            updatedApp.getStudentId(),
            "STATUS_UPDATE",
            "Your application for " + updatedApp.getJobTitle() + " has been updated to: " + status,
            updatedApp.getId()
        );

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
        dto.setAppliedAt(app.getAppliedAt());
        return dto;
    }
}
 
