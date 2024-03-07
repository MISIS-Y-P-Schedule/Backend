package com.schedulebackend.database.repository;

import com.schedulebackend.database.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    //    @Query(value = "SELECT * FROM lessons WHERE external_id=?1",nativeQuery = true)
//    List<Lesson> findLessonByExternalId(Long external_id);
    @Query
    Lesson findLessonByExternalID(int externalId);
}