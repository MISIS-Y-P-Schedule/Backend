package com.schedulebackend.controller.rest;

import com.schedulebackend.database.entity.Group;
import com.schedulebackend.service.GroupService;
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
public class GroupRestController {
    private final GroupService groupService;

    @PostMapping("/admin/group/update")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> updateGroups() throws IOException {
        List<Group> checkGroups = groupService.updateGroups().stream().filter(t -> !(t.getExternalID() == null)).toList();
        return !checkGroups.isEmpty() ? ResponseEntity.status(201).body(checkGroups) :
                ResponseEntity.ok().body("Groups already updated");
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
