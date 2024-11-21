package com.icog.jobs.application;

import com.icog.jobs.application.dtos.ApplicationResponseDto;
import com.icog.jobs.application.dtos.CreateApplicationDto;
import com.icog.jobs.application.enums.ApplicationStatus;
import com.icog.jobs.application.models.Application;
import com.icog.jobs.job.JobService;
import com.icog.jobs.job.models.Job;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ApplicationController {
    private final ApplicationService applicationService;
    private final JobService jobService;
    private final ApplicationMapper applicationMapper;
    private final WebMvcProperties webMvcProperties;

    @Autowired
    public ApplicationController(ApplicationService applicationService, ApplicationMapper applicationMapper, JobService jobService, WebMvcProperties webMvcProperties) {
        this.applicationService = applicationService;
        this.applicationMapper = applicationMapper;
        this.jobService = jobService;
        this.webMvcProperties = webMvcProperties;
    }


    @Operation(
            summary = "Create application.",
            description = "Create a new application for a job."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Application successfully created."),
            @ApiResponse(responseCode = "409", description = "Application already exists."),
            @ApiResponse(responseCode = "404", description = "Application creation failed.")
    })
    @PostMapping(path = "/api/jobs/{id}/apply")
    ResponseEntity<ApplicationResponseDto> createApplication(
            @PathVariable("id") Integer jobId,
            @Valid @RequestBody CreateApplicationDto createApplicationDto
            ) {
        ApplicationResponseDto savedApplicationResponseDto = applicationService.createApplication(createApplicationDto, jobId);
        return new ResponseEntity<>(savedApplicationResponseDto, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get job applications of a job.",
            description = "Fetch job applications by a job id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Applications successfully fetched."),
            @ApiResponse(responseCode = "404", description = "Job not found.")
    })
    @GetMapping(path = "/api/jobs/{id}/applications")
    ResponseEntity<List<ApplicationResponseDto>> getApplications(
            @PathVariable("id") Integer jobId
    ) {
        Optional<Job> foundJob = jobService.findOne(jobId);
        return foundJob.map(job -> {
            List<Application> applications = applicationService.findByJob(job);
            List<ApplicationResponseDto> applicationDtos = applications.stream().map(applicationMapper::mapToResponse).collect(Collectors.toList());
            return new ResponseEntity<>(applicationDtos, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(
            summary = "Get an application.",
            description = "Fetch a job application by a application id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application successfully fetched."),
            @ApiResponse(responseCode = "404", description = "Application not found.")
    })
    @GetMapping(path = "/api/jobs/{jobId}/applications/{applicationId}")
    ResponseEntity<ApplicationResponseDto> getApplication(
            @PathVariable("applicationId") Integer applicationId
    ) {
        Optional<Application> foundApplication = applicationService.findOne(applicationId);
        return foundApplication.map(application -> {
            ApplicationResponseDto applicationDto = applicationMapper.mapToResponse(application);
            return new ResponseEntity<>(applicationDto, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(
            summary = "Update application status.",
            description = "Updates the status of a job application."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application status successfully updated."),
            @ApiResponse(responseCode = "404", description = "Application not found.")
    })
    @PatchMapping(path = "/api/jobs/{jobId}/applications/{applicationId}/status")
    public ResponseEntity<ApplicationResponseDto> updateApplicationStatus(
            @PathVariable("applicationId") Integer applicationId,
            @RequestBody Map<String, String> applicationStatus
    ) {
        if (!applicationStatus.containsKey("status")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String statusValue = applicationStatus.get("status");
        ApplicationStatus status;
        try {
            status = ApplicationStatus.valueOf(statusValue);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<Application> foundApplication = applicationService.findOne(applicationId);
        if (foundApplication.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Application application = foundApplication.get();
        application = applicationService.updateStatus(applicationId, status);
        ApplicationResponseDto savedApplicationDto = applicationMapper.mapToResponse(application);
        return new ResponseEntity<>(savedApplicationDto, HttpStatus.OK);
    }

}
