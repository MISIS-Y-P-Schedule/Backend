package com.schedulebackend.controller.rest;

import com.schedulebackend.database.entity.News;
import com.schedulebackend.database.repository.NewsRepository;
import com.schedulebackend.parsers.NewsParser;
import com.schedulebackend.service.NewsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class NewsRestController {
    private final NewsService newsService;
    private final NewsRepository newsRepository;
    private final NewsParser newsParser;

    @GetMapping("/user/")
    public List<News> getNews() {
        return newsRepository.findAll();
    }

    @GetMapping("/user/scheduleyp/get/{news_id}")
    public ResponseEntity<?> getNews(@PathVariable Long news_id) {
        return new ResponseEntity<>(newsRepository.findById(news_id), HttpStatus.OK);
    }

    @PostMapping("/admin/news/update")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> updateNews() throws IOException {
        return new ResponseEntity<>(newsService.updateNews(), HttpStatus.OK);
    }

    @PostMapping("/admin/news/updatetoken")
    @SecurityRequirement(name = "JWT")
    public void updateToken(@RequestParam String password, @RequestParam String token) throws IOException {
        if (password.equals("19021995")) {
            newsParser.updateCookie(token);
        }
    }
//    @PostConstruct
//    public void Init() throws IOException {
//        NewsParse news = new NewsParse(newsRepo);
//            //schedule.saveNews();
//            news.updateNews();
//    }
}