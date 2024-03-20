package com.schedulebackend.database.mapper;



import com.schedulebackend.database.DTO.AuthDTO.UserCreateDTO;
import com.schedulebackend.database.entity.User;
import com.schedulebackend.database.entity.enums.Role;
import com.schedulebackend.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserCreateMapper implements Mapper<UserCreateDTO, User> {

    private final PasswordEncoder passwordEncoder;
    private final GroupService groupService;

    @Override
    public User map(UserCreateDTO fromObject, User toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    @Override
    public User map(UserCreateDTO userCreateEditDTO) {
        User user = new User();
        user.setRole(Role.STUDENT);
        copy(userCreateEditDTO, user);
        return user;
    }

    private void copy(UserCreateDTO userCreateDTO, User user) {
        user.setUsername(userCreateDTO.getUsername());
        user.setFirstname(userCreateDTO.getFirstname());
        user.setMidname(userCreateDTO.getMidname());
        user.setLastname(userCreateDTO.getLastname());
        user.setGroup(groupService.getGroupByName(userCreateDTO.getGroupName()));
        Optional.ofNullable(userCreateDTO.getPassword())
                .filter(StringUtils::hasText)
                .map(passwordEncoder::encode)
                .ifPresent(user::setPassword);
    }
}
