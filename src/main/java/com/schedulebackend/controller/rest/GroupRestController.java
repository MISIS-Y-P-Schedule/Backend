package com.schedulebackend.controller.rest;

import com.schedulebackend.database.DTO.ErrorResponseDTO;
import com.schedulebackend.database.entity.Group;
import com.schedulebackend.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name="Контроллер групп")
public class GroupRestController {
    private final GroupService groupService;

    @Operation(
            summary = "Обновить группы с API"
    )
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Group.class)))
    })
    @ApiResponse(responseCode = "201", description = "Группы уже созданы", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/admin/group/update")
    public ResponseEntity<?> updateGroups() throws IOException {
        List<Group> checkGroups = groupService.updateGroups().stream().filter(t -> !(t.getExternalID() == null)).toList();
        return !checkGroups.isEmpty() ? ResponseEntity.status(201).body(checkGroups) :
                ResponseEntity.ok().body(new ErrorResponseDTO("Группы уже созданы"));
    }

    @Operation(
            summary = "Получить все группы"
    )
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Group.class)))
    })
    @PreAuthorize("hasAuthority('STUDENT')")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/student/group/getall")
    public ResponseEntity<?> getAllGroups() {
        return new ResponseEntity<>(groupService.getAllGroups(), HttpStatus.OK);
    }

    @Operation(
            summary = "Получить все группы(Только названия)"
    )
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = String.class)))
    })
    @PreAuthorize("hasAuthority('STUDENT')")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/student/group/getallnames")
    public ResponseEntity<?> getAllGroupNames() {
        return new ResponseEntity<>(groupService.getAllNames(), HttpStatus.OK);
    }

//        return ResponseEntity.status(201).body(groupService.updateGroups());
//
//            //.status(HttpStatus.NOT_MODIFIED).body("Groups already created");
//            return ResponseEntity.accepted().body("Groups already created");
//        }
//    @GetMapping("/update")
//    public ResponseEntity<List<Group>> updateGroups() {
//        try {
//            return ResponseEntity.ok(groupService.updateGroups());
//        } catch (GroupException exception) {
//            throw new ResponseStatusException(HttpStatus.NOT_MODIFIED, "Groups already created");
//        } catch (IOException exception) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid JSON data");
//        }
//    }
//     @GetMapping("/update")
//     public ResponseEntity<List<Group>> updateGroups() throws IOException {
//             return ResponseEntity.ok(groupService.updateGroups());
//     }

//    @ExceptionHandler(GroupException.class)
//    @ResponseStatus(code = HttpStatus.NOT_MODIFIED, reason = "Not Created" )
//    public void updateGroupsException() {
//    }
//
//    @ExceptionHandler(IOException.class)
//    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Invalid JSON data")
//    public void updateGroupsIOException() {
//    }
}
