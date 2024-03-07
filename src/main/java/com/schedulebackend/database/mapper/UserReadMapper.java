package com.schedulebackend.database.mapper;

import com.schedulebackend.database.DTO.UserReadDTO;
import com.schedulebackend.database.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserReadMapper implements Mapper<User, UserReadDTO> {

    @Override
    public UserReadDTO map(User user) {
        return new UserReadDTO(
                user.getId(),
                user.getTeacherExternalID(),
                user.getFirstname(),
                user.getMidname(),
                user.getLastname(),
                user.getUsername(),
                user.getGroup().getName(),
                user.getRole());
    }
}
