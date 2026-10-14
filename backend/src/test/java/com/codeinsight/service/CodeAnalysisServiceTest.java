package com.codeinsight.service;

import com.codeinsight.entity.Difficulty;
import com.codeinsight.entity.MistakeCategory;
import com.codeinsight.entity.Question;
import com.codeinsight.entity.Topic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CodeAnalysisServiceTest {

    private CodeAnalysisService service;

    @BeforeEach
    void setUp() {
        service = new CodeAnalysisService();
    }

    @Test
    void testValidLoopSubmission() {
        Topic topic = new Topic("Loops", "loops", "desc", "Repeat", "#3B82F6", 1);
        Question q = new Question();
        q.setTopic(topic);
        q.setTitle("Sum of First N Numbers");
        q.setRequiredPatterns("for;sum;=");
        q.setStarterCode("public class Solution {\n public static int sumUpTo(int n) {\n int sum = 0;\n return sum;\n }\n}");

        String studentCode = "public class Solution {\n" +
                "    public static int sumUpTo(int n) {\n" +
                "        int sum = 0;\n" +
                "        for (int i = 1; i <= n; i++) {\n" +
                "            sum += i;\n" +
                "        }\n" +
                "        return sum;\n" +
                "    }\n" +
                "}";

        CodeAnalysisService.AnalysisResult result = service.analyze(q, studentCode, "JAVA");
        assertTrue(result.isCorrect());
        assertEquals(100, result.getScore());
        assertEquals(MistakeCategory.NONE, result.getMistakeCategory());
    }

    @Test
    void testValidPythonSubmission() {
        Topic topic = new Topic("Loops", "loops", "desc", "Repeat", "#3B82F6", 1);
        Question q = new Question();
        q.setTopic(topic);
        q.setTitle("Sum of First N Numbers");

        String pythonCode = "def sum_up_to(n: int) -> int:\n" +
                "    total = 0\n" +
                "    for i in range(1, n + 1):\n" +
                "        total += i\n" +
                "    return total\n";

        CodeAnalysisService.AnalysisResult result = service.analyze(q, pythonCode, "PYTHON");
        assertTrue(result.isCorrect());
        assertEquals(100, result.getScore());
        assertEquals(MistakeCategory.NONE, result.getMistakeCategory());
    }

    @Test
    void testPythonOffByOneRange() {
        Topic topic = new Topic("Loops", "loops", "desc", "Repeat", "#3B82F6", 1);
        Question q = new Question();
        q.setTopic(topic);
        q.setTitle("Sum of First N Numbers");

        String pythonCode = "def sum_up_to(n: int) -> int:\n" +
                "    total = 0\n" +
                "    for i in range(1, n):\n" +
                "        total += i\n" +
                "    return total\n";

        CodeAnalysisService.AnalysisResult result = service.analyze(q, pythonCode, "PYTHON");
        assertFalse(result.isCorrect());
        assertEquals(MistakeCategory.OFF_BY_ONE, result.getMistakeCategory());
        assertTrue(result.getFeedbackExplanation().contains("range(1, n)"));
    }

    @Test
    void testValidCSubmission() {
        Topic topic = new Topic("Loops", "loops", "desc", "Repeat", "#3B82F6", 1);
        Question q = new Question();
        q.setTopic(topic);
        q.setTitle("Sum of First N Numbers");

        String cCode = "#include <stdio.h>\n" +
                "int sumUpTo(int n) {\n" +
                "    int sum = 0;\n" +
                "    for (int i = 1; i <= n; i++) {\n" +
                "        sum += i;\n" +
                "    }\n" +
                "    return sum;\n" +
                "}\n";

        CodeAnalysisService.AnalysisResult result = service.analyze(q, cCode, "C");
        assertTrue(result.isCorrect());
        assertEquals(100, result.getScore());
    }

    @Test
    void testValidCppSubmission() {
        Topic topic = new Topic("Loops", "loops", "desc", "Repeat", "#3B82F6", 1);
        Question q = new Question();
        q.setTopic(topic);
        q.setTitle("Sum of First N Numbers");

        String cppCode = "#include <iostream>\n" +
                "int sumUpTo(int n) {\n" +
                "    int sum = 0;\n" +
                "    for (int i = 1; i <= n; i++) {\n" +
                "        sum += i;\n" +
                "    }\n" +
                "    return sum;\n" +
                "}\n";

        CodeAnalysisService.AnalysisResult result = service.analyze(q, cppCode, "CPP");
        assertTrue(result.isCorrect());
        assertEquals(100, result.getScore());
    }

    @Test
    void testArrayOutOfBoundsDetection() {
        Topic topic = new Topic("Arrays", "arrays", "desc", "Layers", "#10B981", 2);
        Question q = new Question();
        q.setTopic(topic);
        q.setTitle("Find Max");
        q.setStarterCode("public class Solution { public static int findMax(int[] nums) {} }");

        String studentCode = "public class Solution {\n" +
                "    public static int findMax(int[] nums) {\n" +
                "        int max = nums[0];\n" +
                "        for (int i = 0; i <= nums.length; i++) {\n" +
                "            if (nums[i] > max) max = nums[i];\n" +
                "        }\n" +
                "        return max;\n" +
                "    }\n" +
                "}";

        CodeAnalysisService.AnalysisResult result = service.analyze(q, studentCode, "JAVA");
        assertFalse(result.isCorrect());
        assertEquals(MistakeCategory.OFF_BY_ONE, result.getMistakeCategory());
        assertTrue(result.getFeedbackExplanation().contains("ArrayIndexOutOfBoundsException"));
    }

    @Test
    void testRecursionMissingBaseCaseDetection() {
        Topic topic = new Topic("Recursion", "recursion", "desc", "GitFork", "#F59E0B", 4);
        Question q = new Question();
        q.setTopic(topic);
        q.setTitle("Factorial");
        q.setStarterCode("public class Solution { public static int factorial(int n) {} }");

        String studentCode = "public class Solution {\n" +
                "    public static int factorial(int n) {\n" +
                "        return n * factorial(n - 1);\n" +
                "    }\n" +
                "}";

        CodeAnalysisService.AnalysisResult result = service.analyze(q, studentCode, "JAVA");
        assertFalse(result.isCorrect());
        assertEquals(MistakeCategory.BASE_CASE_MISSING, result.getMistakeCategory());
        assertTrue(result.getFeedbackTitle().contains("Missing Base Case"));
    }

    @Test
    void testLoopPrematureSemicolon() {
        Topic topic = new Topic("Loops", "loops", "desc", "Repeat", "#3B82F6", 1);
        Question q = new Question();
        q.setTopic(topic);
        q.setTitle("Loops Test");
        q.setStarterCode("public class Solution { public static int test(int n) {} }");

        String studentCode = "public class Solution {\n" +
                "    public static int test(int n) {\n" +
                "        int sum = 0;\n" +
                "        for (int i = 0; i < n; i++); {\n" +
                "            sum += i;\n" +
                "        }\n" +
                "        return sum;\n" +
                "    }\n" +
                "}";

        CodeAnalysisService.AnalysisResult result = service.analyze(q, studentCode, "JAVA");
        assertFalse(result.isCorrect());
        assertEquals(MistakeCategory.INFINITE_LOOP_RISK, result.getMistakeCategory());
    }

    @Test
    void testUnbalancedBraces() {
        Question q = new Question();
        String code = "public class Solution { public static void run() { ";

        CodeAnalysisService.AnalysisResult result = service.analyze(q, code, "JAVA");
        assertFalse(result.isCorrect());
        assertEquals(MistakeCategory.SYNTAX_STRUCTURE, result.getMistakeCategory());
    }
}
