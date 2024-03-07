package com.schedulebackend.service;

import com.schedulebackend.database.entity.Teacher;
import com.schedulebackend.database.repository.TeacherRepository;
import com.schedulebackend.parsers.FiliationParserJSON;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final FiliationParserJSON filiationParserJSON;

    @Transactional
    public Teacher createTeacher(Teacher teacher) {
        if (teacherRepository.findAll().isEmpty()) {
            teacherRepository.save(new Teacher(1));
        }
        return teacherRepository.findTeacherByExternalID(teacher.getExternalID()) == null ? teacherRepository.save(teacher) : new Teacher();
    }

    @Transactional
    public List<Teacher> updateTeachers() throws IOException {
        return filiationParserJSON.parseJSON().getResponseFiliationFromAPIDTO().getTeacherList()
                .stream().map(this::createTeacher).collect(Collectors.toList());
    }
}