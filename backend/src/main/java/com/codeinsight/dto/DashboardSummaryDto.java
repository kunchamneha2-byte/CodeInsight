package com.codeinsight.dto;

import java.util.List;

public class DashboardSummaryDto {
    private long totalQuestions;
    private long solvedQuestions;
    private long totalAttempts;
    private long successfulAttempts;
    private double overallAccuracy;
    private int currentStreakDays;
    private String weakestTopicName;
    private String strongestTopicName;
    private List<TopicDto> topics;
    private List<SubmissionResponse> recentSubmissions;
    private RecommendationDto topRecommendation;

    public DashboardSummaryDto() {
    }

    public long getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(long totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public long getSolvedQuestions() {
        return solvedQuestions;
    }

    public void setSolvedQuestions(long solvedQuestions) {
        this.solvedQuestions = solvedQuestions;
    }

    public long getTotalAttempts() {
        return totalAttempts;
    }

    public void setTotalAttempts(long totalAttempts) {
        this.totalAttempts = totalAttempts;
    }

    public long getSuccessfulAttempts() {
        return successfulAttempts;
    }

    public void setSuccessfulAttempts(long successfulAttempts) {
        this.successfulAttempts = successfulAttempts;
    }

    public double getOverallAccuracy() {
        return overallAccuracy;
    }

    public void setOverallAccuracy(double overallAccuracy) {
        this.overallAccuracy = overallAccuracy;
    }

    public int getCurrentStreakDays() {
        return currentStreakDays;
    }

    public void setCurrentStreakDays(int currentStreakDays) {
        this.currentStreakDays = currentStreakDays;
    }

    public String getWeakestTopicName() {
        return weakestTopicName;
    }

    public void setWeakestTopicName(String weakestTopicName) {
        this.weakestTopicName = weakestTopicName;
    }

    public String getStrongestTopicName() {
        return strongestTopicName;
    }

    public void setStrongestTopicName(String strongestTopicName) {
        this.strongestTopicName = strongestTopicName;
    }

    public List<TopicDto> getTopics() {
        return topics;
    }

    public void setTopics(List<TopicDto> topics) {
        this.topics = topics;
    }

    public List<SubmissionResponse> getRecentSubmissions() {
        return recentSubmissions;
    }

    public void setRecentSubmissions(List<SubmissionResponse> recentSubmissions) {
        this.recentSubmissions = recentSubmissions;
    }

    public RecommendationDto getTopRecommendation() {
        return topRecommendation;
    }

    public void setTopRecommendation(RecommendationDto topRecommendation) {
        this.topRecommendation = topRecommendation;
    }
}
