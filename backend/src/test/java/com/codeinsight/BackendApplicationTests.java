package com.codeinsight;

import com.codeinsight.dto.DashboardSummaryDto;
import com.codeinsight.dto.SubmissionRequest;
import com.codeinsight.dto.SubmissionResponse;
import com.codeinsight.entity.MistakeCategory;
import com.codeinsight.entity.Question;
import com.codeinsight.repository.QuestionRepository;
import com.codeinsight.repository.TopicRepository;
import com.codeinsight.service.AnalyticsService;
import com.codeinsight.service.SubmissionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BackendApplicationTests {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private AnalyticsService analyticsService;

    @Test
    void testDatabaseSeedingAndSubmissions() {
        // 1. Verify topics and questions seeded by DataInitializer
        long topicCount = topicRepository.count();
        long questionCount = questionRepository.count();
        assertTrue(topicCount >= 4, "Should have at least 4 topics (loops, arrays, functions, recursion)");
        assertTrue(questionCount >= 12, "Should have at least 12 questions seeded");

        // 2. Test submitting a Java solution to MySQL
        List<Question> questions = questionRepository.findAll();
        assertFalse(questions.isEmpty());
        Question firstQ = questions.get(0);

        String testStudent = "test-student-" + System.currentTimeMillis();

        SubmissionRequest request = new SubmissionRequest();
        request.setQuestionId(firstQ.getId());
        request.setStudentIdentifier(testStudent);
        request.setLanguage("JAVA");
        request.setSubmittedCode(firstQ.getSampleSolution());

        SubmissionResponse response = submissionService.submitCode(request);
        assertNotNull(response.getId(), "Submission should be persisted with generated ID");
        assertTrue(response.getIsCorrect(), "Sample solution should be accepted");
        assertEquals("JAVA", response.getLanguage());
        assertEquals(100, response.getScore());
        assertEquals(MistakeCategory.NONE, response.getMistakeCategory());

        // 3. Test submitting a Python solution to MySQL
        SubmissionRequest pyReq = new SubmissionRequest();
        pyReq.setQuestionId(firstQ.getId());
        pyReq.setStudentIdentifier(testStudent);
        pyReq.setLanguage("PYTHON");
        pyReq.setSubmittedCode("def sum_up_to(n: int) -> int:\n    total = 0\n    for i in range(1, n + 1):\n        total += i\n    return total\n");

        SubmissionResponse pyResp = submissionService.submitCode(pyReq);
        assertNotNull(pyResp.getId());
        assertTrue(pyResp.getIsCorrect());
        assertEquals("PYTHON", pyResp.getLanguage());

        // 4. Test submitting a C solution to MySQL
        SubmissionRequest cReq = new SubmissionRequest();
        cReq.setQuestionId(firstQ.getId());
        cReq.setStudentIdentifier(testStudent);
        cReq.setLanguage("C");
        cReq.setSubmittedCode("#include <stdio.h>\nint sumUpTo(int n) {\n    int sum = 0;\n    for (int i = 1; i <= n; i++) sum += i;\n    return sum;\n}\n");

        SubmissionResponse cResp = submissionService.submitCode(cReq);
        assertNotNull(cResp.getId());
        assertTrue(cResp.getIsCorrect());
        assertEquals("C", cResp.getLanguage());

        // 5. Test submitting a C++ solution to MySQL
        SubmissionRequest cppReq = new SubmissionRequest();
        cppReq.setQuestionId(firstQ.getId());
        cppReq.setStudentIdentifier(testStudent);
        cppReq.setLanguage("CPP");
        cppReq.setSubmittedCode("#include <iostream>\nint sumUpTo(int n) {\n    int sum = 0;\n    for (int i = 1; i <= n; i++) sum += i;\n    return sum;\n}\n");

        SubmissionResponse cppResp = submissionService.submitCode(cppReq);
        assertNotNull(cppResp.getId());
        assertTrue(cppResp.getIsCorrect());
        assertEquals("CPP", cppResp.getLanguage());

        // 6. Test submitting an incorrect attempt to verify mistake detection & MySQL persistence
        SubmissionRequest wrongReq = new SubmissionRequest();
        wrongReq.setQuestionId(firstQ.getId());
        wrongReq.setStudentIdentifier(testStudent);
        wrongReq.setLanguage("PYTHON");
        wrongReq.setSubmittedCode("def sum_up_to(n: int) -> int:\n    total = 0\n    for i in range(1, n):\n        total += i\n    return total\n");

        SubmissionResponse wrongResp = submissionService.submitCode(wrongReq);
        assertNotNull(wrongResp.getId());
        assertFalse(wrongResp.getIsCorrect());
        assertEquals(MistakeCategory.OFF_BY_ONE, wrongResp.getMistakeCategory());

        // 7. Test analytics retrieval
        DashboardSummaryDto dashboard = analyticsService.getDashboardSummary(testStudent);
        assertNotNull(dashboard);
        assertEquals(5, dashboard.getTotalAttempts());
        assertEquals(4, dashboard.getSuccessfulAttempts());
        assertNotNull(dashboard.getTopRecommendation());
    }
}
