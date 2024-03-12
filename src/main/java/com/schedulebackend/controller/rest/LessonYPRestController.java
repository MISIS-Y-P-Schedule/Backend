package com.schedulebackend.controller.rest;

import com.schedulebackend.database.DTO.ErrorResponseDTO;
import com.schedulebackend.database.DTO.LessonYPCreateDTO;
import com.schedulebackend.database.DTO.LessonYPUpdateDTO;
import com.schedulebackend.database.entity.LessonYP;
import com.schedulebackend.database.repository.LessonYPRepository;
import com.schedulebackend.service.LessonYPService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name="Контроллер предметов Я.Практикума")
public class LessonYPRestController {
    private final LessonYPService lessonYPService;
    private final LessonYPRepository lessonYPRepository;
    @Operation(
            summary = "Создание предмета"
    )
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = LessonYP.class))
    })
    @ApiResponse(responseCode = "409", description = "Этот предмет уже создан", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/admin/lessonyp/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createLesson(@Validated @RequestBody LessonYPCreateDTO lessonYPCreateDTO) {
        try{
            return new ResponseEntity<>(lessonYPService.createLesson(lessonYPCreateDTO), HttpStatus.CREATED);
        } catch (EntityExistsException e){
            return ResponseEntity.status(409).body(new ErrorResponseDTO(e.getMessage()));
        }
    }
    @Operation(
            summary = "Изменение предмета"
    )
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = LessonYP.class))
    })
    @ApiResponse(responseCode = "404", description = "Предмет с таким id не найден / Преподаватель с таким id не найден", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "JWT")
    @PutMapping( "/admin/lessonyp/update")
    public ResponseEntity<?> updateLesson(@Validated @RequestBody LessonYPUpdateDTO lessonYPUpdateDTO) {
//        return new ResponseEntity<>(lessonYPService.updateLesson(lessonYPUpdateDTO), HttpStatus.OK);
        try {
            return ResponseEntity.ok().body(lessonYPService.updateLesson(lessonYPUpdateDTO));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorResponseDTO(e.getMessage()));
        }
    }
    @Operation(
            summary = "Изменение предмета"
    )
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = LessonYP.class))
    })
    @ApiResponse(responseCode = "404", description = "Предмет с таким id не найден", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "JWT")
    @DeleteMapping("/admin/lessonyp/delete/{lesson_id}")
    public ResponseEntity<?> deleteLesson(@PathVariable Long lesson_id){
        try {
            return ResponseEntity.ok().body(lessonYPService.deleteLesson(lesson_id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorResponseDTO(e.getMessage()));
        }
    }

    @Operation(
            summary = "Получить все предметы"
    )
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LessonYP.class)))
    })
    @PreAuthorize("hasAuthority('STUDENT')")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/student/lessonyp/getall")
    public ResponseEntity<?> getAllLessons() {
        return new ResponseEntity<>(lessonYPRepository.findAll(), HttpStatus.OK);
    }

    @Operation(
            summary = "Получить предмет по id"
    )
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = LessonYP.class))
    })
    @PreAuthorize("hasAuthority('STUDENT')")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/student/lessonyp/get/{lesson_id}")
    public ResponseEntity<?> getLesson(@PathVariable Long lesson_id) {
        return new ResponseEntity<>(lessonYPRepository.findById(lesson_id), HttpStatus.OK);
    }
}
