package com.icog.jobs.job;

import com.icog.jobs.Mapper;
import com.icog.jobs.company.models.Industry;
import com.icog.jobs.job.dtos.JobDto;
import com.icog.jobs.job.enums.ExperienceLevel;
import com.icog.jobs.job.enums.JobType;
import com.icog.jobs.job.enums.WorkMode;
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
    private final Mapper<Job, JobDto> jobMapper;

    public JobController(JobService jobService, Mapper<Job, JobDto> jobMapper) {
        this.jobService = jobService;
        this.jobMapper = jobMapper;
    }

    @Operation(summary = "Get all jobs.",
            description = "Fetch all companies, an empty list if no company found.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved jobs.")
    @GetMapping(path = "/api/jobs")
    ResponseEntity<List<JobDto>> getJobs(
            @RequestParam(required = false) List<Industry> industries,
            @RequestParam(required = false) List<ExperienceLevel> experienceLevels,
            @RequestParam(required = false) List<JobType> types,
            @RequestParam(required = false) List<WorkMode> workModes,
            @RequestParam(required = false) String query
            ) {
        List<Job> jobs;
        if (industries != null || experienceLevels != null || types != null || workModes != null || query != null) {
            jobs = jobService.searchAndFilter(industries, experienceLevels, types, workModes, query);
        } else {
            jobs = jobService.findAll();
        }
        List<JobDto> jobDtos = jobs.stream().map(jobMapper::mapTo).toList();
        return new ResponseEntity<>(jobDtos, HttpStatus.OK);
    }


    @Operation(summary = "Get a job.",
            description = "Fetch a job by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved job."),
            @ApiResponse(responseCode = "404", description = "No Job found with that id.")
    })
    @GetMapping(path = "/api/jobs/{id}")
    ResponseEntity<JobDto> getJob(@PathVariable Integer id) {
        Optional<Job> foundJob = jobService.findOne(id);
        return foundJob.map(job -> {
            JobDto jobDto = jobMapper.mapTo(job);
            return new ResponseEntity<>(jobDto, HttpStatus.OK);
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
    ResponseEntity<JobDto> createJob(@RequestBody JobDto jobDto) {
        Job job = jobMapper.mapFrom(jobDto);
        Job savedJob = jobService.save(job);
        JobDto savedJobDto = jobMapper.mapTo(savedJob);
        return new ResponseEntity<>(savedJobDto, HttpStatus.CREATED);
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
    ResponseEntity<JobDto> updateJob(
            @PathVariable("id") Integer id,
            @RequestBody JobDto jobDto
    ) {
        if (!jobService.isExisting(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        jobDto.setId(id);
        Job job = jobMapper.mapFrom(jobDto);
        Job savedJob = jobService.save(job);
        JobDto updatedJobDto = jobMapper.mapTo(savedJob);
        return new ResponseEntity<>(updatedJobDto, HttpStatus.OK);
    }


    @Operation(
            summary = "Delete a job.",
            description = "Delete a job from the database."
    )
    @ApiResponse(responseCode = "204", description = "Company deleted.")
    @DeleteMapping(path = "api/jobs/{id}")
    ResponseEntity<JobDto> deleteJob(@PathVariable Integer id) {
        jobService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
