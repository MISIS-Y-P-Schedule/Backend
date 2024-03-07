package com.schedulebackend.service;

import com.schedulebackend.database.DTO.TeacherYPCreateDTO;
import com.schedulebackend.database.DTO.TeacherYPUpdateDTO;
import com.schedulebackend.database.entity.TeacherYP;
import com.schedulebackend.database.repository.TeacherYPRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeacherYPService {

    private final TeacherYPRepository teacherYPRepository;

    @Transactional
    public TeacherYP createOrReturnTeacher(TeacherYPCreateDTO teacher) {
        TeacherYP responseTeacher = teacherYPRepository.findTeacherYPByFirstnameAndLastname(teacher.getFirstname(), teacher.getLastname());
        TeacherYP newTeacher = teacher.getMidname() == null ? new TeacherYP(teacher.getFirstname(), teacher.getLastname()) : new TeacherYP(teacher.getFirstname(), teacher.getMidname(), teacher.getLastname());
        return responseTeacher == null ? teacherYPRepository.save(newTeacher) : responseTeacher;
    }

    @Transactional
    public TeacherYP updateTeacher(TeacherYPUpdateDTO teacher){
        TeacherYP responseTeacher = teacherYPRepository.findById(teacher.getId()).orElseThrow();
        responseTeacher.setFirstname(teacher.getFirstname()==null? responseTeacher.getFirstname() : teacher.getFirstname());
        responseTeacher.setMidname(teacher.getMidname()==null? responseTeacher.getMidname() : teacher.getMidname());
        responseTeacher.setLastname(teacher.getLastname()==null? responseTeacher.getLastname() : teacher.getLastname());
        return teacherYPRepository.save(responseTeacher);
    }
}