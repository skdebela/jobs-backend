package com.icog.jobs.jobs;

import com.icog.jobs.jobs.dtos.CreateJobDto;
import com.icog.jobs.jobs.dtos.JobResponseDto;
import com.icog.jobs.jobs.dtos.UpdateJobDto;
import com.icog.jobs.companies.models.Industry;
import com.icog.jobs.jobs.enums.ExperienceLevel;
import com.icog.jobs.jobs.enums.JobType;
import com.icog.jobs.jobs.enums.WorkMode;
import com.icog.jobs.jobs.models.Job;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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

    @Operation(
            summary = "Get all jobs.",
            description = """
        Fetch all jobs with optional filtering and search capabilities. 
        Use query parameters to filter by experience level, job type, work mode, and search by keyword.
        """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved jobs."),
            @ApiResponse(responseCode = "400", description = "Invalid query parameters.")
    })
    @GetMapping(path = "/api/jobs")
    ResponseEntity<List<JobResponseDto>> getJobs(
            @Parameter(
                    description = "Filter by industries.",
                    schema = @Schema(allowableValues = {
                            "TECHNOLOGY", "FINANCE", "HEALTHCARE", "EDUCATION", "CONSTRUCTION", "RETAIL", "MANUFACTURING"
                    })

            )
            @RequestParam(required = false) List<Industry> industries,


            @Parameter(
                    description = "Filter by Experience Level.",
                    schema = @Schema(allowableValues = {
                            "INTERNSHIP", "ASSOCIATE", "DIRECTOR", "ENTRY_LEVEL", "MID_SENIOR_LEVEL", "SENIOR_LEVEL", "EXECUTIVE_LEVEL"
                    })

            )
            @RequestParam(required = false) List<ExperienceLevel> experienceLevels,

            @Parameter(
                    description = "Filter by Job Types.",
                    schema = @Schema(allowableValues = {
                            "FULL_TIME", "PART_TIME", "CONTRACTUAL"
                    })

            )
            @RequestParam(required = false) List<JobType> types,

            @Parameter(
                    description = "Filter by Work Mode.",
                    schema = @Schema(allowableValues = {
                            "REMOTE", "ON_SITE", "HYBRID"
                    })

            )
            @RequestParam(required = false) List<WorkMode> workModes,

            @Parameter(
                    description = "Search query for title, company, description, etc."
            )
            @RequestParam(required = false) String query
            ) {
        List<Job> jobs;
        if (industries != null || experienceLevels != null || types != null || workModes != null || query != null) {
            jobs = jobService.searchAndFilter(industries, experienceLevels, types, workModes, query);
        } else {
            jobs = jobService.findAll();
        }
        List<JobResponseDto> jobDtos = jobs.stream().map(jobMapper::mapToResponse).toList();
        return new ResponseEntity<>(jobDtos, HttpStatus.OK);
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


    @Operation(summary = "Create a job.",
    description = "Add a new job to the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Job created successfully."),
            @ApiResponse(responseCode = "409", description = "Duplicate job detected."),
            @ApiResponse(responseCode = "404", description = "Invalid input.")
    })
    @PostMapping(path = "api/jobs")
    ResponseEntity<JobResponseDto> createJob(@Valid @RequestBody CreateJobDto createJobDto) {
        JobResponseDto savedJobResponseDto = jobService.createJob(createJobDto);
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
