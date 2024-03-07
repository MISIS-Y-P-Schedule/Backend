package com.schedulebackend.service;

import com.schedulebackend.database.entity.ScheduledTask;
import com.schedulebackend.database.repository.ScheduledTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduledTaskService {

    private final ScheduledTaskRepository scheduledTaskRepository;

    public ScheduledTask createTask(ScheduledTask task) {
        return scheduledTaskRepository.save(task);
    }

    public void deleteTask(Long id) {
        scheduledTaskRepository.deleteById(id);
    }
    public void setTaskActiveFalse(Long id){
        ScheduledTask changedTask = scheduledTaskRepository.findById(id).orElseThrow();
        changedTask.setActive(false);
        scheduledTaskRepository.save(changedTask);
    }
}
