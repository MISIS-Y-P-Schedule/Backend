package com.schedulebackend.service;

import com.schedulebackend.database.DTO.TeacherYPCreateDTO;
import com.schedulebackend.database.DTO.TeacherYPUpdateDTO;
import com.schedulebackend.database.entity.TeacherYP;
import com.schedulebackend.database.repository.TeacherYPRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
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
        TeacherYP responseTeacher = teacherYPRepository.findTeacherYPByLastnameAndFirstnameAndMidname(teacher.getLastname(), teacher.getFirstname(), teacher.getMidname());
        TeacherYP newTeacher = teacher.getMidname() == null ? new TeacherYP(teacher.getFirstname(), teacher.getLastname()) : new TeacherYP(teacher.getFirstname(), teacher.getMidname(), teacher.getLastname());
        return responseTeacher == null ? teacherYPRepository.save(newTeacher) : responseTeacher;
    }

    @Transactional
    public TeacherYP createTeacher(TeacherYPCreateDTO teacher) {
        TeacherYP responseTeacher = teacherYPRepository.findTeacherYPByLastnameAndFirstnameAndMidname(teacher.getLastname(), teacher.getFirstname(), teacher.getMidname());
        TeacherYP newTeacher = teacher.getMidname() == null ? new TeacherYP(teacher.getFirstname(), teacher.getLastname()) : new TeacherYP(teacher.getFirstname(), teacher.getMidname(), teacher.getLastname());
        if(responseTeacher == null){return teacherYPRepository.save(newTeacher);}
        throw new EntityExistsException("Этот преподаватель уже создан");
    }

    @Transactional
    public TeacherYP updateTeacher(TeacherYPUpdateDTO teacher){
        TeacherYP responseTeacher = teacherYPRepository.findById(teacher.getId()).orElseThrow();
        responseTeacher.setFirstname(teacher.getFirstname()==null? responseTeacher.getFirstname() : teacher.getFirstname());
        responseTeacher.setMidname(teacher.getMidname()==null? responseTeacher.getMidname() : teacher.getMidname());
        responseTeacher.setLastname(teacher.getLastname()==null? responseTeacher.getLastname() : teacher.getLastname());
        return teacherYPRepository.save(responseTeacher);
    }

    @Transactional
    public TeacherYP deleteTeacher(Long teacherId){
        TeacherYP response = teacherYPRepository.findById(teacherId).orElseThrow(() -> new EntityNotFoundException("Преподаватель с таким id не найден"));
        if(response != null) teacherYPRepository.delete(response);
        return response;
    }
}