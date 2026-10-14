package com.codeinsight.dto;

import com.codeinsight.entity.MistakeCategory;
import java.time.LocalDateTime;

public class SubmissionResponse {
    private Long id;
    private Long questionId;
    private String questionTitle;
    private String topicName;
    private String topicSlug;
    private String language;
    private Boolean isCorrect;
    private Integer score;
    private MistakeCategory mistakeCategory;
    private String mistakeDisplayName;
    private String feedbackTitle;
    private String feedbackExplanation;
    private String suggestions;
    private String sampleSolution;
    private LocalDateTime submittedAt;

    public SubmissionResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public MistakeCategory getMistakeCategory() {
        return mistakeCategory;
    }

    public void setMistakeCategory(MistakeCategory mistakeCategory) {
        this.mistakeCategory = mistakeCategory;
    }

    public String getMistakeDisplayName() {
        return mistakeDisplayName;
    }

    public void setMistakeDisplayName(String mistakeDisplayName) {
        this.mistakeDisplayName = mistakeDisplayName;
    }

    public String getFeedbackTitle() {
        return feedbackTitle;
    }

    public void setFeedbackTitle(String feedbackTitle) {
        this.feedbackTitle = feedbackTitle;
    }

    public String getFeedbackExplanation() {
        return feedbackExplanation;
    }

    public void setFeedbackExplanation(String feedbackExplanation) {
        this.feedbackExplanation = feedbackExplanation;
    }

    public String getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(String suggestions) {
        this.suggestions = suggestions;
    }

    public String getSampleSolution() {
        return sampleSolution;
    }

    public void setSampleSolution(String sampleSolution) {
        this.sampleSolution = sampleSolution;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
}
