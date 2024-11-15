package com.icog.jobs.application;

import com.icog.jobs.application.dtos.ApplicationDto;
import com.icog.jobs.application.enums.ApplicationStatus;
import com.icog.jobs.application.models.Application;
import com.icog.jobs.job.JobService;
import com.icog.jobs.job.models.Job;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.websocket.server.PathParam;
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
            @ApiResponse(responseCode = "404", description = "Application creation failed.")
    })
    @PostMapping(path = "/api/jobs/{id}/apply")
    ResponseEntity<ApplicationDto> createApplication(
            @PathVariable("id") Integer jobId,
            @RequestBody ApplicationDto applicationDto
    ) {
        Optional<Job> foundJob = jobService.findOne(jobId);
        return foundJob.map(job -> {
            Application application = applicationMapper.mapFrom(applicationDto);
            application.setJob(job);
            Application savedApplication = applicationService.save(application);
            ApplicationDto savedApplicationDto = applicationMapper.mapTo(savedApplication);
            return new ResponseEntity<>(savedApplicationDto, HttpStatus.CREATED);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
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
    ResponseEntity<List<ApplicationDto>> getApplications(
            @PathVariable("id") Integer jobId
    ) {
        Optional<Job> foundJob = jobService.findOne(jobId);
        return foundJob.map(job -> {
            List<Application> applications = applicationService.findByJob(job);
            List<ApplicationDto> applicationDtos = applications.stream().map(applicationMapper::mapTo).collect(Collectors.toList());
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
    ResponseEntity<ApplicationDto> getApplication(
            @PathVariable("applicationId") Integer applicationId
    ) {
        Optional<Application> foundApplication = applicationService.findOne(applicationId);
        return foundApplication.map(application -> {
            ApplicationDto applicationDto = applicationMapper.mapTo(application);
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
    public ResponseEntity<ApplicationDto> updateApplicationStatus(
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
        ApplicationDto savedApplicationDto = applicationMapper.mapTo(application);
        return new ResponseEntity<>(savedApplicationDto, HttpStatus.OK);
    }

}
