package com.schedulebackend.database.repository;

import com.schedulebackend.database.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    @Query(value = "SELECT external_id FROM groups", nativeQuery = true)
    List<Integer> findAllExternalID();
    @Query
    Group findByExternalID(int externalID);

    @Query
    Optional<Group> findByName(String name);
}