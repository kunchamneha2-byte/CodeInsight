package com.codeinsight.service;

import com.codeinsight.entity.MistakeCategory;
import com.codeinsight.entity.Question;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CodeAnalysisService {

    public static class AnalysisResult {
        private final boolean isCorrect;
        private final int score;
        private final MistakeCategory mistakeCategory;
        private final String feedbackTitle;
        private final String feedbackExplanation;
        private final String suggestions;

        public AnalysisResult(boolean isCorrect, int score, MistakeCategory mistakeCategory,
                              String feedbackTitle, String feedbackExplanation, String suggestions) {
            this.isCorrect = isCorrect;
            this.score = score;
            this.mistakeCategory = mistakeCategory;
            this.feedbackTitle = feedbackTitle;
            this.feedbackExplanation = feedbackExplanation;
            this.suggestions = suggestions;
        }

        public boolean isCorrect() {
            return isCorrect;
        }

        public int getScore() {
            return score;
        }

        public MistakeCategory getMistakeCategory() {
            return mistakeCategory;
        }

        public String getFeedbackTitle() {
            return feedbackTitle;
        }

        public String getFeedbackExplanation() {
            return feedbackExplanation;
        }

        public String getSuggestions() {
            return suggestions;
        }
    }

    public AnalysisResult analyze(Question question, String rawCode) {
        return analyze(question, rawCode, "JAVA");
    }

    public AnalysisResult analyze(Question question, String rawCode, String language) {
        String lang = (language != null && !language.trim().isEmpty()) ? language.toUpperCase() : "JAVA";

        if (rawCode == null || rawCode.trim().isEmpty()) {
            return new AnalysisResult(
                    false,
                    0,
                    MistakeCategory.SYNTAX_STRUCTURE,
                    "Empty Submission",
                    "No code was submitted. Please write your solution before submitting.",
                    "Review the problem prompt and write your logic inside the method body."
            );
        }

        String cleanedCode = removeComments(rawCode, lang);

        // 1. Structural / Syntax Check
        StructuralCheck syntaxCheck = checkStructure(cleanedCode, lang);
        if (!syntaxCheck.isValid()) {
            return new AnalysisResult(
                    false,
                    20,
                    MistakeCategory.SYNTAX_STRUCTURE,
                    syntaxCheck.getTitle(),
                    syntaxCheck.getMessage(),
                    syntaxCheck.getSuggestion()
            );
        }

        String topicSlug = (question.getTopic() != null && question.getTopic().getSlug() != null)
                ? question.getTopic().getSlug().toLowerCase()
                : "";

        // 2. Language-aware Topic Diagnostics
        switch (topicSlug) {
            case "loops":
                AnalysisResult loopResult = analyzeLoops(question, cleanedCode, lang);
                if (loopResult != null) return loopResult;
                break;

            case "arrays":
                AnalysisResult arrayResult = analyzeArrays(question, cleanedCode, lang);
                if (arrayResult != null) return arrayResult;
                break;

            case "functions":
                AnalysisResult funcResult = analyzeFunctions(question, cleanedCode, lang);
                if (funcResult != null) return funcResult;
                break;

            case "recursion":
                AnalysisResult recResult = analyzeRecursion(question, cleanedCode, lang);
                if (recResult != null) return recResult;
                break;

            default:
                break;
        }

        // 3. Question-level Pattern Checking
        return evaluateQuestionPatterns(question, cleanedCode, lang);
    }

    private AnalysisResult analyzeLoops(Question question, String code, String lang) {
        boolean hasLoop = code.contains("for") || code.contains("while") || code.contains("do");
        if (!hasLoop) {
            return new AnalysisResult(
                    false,
                    25,
                    MistakeCategory.LOGIC_MISMATCH,
                    "Missing Loop Construct",
                    "This problem requires iterative repetition, but no loop construct ('for' or 'while') was found.",
                    lang.equals("PYTHON")
                            ? "Use 'for i in range(...):' or a 'while' loop with an updating counter."
                            : "Use 'for (int i = 0; i < n; i++)' or a 'while' loop with an incrementing counter."
            );
        }

        if (lang.equals("PYTHON")) {
            // Python specific off-by-one in loops: range(1, n) vs range(1, n + 1) for inclusive sum
            if (question.getTitle().toLowerCase().contains("sum") &&
                    Pattern.compile("range\\s*\\(\\s*1\\s*,\\s*[a-zA-Z0-9_]+\\s*\\)").matcher(code).find()) {
                return new AnalysisResult(
                        false,
                        40,
                        MistakeCategory.OFF_BY_ONE,
                        "Off-by-One in Python range()",
                        "In Python, range(start, stop) stops before reaching 'stop' (it is exclusive of the upper bound). Calling range(1, n) will miss n itself.",
                        "Change your upper bound to 'n + 1': 'for i in range(1, n + 1):'."
                );
            }
        } else {
            // C, C++, Java: Check for accidental semicolon right after loop header
            if (Pattern.compile("(for|while)\\s*\\([^)]*\\)\\s*;").matcher(code).find()) {
                return new AnalysisResult(
                        false,
                        30,
                        MistakeCategory.INFINITE_LOOP_RISK,
                        "Premature Loop Semicolon",
                        "You placed a semicolon ';' immediately after your loop header. This causes the loop body to be empty, leading to either an infinite freeze or premature exit.",
                        "Remove the semicolon right after the parenthesis: write 'for (...) { ... }' instead of 'for (...); { ... }'."
                );
            }
        }

        // Check for infinite while loop: while(condition) without counter modification
        Matcher whileMatcher = Pattern.compile("while\\s*\\(?([^):{]+)\\)?[:{]?([\\s\\S]*?)(?:\\}|(?=\\n\\S|$))").matcher(code);
        if (whileMatcher.find()) {
            String cond = whileMatcher.group(1);
            String body = whileMatcher.group(2);
            if (cond.contains("<") || cond.contains(">") || cond.contains("!=")) {
                String var = extractFirstIdentifier(cond);
                if (var != null && !body.contains(var + "++") && !body.contains(var + "--") &&
                        !body.contains(var + " +=") && !body.contains(var + " -=") &&
                        !body.contains(var + "+=") && !body.contains(var + "-=") &&
                        !body.contains(var + "=") && !body.contains(var + " = ")) {
                    return new AnalysisResult(
                            false,
                            35,
                            MistakeCategory.INFINITE_LOOP_RISK,
                            "Infinite Loop: Counter Never Updates",
                            "The loop condition depends on '" + var + "', but '" + var + "' is never updated inside the while loop body. This will run infinitely.",
                            "Make sure to increment or update '" + var + "' inside the while loop (e.g. '" + var + " += 1' or '" + var + "++;')."
                    );
                }
            }
        }

        return null;
    }

    private AnalysisResult analyzeArrays(Question question, String code, String lang) {
        if (lang.equals("PYTHON")) {
            // Python IndexError: range(len(...) + 1)
            if (Pattern.compile("range\\s*\\([^)]*len\\s*\\([^)]*\\)\\s*\\+\\s*1\\)").matcher(code).find()) {
                return new AnalysisResult(
                        false,
                        40,
                        MistakeCategory.OFF_BY_ONE,
                        "List Index Out of Range: range(len + 1)",
                        "You added +1 to len(nums) in range(). In Python, valid list indices are 0 to len(nums) - 1. Accessing index len(nums) raises an IndexError.",
                        "Use 'range(len(nums))' or an iterator like 'for num in nums:'."
                );
            }
            boolean hasListAccess = Pattern.compile("[a-zA-Z0-9_]+\\s*\\[[^\\]]+\\]").matcher(code).find() || code.contains(" in ");
            if (!hasListAccess) {
                return new AnalysisResult(
                        false,
                        30,
                        MistakeCategory.LOGIC_MISMATCH,
                        "No List Access Found",
                        "To inspect elements, access items using 'nums[i]' or iterate directly using 'for num in nums:'.",
                        "Iterate through the list to inspect elements."
                );
            }
        } else {
            // C, C++, Java: check for <= length or <= size()
            Matcher outOfBoundsMatcher = Pattern.compile("(<=)\\s*([a-zA-Z0-9_]+\\.length|[a-zA-Z0-9_]+\\.size\\(\\)|[a-zA-Z0-9_]+_count|[a-zA-Z0-9_]+_len|n\\b|length\\b)").matcher(code);
            if (outOfBoundsMatcher.find() && (code.contains("[i]") || code.contains("[ i ]"))) {
                String errorDetail = lang.equals("JAVA")
                        ? "throw an ArrayIndexOutOfBoundsException at runtime"
                        : "cause an out-of-bounds memory error / buffer overflow";
                return new AnalysisResult(
                        false,
                        40,
                        MistakeCategory.OFF_BY_ONE,
                        "Array Index Out Of Bounds: i <= length",
                        "You used '<=' with array length. Array indices range from 0 to length - 1. Accessing index 'length' will " + errorDetail + ".",
                        "Change '<=' to '<' in your loop condition: 'for (int i = 0; i < length; i++)'."
                );
            }

            boolean hasIndexAccess = Pattern.compile("[a-zA-Z0-9_]+\\s*\\[[^\\]]+\\]").matcher(code).find();
            if (!hasIndexAccess && !code.contains(":")) {
                return new AnalysisResult(
                        false,
                        30,
                        MistakeCategory.LOGIC_MISMATCH,
                        "No Array Indexing Found",
                        "To read or modify array elements, you must access elements using square brackets like 'arr[i]'.",
                        "Iterate through the array and inspect individual elements with 'arr[i]'."
                );
            }
        }

        return null;
    }

    private AnalysisResult analyzeFunctions(Question question, String code, String lang) {
        if (!code.contains("return")) {
            return new AnalysisResult(
                    false,
                    35,
                    MistakeCategory.MISSING_RETURN,
                    "Missing Return Statement",
                    "The function is expected to calculate and return a result, but no 'return' statement was found.",
                    "Ensure you compute the result and return it using 'return result;' at the end of the function."
            );
        }

        return null;
    }

    private AnalysisResult analyzeRecursion(Question question, String code, String lang) {
        boolean hasIf = code.contains("if");
        if (!hasIf) {
            return new AnalysisResult(
                    false,
                    25,
                    MistakeCategory.BASE_CASE_MISSING,
                    "Missing Base Case in Recursion",
                    "Every recursive function must have at least one base case (an 'if' statement checking for the simplest input). Without a base case, recursion will execute infinitely until maximum call stack size is exceeded.",
                    lang.equals("PYTHON")
                            ? "Add an if-condition at the top: 'if n <= 1: return 1'."
                            : "Add an if-condition at the top: 'if (n <= 1) return 1;'."
            );
        }

        // Recursive call check
        String funcName = extractFunctionName(code, lang);
        if (funcName != null && !funcName.isEmpty()) {
            Pattern callPattern = Pattern.compile("\\b" + Pattern.quote(funcName) + "\\s*\\(");
            Matcher callMatcher = callPattern.matcher(code);

            int callCount = 0;
            while (callMatcher.find()) {
                callCount++;
            }

            if (callCount <= 1) {
                return new AnalysisResult(
                        false,
                        30,
                        MistakeCategory.LOGIC_MISMATCH,
                        "No Recursive Invocation Detected",
                        "Recursion requires the function to call itself with a smaller subproblem, but no recursive call to '" + funcName + "(...)' was found.",
                        "Call '" + funcName + "' inside the function body, passing a decremented argument like '" + funcName + "(n - 1)'."
                );
            }

            // Same argument call
            Pattern sameArgCall = Pattern.compile("\\b" + Pattern.quote(funcName) + "\\s*\\(\\s*n\\s*\\)");
            if (sameArgCall.matcher(code).find()) {
                return new AnalysisResult(
                        false,
                        35,
                        MistakeCategory.INFINITE_LOOP_RISK,
                        "Unreduced Recursive Argument",
                        "You called '" + funcName + "(n)' with the exact same argument 'n'. The problem size never shrinks, which leads to infinite recursion.",
                        "Progress towards the base case by reducing the input: '" + funcName + "(n - 1)'."
                );
            }
        }

        return null;
    }

    private AnalysisResult evaluateQuestionPatterns(Question question, String code, String lang) {
        // Universal checks: required operations
        String title = question.getTitle().toLowerCase();

        if (title.contains("even")) {
            if (!code.contains("% 2") && !code.contains("%2")) {
                return new AnalysisResult(
                        false,
                        50,
                        MistakeCategory.LOGIC_MISMATCH,
                        "Missing Even Parity Check",
                        "Checking whether a number is even requires inspecting the remainder when divided by 2 (e.g. 'num % 2 == 0').",
                        "Use the modulo operator: if (num % 2 == 0) increment your count."
                );
            }
        }

        if (title.contains("prime")) {
            if (!code.contains("%") && !code.contains("mod")) {
                return new AnalysisResult(
                        false,
                        50,
                        MistakeCategory.LOGIC_MISMATCH,
                        "Missing Divisibility Check",
                        "Checking prime status requires testing divisibility by factors using the modulo operator '%'.",
                        "Test if n % i == 0 inside your loop."
                );
            }
        }

        if (title.contains("circle") || title.contains("area")) {
            if (!code.contains("3.14") && !code.contains("PI") && !code.contains("pi") && !code.contains("M_PI")) {
                return new AnalysisResult(
                        false,
                        50,
                        MistakeCategory.LOGIC_MISMATCH,
                        "Missing Circle Area Formula",
                        "Calculating circle area requires multiplying pi by radius squared (pi * r * r).",
                        "Use Math.PI (Java), math.pi (Python), or M_PI / 3.14159 (C/C++)."
                );
            }
        }

        if (title.contains("digits")) {
            if (!code.contains("% 10") && !code.contains("%10")) {
                return new AnalysisResult(
                        false,
                        50,
                        MistakeCategory.LOGIC_MISMATCH,
                        "Missing Digit Extraction",
                        "To extract the last digit of an integer, use modulo 10 ('n % 10').",
                        "Extract the rightmost digit with 'n % 10' and recurse on the quotient 'n / 10' (or 'n // 10' in Python)."
                );
            }
        }

        return new AnalysisResult(
                true,
                100,
                MistakeCategory.NONE,
                "Solution Verified & Accepted!",
                "Great job! Your " + lang + " solution satisfies all structural, logical, and algorithmic constraints.",
                "Optimal solution! You demonstrated strong understanding of the concept in " + lang + ". Keep up the great work!"
        );
    }

    private StructuralCheck checkStructure(String code, String lang) {
        if (lang.equals("PYTHON")) {
            int parenCount = 0;
            int bracketCount = 0;
            for (int i = 0; i < code.length(); i++) {
                char c = code.charAt(i);
                if (c == '(') parenCount++;
                else if (c == ')') {
                    parenCount--;
                    if (parenCount < 0) return new StructuralCheck(false, "Mismatched Parenthesis", "Unexpected closing parenthesis ')' in Python code.", "Check your function calls and expressions.");
                } else if (c == '[') bracketCount++;
                else if (c == ']') {
                    bracketCount--;
                    if (bracketCount < 0) return new StructuralCheck(false, "Mismatched Bracket", "Unexpected closing bracket ']' in Python list syntax.", "Check list indexing brackets.");
                }
            }
            if (parenCount != 0) return new StructuralCheck(false, "Unclosed Parentheses", "There are unclosed parentheses in your Python code.", "Ensure all '(' have matching ')'.");
            if (bracketCount != 0) return new StructuralCheck(false, "Unclosed Brackets", "There are unclosed brackets in your Python code.", "Ensure all '[' have matching ']'.");

            return new StructuralCheck(true, null, null, null);
        }

        // C, C++, Java brace & paren check
        int braceCount = 0;
        int parenCount = 0;

        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);
            if (c == '{') braceCount++;
            else if (c == '}') {
                braceCount--;
                if (braceCount < 0) {
                    return new StructuralCheck(false, "Unexpected Closing Brace",
                            "Found a closing brace '}' without a matching opening '{'.",
                            "Check your block scoping and ensure braces are properly paired.");
                }
            } else if (c == '(') parenCount++;
            else if (c == ')') {
                parenCount--;
                if (parenCount < 0) {
                    return new StructuralCheck(false, "Unexpected Closing Parenthesis",
                            "Found a closing parenthesis ')' without a matching opening '('.",
                            "Check your conditions and expressions for mismatched parentheses.");
                }
            }
        }

        if (braceCount != 0) {
            return new StructuralCheck(false, "Unclosed Curly Braces",
                    "There are " + braceCount + " unclosed curly brace(s) '{'. Every opening brace must have a closing brace.",
                    "Add matching '}' at the end of the class or function.");
        }

        if (parenCount != 0) {
            return new StructuralCheck(false, "Unclosed Parentheses",
                    "There are unclosed parentheses '(' in your code.",
                    "Ensure every opened parenthesis is closed.");
        }

        return new StructuralCheck(true, null, null, null);
    }

    private String removeComments(String code, String lang) {
        if (lang.equals("PYTHON")) {
            String noDoc = code.replaceAll("\"\"\"[\\s\\S]*?\"\"\"", "").replaceAll("'''[\\s\\S]*?'''", "");
            return noDoc.replaceAll("#.*", "");
        }
        String noBlock = code.replaceAll("/\\*.*?\\*/", "");
        return noBlock.replaceAll("//.*", "");
    }

    private String extractFunctionName(String code, String lang) {
        if (code == null) return null;
        if (lang.equals("PYTHON")) {
            Matcher m = Pattern.compile("def\\s+([a-zA-Z0-9_]+)\\s*\\(").matcher(code);
            if (m.find()) return m.group(1);
        }
        Matcher m = Pattern.compile("(?:public|private|protected|static|inline|\\s)+[a-zA-Z0-9_<>\\[\\]*&]+\\s+([a-zA-Z0-9_]+)\\s*\\(").matcher(code);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

    private String extractFirstIdentifier(String condition) {
        Matcher m = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*").matcher(condition);
        if (m.find()) {
            return m.group(0);
        }
        return null;
    }

    private static class StructuralCheck {
        private final boolean valid;
        private final String title;
        private final String message;
        private final String suggestion;

        public StructuralCheck(boolean valid, String title, String message, String suggestion) {
            this.valid = valid;
            this.title = title;
            this.message = message;
            this.suggestion = suggestion;
        }

        public boolean isValid() {
            return valid;
        }

        public String getTitle() {
            return title;
        }

        public String getMessage() {
            return message;
        }

        public String getSuggestion() {
            return suggestion;
        }
    }
}
