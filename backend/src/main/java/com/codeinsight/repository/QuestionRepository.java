package com.codeinsight.repository;

import com.codeinsight.entity.Difficulty;
import com.codeinsight.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByTopicId(Long topicId);
    List<Question> findByTopicSlug(String topicSlug);
    List<Question> findByDifficulty(Difficulty difficulty);

    @Query("SELECT q FROM Question q JOIN FETCH q.topic WHERE q.id = :id")
    Question findByIdWithTopic(@Param("id") Long id);

    @Query("SELECT q FROM Question q JOIN FETCH q.topic")
    List<Question> findAllWithTopic();
}
