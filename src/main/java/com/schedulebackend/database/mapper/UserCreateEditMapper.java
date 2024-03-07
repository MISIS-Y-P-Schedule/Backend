package com.schedulebackend.database.mapper;

import com.schedulebackend.database.DTO.UserCreateEditDTO;
import com.schedulebackend.database.entity.User;
import com.schedulebackend.database.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserCreateEditMapper implements Mapper<UserCreateEditDTO, User> {

    private final PasswordEncoder passwordEncoder;
    private final GroupRepository groupRepository;

    @Override
    public User map(UserCreateEditDTO fromObject, User toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    @Override
    public User map(UserCreateEditDTO userCreateEditDTO) {
        User user = new User();
        copy(userCreateEditDTO, user);
        return user;
    }

    private void copy(UserCreateEditDTO userCreateEditDTO, User user) {
        user.setUsername(userCreateEditDTO.getUsername());
        user.setFirstname(userCreateEditDTO.getFirstname());
        user.setMidname(userCreateEditDTO.getMidname());
        user.setLastname(userCreateEditDTO.getLastname());
        user.setTeacherExternalID(userCreateEditDTO.getTeacherExternalID());
        user.setGroup(groupRepository.findByName(userCreateEditDTO.getGroupName()).orElse(null));
        if (userCreateEditDTO.getRole() != null) {
            user.setRole(userCreateEditDTO.getRole());
        }

        Optional.ofNullable(userCreateEditDTO.getPassword())
                .filter(StringUtils::hasText)
                .map(passwordEncoder::encode)
                .ifPresent(user::setPassword);
    }
}
