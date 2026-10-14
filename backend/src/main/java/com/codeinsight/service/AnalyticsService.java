package com.codeinsight.service;

import com.codeinsight.dto.*;
import com.codeinsight.entity.MistakeCategory;
import com.codeinsight.entity.Question;
import com.codeinsight.entity.Submission;
import com.codeinsight.entity.Topic;
import com.codeinsight.repository.QuestionRepository;
import com.codeinsight.repository.SubmissionRepository;
import com.codeinsight.repository.TopicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AnalyticsService {

    private final SubmissionRepository submissionRepository;
    private final TopicRepository topicRepository;
    private final QuestionRepository questionRepository;
    private final TopicService topicService;

    public AnalyticsService(SubmissionRepository submissionRepository,
                            TopicRepository topicRepository,
                            QuestionRepository questionRepository,
                            TopicService topicService) {
        this.submissionRepository = submissionRepository;
        this.topicRepository = topicRepository;
        this.questionRepository = questionRepository;
        this.topicService = topicService;
    }

    public DashboardSummaryDto getDashboardSummary(String studentIdentifier) {
        String studentId = sanitizeStudentId(studentIdentifier);
        List<Submission> submissions = submissionRepository.findAllByStudentWithDetails(studentId);
        List<TopicDto> topics = topicService.getAllTopicsWithProgress(studentId);
        long totalQuestions = questionRepository.count();

        long solvedQuestions = submissions.stream()
                .filter(Submission::getIsCorrect)
                .map(s -> s.getQuestion().getId())
                .distinct()
                .count();

        long totalAttempts = submissions.size();
        long successfulAttempts = submissions.stream().filter(Submission::getIsCorrect).count();
        double overallAccuracy = totalAttempts > 0
                ? Math.round(((double) successfulAttempts / totalAttempts) * 1000.0) / 10.0
                : 0.0;

        int streak = calculateStreakDays(submissions);

        WeakTopicAnalysisDto weakAnalysis = analyzeWeakTopics(studentId, submissions, topics);

        DashboardSummaryDto summary = new DashboardSummaryDto();
        summary.setTotalQuestions(totalQuestions);
        summary.setSolvedQuestions(solvedQuestions);
        summary.setTotalAttempts(totalAttempts);
        summary.setSuccessfulAttempts(successfulAttempts);
        summary.setOverallAccuracy(overallAccuracy);
        summary.setCurrentStreakDays(streak);
        summary.setTopics(topics);

        if (weakAnalysis.getWeakestTopic() != null) {
            summary.setWeakestTopicName(weakAnalysis.getWeakestTopic().getTopicName());
        }
        if (weakAnalysis.getStrongestTopic() != null) {
            summary.setStrongestTopicName(weakAnalysis.getStrongestTopic().getTopicName());
        }

        // Recent 5 submissions
        List<SubmissionResponse> recent = submissions.stream()
                .limit(5)
                .map(this::toSubmissionResponse)
                .collect(Collectors.toList());
        summary.setRecentSubmissions(recent);

        // Top recommendation
        summary.setTopRecommendation(getRecommendations(studentId));

        return summary;
    }

    public WeakTopicAnalysisDto getWeakTopicAnalysis(String studentIdentifier) {
        String studentId = sanitizeStudentId(studentIdentifier);
        List<Submission> submissions = submissionRepository.findAllByStudentWithDetails(studentId);
        List<TopicDto> topics = topicService.getAllTopicsWithProgress(studentId);
        return analyzeWeakTopics(studentId, submissions, topics);
    }

    public RecommendationDto getRecommendations(String studentIdentifier) {
        String studentId = sanitizeStudentId(studentIdentifier);
        WeakTopicAnalysisDto analysis = getWeakTopicAnalysis(studentId);
        List<Question> allQuestions = questionRepository.findAllWithTopic();
        List<Submission> submissions = submissionRepository.findAllByStudentWithDetails(studentId);

        Set<Long> solvedIds = submissions.stream()
                .filter(Submission::getIsCorrect)
                .map(s -> s.getQuestion().getId())
                .collect(Collectors.toSet());

        RecommendationDto rec = new RecommendationDto();

        // 1. If there's an identified weak topic with failed attempts
        if (analysis.getWeakestTopic() != null && analysis.getWeakestTopic().getFailedAttempts() > 0) {
            TopicProgressDto weak = analysis.getWeakestTopic();
            rec.setTargetTopicId(weak.getTopicId());
            rec.setTargetTopicName(weak.getTopicName());
            rec.setTargetTopicSlug(weak.getTopicSlug());
            rec.setPriorityLevel("HIGH");
            rec.setRationale("Based on " + weak.getFailedAttempts() + " unsuccessful attempt(s) in " + weak.getTopicName() +
                    " (Accuracy: " + weak.getAccuracyRate() + "%). Your most frequent challenge is " +
                    (analysis.getPrimaryMistakeName() != null ? analysis.getPrimaryMistakeName() : "logical structure") + ".");

            rec.setLearningTip(getTopicSpecificTip(weak.getTopicSlug(), analysis.getPrimaryMistake()));
            rec.setRecommendedAction("Target unsolved questions in " + weak.getTopicName() + " to improve mastery.");

            // Find up to 3 unsolved questions from this topic
            List<QuestionSummaryDto> recommendedQuestions = allQuestions.stream()
                    .filter(q -> q.getTopic().getId().equals(weak.getTopicId()))
                    .filter(q -> !solvedIds.contains(q.getId()))
                    .limit(3)
                    .map(this::toQuestionSummary)
                    .collect(Collectors.toList());

            rec.setRecommendedQuestions(recommendedQuestions);
            return rec;
        }

        // 2. Otherwise recommend the first uncompleted or unattempted topic
        for (TopicProgressDto tp : analysis.getAllTopicProgress()) {
            if (tp.getMasteryPercentage() < 100.0) {
                rec.setTargetTopicId(tp.getTopicId());
                rec.setTargetTopicName(tp.getTopicName());
                rec.setTargetTopicSlug(tp.getTopicSlug());
                rec.setPriorityLevel(tp.getTotalAttempts() == 0 ? "MEDIUM" : "LOW");
                rec.setRationale("Continue your structured coding journey by working through " + tp.getTopicName() +
                        " (" + tp.getSolvedQuestions() + "/" + tp.getTotalQuestions() + " completed).");
                rec.setLearningTip(getTopicSpecificTip(tp.getTopicSlug(), null));
                rec.setRecommendedAction("Practice the next beginner challenge in " + tp.getTopicName() + ".");

                List<QuestionSummaryDto> recommendedQuestions = allQuestions.stream()
                        .filter(q -> q.getTopic().getId().equals(tp.getTopicId()))
                        .filter(q -> !solvedIds.contains(q.getId()))
                        .limit(3)
                        .map(this::toQuestionSummary)
                        .collect(Collectors.toList());

                rec.setRecommendedQuestions(recommendedQuestions);
                return rec;
            }
        }

        // 3. All solved!
        rec.setTargetTopicName("Mastery Achieved");
        rec.setPriorityLevel("LOW");
        rec.setRationale("Exceptional progress! You have successfully completed all core coding practice questions.");
        rec.setLearningTip("Keep your coding fluency sharp by reviewing time and space complexity optimizations.");
        rec.setRecommendedAction("Review past submissions or test yourself by refactoring solutions with clean code principles.");
        rec.setRecommendedQuestions(Collections.emptyList());
        return rec;
    }

    @Transactional
    public void resetStudentProgress(String studentIdentifier) {
        String studentId = sanitizeStudentId(studentIdentifier);
        List<Submission> submissions = submissionRepository.findByStudentIdentifierOrderBySubmittedAtDesc(studentId);
        submissionRepository.deleteAll(submissions);
    }

    private WeakTopicAnalysisDto analyzeWeakTopics(String studentId, List<Submission> submissions, List<TopicDto> topics) {
        WeakTopicAnalysisDto analysis = new WeakTopicAnalysisDto();

        Map<Long, List<Submission>> subsByTopic = submissions.stream()
                .collect(Collectors.groupingBy(s -> s.getQuestion().getTopic().getId()));

        List<TopicProgressDto> progressList = new ArrayList<>();

        for (TopicDto topic : topics) {
            List<Submission> topicSubs = subsByTopic.getOrDefault(topic.getId(), Collections.emptyList());
            long totalAttempts = topicSubs.size();
            long successfulAttempts = topicSubs.stream().filter(Submission::getIsCorrect).count();
            long failedAttempts = totalAttempts - successfulAttempts;

            double accuracyRate = totalAttempts > 0
                    ? Math.round(((double) successfulAttempts / totalAttempts) * 1000.0) / 10.0
                    : 0.0;

            String status;
            if (totalAttempts == 0) {
                status = "Unattempted";
            } else if (accuracyRate < 50.0 || failedAttempts >= 2) {
                status = "Needs Focus";
            } else if (accuracyRate < 75.0) {
                status = "Developing";
            } else if (topic.getMasteryPercentage() >= 100.0) {
                status = "Mastered";
            } else {
                status = "Proficient";
            }

            TopicProgressDto tp = new TopicProgressDto();
            tp.setTopicId(topic.getId());
            tp.setTopicName(topic.getName());
            tp.setTopicSlug(topic.getSlug());
            tp.setIcon(topic.getIcon());
            tp.setColor(topic.getColor());
            tp.setTotalQuestions(topic.getTotalQuestions());
            tp.setSolvedQuestions(topic.getSolvedQuestions());
            tp.setTotalAttempts(totalAttempts);
            tp.setSuccessfulAttempts(successfulAttempts);
            tp.setFailedAttempts(failedAttempts);
            tp.setAccuracyRate(accuracyRate);
            tp.setMasteryPercentage(topic.getMasteryPercentage());
            tp.setMasteryStatus(status);

            progressList.add(tp);
        }

        analysis.setAllTopicProgress(progressList);

        // Find topics needing focus
        List<TopicProgressDto> needingFocus = progressList.stream()
                .filter(tp -> "Needs Focus".equals(tp.getMasteryStatus()))
                .collect(Collectors.toList());
        analysis.setTopicsNeedingFocus(needingFocus);

        // Determine weakest topic: lowest accuracy among attempted with failed attempts
        TopicProgressDto weakest = progressList.stream()
                .filter(tp -> tp.getTotalAttempts() > 0 && tp.getFailedAttempts() > 0)
                .min(Comparator.comparingDouble(TopicProgressDto::getAccuracyRate)
                        .thenComparing(Comparator.comparingLong(TopicProgressDto::getFailedAttempts).reversed()))
                .orElse(null);
        analysis.setWeakestTopic(weakest);

        // Determine strongest topic: highest accuracy among attempted
        TopicProgressDto strongest = progressList.stream()
                .filter(tp -> tp.getTotalAttempts() > 0 && tp.getSuccessfulAttempts() > 0)
                .max(Comparator.comparingDouble(TopicProgressDto::getAccuracyRate)
                        .thenComparingDouble(TopicProgressDto::getMasteryPercentage))
                .orElse(null);
        analysis.setStrongestTopic(strongest);

        // Analyze mistakes
        Map<MistakeCategory, Long> mistakeCounts = submissions.stream()
                .filter(s -> !s.getIsCorrect() && s.getMistakeCategory() != MistakeCategory.NONE)
                .collect(Collectors.groupingBy(Submission::getMistakeCategory, Collectors.counting()));

        long totalMistakes = mistakeCounts.values().stream().mapToLong(Long::longValue).sum();

        List<MistakeStatDto> mistakeBreakdown = new ArrayList<>();
        MistakeCategory topMistake = null;
        long maxCount = 0;

        for (Map.Entry<MistakeCategory, Long> entry : mistakeCounts.entrySet()) {
            MistakeCategory cat = entry.getKey();
            long count = entry.getValue();
            double pct = totalMistakes > 0 ? Math.round(((double) count / totalMistakes) * 1000.0) / 10.0 : 0.0;
            mistakeBreakdown.add(new MistakeStatDto(cat, cat.getDisplayName(), count, pct, cat.getGeneralExplanation()));

            if (count > maxCount) {
                maxCount = count;
                topMistake = cat;
            }
        }

        mistakeBreakdown.sort((a, b) -> Long.compare(b.getCount(), a.getCount()));
        analysis.setMistakeBreakdown(mistakeBreakdown);

        if (topMistake != null) {
            analysis.setPrimaryMistake(topMistake);
            analysis.setPrimaryMistakeName(topMistake.getDisplayName());
            analysis.setPrimaryMistakeExplanation(topMistake.getGeneralExplanation());
        }

        // Diagnostic narrative summary
        if (totalMistakes == 0 && submissions.isEmpty()) {
            analysis.setDiagnosticSummary("Welcome to CodeInsight! Start with Loops and Arrays to establish your foundational problem-solving pattern.");
        } else if (totalMistakes == 0) {
            analysis.setDiagnosticSummary("Flawless run! Every submitted attempt has been accepted with zero recorded mistake categories.");
        } else {
            String weakName = (weakest != null) ? weakest.getTopicName() : "current exercises";
            String mistakeName = (topMistake != null) ? topMistake.getDisplayName() : "edge cases";
            analysis.setDiagnosticSummary("Analysis indicates recurrent friction in " + weakName +
                    ", primarily caused by " + mistakeName + ". Reviewing targeted boundary conditions and base patterns will accelerate your progress.");
        }

        return analysis;
    }

    private int calculateStreakDays(List<Submission> submissions) {
        if (submissions.isEmpty()) return 0;
        Set<LocalDate> dates = submissions.stream()
                .map(s -> s.getSubmittedAt().toLocalDate())
                .collect(Collectors.toSet());

        LocalDate today = LocalDate.now();
        int streak = 0;
        LocalDate check = today;

        if (!dates.contains(check)) {
            check = check.minusDays(1);
        }

        while (dates.contains(check)) {
            streak++;
            check = check.minusDays(1);
        }

        return streak;
    }

    private String getTopicSpecificTip(String topicSlug, MistakeCategory mistake) {
        if (mistake == MistakeCategory.OFF_BY_ONE) {
            return "Off-by-One Tip: Always remember that Java indices are 0-based. If array length is N, the last valid index is N-1. Use '< N' rather than '<= N'.";
        }
        if (mistake == MistakeCategory.BASE_CASE_MISSING) {
            return "Recursion Tip: Before writing recursive logic, ask yourself: 'What is the absolute smallest input where I immediately know the answer without further calls?' Handle that first with an 'if'.";
        }
        if (mistake == MistakeCategory.INFINITE_LOOP_RISK) {
            return "Loop Safety Tip: Ensure that the counter variable tested in your 'while' condition is modified on every single execution path within the loop.";
        }

        if (topicSlug == null) return "Focus on breaking down problems into smaller steps before writing syntax.";

        switch (topicSlug.toLowerCase()) {
            case "loops":
                return "Loops Rule: Clarify whether the upper boundary should be inclusive or exclusive before setting loop conditions.";
            case "arrays":
                return "Arrays Rule: Check for null or empty array inputs before accessing index 0 to prevent NullPointerException or ArrayIndexOutOfBoundsException.";
            case "functions":
                return "Functions Rule: Write single-responsibility functions that take explicit parameters and return explicit values rather than mutating shared global state.";
            case "recursion":
                return "Recursion Rule: A solid recursive function always has two parts: 1) Base case (stops recursion) and 2) Recursive case (reduces the problem towards the base case).";
            default:
                return "Trace your code step-by-step with small example inputs (e.g. n=0, n=1, n=2) before submitting.";
        }
    }

    private String sanitizeStudentId(String studentIdentifier) {
        return (studentIdentifier != null && !studentIdentifier.trim().isEmpty())
                ? studentIdentifier.trim()
                : "student-1";
    }

    private SubmissionResponse toSubmissionResponse(Submission s) {
        SubmissionResponse res = new SubmissionResponse();
        res.setId(s.getId());
        res.setQuestionId(s.getQuestion().getId());
        res.setQuestionTitle(s.getQuestion().getTitle());
        res.setTopicName(s.getQuestion().getTopic().getName());
        res.setTopicSlug(s.getQuestion().getTopic().getSlug());
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

    private QuestionSummaryDto toQuestionSummary(Question q) {
        QuestionSummaryDto dto = new QuestionSummaryDto();
        dto.setId(q.getId());
        dto.setTitle(q.getTitle());
        dto.setDifficulty(q.getDifficulty());
        dto.setTopicId(q.getTopic().getId());
        dto.setTopicName(q.getTopic().getName());
        dto.setTopicSlug(q.getTopic().getSlug());
        dto.setSolved(false);
        dto.setAttemptCount(0);
        return dto;
    }
}
