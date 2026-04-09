package com.placement.controller;

import com.placement.dto.JobDTO;
import com.placement.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = {"http://localhost:5173", "https://skill-shala.netlify.app"})
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping
    public List<JobDTO> getAllJobs() {
        return jobService.getAllJobs();
    }

    @GetMapping("/employer/{employerId}")
    @PreAuthorize("hasAnyRole('EMPLOYER', 'OFFICER', 'ADMIN')")
    public List<JobDTO> getByEmployerId(@PathVariable Long employerId) {
        return jobService.getJobsByEmployer(employerId);
    }

    @GetMapping("/matched/{studentId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN', 'OFFICER')")
    public List<JobDTO> getMatchedJobs(@PathVariable Long studentId) {
        return jobService.getMatchedJobs(studentId);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('EMPLOYER', 'ADMIN')")
    public JobDTO createJob(@RequestBody JobDTO jobDTO) {
        return jobService.createJob(jobDTO);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('EMPLOYER', 'ADMIN', 'OFFICER')")
    public ResponseEntity<JobDTO> updateJobStatus(@PathVariable Long id, @RequestBody Map<String, String> statusMap) {
        try {
            JobDTO updated = jobService.updateJobStatus(id, statusMap.get("status"));
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
 
