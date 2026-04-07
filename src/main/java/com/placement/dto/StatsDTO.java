package com.placement.dto;

import java.util.Map;

public class StatsDTO {
    private long totalStudents;
    private long totalEmployers;
    private long totalJobs;
    private long totalApplications;
    private Map<String, Long> applicationStatusCounts;
    private double placementRate;
    
    // Role-specific stats
    private long activeJobs;
    private long interviewsScheduled;
    private long pendingApplications;

    public StatsDTO() {}

    // Getters and Setters
    public long getTotalStudents() { return totalStudents; }
    public void setTotalStudents(long totalStudents) { this.totalStudents = totalStudents; }
    public long getTotalEmployers() { return totalEmployers; }
    public void setTotalEmployers(long totalEmployers) { this.totalEmployers = totalEmployers; }
    public long getTotalJobs() { return totalJobs; }
    public void setTotalJobs(long totalJobs) { this.totalJobs = totalJobs; }
    public long getTotalApplications() { return totalApplications; }
    public void setTotalApplications(long totalApplications) { this.totalApplications = totalApplications; }
    public Map<String, Long> getApplicationStatusCounts() { return applicationStatusCounts; }
    public void setApplicationStatusCounts(Map<String, Long> applicationStatusCounts) { this.applicationStatusCounts = applicationStatusCounts; }
    public double getPlacementRate() { return placementRate; }
    public void setPlacementRate(double placementRate) { this.placementRate = placementRate; }
    
    public long getActiveJobs() { return activeJobs; }
    public void setActiveJobs(long activeJobs) { this.activeJobs = activeJobs; }
    public long getInterviewsScheduled() { return interviewsScheduled; }
    public void setInterviewsScheduled(long interviewsScheduled) { this.interviewsScheduled = interviewsScheduled; }
    public long getPendingApplications() { return pendingApplications; }
    public void setPendingApplications(long pendingApplications) { this.pendingApplications = pendingApplications; }
}
 
