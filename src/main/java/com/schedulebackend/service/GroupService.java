package com.schedulebackend.service;

import com.schedulebackend.database.entity.Group;
import com.schedulebackend.database.repository.GroupRepository;
import com.schedulebackend.parsers.FiliationParserJSON;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {

    private final GroupRepository groupRepository;
    private final FiliationParserJSON filiationParserJSON;

    @Transactional
    public Group createGroup(Group group) {
        return groupRepository.findByExternalID(group.getExternalID()) == null ? groupRepository.save(group) : new Group();
    }

    @Transactional
    public List<Group> updateGroups() throws IOException {
        return filiationParserJSON.parseJSON().getResponseFiliationFromAPIDTO().getGroupList()
                .stream().map(this::createGroup).collect(Collectors.toList());
    }

    @Transactional
    public List<Group> getAllGroups(){
        return groupRepository.findAll();
    }

    @Transactional
    public List<String> getAllNames(){
        return groupRepository.findAllName();
    }

    @Transactional
    public Group getGroupByName(String name){
        return groupRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException("Группа с таким именем не найдена"));
    }
}
