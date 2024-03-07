package com.schedulebackend.database.DTO;

import com.schedulebackend.database.entity.enums.Role;


public record UserReadDTO(Long id, Integer teacherExternalID, String firstname, String midname, String lastname,
                          String username, String groupName, Role role) {

}
