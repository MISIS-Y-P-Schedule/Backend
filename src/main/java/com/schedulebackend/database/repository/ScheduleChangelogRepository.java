package com.schedulebackend.database.repository;

import com.schedulebackend.database.entity.ScheduleChangelog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ScheduleChangelogRepository extends JpaRepository<ScheduleChangelog, Long> {
}
