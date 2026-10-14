package com.codeinsight.entity;

public enum MistakeCategory {
    NONE("No Mistake", "Great job! The solution logic meets all specifications."),
    OFF_BY_ONE("Off-By-One Error", "Loop boundary or indexing is off by one, causing missing values or out-of-range iterations."),
    INFINITE_LOOP_RISK("Infinite Loop Risk", "The loop termination condition may never be reached because loop counters or conditions are not progressing."),
    BASE_CASE_MISSING("Missing or Flawed Base Case", "The recursive function lacks a valid termination base case, which leads to infinite recursion or stack overflow."),
    INDEX_OUT_OF_BOUNDS("Array Index Out Of Bounds Risk", "An array access expression may attempt to read or write an element outside the 0 to length-1 boundary."),
    MISSING_RETURN("Missing Return Statement", "The method signature expects a return value, but the execution path lacks a proper return expression."),
    SYNTAX_STRUCTURE("Structural or Syntax Issue", "Mismatched brackets, missing braces, or incorrect statement placement was detected in the code structure."),
    LOGIC_MISMATCH("Logic Mismatch", "The algorithmic approach does not satisfy the problem's expected conditions or output transformations.");

    private final String displayName;
    private final String generalExplanation;

    MistakeCategory(String displayName, String generalExplanation) {
        this.displayName = displayName;
        this.generalExplanation = generalExplanation;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getGeneralExplanation() {
        return generalExplanation;
    }
}
