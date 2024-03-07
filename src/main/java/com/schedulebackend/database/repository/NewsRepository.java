package com.schedulebackend.database.repository;


import com.schedulebackend.database.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    News findFirstByOrderByCreatedAtDesc();
}
