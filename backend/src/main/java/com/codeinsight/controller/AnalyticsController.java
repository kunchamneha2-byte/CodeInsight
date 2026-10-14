package com.codeinsight.controller;

import com.codeinsight.dto.DashboardSummaryDto;
import com.codeinsight.dto.RecommendationDto;
import com.codeinsight.dto.WeakTopicAnalysisDto;
import com.codeinsight.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardSummaryDto> getDashboardSummary(
            @RequestParam(defaultValue = "student-1") String studentId) {
        return ResponseEntity.ok(analyticsService.getDashboardSummary(studentId));
    }

    @GetMapping("/weak-topics")
    public ResponseEntity<WeakTopicAnalysisDto> getWeakTopicAnalysis(
            @RequestParam(defaultValue = "student-1") String studentId) {
        return ResponseEntity.ok(analyticsService.getWeakTopicAnalysis(studentId));
    }

    @GetMapping("/recommendations")
    public ResponseEntity<RecommendationDto> getRecommendations(
            @RequestParam(defaultValue = "student-1") String studentId) {
        return ResponseEntity.ok(analyticsService.getRecommendations(studentId));
    }

    @PostMapping("/reset")
    public ResponseEntity<Map<String, String>> resetProgress(
            @RequestParam(defaultValue = "student-1") String studentId) {
        analyticsService.resetStudentProgress(studentId);
        return ResponseEntity.ok(Map.of("message", "Progress reset successfully for " + studentId));
    }
}
