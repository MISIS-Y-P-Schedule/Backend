package com.schedulebackend.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "teachersyp")
public class TeacherYP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname;
    private String midname;
    private String lastname;

    public TeacherYP(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    @Override
    public String toString() {
        return  lastname + " " + firstname + " "  + midname;
    }

    public TeacherYP(String firstname, String midname, String lastname) {
        this.firstname = firstname;
        this.midname = midname;
        this.lastname = lastname;
    }
}
