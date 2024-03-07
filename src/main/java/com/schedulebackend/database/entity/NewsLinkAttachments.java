package com.schedulebackend.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.schedulebackend.database.entity.enums.NewsLinksType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "news_link_attachments")
public class NewsLinkAttachments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Enumerated(EnumType.STRING)
    private NewsLinksType newsLinksType;

    @Column(length = 800)
    @Getter
    private String newsLink;

    @JsonIgnore
    @Getter
    @ManyToOne
    private News news;

    public NewsLinkAttachments(News news, NewsLinksType newsLinksType, String newsLink) {
        this.news = news;
        this.newsLinksType = newsLinksType;
        this.newsLink = newsLink;
    }

    @Override
    public String toString() {
        return "NewsLinks{" +
                "newsLinksType=" + newsLinksType +
                ", newsLink='" + newsLink + '\'' +
                '}';
    }
}