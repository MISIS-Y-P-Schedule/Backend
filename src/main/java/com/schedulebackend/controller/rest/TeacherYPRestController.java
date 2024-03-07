package com.schedulebackend.controller.rest;

import com.schedulebackend.database.DTO.TeacherYPCreateDTO;
import com.schedulebackend.database.DTO.TeacherYPUpdateDTO;
import com.schedulebackend.database.entity.TeacherYP;
import com.schedulebackend.database.repository.TeacherYPRepository;
import com.schedulebackend.service.TeacherYPService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TeacherYPRestController {
    private final TeacherYPService teacherYPService;
    private final TeacherYPRepository teacherYPRepository;

    @PostMapping("/admin/teacheryp/create")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<TeacherYP> createTeacher(@Validated @RequestBody TeacherYPCreateDTO teacherYP) {
        return new ResponseEntity<>(teacherYPService.createOrReturnTeacher(teacherYP), HttpStatus.CREATED);
    }

    @PutMapping("/admin/teacheryp/update")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> updateTeacher(@Validated @RequestBody TeacherYPUpdateDTO teacherYP){
        TeacherYP response;
        try {
            response = teacherYPService.updateTeacher(teacherYP);
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("Неверный id учителя");
        }
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/admin/teacheryp/delete/{teacher_id}")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> deleteTeacher(@PathVariable Long teacher_id){
        TeacherYP response = teacherYPRepository.findById(teacher_id).orElse(null);
        if(response != null) teacherYPRepository.delete(response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user/teacheryp/getall")
    public ResponseEntity<?> getAllTeachers() {
        return new ResponseEntity<>(teacherYPRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/user/teacheryp/get/{teacher_id}")
    public ResponseEntity<?> getTeacher(@PathVariable Long teacher_id) {
        return new ResponseEntity<>(teacherYPRepository.findById(teacher_id), HttpStatus.OK);
    }
}
