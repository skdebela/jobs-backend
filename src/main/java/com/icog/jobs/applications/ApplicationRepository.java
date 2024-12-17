package com.icog.jobs.applications;

import com.icog.jobs.applications.models.Application;
import com.icog.jobs.jobs.models.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {
    List<Application> findByJob(Job job);

    @Query("SELECT CASE WHEN count(a) > 0 THEN true ELSE false END FROM Application a WHERE a.job.id = :jobId AND a.applicantEmail = :applicantEmail")
    boolean existsByJobIdAndApplicantEmail(@Param("jobId") Integer jobId, @Param("applicantEmail") String applicantEmail);
}
