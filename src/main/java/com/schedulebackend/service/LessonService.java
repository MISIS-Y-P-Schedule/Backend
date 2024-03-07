package com.schedulebackend.service;

import com.schedulebackend.database.entity.Lesson;
import com.schedulebackend.database.repository.LessonRepository;
import com.schedulebackend.parsers.FiliationParserJSON;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LessonService {
    private final LessonRepository lessonRepository;
    private final FiliationParserJSON filiationParserJSON;

    @Transactional
    public Lesson createOrReturnLesson(Lesson lesson) {
        Lesson responseLesson = lessonRepository.findLessonByExternalID(lesson.getExternalID());
        return responseLesson == null ? lessonRepository.save(lesson) : responseLesson;
    }
//
//    @Transactional
//    public List<Lesson> updateLessons() throws IOException {
//        return filiationParserJSON.parseJSON().getResponseFromAPIDTO().getLessonList()
//                .stream().map(this::createLesson).collect(Collectors.toList());
//    }
}
