package com.codeinsight.dto;

import java.util.List;

public class RecommendationDto {
    private Long targetTopicId;
    private String targetTopicName;
    private String targetTopicSlug;
    private String priorityLevel; // "HIGH", "MEDIUM", "LOW"
    private String rationale;
    private String learningTip;
    private String recommendedAction;
    private List<QuestionSummaryDto> recommendedQuestions;

    public RecommendationDto() {
    }

    public Long getTargetTopicId() {
        return targetTopicId;
    }

    public void setTargetTopicId(Long targetTopicId) {
        this.targetTopicId = targetTopicId;
    }

    public String getTargetTopicName() {
        return targetTopicName;
    }

    public void setTargetTopicName(String targetTopicName) {
        this.targetTopicName = targetTopicName;
    }

    public String getTargetTopicSlug() {
        return targetTopicSlug;
    }

    public void setTargetTopicSlug(String targetTopicSlug) {
        this.targetTopicSlug = targetTopicSlug;
    }

    public String getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(String priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public String getRationale() {
        return rationale;
    }

    public void setRationale(String rationale) {
        this.rationale = rationale;
    }

    public String getLearningTip() {
        return learningTip;
    }

    public void setLearningTip(String learningTip) {
        this.learningTip = learningTip;
    }

    public String getRecommendedAction() {
        return recommendedAction;
    }

    public void setRecommendedAction(String recommendedAction) {
        this.recommendedAction = recommendedAction;
    }

    public List<QuestionSummaryDto> getRecommendedQuestions() {
        return recommendedQuestions;
    }

    public void setRecommendedQuestions(List<QuestionSummaryDto> recommendedQuestions) {
        this.recommendedQuestions = recommendedQuestions;
    }
}
