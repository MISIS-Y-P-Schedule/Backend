package com.schedulebackend.database.entity;

import com.schedulebackend.database.entity.enums.UserState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tg_users")
public class TgUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long tgId;

    @Enumerated(EnumType.STRING)
    UserState userState = UserState.ALL_NOTIFICATIONS;

    public TgUser(Long tgId, UserState userState) {
        this.tgId = tgId;
        this.userState = userState;
    }
}
