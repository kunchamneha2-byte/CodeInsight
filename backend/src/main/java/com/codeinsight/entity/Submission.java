package com.codeinsight.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "submissions")
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String studentIdentifier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(length = 20)
    private String language = "JAVA";

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String submittedCode;

    @Column(nullable = false)
    private Boolean isCorrect;

    @Column(nullable = false)
    private Integer score;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private MistakeCategory mistakeCategory;

    @Column(length = 200)
    private String feedbackTitle;

    @Column(columnDefinition = "TEXT")
    private String feedbackExplanation;

    @Column(columnDefinition = "TEXT")
    private String suggestions;

    @Column(nullable = false)
    private LocalDateTime submittedAt;

    public Submission() {
    }

    public Submission(String studentIdentifier, Question question, String language, String submittedCode,
                      Boolean isCorrect, Integer score, MistakeCategory mistakeCategory, String feedbackTitle,
                      String feedbackExplanation, String suggestions) {
        this.studentIdentifier = studentIdentifier;
        this.question = question;
        this.language = (language != null && !language.trim().isEmpty()) ? language.toUpperCase() : "JAVA";
        this.submittedCode = submittedCode;
        this.isCorrect = isCorrect;
        this.score = score;
        this.mistakeCategory = mistakeCategory;
        this.feedbackTitle = feedbackTitle;
        this.feedbackExplanation = feedbackExplanation;
        this.suggestions = suggestions;
        this.submittedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (this.submittedAt == null) {
            this.submittedAt = LocalDateTime.now();
        }
        if (this.language == null) {
            this.language = "JAVA";
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentIdentifier() {
        return studentIdentifier;
    }

    public void setStudentIdentifier(String studentIdentifier) {
        this.studentIdentifier = studentIdentifier;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSubmittedCode() {
        return submittedCode;
    }

    public void setSubmittedCode(String submittedCode) {
        this.submittedCode = submittedCode;
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

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
}
