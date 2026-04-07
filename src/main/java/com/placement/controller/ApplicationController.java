package com.placement.controller;

import com.placement.dto.ApplicationDTO;
import com.placement.model.Job;
import com.placement.model.User;
import com.placement.repository.JobRepository;
import com.placement.repository.UserRepository;
import com.placement.service.ApplicationService;
import com.placement.service.MatchService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = {"http://localhost:5173", "https://skill-shala.netlify.app"})
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private MatchService matchService;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('OFFICER', 'ADMIN', 'EMPLOYER')")
    public List<ApplicationDTO> getAllApplications() {
        return applicationService.getAllApplications();
    }

    /**
     * Employer filter: Get applications for a job and calculate skill match percentage.
     */
    @GetMapping("/job/{jobId}/match")
    @PreAuthorize("hasAnyRole('EMPLOYER', 'ADMIN', 'OFFICER')")
    public List<Map<String, Object>> getApplicationsWithMatch(@PathVariable Long jobId) {
        Job job = jobRepository.findById(jobId).orElseThrow();
        List<ApplicationDTO> apps = applicationService.getAllApplications().stream()
                .filter(a -> a.getJobId().equals(jobId))
                .collect(Collectors.toList());

        return apps.stream().map(app -> {
            User student = userRepository.findById(app.getStudentId()).orElseThrow();
            int matchScore = matchService.calculateMatch(student, job);
            
            return Map.of(
                "application", app,
                "matchScore", matchScore,
                "studentDetails", Map.of("skills", student.getSkills(), "email", student.getEmail())
            );
        }).collect(Collectors.toList());
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'OFFICER', 'ADMIN')")
    public List<ApplicationDTO> getByStudentId(@PathVariable Long studentId) {
        return applicationService.getApplicationsByStudent(studentId);
    }

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApplicationDTO> createApplication(@RequestBody ApplicationDTO applicationDTO) {
        return ResponseEntity.ok(applicationService.createApplication(applicationDTO));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('EMPLOYER', 'ADMIN', 'OFFICER')")
    public ResponseEntity<ApplicationDTO> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        ApplicationDTO updated = applicationService.updateStatus(id, body.get("status"));
        return ResponseEntity.ok(updated);
    }
}
 
