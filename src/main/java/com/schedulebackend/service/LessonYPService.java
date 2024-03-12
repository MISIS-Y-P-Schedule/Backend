package com.schedulebackend.service;

import com.schedulebackend.database.DTO.LessonYPCreateDTO;
import com.schedulebackend.database.DTO.LessonYPUpdateDTO;
import com.schedulebackend.database.entity.LessonYP;
import com.schedulebackend.database.repository.LessonYPRepository;
import com.schedulebackend.database.repository.TeacherYPRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
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
    public LessonYP createLesson(LessonYPCreateDTO lesson) {
        LessonYP responseLesson = lessonYPRepository.findLessonYPByName(lesson.getName());
        LessonYP newLesson = lesson.getLessonLink() == null ? new LessonYP(lesson.getName()) : new LessonYP(lesson.getName(), lesson.getLessonLink());
        if(responseLesson == null){return lessonYPRepository.save(newLesson);}
        throw new EntityExistsException("Этот предмет уже создан");
    }

    @Transactional
    public LessonYP updateLesson(LessonYPUpdateDTO lesson){
        LessonYP responseLesson = lessonYPRepository.findById(lesson.getId()).orElseThrow(() -> new EntityNotFoundException("Предмет с таким id не найден"));
        responseLesson.setLessonLink(lesson.getLessonLink());
        responseLesson.setTeacher(teacherYPRepository.findById(lesson.getTeacherId()).orElseThrow(() -> new EntityNotFoundException("Преподаватель с таким id не найден")));
        return lessonYPRepository.save(responseLesson);
    }
    @Transactional
    public LessonYP deleteLesson(Long lessonId){
        LessonYP response = lessonYPRepository.findById(lessonId).orElseThrow(() -> new EntityNotFoundException("Предмет с таким id не найден"));
        if(response != null) lessonYPRepository.delete(response);
        return response;
    }
}
