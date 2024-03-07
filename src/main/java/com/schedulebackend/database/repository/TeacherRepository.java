package com.schedulebackend.database.repository;

import com.schedulebackend.database.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    //    @Query(value = "SELECT * FROM teachers WHERE external_id=?1",nativeQuery = true)
//    List<Teacher> findTeacherByExternalId(int external_id);
    @Query
    Teacher findTeacherByExternalID(int externalID);
}