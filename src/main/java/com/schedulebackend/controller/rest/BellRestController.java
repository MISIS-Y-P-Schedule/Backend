package com.schedulebackend.controller.rest;

import com.schedulebackend.service.BellService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
@Hidden
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BellRestController {
    private final BellService bellService;

    @PostMapping("/admin/bell/createinit")
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.CREATED)
    public void createBellInit() {
        bellService.createBells();
    }

}
