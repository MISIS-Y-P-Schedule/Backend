package com.schedulebackend.database.repository;

import com.schedulebackend.database.entity.Bell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Time;
@Repository
public interface BellRepository extends JpaRepository<Bell, Long> {
    @Query(value = "SELECT start_time FROM bells WHERE id=?1", nativeQuery = true)
    Time getStartTime(Long id);

    @Query(value = "SELECT end_time FROM bells WHERE id=?1", nativeQuery = true)
    Time getEndTime(Long id);
}
