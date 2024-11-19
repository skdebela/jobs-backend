package com.icog.jobs.job;

import com.icog.jobs.Mapper;
import com.icog.jobs.job.dtos.CreateJobDto;
import com.icog.jobs.job.dtos.JobResponseDto;
import com.icog.jobs.job.dtos.UpdateJobDto;
import com.icog.jobs.job.models.Job;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class JobController {
    private final JobService jobService;
    private final JobMapper jobMapper;

    public JobController(JobService jobService, JobMapper jobMapper) {
        this.jobService = jobService;
        this.jobMapper = jobMapper;
    }

    @Operation(summary = "Get all jobs.",
            description = "Fetch all companies, an empty list if no company found.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved jobs.")
    @GetMapping(path = "/api/jobs")
    ResponseEntity<List<JobResponseDto>> getJobs() {
        List<Job> jobs = jobService.findAll();
        List<JobResponseDto> jobResponseDtos = jobs.stream().map(jobMapper::mapToResponse).toList();
        return new ResponseEntity<>(jobResponseDtos, HttpStatus.OK);
    }


    @Operation(summary = "Get a job.",
            description = "Fetch a job by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved job."),
            @ApiResponse(responseCode = "404", description = "No Job found with that id.")
    })
    @GetMapping(path = "/api/jobs/{id}")
    ResponseEntity<JobResponseDto> getJob(@PathVariable Integer id) {
        Optional<Job> foundJob = jobService.findOne(id);
        return foundJob.map(job -> {
            JobResponseDto jobResponseDto = jobMapper.mapToResponse(job);
            return new ResponseEntity<>(jobResponseDto, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    // TODO: POST /api/jobs
    @Operation(summary = "Create a job.",
    description = "Add a new job to the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Job created successfully."),
            @ApiResponse(responseCode = "404", description = "Invalid input.")
    })
    @PostMapping(path = "api/jobs")
    ResponseEntity<JobResponseDto> createJob(@RequestBody CreateJobDto createJobDto) {
        JobResponseDto savedJobResponseDto = jobService.save(createJobDto);
        return new ResponseEntity<>(savedJobResponseDto, HttpStatus.CREATED);
    }


    @Operation(
            summary = "Update a job.",
            description = ""
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "job successfully updated"),
            @ApiResponse(responseCode = "404", description = "Job not found.")
    })
    @PutMapping(path = "api/jobs/{id}")
    ResponseEntity<JobResponseDto> updateJob(
            @PathVariable("id") Integer id,
            @RequestBody UpdateJobDto updateJobDto
            ) {
        if (!jobService.isExisting(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        updateJobDto.setId(id);
        JobResponseDto savedJob = jobService.update(updateJobDto, id);
        return new ResponseEntity<>(savedJob, HttpStatus.OK);
    }

    // TODO: Patch api/jobs/{id}/status


    @Operation(
            summary = "Delete a job.",
            description = "Delete a job from the database."
    )
    @ApiResponse(responseCode = "204", description = "Company deleted.")
    @DeleteMapping(path = "api/jobs/{id}")
    ResponseEntity<JobResponseDto> deleteJob(@PathVariable Integer id) {
        jobService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
