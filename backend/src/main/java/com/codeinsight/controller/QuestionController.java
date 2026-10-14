package com.codeinsight.controller;

import com.codeinsight.dto.QuestionDto;
import com.codeinsight.dto.QuestionSummaryDto;
import com.codeinsight.entity.Difficulty;
import com.codeinsight.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    public ResponseEntity<List<QuestionSummaryDto>> getQuestions(
            @RequestParam(required = false) Long topicId,
            @RequestParam(required = false) String topicSlug,
            @RequestParam(required = false) Difficulty difficulty,
            @RequestParam(defaultValue = "student-1") String studentId) {
        return ResponseEntity.ok(questionService.getQuestions(topicId, topicSlug, difficulty, studentId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDto> getQuestionById(
            @PathVariable Long id,
            @RequestParam(defaultValue = "student-1") String studentId) {
        return ResponseEntity.ok(questionService.getQuestionById(id, studentId));
    }
}
