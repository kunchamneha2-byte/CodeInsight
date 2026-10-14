package com.codeinsight.service;

import com.codeinsight.dto.SubmissionRequest;
import com.codeinsight.dto.SubmissionResponse;
import com.codeinsight.entity.Question;
import com.codeinsight.entity.Submission;
import com.codeinsight.exception.ResourceNotFoundException;
import com.codeinsight.repository.QuestionRepository;
import com.codeinsight.repository.SubmissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final QuestionRepository questionRepository;
    private final CodeAnalysisService codeAnalysisService;

    public SubmissionService(SubmissionRepository submissionRepository,
                             QuestionRepository questionRepository,
                             CodeAnalysisService codeAnalysisService) {
        this.submissionRepository = submissionRepository;
        this.questionRepository = questionRepository;
        this.codeAnalysisService = codeAnalysisService;
    }

    @Transactional
    public SubmissionResponse submitCode(SubmissionRequest request) {
        Question question = questionRepository.findByIdWithTopic(request.getQuestionId());
        if (question == null) {
            throw new ResourceNotFoundException("Question not found with id: " + request.getQuestionId());
        }

        String studentId = (request.getStudentIdentifier() != null && !request.getStudentIdentifier().trim().isEmpty())
                ? request.getStudentIdentifier().trim()
                : "student-1";

        String language = (request.getLanguage() != null && !request.getLanguage().trim().isEmpty())
                ? request.getLanguage().trim().toUpperCase()
                : "JAVA";

        // Analyze code safely with language-specific rules
        CodeAnalysisService.AnalysisResult analysis = codeAnalysisService.analyze(question, request.getSubmittedCode(), language);

        // Persist attempt in MySQL
        Submission submission = new Submission(
                studentId,
                question,
                language,
                request.getSubmittedCode(),
                analysis.isCorrect(),
                analysis.getScore(),
                analysis.getMistakeCategory(),
                analysis.getFeedbackTitle(),
                analysis.getFeedbackExplanation(),
                analysis.getSuggestions()
        );

        Submission saved = submissionRepository.save(submission);

        // Build Response
        SubmissionResponse response = toResponse(saved);
        response.setLanguage(language);

        // Language-appropriate sample solution
        String sampleSolution = getSampleSolutionForLanguage(question, language);
        response.setSampleSolution(sampleSolution);

        return response;
    }

    @Transactional(readOnly = true)
    public List<SubmissionResponse> getSubmissionHistory(String studentIdentifier, Long questionId) {
        String studentId = (studentIdentifier != null && !studentIdentifier.trim().isEmpty())
                ? studentIdentifier.trim()
                : "student-1";

        List<Submission> list;
        if (questionId != null) {
            list = submissionRepository.findByStudentIdentifierAndQuestionIdOrderBySubmittedAtDesc(studentId, questionId);
        } else {
            list = submissionRepository.findAllByStudentWithDetails(studentId);
        }

        return list.stream().map(this::toResponse).collect(Collectors.toList());
    }

    private SubmissionResponse toResponse(Submission s) {
        SubmissionResponse res = new SubmissionResponse();
        res.setId(s.getId());
        res.setQuestionId(s.getQuestion().getId());
        res.setQuestionTitle(s.getQuestion().getTitle());
        res.setTopicName(s.getQuestion().getTopic().getName());
        res.setTopicSlug(s.getQuestion().getTopic().getSlug());
        res.setLanguage(s.getLanguage() != null ? s.getLanguage() : "JAVA");
        res.setIsCorrect(s.getIsCorrect());
        res.setScore(s.getScore());
        res.setMistakeCategory(s.getMistakeCategory());
        res.setMistakeDisplayName(s.getMistakeCategory().getDisplayName());
        res.setFeedbackTitle(s.getFeedbackTitle());
        res.setFeedbackExplanation(s.getFeedbackExplanation());
        res.setSuggestions(s.getSuggestions());
        res.setSubmittedAt(s.getSubmittedAt());
        return res;
    }

    private String getSampleSolutionForLanguage(Question q, String language) {
        switch (language) {
            case "PYTHON":
                return q.getSampleSolutionPython() != null ? q.getSampleSolutionPython() : q.getSampleSolution();
            case "C":
                return q.getSampleSolutionC() != null ? q.getSampleSolutionC() : q.getSampleSolution();
            case "CPP":
                return q.getSampleSolutionCpp() != null ? q.getSampleSolutionCpp() : q.getSampleSolution();
            default:
                return q.getSampleSolution();
        }
    }
}
