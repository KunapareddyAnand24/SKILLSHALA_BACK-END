package com.placement.controller;

import com.placement.dto.InterviewDTO;
import com.placement.service.InterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/interviews")
@CrossOrigin(origins = {"http://localhost:5173", "https://skill-shala.netlify.app"})
public class InterviewController {

    @Autowired
    private InterviewService interviewService;

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'OFFICER', 'ADMIN')")
    public List<InterviewDTO> getStudentInterviews(@PathVariable Long studentId) {
        return interviewService.getInterviewsByStudent(studentId);
    }

    @GetMapping("/employer/{employerId}")
    @PreAuthorize("hasAnyRole('EMPLOYER', 'OFFICER', 'ADMIN')")
    public List<InterviewDTO> getEmployerInterviews(@PathVariable Long employerId) {
        return interviewService.getInterviewsByEmployer(employerId);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('EMPLOYER', 'ADMIN')")
    public ResponseEntity<InterviewDTO> scheduleInterview(@RequestBody InterviewDTO interviewDTO) {
        return ResponseEntity.ok(interviewService.scheduleInterview(interviewDTO));
    }
}
 
