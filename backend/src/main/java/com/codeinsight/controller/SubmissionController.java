package com.codeinsight.controller;

import com.codeinsight.dto.SubmissionRequest;
import com.codeinsight.dto.SubmissionResponse;
import com.codeinsight.service.SubmissionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @PostMapping
    public ResponseEntity<SubmissionResponse> submitSolution(
            @Valid @RequestBody SubmissionRequest request) {
        SubmissionResponse response = submissionService.submitCode(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/history")
    public ResponseEntity<List<SubmissionResponse>> getSubmissionHistory(
            @RequestParam(defaultValue = "student-1") String studentId,
            @RequestParam(required = false) Long questionId) {
        return ResponseEntity.ok(submissionService.getSubmissionHistory(studentId, questionId));
    }
}
