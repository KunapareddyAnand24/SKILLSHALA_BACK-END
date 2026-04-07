package com.placement.service;

import com.placement.model.Job;
import com.placement.model.User;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MatchService {

    /**
     * Calculates the match percentage based on overlapping skills/requirements.
     */
    public int calculateMatch(User student, Job job) {
        if (student.getSkills() == null || job.getRequirements() == null) return 0;

        Set<String> studentSkills = Arrays.stream(student.getSkills().split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        Set<String> jobRequirements = Arrays.stream(job.getRequirements().split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        if (jobRequirements.isEmpty()) return 100;

        long commonCount = jobRequirements.stream()
                .filter(studentSkills::contains)
                .count();

        return (int) ((commonCount * 100) / jobRequirements.size());
    }
}
 
