package com.schedulebackend.service;

import com.schedulebackend.database.entity.Classroom;
import com.schedulebackend.database.repository.ClassroomRepository;
import com.schedulebackend.parsers.FiliationParserJSON;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClassroomService {

    private final ClassroomRepository classroomRepository;
    private final FiliationParserJSON filiationParserJSON;

    @Transactional
    public Classroom createClassroom(Classroom classroom) {
        return classroomRepository.findClassroomByExternalID(classroom.getExternalID()) == null ? classroomRepository.save(classroom) : new Classroom();
    }

    @Transactional
    public List<Classroom> updateClassrooms() throws IOException {
        return filiationParserJSON.parseJSON().getResponseFiliationFromAPIDTO().getClassroomList()
                .stream().map(this::createClassroom).collect(Collectors.toList());
    }
}