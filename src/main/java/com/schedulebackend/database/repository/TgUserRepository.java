package com.schedulebackend.database.repository;

import com.schedulebackend.database.entity.TgUser;
import com.schedulebackend.database.entity.enums.UserState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface  TgUserRepository extends JpaRepository<TgUser, Long> {
    @Query
    List<TgUser> findByUserState(UserState state);
    @Query
    Optional<TgUser> findByTgId(Long tgId);
    @Query
    Boolean existsByTgId(Long tgId);
    @Query
    void deleteByTgId(Long tgId);
}
