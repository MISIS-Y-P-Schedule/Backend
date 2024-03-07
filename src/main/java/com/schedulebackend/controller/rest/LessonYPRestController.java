package com.schedulebackend.controller.rest;

import com.schedulebackend.database.DTO.LessonYPCreateDTO;
import com.schedulebackend.database.DTO.LessonYPUpdateDTO;
import com.schedulebackend.database.entity.LessonYP;
import com.schedulebackend.database.repository.LessonYPRepository;
import com.schedulebackend.service.LessonYPService;
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
public class LessonYPRestController {
    private final LessonYPService lessonYPService;
    private final LessonYPRepository lessonYPRepository;

    @PostMapping("/admin/lessonyp/create")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<LessonYP> createLesson(@Validated @RequestBody LessonYPCreateDTO lessonYPCreateDTO) {
        return new ResponseEntity<>(lessonYPService.createOrReturnLesson(lessonYPCreateDTO), HttpStatus.CREATED);
    }

    @PutMapping( "/admin/lessonyp/update")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> updateLesson(@Validated @RequestBody LessonYPUpdateDTO lessonYPUpdateDTO) {
//        return new ResponseEntity<>(lessonYPService.updateLesson(lessonYPUpdateDTO), HttpStatus.OK);
        LessonYP response;
        try {
            response = lessonYPService.updateLesson(lessonYPUpdateDTO);
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("Неверный id урока");
        }
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/admin/lessonyp/delete/{lesson_id}")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> deleteLesson(@PathVariable Long lesson_id){
        LessonYP response = lessonYPRepository.findById(lesson_id).orElse(null);
        if(response != null) lessonYPRepository.delete(response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user/lessonyp/getall")
    public ResponseEntity<?> getAllLessons() {
        return new ResponseEntity<>(lessonYPRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/user/lessonyp/get/{lesson_id}")
    public ResponseEntity<?> getLesson(@PathVariable Long lesson_id) {
        return new ResponseEntity<>(lessonYPRepository.findById(lesson_id), HttpStatus.OK);
    }
}
