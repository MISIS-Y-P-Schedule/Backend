package com.schedulebackend.service;

import com.schedulebackend.database.entity.TgUser;
import com.schedulebackend.database.entity.enums.UserState;
import com.schedulebackend.database.repository.TgUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TgUserService {

    private final TgUserRepository tgUserRepository;

    @Transactional
    public void createTgUser(Long userId) {
        if(!tgUserRepository.existsByTgId(userId)) tgUserRepository.save(new TgUser(userId, UserState.ALL_NOTIFICATIONS));
    }

    @Transactional
    public void updateUserState(Long userId, UserState state) {
        TgUser user = tgUserRepository.findByTgId(userId).orElse(new TgUser(userId, state));
        user.setUserState(state);
        tgUserRepository.save(user);
    }

    @Transactional
    public void deleteTgUser(Long userId){
        tgUserRepository.deleteByTgId(userId);
    }
    public List<TgUser> getUsersByState(UserState state) {
        return tgUserRepository.findByUserState(state);
    }

    public  List<TgUser> getAllUsers(){
        return tgUserRepository.findAll();
    }
}