package com.codeinsight.dto;

import com.codeinsight.entity.MistakeCategory;

public class MistakeStatDto {
    private MistakeCategory category;
    private String categoryName;
    private long count;
    private double percentage;
    private String explanation;

    public MistakeStatDto() {
    }

    public MistakeStatDto(MistakeCategory category, String categoryName, long count, double percentage, String explanation) {
        this.category = category;
        this.categoryName = categoryName;
        this.count = count;
        this.percentage = percentage;
        this.explanation = explanation;
    }

    public MistakeCategory getCategory() {
        return category;
    }

    public void setCategory(MistakeCategory category) {
        this.category = category;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
