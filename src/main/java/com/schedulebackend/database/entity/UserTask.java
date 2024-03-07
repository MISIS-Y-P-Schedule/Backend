package com.schedulebackend.database.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode()
@Getter
@Setter
@Entity
@Table(name = "user_tasks")
@IdClass(UserTask.UserTaskPK.class)
public class UserTask {

    @Id
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    @Id
    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task taskId;

    private Boolean readiness;

    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode()
    @Getter
    @Setter
    static class UserTaskPK implements Serializable {
        private User userId;
        private Task taskId;
    }
}
