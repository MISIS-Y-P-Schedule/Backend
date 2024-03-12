package com.schedulebackend.controller.rest;

import com.schedulebackend.database.DTO.ErrorResponseDTO;
import com.schedulebackend.database.DTO.TeacherYPCreateDTO;
import com.schedulebackend.database.DTO.TeacherYPUpdateDTO;
import com.schedulebackend.database.entity.TeacherYP;
import com.schedulebackend.database.repository.TeacherYPRepository;
import com.schedulebackend.service.TeacherYPService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name="Контроллер преподавателей Я.Практикума")
public class TeacherYPRestController {
    private final TeacherYPService teacherYPService;
    private final TeacherYPRepository teacherYPRepository;

    @Operation(
            summary = "Создать преподавателя"
    )
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = TeacherYP.class))
    })
    @ApiResponse(responseCode = "409", description = "Этот преподаватель уже создан", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/admin/teacheryp/create")
    public ResponseEntity<?> createTeacher(@Validated @RequestBody TeacherYPCreateDTO teacherYP) {
        try {
            return new ResponseEntity<>(teacherYPService.createTeacher(teacherYP), HttpStatus.CREATED);
        } catch (EntityExistsException e) {
            return ResponseEntity.status(409).body(new ErrorResponseDTO(e.getMessage()));
        }
    }

    @Operation(
            summary = "Обновить учителя"
    )
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = TeacherYP.class))
    })
    @ApiResponse(responseCode = "404", description = "Неверный id преподавателя", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "JWT")
    @PutMapping("/admin/teacheryp/update")
    public ResponseEntity<?> updateTeacher(@Validated @RequestBody TeacherYPUpdateDTO teacherYP){
        try {
            return new ResponseEntity<>(teacherYPService.updateTeacher(teacherYP), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body("Неверный id преподавателя");
        }
    }

    @Operation(
            summary = "Удалить преподавателя по id"
    )
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = TeacherYP.class))
    })
    @ApiResponse(responseCode = "404", description = "Неверный id преподавателя", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "JWT")
    @DeleteMapping("/admin/teacheryp/delete/{teacher_id}")
    public ResponseEntity<?> deleteTeacher(@PathVariable Long teacher_id){
        try {
            return ResponseEntity.ok().body(teacherYPService.deleteTeacher(teacher_id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorResponseDTO(e.getMessage()));
        }
    }

    @Operation(
            summary = "Получить всех преподавателей Я.Практикума"
    )
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TeacherYP.class)))
    })
    @PreAuthorize("hasAuthority('STUDENT')")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/student/teacheryp/getall")
    public ResponseEntity<?> getAllTeachers() {
        return new ResponseEntity<>(teacherYPRepository.findAll(), HttpStatus.OK);
    }

    @Operation(
            summary = "Получить преподавателя по id"
    )
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = TeacherYP.class))
    })
    @PreAuthorize("hasAuthority('STUDENT')")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/student/teacheryp/get/{teacher_id}")
    public ResponseEntity<?> getTeacher(@PathVariable Long teacher_id) {
        return new ResponseEntity<>(teacherYPRepository.findById(teacher_id), HttpStatus.OK);
    }
}
