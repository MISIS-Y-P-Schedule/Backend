package com.schedulebackend.controller.rest;


import com.schedulebackend.database.DTO.AuthDTO.JWTRequestDTO;
import com.schedulebackend.database.DTO.AuthDTO.JWTResponseDTO;
import com.schedulebackend.database.DTO.AuthDTO.RefreshJWTRequestDTO;
import com.schedulebackend.database.DTO.AuthDTO.UserCreateDTO;
import com.schedulebackend.database.DTO.ErrorResponseDTO;
import com.schedulebackend.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name="Контроллер авторизации")
public class AuthenticationRestController {

    private final AuthenticationService authService;

    @Operation(
            summary = "Авторизация пользователя"
    )
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = JWTResponseDTO.class))
    })
    @ApiResponse(responseCode = "401", description = "Пользователь не найден / Неправильный пароль", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
    })
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody JWTRequestDTO authRequest) {
        try{
            return ResponseEntity.ok(authService.login(authRequest));
        } catch (AuthException e){
            return ResponseEntity.status(401).body(new ErrorResponseDTO(e.getMessage()));
        }
    }

    @Operation(
            summary = "Регистрация пользователя"
    )
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = JWTResponseDTO.class))
    })
    @ApiResponse(responseCode = "409", description = "Эта почта уже занята", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(UserCreateDTO request) {
        try{
            return ResponseEntity.ok(authService.register(request));
        } catch (AuthException e){
            return ResponseEntity.status(409).body(new ErrorResponseDTO(e.getMessage()));
        }
    }

    @Operation(
            summary = "Получить access токен"
    )
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = JWTResponseDTO.class))
    })
    @ApiResponse(responseCode = "401", description = "Пользователь не найден / Невалидный JWT токен / Время работы токена истекло / Неподдерживаемый токен / Неправильный токен", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
    })
    @PostMapping("token")
    public ResponseEntity<?> getNewAccessToken(@RequestBody RefreshJWTRequestDTO request) {
        try{
            return ResponseEntity.ok(authService.getAccessToken(request.getRefreshToken()));
        } catch (AuthException e){
            return ResponseEntity.status(401).body(new ErrorResponseDTO(e.getMessage()));
        }
    }

    @Operation(
            summary = "Обновить refresh токен"
    )
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = JWTResponseDTO.class))
    })
    @ApiResponse(responseCode = "401", description = "Пользователь не найден / Невалидный JWT токен / Время работы токена истекло / Неподдерживаемый токен / Неправильный токен", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
    })
    @PostMapping("refresh")
    public ResponseEntity<?> getNewRefreshToken(@RequestBody RefreshJWTRequestDTO request) {
        try{
            return ResponseEntity.ok(authService.refresh(request.getRefreshToken()));
        } catch (AuthException e){
            return ResponseEntity.status(401).body(new ErrorResponseDTO(e.getMessage()));
        }
    }
}
