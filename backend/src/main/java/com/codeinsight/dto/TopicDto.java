package com.codeinsight.dto;

import java.util.List;

public class TopicDto {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private String icon;
    private String color;
    private Integer sortOrder;
    private long totalQuestions;
    private long solvedQuestions;
    private double masteryPercentage;
    private List<QuestionSummaryDto> questions;

    public TopicDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
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

    public double getMasteryPercentage() {
        return masteryPercentage;
    }

    public void setMasteryPercentage(double masteryPercentage) {
        this.masteryPercentage = masteryPercentage;
    }

    public List<QuestionSummaryDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionSummaryDto> questions) {
        this.questions = questions;
    }
}
