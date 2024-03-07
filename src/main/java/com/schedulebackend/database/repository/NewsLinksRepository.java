package com.schedulebackend.database.repository;

import com.schedulebackend.database.entity.NewsLinkAttachments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsLinksRepository extends JpaRepository<NewsLinkAttachments, Long> {
}