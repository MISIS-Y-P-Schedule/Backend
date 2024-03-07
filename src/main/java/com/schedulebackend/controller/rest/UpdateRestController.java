package com.schedulebackend.controller.rest;

import com.schedulebackend.database.DTO.ResponseFiliationFromAPIDTO;
import com.schedulebackend.database.entity.Classroom;
import com.schedulebackend.database.entity.Group;
import com.schedulebackend.database.entity.Teacher;
import com.schedulebackend.service.UpdateService;
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
@RequestMapping("/api/v1/admin")
public class UpdateRestController {
    private final UpdateService updateService;

    @PostMapping("/updatefiliation")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> updateFiliation() throws IOException {
        ResponseFiliationFromAPIDTO checkResponse = updateService.updateAll();
        List<Group> checkGroups = checkResponse.getGroupList().stream().filter(t -> !(t.getExternalID() == null)).toList();
        List<Classroom> checkClassrooms = checkResponse.getClassroomList().stream().filter(t -> !(t.getExternalID() == null)).toList();
        List<Teacher> checkTeachers = checkResponse.getTeacherList().stream().filter(t -> !(t.getExternalID() == null)).toList();
        ResponseFiliationFromAPIDTO returnValue = new ResponseFiliationFromAPIDTO(checkGroups, checkClassrooms, checkTeachers);
        return !checkGroups.isEmpty() || !checkClassrooms.isEmpty() || !checkTeachers.isEmpty() ? ResponseEntity.status(201).body(returnValue) :
                ResponseEntity.ok().body("All already updated");
    }
}
