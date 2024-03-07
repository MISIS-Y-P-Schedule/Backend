package com.schedulebackend.controller.rest;

import com.schedulebackend.database.entity.Teacher;
import com.schedulebackend.service.TeacherService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
@Hidden
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TeacherRestController {
    private final TeacherService teacherService;

//    @GetMapping("/create")
//    @ResponseStatus(HttpStatus.CREATED)
//    public ResponseEntity<Teacher> createTeacher(Teacher teacher) {
//        return teacherService.createTeacher(teacher);
//    }

    @PostMapping("/admin/teacher/update")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> updateTeachers() throws IOException {
        List<Teacher> checkTeachers = teacherService.updateTeachers().stream().filter(t -> !(t.getExternalID() == 0)).toList();
        return !checkTeachers.isEmpty() ? ResponseEntity.status(201).body(checkTeachers) :
                ResponseEntity.ok().body("Groups already updated");
    }
}
