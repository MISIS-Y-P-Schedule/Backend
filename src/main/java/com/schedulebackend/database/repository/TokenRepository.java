package com.schedulebackend.database.repository;

import com.schedulebackend.database.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query(value = "SELECT * FROM tokens WHERE user_id=?1 AND (expired=false OR revoked=false)",nativeQuery = true)
    List<Token> findAllValidTokenByUser(Long user_id);
    @Query(value = "SELECT * FROM tokens WHERE user_id=?1 AND (expired=false OR revoked=false) LIMIT 1",nativeQuery = true)
    Token findValidTokenByUser(Long user_id);
    @Query
    Optional<Token> findByToken(String token);
}
