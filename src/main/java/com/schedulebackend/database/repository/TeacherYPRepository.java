package com.schedulebackend.database.repository;

import com.schedulebackend.database.entity.TeacherYP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherYPRepository extends JpaRepository<TeacherYP, Long> {
    @Query
    TeacherYP findTeacherYPByFirstnameAndLastname(String firstName, String lastName);
}