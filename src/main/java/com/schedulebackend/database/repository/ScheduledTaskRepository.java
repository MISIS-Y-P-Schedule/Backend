package com.schedulebackend.database.repository;

import com.schedulebackend.database.entity.ScheduledTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
@Repository
public interface ScheduledTaskRepository extends JpaRepository<ScheduledTask, Long> {
    @Query
    List<ScheduledTask> findByActive(boolean active);

    @Query
    ScheduledTask findByDateAndDescriptionAndActive(Date date, String description, boolean active);
}