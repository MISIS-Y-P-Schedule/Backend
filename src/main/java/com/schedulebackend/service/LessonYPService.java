package com.schedulebackend.service;

import com.schedulebackend.database.DTO.LessonYPCreateDTO;
import com.schedulebackend.database.DTO.LessonYPUpdateDTO;
import com.schedulebackend.database.entity.LessonYP;
import com.schedulebackend.database.repository.LessonYPRepository;
import com.schedulebackend.database.repository.TeacherYPRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LessonYPService {
    private final LessonYPRepository lessonYPRepository;
    private final TeacherYPRepository teacherYPRepository;
    @Transactional
    public LessonYP createOrReturnLesson(LessonYPCreateDTO lesson) {
        LessonYP responseLesson = lessonYPRepository.findLessonYPByName(lesson.getName());
        LessonYP newLesson = lesson.getLessonLink() == null ? new LessonYP(lesson.getName()) : new LessonYP(lesson.getName(), lesson.getLessonLink());
        return responseLesson == null ? lessonYPRepository.save(newLesson) : responseLesson;
    }

    @Transactional
    public LessonYP updateLesson(LessonYPUpdateDTO lesson){
        LessonYP responseLesson = lessonYPRepository.findById(lesson.getId()).orElseThrow();
        responseLesson.setLessonLink(lesson.getLessonLink());
        responseLesson.setTeacher(teacherYPRepository.findById(lesson.getTeacherId()).orElse(null));
        return lessonYPRepository.save(responseLesson);
    }
}
