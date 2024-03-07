package com.schedulebackend.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date createdAt; // Номер подгруппы для английского

    @Column(length = 5000)
    private String content; // Название предмета

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL, targetEntity = NewsLinkAttachments.class)
    private List<NewsLinkAttachments> newsLinkList = new ArrayList<>();


    public News(Date createdAt, String content) {
        this.createdAt = createdAt;
        this.content = content;
    }

    @Override
    public String toString() {
        return "News{" +
                "createdAt=" + createdAt +
                ", content='" + content + '\'' +
                ", newsLinksList=" + newsLinkList +
                '}';
    }
}
