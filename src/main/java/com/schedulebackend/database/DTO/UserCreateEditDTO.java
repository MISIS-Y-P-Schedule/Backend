package com.schedulebackend.database.DTO;

import com.schedulebackend.database.entity.enums.Role;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserCreateEditDTO {
    @NotEmpty String username;
    @NotEmpty String password;
    String firstname;
    String midname;
    String lastname;
    Integer teacherExternalID;
    String groupName;
    Role role;

    public UserCreateEditDTO(@NotEmpty String username, @NotEmpty String password, String firstname, String midname,
                             String lastname, Integer teacherExternalID, String groupName, Role role) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.midname = midname;
        this.lastname = lastname;
        this.teacherExternalID = teacherExternalID;
        this.groupName = groupName;
        this.role = role;
    }



}
