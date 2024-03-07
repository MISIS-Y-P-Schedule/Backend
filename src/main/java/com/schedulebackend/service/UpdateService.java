package com.schedulebackend.service;

import com.schedulebackend.database.DTO.ResponseFiliationFromAPIDTO;
import com.schedulebackend.parsers.FiliationParserJSON;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UpdateService {

    private final ClassroomService classroomService;
    private final GroupService groupService;
    private final TeacherService teacherService;
    private final BellService bellService;
    private final FiliationParserJSON filiationParserJSON;

    @Transactional
    public ResponseFiliationFromAPIDTO updateAll() throws IOException {
        bellService.createBells();
        ResponseFiliationFromAPIDTO bufValue = filiationParserJSON.parseJSON().getResponseFiliationFromAPIDTO();
        return new ResponseFiliationFromAPIDTO(bufValue.getGroupList().stream().map(groupService::createGroup).collect(Collectors.toList()),
                bufValue.getClassroomList().stream().map(classroomService::createClassroom).collect(Collectors.toList()),
                bufValue.getTeacherList().stream().map(teacherService::createTeacher).collect(Collectors.toList()));
    }
}