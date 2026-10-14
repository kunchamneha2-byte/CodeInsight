package com.codeinsight.repository;

import com.codeinsight.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    List<Submission> findByStudentIdentifierOrderBySubmittedAtDesc(String studentIdentifier);

    List<Submission> findByStudentIdentifierAndQuestionIdOrderBySubmittedAtDesc(String studentIdentifier, Long questionId);

    @Query("SELECT s FROM Submission s JOIN FETCH s.question q JOIN FETCH q.topic WHERE s.studentIdentifier = :studentIdentifier ORDER BY s.submittedAt DESC")
    List<Submission> findAllByStudentWithDetails(@Param("studentIdentifier") String studentIdentifier);

    long countByStudentIdentifier(String studentIdentifier);

    long countByStudentIdentifierAndIsCorrectTrue(String studentIdentifier);

    @Query("SELECT COUNT(DISTINCT s.question.id) FROM Submission s WHERE s.studentIdentifier = :studentIdentifier AND s.isCorrect = true")
    long countDistinctSolvedQuestions(@Param("studentIdentifier") String studentIdentifier);

    @Query("SELECT COUNT(DISTINCT s.question.id) FROM Submission s WHERE s.studentIdentifier = :studentIdentifier")
    long countDistinctAttemptedQuestions(@Param("studentIdentifier") String studentIdentifier);
}
