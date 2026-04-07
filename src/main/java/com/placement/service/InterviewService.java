package com.placement.service;

import com.placement.dto.InterviewDTO;
import com.placement.model.Interview;
import com.placement.repository.InterviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InterviewService {

    @Autowired
    private InterviewRepository interviewRepository;

    public List<InterviewDTO> getInterviewsByStudent(Long studentId) {
        return interviewRepository.findByStudentId(studentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<InterviewDTO> getInterviewsByEmployer(Long employerId) {
        return interviewRepository.findByEmployerId(employerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public InterviewDTO scheduleInterview(InterviewDTO dto) {
        Interview interview = new Interview();
        interview.setApplicationId(dto.getApplicationId());
        interview.setStudentId(dto.getStudentId());
        interview.setEmployerId(dto.getEmployerId());
        interview.setJobTitle(dto.getJobTitle());
        interview.setScheduledAt(dto.getScheduledAt());
        interview.setLocation(dto.getLocation());
        interview.setStatus("scheduled");
        
        return convertToDTO(interviewRepository.save(interview));
    }

    private InterviewDTO convertToDTO(Interview interview) {
        InterviewDTO dto = new InterviewDTO();
        dto.setId(interview.getId());
        dto.setApplicationId(interview.getApplicationId());
        dto.setStudentId(interview.getStudentId());
        dto.setEmployerId(interview.getEmployerId());
        dto.setJobTitle(interview.getJobTitle());
        dto.setScheduledAt(interview.getScheduledAt());
        dto.setLocation(interview.getLocation());
        dto.setStatus(interview.getStatus());
        dto.setFeedback(interview.getFeedback());
        return dto;
    }
}
 
