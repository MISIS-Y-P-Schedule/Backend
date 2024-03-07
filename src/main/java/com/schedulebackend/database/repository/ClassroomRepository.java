package com.schedulebackend.database.repository;

import com.schedulebackend.database.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    @Query
    Classroom findClassroomByExternalID(int externalID);
//    @Query(value = "SELECT * FROM classrooms WHERE external_id=?1",nativeQuery = true)
//    List<Classroom> findClassroomByExternalId(int external_id);
}