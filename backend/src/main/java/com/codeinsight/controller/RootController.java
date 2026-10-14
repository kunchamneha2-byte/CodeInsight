package com.codeinsight.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class RootController {

    @GetMapping("/")
    public Map<String, Object> root() {
        Map<String, Object> status = new HashMap<>();
        status.put("service", "CodeInsight Backend API");
        status.put("status", "UP");
        status.put("timestamp", LocalDateTime.now());
        status.put("documentation", "API endpoints are available under /api/**");
        return status;
    }
}

