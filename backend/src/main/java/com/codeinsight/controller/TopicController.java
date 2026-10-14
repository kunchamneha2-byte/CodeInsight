package com.codeinsight.controller;

import com.codeinsight.dto.TopicDto;
import com.codeinsight.service.TopicService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping
    public ResponseEntity<List<TopicDto>> getAllTopics(
            @RequestParam(defaultValue = "student-1") String studentId) {
        return ResponseEntity.ok(topicService.getAllTopicsWithProgress(studentId));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<TopicDto> getTopicBySlug(
            @PathVariable String slug,
            @RequestParam(defaultValue = "student-1") String studentId) {
        return ResponseEntity.ok(topicService.getTopicBySlug(slug, studentId));
    }
}
