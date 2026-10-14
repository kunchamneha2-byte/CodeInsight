package com.codeinsight.service;

import com.codeinsight.dto.QuestionSummaryDto;
import com.codeinsight.dto.TopicDto;
import com.codeinsight.entity.Question;
import com.codeinsight.entity.Submission;
import com.codeinsight.entity.Topic;
import com.codeinsight.exception.ResourceNotFoundException;
import com.codeinsight.repository.QuestionRepository;
import com.codeinsight.repository.SubmissionRepository;
import com.codeinsight.repository.TopicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TopicService {

    private final TopicRepository topicRepository;
    private final QuestionRepository questionRepository;
    private final SubmissionRepository submissionRepository;

    public TopicService(TopicRepository topicRepository,
                        QuestionRepository questionRepository,
                        SubmissionRepository submissionRepository) {
        this.topicRepository = topicRepository;
        this.questionRepository = questionRepository;
        this.submissionRepository = submissionRepository;
    }

    public List<TopicDto> getAllTopicsWithProgress(String studentIdentifier) {
        List<Topic> topics = topicRepository.findAllByOrderBySortOrderAsc();
        List<Submission> submissions = submissionRepository.findByStudentIdentifierOrderBySubmittedAtDesc(studentIdentifier);

        Set<Long> solvedQuestionIds = submissions.stream()
                .filter(Submission::getIsCorrect)
                .map(s -> s.getQuestion().getId())
                .collect(Collectors.toSet());

        return topics.stream()
                .map(topic -> toDto(topic, solvedQuestionIds, false))
                .collect(Collectors.toList());
    }

    public TopicDto getTopicBySlug(String slug, String studentIdentifier) {
        Topic topic = topicRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with slug: " + slug));

        List<Submission> submissions = submissionRepository.findByStudentIdentifierOrderBySubmittedAtDesc(studentIdentifier);
        Set<Long> solvedQuestionIds = submissions.stream()
                .filter(Submission::getIsCorrect)
                .map(s -> s.getQuestion().getId())
                .collect(Collectors.toSet());

        Map<Long, Long> attemptCountMap = submissions.stream()
                .collect(Collectors.groupingBy(s -> s.getQuestion().getId(), Collectors.counting()));

        TopicDto dto = toDto(topic, solvedQuestionIds, true);

        // Populate question summary details
        List<QuestionSummaryDto> questionSummaries = topic.getQuestions().stream()
                .map(q -> {
                    QuestionSummaryDto qs = new QuestionSummaryDto();
                    qs.setId(q.getId());
                    qs.setTitle(q.getTitle());
                    qs.setDifficulty(q.getDifficulty());
                    qs.setTopicId(topic.getId());
                    qs.setTopicName(topic.getName());
                    qs.setTopicSlug(topic.getSlug());
                    qs.setSolved(solvedQuestionIds.contains(q.getId()));
                    qs.setAttemptCount(attemptCountMap.getOrDefault(q.getId(), 0L).intValue());
                    return qs;
                })
                .sorted(Comparator.comparing(QuestionSummaryDto::getId))
                .collect(Collectors.toList());

        dto.setQuestions(questionSummaries);
        return dto;
    }

    private TopicDto toDto(Topic topic, Set<Long> solvedQuestionIds, boolean includeQuestions) {
        TopicDto dto = new TopicDto();
        dto.setId(topic.getId());
        dto.setName(topic.getName());
        dto.setSlug(topic.getSlug());
        dto.setDescription(topic.getDescription());
        dto.setIcon(topic.getIcon());
        dto.setColor(topic.getColor());
        dto.setSortOrder(topic.getSortOrder());

        int totalQuestions = topic.getQuestions().size();
        long solved = topic.getQuestions().stream()
                .filter(q -> solvedQuestionIds.contains(q.getId()))
                .count();

        dto.setTotalQuestions(totalQuestions);
        dto.setSolvedQuestions(solved);
        dto.setMasteryPercentage(totalQuestions > 0 ? Math.round(((double) solved / totalQuestions) * 100.0) : 0.0);

        return dto;
    }
}
