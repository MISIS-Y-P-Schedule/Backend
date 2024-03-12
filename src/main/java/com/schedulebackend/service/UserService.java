package com.schedulebackend.service;


import com.schedulebackend.database.DTO.AuthDTO.UserCreateDTO;
import com.schedulebackend.database.DTO.AuthDTO.UserReadDTO;
import com.schedulebackend.database.entity.User;
import com.schedulebackend.database.mapper.UserCreateMapper;
import com.schedulebackend.database.mapper.UserReadMapper;
import com.schedulebackend.database.repository.UserRepository;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final UserCreateMapper userCreateMapper;

    public List<UserReadDTO> findAll() {
        return userRepository.findAll().stream()
                .map(userReadMapper::map)
                .toList();
    }

    public Optional<UserReadDTO> findById(Long id) {
        return userRepository.findById(id)
                .map(userReadMapper::map);
    }

    @Transactional
    public User create(UserCreateDTO userDto) throws AuthException {
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new AuthException("Эта почта уже занята");
        }
        return Optional.of(userDto)
                .map(userCreateMapper::map)
                .map(userRepository::save)
                .orElseThrow();
    }

//    @Transactional
//    public Optional<UserReadDTO> update(Long id, UserCreateDTO userDto) {
//        return userRepository.findById(id)
//                .map(user -> userCreateMapper.map(userDto, user))
//                .map(userRepository::saveAndFlush)
//                .map(userReadMapper::map);
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        Collections.singleton(user.getRole())
                )).orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user: " + username));
    }
}
