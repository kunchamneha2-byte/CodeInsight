package com.codeinsight.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Difficulty difficulty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String prompt;

    @Column(columnDefinition = "TEXT")
    private String promptPython;

    @Column(columnDefinition = "TEXT")
    private String promptC;

    @Column(columnDefinition = "TEXT")
    private String promptCpp;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String starterCode;

    @Column(columnDefinition = "TEXT")
    private String starterCodePython;

    @Column(columnDefinition = "TEXT")
    private String starterCodeC;

    @Column(columnDefinition = "TEXT")
    private String starterCodeCpp;

    @Column(columnDefinition = "TEXT")
    private String expectedBehavior;

    @Column(columnDefinition = "TEXT")
    private String requiredPatterns;

    @Column(columnDefinition = "TEXT")
    private String forbiddenPatterns;

    @Column(columnDefinition = "TEXT")
    private String hint;

    @Column(columnDefinition = "TEXT")
    private String solutionExplanation;

    @Column(columnDefinition = "TEXT")
    private String sampleSolution;

    @Column(columnDefinition = "TEXT")
    private String sampleSolutionPython;

    @Column(columnDefinition = "TEXT")
    private String sampleSolutionC;

    @Column(columnDefinition = "TEXT")
    private String sampleSolutionCpp;

    @Column(columnDefinition = "TEXT")
    private String testCasesJson;

    public Question() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getPromptPython() {
        return promptPython;
    }

    public void setPromptPython(String promptPython) {
        this.promptPython = promptPython;
    }

    public String getPromptC() {
        return promptC;
    }

    public void setPromptC(String promptC) {
        this.promptC = promptC;
    }

    public String getPromptCpp() {
        return promptCpp;
    }

    public void setPromptCpp(String promptCpp) {
        this.promptCpp = promptCpp;
    }

    public String getStarterCode() {
        return starterCode;
    }

    public void setStarterCode(String starterCode) {
        this.starterCode = starterCode;
    }

    public String getStarterCodePython() {
        return starterCodePython;
    }

    public void setStarterCodePython(String starterCodePython) {
        this.starterCodePython = starterCodePython;
    }

    public String getStarterCodeC() {
        return starterCodeC;
    }

    public void setStarterCodeC(String starterCodeC) {
        this.starterCodeC = starterCodeC;
    }

    public String getStarterCodeCpp() {
        return starterCodeCpp;
    }

    public void setStarterCodeCpp(String starterCodeCpp) {
        this.starterCodeCpp = starterCodeCpp;
    }

    public String getExpectedBehavior() {
        return expectedBehavior;
    }

    public void setExpectedBehavior(String expectedBehavior) {
        this.expectedBehavior = expectedBehavior;
    }

    public String getRequiredPatterns() {
        return requiredPatterns;
    }

    public void setRequiredPatterns(String requiredPatterns) {
        this.requiredPatterns = requiredPatterns;
    }

    public String getForbiddenPatterns() {
        return forbiddenPatterns;
    }

    public void setForbiddenPatterns(String forbiddenPatterns) {
        this.forbiddenPatterns = forbiddenPatterns;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getSolutionExplanation() {
        return solutionExplanation;
    }

    public void setSolutionExplanation(String solutionExplanation) {
        this.solutionExplanation = solutionExplanation;
    }

    public String getSampleSolution() {
        return sampleSolution;
    }

    public void setSampleSolution(String sampleSolution) {
        this.sampleSolution = sampleSolution;
    }

    public String getSampleSolutionPython() {
        return sampleSolutionPython;
    }

    public void setSampleSolutionPython(String sampleSolutionPython) {
        this.sampleSolutionPython = sampleSolutionPython;
    }

    public String getSampleSolutionC() {
        return sampleSolutionC;
    }

    public void setSampleSolutionC(String sampleSolutionC) {
        this.sampleSolutionC = sampleSolutionC;
    }

    public String getSampleSolutionCpp() {
        return sampleSolutionCpp;
    }

    public void setSampleSolutionCpp(String sampleSolutionCpp) {
        this.sampleSolutionCpp = sampleSolutionCpp;
    }

    public String getTestCasesJson() {
        return testCasesJson;
    }

    public void setTestCasesJson(String testCasesJson) {
        this.testCasesJson = testCasesJson;
    }
}
