package com.codeinsight.dto;

import com.codeinsight.entity.Difficulty;
import java.util.Map;

public class QuestionDto {
    private Long id;
    private String title;
    private Difficulty difficulty;
    private Long topicId;
    private String topicName;
    private String topicSlug;
    private String prompt;
    private String promptPython;
    private String promptC;
    private String promptCpp;
    private String starterCode;
    private String starterCodePython;
    private String starterCodeC;
    private String starterCodeCpp;
    private String expectedBehavior;
    private String hint;
    private String solutionExplanation;
    private String testCasesJson;
    private boolean solved;
    private int attemptCount;
    private String lastSubmittedCode;
    private String lastSubmittedLanguage;
    private Map<String, String> starterCodes;
    private Map<String, String> prompts;
    private Map<String, String> fileNames;

    public QuestionDto() {
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

    public String getTestCasesJson() {
        return testCasesJson;
    }

    public void setTestCasesJson(String testCasesJson) {
        this.testCasesJson = testCasesJson;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public int getAttemptCount() {
        return attemptCount;
    }

    public void setAttemptCount(int attemptCount) {
        this.attemptCount = attemptCount;
    }

    public String getLastSubmittedCode() {
        return lastSubmittedCode;
    }

    public void setLastSubmittedCode(String lastSubmittedCode) {
        this.lastSubmittedCode = lastSubmittedCode;
    }

    public String getLastSubmittedLanguage() {
        return lastSubmittedLanguage;
    }

    public void setLastSubmittedLanguage(String lastSubmittedLanguage) {
        this.lastSubmittedLanguage = lastSubmittedLanguage;
    }

    public Map<String, String> getStarterCodes() {
        return starterCodes;
    }

    public void setStarterCodes(Map<String, String> starterCodes) {
        this.starterCodes = starterCodes;
    }

    public Map<String, String> getPrompts() {
        return prompts;
    }

    public void setPrompts(Map<String, String> prompts) {
        this.prompts = prompts;
    }

    public Map<String, String> getFileNames() {
        return fileNames;
    }

    public void setFileNames(Map<String, String> fileNames) {
        this.fileNames = fileNames;
    }
}
