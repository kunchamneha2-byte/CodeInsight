package com.codeinsight.dto;

import com.codeinsight.entity.MistakeCategory;
import java.util.List;

public class WeakTopicAnalysisDto {
    private TopicProgressDto weakestTopic;
    private TopicProgressDto strongestTopic;
    private List<TopicProgressDto> topicsNeedingFocus;
    private List<TopicProgressDto> allTopicProgress;
    private MistakeCategory primaryMistake;
    private String primaryMistakeName;
    private String primaryMistakeExplanation;
    private String diagnosticSummary;
    private List<MistakeStatDto> mistakeBreakdown;

    public WeakTopicAnalysisDto() {
    }

    public TopicProgressDto getWeakestTopic() {
        return weakestTopic;
    }

    public void setWeakestTopic(TopicProgressDto weakestTopic) {
        this.weakestTopic = weakestTopic;
    }

    public TopicProgressDto getStrongestTopic() {
        return strongestTopic;
    }

    public void setStrongestTopic(TopicProgressDto strongestTopic) {
        this.strongestTopic = strongestTopic;
    }

    public List<TopicProgressDto> getTopicsNeedingFocus() {
        return topicsNeedingFocus;
    }

    public void setTopicsNeedingFocus(List<TopicProgressDto> topicsNeedingFocus) {
        this.topicsNeedingFocus = topicsNeedingFocus;
    }

    public List<TopicProgressDto> getAllTopicProgress() {
        return allTopicProgress;
    }

    public void setAllTopicProgress(List<TopicProgressDto> allTopicProgress) {
        this.allTopicProgress = allTopicProgress;
    }

    public MistakeCategory getPrimaryMistake() {
        return primaryMistake;
    }

    public void setPrimaryMistake(MistakeCategory primaryMistake) {
        this.primaryMistake = primaryMistake;
    }

    public String getPrimaryMistakeName() {
        return primaryMistakeName;
    }

    public void setPrimaryMistakeName(String primaryMistakeName) {
        this.primaryMistakeName = primaryMistakeName;
    }

    public String getPrimaryMistakeExplanation() {
        return primaryMistakeExplanation;
    }

    public void setPrimaryMistakeExplanation(String primaryMistakeExplanation) {
        this.primaryMistakeExplanation = primaryMistakeExplanation;
    }

    public String getDiagnosticSummary() {
        return diagnosticSummary;
    }

    public void setDiagnosticSummary(String diagnosticSummary) {
        this.diagnosticSummary = diagnosticSummary;
    }

    public List<MistakeStatDto> getMistakeBreakdown() {
        return mistakeBreakdown;
    }

    public void setMistakeBreakdown(List<MistakeStatDto> mistakeBreakdown) {
        this.mistakeBreakdown = mistakeBreakdown;
    }
}
