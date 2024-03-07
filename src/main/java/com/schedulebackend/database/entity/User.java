package com.schedulebackend.database.entity;

import com.schedulebackend.database.entity.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//TODO УБРАТЬ Data
@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "teacher_external_id")
    private Integer teacherExternalID;

    @Column(length = 50)
    private String firstname;

    @Column(length = 50)
    private String midname;

    @Column(length = 50)
    private String lastname;

    @NotEmpty
    private String username;

    @NotEmpty
    @ColumnDefault("{noop}123")
    private String password;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "lesson_id")
//    private Lesson lessonSync;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userId")
    private List<UserTask> userTasks = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Role role = Role.STUDENT;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Token> tokens;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
