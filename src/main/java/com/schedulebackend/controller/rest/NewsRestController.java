package com.schedulebackend.controller.rest;

import com.schedulebackend.database.DTO.ErrorResponseDTO;
import com.schedulebackend.database.entity.News;
import com.schedulebackend.parsers.NewsParser;
import com.schedulebackend.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name="Контроллер новостей из Пачки")
public class NewsRestController {
    private final NewsService newsService;
    private final NewsParser newsParser;
    @Operation(
            summary = "Получить все новости"
    )
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = News.class)))
    })
    @PreAuthorize("hasAuthority('STUDENT')")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/student/news")
    public ResponseEntity<?> getAllNews() {
        return new ResponseEntity<>(newsService.getAllNews(), HttpStatus.OK);
    }

    @Operation(
            summary = "Получить новость по id"
    )
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = News.class))
    })
    @PreAuthorize("hasAuthority('STUDENT')")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/student/scheduleyp/get/{news_id}")
    public ResponseEntity<?> getNews(@PathVariable Long news_id) {
        return new ResponseEntity<>(newsService.getNews(news_id), HttpStatus.OK);
    }

    @Operation(
            summary = "Обновить новости"
    )
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = News.class)))
    })
    @ApiResponse(responseCode = "401", description = "Обновите токен пачки", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
    })
    @ApiResponse(responseCode = "500", description = "Файл с токеном и ссылкой не найден", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/admin/news/update")
    public ResponseEntity<?> updateNews() {
        try {
            return new ResponseEntity<>(newsService.updateNews(), HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(new ErrorResponseDTO("Файл с токеном и ссылкой не найден"));
        } catch (AuthException e) {
            return ResponseEntity.status(401).body(new ErrorResponseDTO(e.getMessage()));
        } catch (RuntimeException e){
            return ResponseEntity.status(Integer.parseInt(e.getMessage())).body("");
        }
    }

    @Operation(
            summary = "Обновить новости"
    )
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = News.class)))
    })
    @ApiResponse(responseCode = "500", description = "Файл с токеном и ссылкой не найден", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/admin/news/updatetoken")
    public ResponseEntity<?> updateToken(@RequestParam String password, @RequestParam String token) {
        try {
            if (password.equals("19021995")) {
                newsParser.updateCookie(token);
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body(new ErrorResponseDTO("Файл с токеном и ссылкой не найден"));
        }
        return new ResponseEntity<>("Обновлено", HttpStatus.OK);
    }

//    @PostConstruct
//    public void Init() throws IOException {
//        NewsParse news = new NewsParse(newsRepo);
//            //schedule.saveNews();
//            news.updateNews();
//    }
}