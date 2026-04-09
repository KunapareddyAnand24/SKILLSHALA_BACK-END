package com.placement.service;

import com.placement.dto.JobDTO;
import com.placement.model.Job;
import com.placement.model.User;
import com.placement.repository.JobRepository;
import com.placement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MatchService matchService;

    @Autowired
    private NotificationService notificationService;

    public List<JobDTO> getAllJobs() {
        return jobRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<JobDTO> getJobsByEmployer(Long employerId) {
        return jobRepository.findAll().stream()
                .filter(j -> j.getEmployerId().equals(employerId))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<JobDTO> getMatchedJobs(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        return jobRepository.findAll().stream()
                .filter(j -> "active".equalsIgnoreCase(j.getStatus()))
                .map(j -> {
                    JobDTO dto = convertToDTO(j);
                    dto.setMatchScore(matchService.calculateMatch(student, j));
                    return dto;
                })
                .sorted((a, b) -> b.getMatchScore().compareTo(a.getMatchScore()))
                .collect(Collectors.toList());
    }

    @Transactional
    public JobDTO createJob(JobDTO dto) {
        Job job = new Job();
        job.setEmployerId(dto.getEmployerId());
        job.setTitle(dto.getTitle());
        job.setCompanyName(dto.getCompanyName());
        job.setLocation(dto.getLocation());
        job.setSalary(dto.getSalary());
        job.setType(dto.getType());
        job.setDescription(dto.getDescription());
        job.setDepartment(dto.getDepartment());
        job.setOpenings(dto.getOpenings());
        job.setRequirements(dto.getRequirements());
        job.setStatus("active");
        job.setDeadline(dto.getDeadline());
        job.setPostedAt(LocalDate.now());
        
        Job savedJob = jobRepository.save(job);

        // Notify matching students
        List<User> students = userRepository.findAll().stream()
                .filter(u -> "STUDENT".equalsIgnoreCase(u.getRole()))
                .collect(Collectors.toList());

        for (User student : students) {
            int matchScore = matchService.calculateMatch(student, savedJob);
            if (matchScore > 0) {
                notificationService.createNotification(
                    student.getId(),
                    "JOB_POST",
                    "New job match: " + savedJob.getTitle() + " at " + savedJob.getCompanyName() + " (" + matchScore + "% match)",
                    savedJob.getId()
                );
            }
        }
        
        return convertToDTO(savedJob);
    }

    public JobDTO updateJobStatus(Long id, String status) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));
        job.setStatus(status);
        return convertToDTO(jobRepository.save(job));
    }

    private JobDTO convertToDTO(Job job) {
        JobDTO dto = new JobDTO();
        dto.setId(job.getId());
        dto.setEmployerId(job.getEmployerId());
        dto.setTitle(job.getTitle());
        dto.setCompanyName(job.getCompanyName());
        dto.setLocation(job.getLocation());
        dto.setSalary(job.getSalary());
        dto.setType(job.getType());
        dto.setDescription(job.getDescription());
        dto.setDepartment(job.getDepartment());
        dto.setOpenings(job.getOpenings());
        dto.setRequirements(job.getRequirements());
        dto.setStatus(job.getStatus());
        dto.setDeadline(job.getDeadline());
        dto.setPostedAt(job.getPostedAt());
        return dto;
    }
}
 
