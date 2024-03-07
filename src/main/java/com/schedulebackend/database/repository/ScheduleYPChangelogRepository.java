package com.schedulebackend.database.repository;

import com.schedulebackend.database.entity.ScheduleYPChangelog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleYPChangelogRepository extends JpaRepository<ScheduleYPChangelog, Long> {
}
