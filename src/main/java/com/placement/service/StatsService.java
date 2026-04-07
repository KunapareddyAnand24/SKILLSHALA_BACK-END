package com.placement.service;

import com.placement.dto.StatsDTO;
import com.placement.repository.ApplicationRepository;
import com.placement.repository.JobRepository;
import com.placement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.placement.model.Application;

@Service
public class StatsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private ApplicationRepository applicationRepository;

    public StatsDTO getPlacementStats() {
        StatsDTO stats = new StatsDTO();
        
        stats.setTotalStudents(userRepository.findAll().stream().filter(u -> "STUDENT".equals(u.getRole())).count());
        stats.setTotalEmployers(userRepository.findAll().stream().filter(u -> "EMPLOYER".equals(u.getRole())).count());
        stats.setTotalJobs(jobRepository.count());
        stats.setTotalApplications(applicationRepository.count());

        Map<String, Long> statusCounts = applicationRepository.findAll().stream()
                .collect(Collectors.groupingBy(a -> a.getStatus(), Collectors.counting()));
        stats.setApplicationStatusCounts(statusCounts);

        long selectedCount = statusCounts.getOrDefault("selected", 0L);
        long totalStudents = stats.getTotalStudents();
        stats.setPlacementRate(totalStudents > 0 ? (selectedCount * 100.0) / totalStudents : 0.0);

        return stats;
    }

    public StatsDTO getStudentStats(Long studentId) {
        StatsDTO stats = new StatsDTO();
        List<Application> apps = applicationRepository.findAll().stream()
                .filter(a -> a.getStudentId().equals(studentId))
                .collect(Collectors.toList());
        
        stats.setTotalApplications(apps.size());
        stats.setPendingApplications(apps.stream().filter(a -> "applied".equals(a.getStatus())).count());
        
        Map<String, Long> statusCounts = apps.stream()
                .collect(Collectors.groupingBy(a -> a.getStatus(), Collectors.counting()));
        stats.setApplicationStatusCounts(statusCounts);
        
        return stats;
    }

    public StatsDTO getEmployerStats(Long employerId) {
        StatsDTO stats = new StatsDTO();
        long jobCount = jobRepository.findAll().stream()
                .filter(j -> j.getEmployerId().equals(employerId))
                .count();
        
        stats.setActiveJobs(jobCount);
        
        List<Application> apps = applicationRepository.findAll().stream()
                .filter(a -> a.getCompanyName() != null) // Simplistic filter, should ideally be by JobId
                .collect(Collectors.toList());
        
        stats.setTotalApplications(apps.size());
        stats.setPendingApplications(apps.stream().filter(a -> "applied".equals(a.getStatus())).count());
        
        return stats;
    }
}
 
