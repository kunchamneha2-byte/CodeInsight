package com.codeinsight.dto;

public class TopicProgressDto {
    private Long topicId;
    private String topicName;
    private String topicSlug;
    private String icon;
    private String color;
    private long totalQuestions;
    private long solvedQuestions;
    private long totalAttempts;
    private long successfulAttempts;
    private long failedAttempts;
    private double accuracyRate;
    private double masteryPercentage;
    private String masteryStatus; // "Mastered", "Proficient", "Developing", "Needs Focus", "Unattempted"

    public TopicProgressDto() {
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicSlug() {
        return topicSlug;
    }

    public void setTopicSlug(String topicSlug) {
        this.topicSlug = topicSlug;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public long getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(long failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public double getAccuracyRate() {
        return accuracyRate;
    }

    public void setAccuracyRate(double accuracyRate) {
        this.accuracyRate = accuracyRate;
    }

    public double getMasteryPercentage() {
        return masteryPercentage;
    }

    public void setMasteryPercentage(double masteryPercentage) {
        this.masteryPercentage = masteryPercentage;
    }

    public String getMasteryStatus() {
        return masteryStatus;
    }

    public void setMasteryStatus(String masteryStatus) {
        this.masteryStatus = masteryStatus;
    }
}
