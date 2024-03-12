package com.schedulebackend.database.DTO.AuthDTO;


import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserCreateDTO {
    @NotEmpty String username;
    @NotEmpty String password;
    String firstname;
    String midname;
    String lastname;
    String groupName;

    public UserCreateDTO(@NotEmpty String username, @NotEmpty String password, String firstname, String midname,
                             String lastname, String groupName) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.midname = midname;
        this.lastname = lastname;
        this.groupName = groupName;
    }



}
