package com.icog.jobs.jobs;

import com.icog.jobs.jobs.enums.JobType;
import com.icog.jobs.jobs.models.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer>, JpaSpecificationExecutor<Job> {
    @Query("SELECT CASE WHEN COUNT(j) > 0 THEN true ELSE false END FROM Job j WHERE j.title = :title AND j.company.id = :companyId AND j.type = :type")
    boolean existsByTitleAndCompanyAndType(@Param("title") String title, @Param("companyId") Integer CompanyId, @Param("type") JobType type);
}
