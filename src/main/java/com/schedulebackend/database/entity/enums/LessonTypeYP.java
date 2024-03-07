package com.schedulebackend.database.entity.enums;

import lombok.Getter;

@Getter
public enum LessonTypeYP {
    ASYNC("Асинхрон"),
    ONLINE("Онлайн-занятие"),
    INFO("Информационное"),
    DELETE("");
    private final String name;

    LessonTypeYP(String name) {
        this.name = name;
    }

}
