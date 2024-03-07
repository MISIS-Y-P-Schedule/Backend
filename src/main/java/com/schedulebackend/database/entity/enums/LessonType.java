package com.schedulebackend.database.entity.enums;


import lombok.Getter;

@Getter
public enum LessonType {

    LECTURE("Лекционные"),
    PRACTICE("Практические"),
    LAB("Лабораторные");

    private final String name;

    public static LessonType getLessonType(String name) {
        return switch (name) {
            case ("Лекционные") -> LessonType.LECTURE;
            case ("Практические") -> LessonType.PRACTICE;
            case ("Лабораторные") -> LessonType.LAB;
            default -> throw new IllegalStateException("Unexpected value: " + name);
        };
    }
//    public enum getEnum(String name) {
//        return switch(name) {
//            case ("Лекционные") ->  LessonType.LECTURE;
//            case ("Практические") ->  LessonType.LECTURE;
//            case ("Лабораторные") ->  LessonType.LECTURE;
//            default -> "ошибка";
//        };
//    }

    LessonType(String name) {
        this.name = name;
    }

}
