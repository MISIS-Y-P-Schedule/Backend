package com.schedulebackend.controller.rest;

import com.schedulebackend.database.entity.Classroom;
import com.schedulebackend.service.ClassroomService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Hidden
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
@RequestMapping("/api/v1")
public class ClassroomRestController {
    private final ClassroomService classroomService;

    @PostMapping("/admin/classroom/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Classroom createGroup(Classroom classroom) {
        return classroomService.createClassroom(classroom);
    }


    @PostMapping("/admin/classroom/update")
    public ResponseEntity<?> updateGroups() throws IOException {
        List<Classroom> checkClassrooms = classroomService.updateClassrooms().stream().filter(t -> !(t.getExternalID() == 0)).toList();
        return !checkClassrooms.isEmpty() ? ResponseEntity.status(201).body(checkClassrooms) :
                ResponseEntity.ok().body("Classrooms already updated");
    }
}
