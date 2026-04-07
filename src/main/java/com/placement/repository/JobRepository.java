package com.placement.repository;

import com.placement.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    // This will allow JobService to use findAll(), save(), etc.
}
 
