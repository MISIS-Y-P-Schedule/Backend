package com.schedulebackend.controller.rest;

import com.schedulebackend.database.DTO.AuthenticationRequest;
import com.schedulebackend.database.DTO.AuthenticationResponse;
import com.schedulebackend.database.DTO.UserCreateEditDTO;
import com.schedulebackend.database.entity.enums.Role;
import com.schedulebackend.service.AuthenticationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthenticationRestController {

    private final AuthenticationService service;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/user/register")
    public ResponseEntity<AuthenticationResponse> register(UserCreateEditDTO request) {
        request.setRole(Role.STUDENT);
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/user/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            AuthenticationRequest request,
            HttpSession httpSession
    ) {
        AuthenticationResponse authenticationResponse = service.authenticate(request, authenticationManager);
        httpSession.setAttribute("Authorization",
                "Bearer " + authenticationResponse.getToken());
        return ResponseEntity.ok()
                .header("Authorization",
                        "Bearer " + authenticationResponse.getToken())
                .body(authenticationResponse);
    }

}
