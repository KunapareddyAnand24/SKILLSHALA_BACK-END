package com.placement.controller;

import com.placement.dto.StatsDTO;
import com.placement.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stats")
@CrossOrigin(origins = {"http://localhost:5173", "https://skill-shala.netlify.app"})
public class StatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping
    @PreAuthorize("hasAnyRole('OFFICER', 'ADMIN')")
    public StatsDTO getPlacementStats() {
        return statsService.getPlacementStats();
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'OFFICER', 'ADMIN')")
    public StatsDTO getStudentStats(@PathVariable Long studentId) {
        return statsService.getStudentStats(studentId);
    }

    @GetMapping("/employer/{employerId}")
    @PreAuthorize("hasAnyRole('EMPLOYER', 'OFFICER', 'ADMIN')")
    public StatsDTO getEmployerStats(@PathVariable Long employerId) {
        return statsService.getEmployerStats(employerId);
    }
}
 
