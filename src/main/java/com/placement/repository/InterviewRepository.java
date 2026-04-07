package com.placement.repository;

import com.placement.model.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {
    List<Interview> findByStudentId(Long studentId);
    List<Interview> findByEmployerId(Long employerId);
}
 
