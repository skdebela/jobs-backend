package com.icog.jobs.application;

import com.icog.jobs.application.models.Application;
import com.icog.jobs.job.models.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {
    List<Application> findByJob(Job job);
}
