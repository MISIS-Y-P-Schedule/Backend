package com.schedulebackend.service;

import com.schedulebackend.database.DTO.ScheduleParseDTO.DayDTO;
import com.schedulebackend.database.DTO.ScheduleParseDTO.LessonDTO;
import com.schedulebackend.database.DTO.ScheduleParseDTO.ScheduleParseDTO;
import com.schedulebackend.database.entity.*;
import com.schedulebackend.database.entity.enums.ChangeType;
import com.schedulebackend.database.entity.enums.LessonType;
import com.schedulebackend.database.mapper.ScheduleChangelogCreateMapper;
import com.schedulebackend.database.repository.*;
import com.schedulebackend.parsers.ScheduleParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {
    private final ClassroomRepository classroomRepository;
    private final ScheduleParser scheduleParser;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleChangelogCreateMapper scheduleChangelogCreateMapper;
    private final ScheduleChangelogRepository scheduleChangelogRepository;
    private final BellRepository bellRepository;
    private final GroupRepository groupRepository;
    private final TeacherRepository teacherRepository;
    private final LessonService lessonService;
    List<Schedule> newScheduleList = new ArrayList<>();

    //TODO Обработать расписания на прошлые недели
    @Transactional
    public Schedule createSchedule(Schedule schedule, Timestamp changeDateTime) {
        scheduleRepository.save(schedule);
        ScheduleChangelog scheduleChangelog = new ScheduleChangelog();
        scheduleChangelog.setChangeType(ChangeType.CREATE);

        scheduleChangelog.setChangeTime(changeDateTime);
        scheduleChangelogRepository.save(scheduleChangelogCreateMapper.map(scheduleChangelog, schedule));
        return schedule;
    }

    @Transactional
    public Schedule editSchedule(Schedule schedule, Timestamp changeDateTime) {
        Schedule oldSchedule = scheduleRepository.findByDateAndStartTimeAndGroup(schedule.getDate(), schedule.getStartTime(), schedule.getGroup());

        ScheduleChangelog scheduleChangelog = new ScheduleChangelog();
        scheduleChangelog.setChangeType(ChangeType.UPDATE);
        scheduleChangelog.setChangeTime(changeDateTime);
        scheduleChangelogRepository.save(scheduleChangelogCreateMapper.map(scheduleChangelog, oldSchedule));

        oldSchedule.setLesson(schedule.getLesson());
        oldSchedule.setClassroom(schedule.getClassroom());
        oldSchedule.setTeacher(schedule.getTeacher());
        oldSchedule.setLessonType(schedule.getLessonType());
        scheduleRepository.save(oldSchedule);
        return oldSchedule;
    }

    @Transactional
    public Boolean deleteSchedule(Schedule schedule, Timestamp changeDateTime) {
        ScheduleChangelog scheduleChangelog = new ScheduleChangelog();
        scheduleChangelog.setChangeType(ChangeType.DELETE);
        scheduleChangelog.setChangeTime(changeDateTime);
        scheduleChangelogRepository.save(scheduleChangelogCreateMapper.map(scheduleChangelog, schedule));
        scheduleRepository.delete(schedule);
        return true;
    }


//    @Transactional
//    public void addToScheduleChangelog(ScheduleChangelog scheduleChangelog) {
//        scheduleChangelogRepository.save(scheduleChangelog);
//    }

//    @Transactional
//    public List<Schedule> updateOldDtoSchedule(LessonsArrayJsonDTO lessonDTO, Date date, Time startTime, Time endTime, List<Schedule> newSchedule, Timestamp changeDateTime) {
//        List<Teacher> teachers = lessonDTO.getTeacherList();
//        for (Teacher teacherOnlyExternalID : teachers) {
//            Teacher teacher = teacherRepository.findTeacherByExternalID(teacherOnlyExternalID.getExternalID());
//            Lesson lesson = lessonService.createOrReturnLesson(new Lesson(lessonDTO.getSubjectId(), lessonDTO.getSubjectName()));
//            Group group = groupRepository.findByExternalID(lessonDTO.getGroupList().get(0).getExternalID());
//            Schedule schedule = new Schedule(date, startTime, endTime, lesson, teacher, classroomRepository.findClassroomByExternalID(lessonDTO.getClassroomList().get(teachers.indexOf(teacherOnlyExternalID)).getExternalID()), group, LessonType.valueOf(lessonDTO.getLessonType().toString()));
//            if (scheduleRepository.findByDateAndStartTimeAndTeacherAndGroupAndLesson(date, startTime, teacher, group, lesson) == null) {
//                if (scheduleRepository.findByDateAndStartTimeAndGroup(date, startTime, group) == null) {
//                    newSchedule.add(this.createSchedule(schedule, changeDateTime));
//                } else {
//                    newSchedule.add(this.editSchedule(schedule, changeDateTime));
//                }
//            } else {
//                newSchedule.add(schedule);
//            }
//        }
//        return newSchedule;
//    }

    @Transactional
    public List<Schedule> updateDtoSchedule(LessonDTO lessonDTO, Date date, Time startTime, Time endTime, List<Schedule> newSchedule, Timestamp changeDateTime) {
        List<Teacher> teachers = lessonDTO.getTeacherList();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDateTime.now().format()
        for (Teacher teacherOnlyExternalID : teachers) {
            Teacher teacher;
            if (teacherOnlyExternalID.getExternalID() == null) {
                teacher = teacherRepository.findTeacherByExternalID(1);
            } else {
                teacher = teacherRepository.findTeacherByExternalID(teacherOnlyExternalID.getExternalID());
            }
            Lesson lesson = lessonService.createOrReturnLesson(new Lesson(lessonDTO.getSubjectId(), lessonDTO.getSubjectName()));
            Group group = groupRepository.findByExternalID(lessonDTO.getGroupList().get(0).getExternalID());
            //System.out.println("Lab "+LessonType.LAB.getName().equals(lessonDTO.getLessonType()));
            //System.out.println("PR "+LessonType.PRACTICE.getName().equals(lessonDTO.getLessonType()));
            //System.out.println("Lk "+LessonType.LECTURE.getName().equals(lessonDTO.getLessonType()));
            //System.out.println(LessonType.getLessonType(lessonDTO.getLessonType()));
            LessonType lessonType = LessonType.getLessonType(lessonDTO.getLessonType());
            //System.out.println(lessonType);
            Schedule schedule = new Schedule(date, startTime, endTime, lesson, teacher, classroomRepository.findClassroomByExternalID(lessonDTO.getClassroomList().get(teachers.indexOf(teacherOnlyExternalID)).getExternalID()), group, lessonType);
            if (scheduleRepository.findByDateAndStartTimeAndTeacherAndGroupAndLesson(date, startTime, teacher, group, lesson) == null) {
                if ((scheduleRepository.findByDateAndStartTimeAndGroup(date, startTime, group) == null || teachers.indexOf(teacherOnlyExternalID) > 0)) {
                    System.out.println(teachers.indexOf(teacherOnlyExternalID));
                    newSchedule.add(this.createSchedule(schedule, changeDateTime));
                } else {
                    newSchedule.add(this.editSchedule(schedule, changeDateTime));
                }
            } else {
                newSchedule.add(schedule);
            }
        }
        return newSchedule;
    }

    @Transactional
    public void updateSchedule() throws IOException, InterruptedException {
        //List<Schedule> newScheduleList = new ArrayList<>();
        newScheduleList = new ArrayList<>();
        List<Integer> allGroupExternalIDs = groupRepository.findAllExternalID();
        Timestamp changeDateTime = Timestamp.from(Instant.from(ZonedDateTime.now()));
        LocalDate localdate = LocalDate.now();
        localdate = localdate.minusDays(localdate.getDayOfWeek().getValue() - 1);

        //for (int groupExternalID : allGroupExternalIDs) {
        ScheduleParseDTO deserializedSchedule = scheduleParser.getScheduleFromAPI(7711, localdate.toString());
        if (deserializedSchedule.getStatus().equals("FOUND")) {
            Collection<Map<String, DayDTO>> bellDayMap = deserializedSchedule.getSchedule().values();
            int bellCounter = 1;
            for (Map<String, DayDTO> bellMap : bellDayMap) {
                int dayCounter = 0;
                //bellCounter++;
                boolean test = false;
                for (DayDTO day : bellMap.values()) {
                    if (!(day.getLessons() == null)) {
                        if (!day.getLessons().isEmpty()) {
                            System.out.println(day.getLessons().get(0).getTeacherList());
                            System.out.println(localdate.plusDays(dayCounter));
//                            newScheduleList.addAll(updateDtoSchedule(day.getLessons().get(0), java.sql.Date.valueOf(localdate.plusDays(dayCounter)), startTimes.get(bellCounter), endTimes.get(bellCounter), newScheduleList, changeDateTime));
                            updateDtoSchedule(day.getLessons().get(0), java.sql.Date.valueOf(localdate.plusDays(dayCounter)), bellRepository.getStartTime((long) bellCounter), bellRepository.getEndTime((long) bellCounter), newScheduleList, changeDateTime);
                            //lessonDTOs.addAll(day.getLessons());
                            dayCounter++;
                            if (!test) {
                                bellCounter++;
                                test = true;
                            }
                        }
                    }
                }
            }
        }
        Thread.sleep(2000);
        //}
        System.out.println("newScheduleList: " + newScheduleList.size());
        if (!newScheduleList.isEmpty()) {
            for (Schedule schedule : scheduleRepository.findAll()) {
                boolean exists = false;
                for (Schedule newSchedule : newScheduleList) {
                    if (newSchedule.getDate().equals(schedule.getDate()) && newSchedule.getStartTime().toString().equals(schedule.getStartTime().toString()) && newSchedule.getTeacher().getExternalID().equals(schedule.getTeacher().getExternalID()) && newSchedule.getLesson().getExternalID().equals(schedule.getLesson().getExternalID()) && newSchedule.getGroup().getExternalID().equals(schedule.getGroup().getExternalID())) {
                        exists = true;
                    }
                    System.out.println("id" + newScheduleList.indexOf(newSchedule));
                    if (newScheduleList.indexOf(newSchedule) == (newScheduleList.size() - 1))
                        System.out.println(exists);
                    if (newScheduleList.indexOf(newSchedule) == newScheduleList.size() - 1 && !exists) {
                        System.out.println("Delete " + schedule);
                        this.deleteSchedule(schedule, changeDateTime);
                    }
                }
            }
        }
        System.out.println("test1");
//        System.out.println("sdda");
//        System.out.println(lessonDTOs);
//            LessonsArrayJsonDTO[][] lessonDTOs = new LessonsArrayJsonDTO[7][6]; // Предполагая, что у вас 7 звонков и 6 дней
//
//
//            lessonDTOs[0][0] = deserializedSchedule.getBellJsonDTO1().getDayJsonDTO1().getLessonsArrayJsonDTOList().get(0);
//            lessonDTOs[0][1] = deserializedSchedule.getBellJsonDTO1().getDayJsonDTO2().getLessonsArrayJsonDTOList().get(0);
//            lessonDTOs[0][2] = deserializedSchedule.getBellJsonDTO1().getDayJsonDTO3().getLessonsArrayJsonDTOList().get(0);
//            lessonDTOs[0][3] = deserializedSchedule.getBellJsonDTO1().getDayJsonDTO4().getLessonsArrayJsonDTOList().get(0);
//            lessonDTOs[0][4] = deserializedSchedule.getBellJsonDTO1().getDayJsonDTO5().getLessonsArrayJsonDTOList().get(0);
//            lessonDTOs[0][5] = deserializedSchedule.getBellJsonDTO1().getDayJsonDTO6().getLessonsArrayJsonDTOList().get(0);
//
//            lessonDTOs[1][0] = deserializedSchedule.getBellJsonDTO2().getDayJsonDTO1().getLessonsArrayJsonDTOList().get(0);
//            lessonDTOs[1][1] = deserializedSchedule.getBellJsonDTO2().getDayJsonDTO2().getLessonsArrayJsonDTOList().get(0);
//            lessonDTOs[1][2] = deserializedSchedule.getBellJsonDTO2().getDayJsonDTO3().getLessonsArrayJsonDTOList().get(0);
//            lessonDTOs[1][3] = deserializedSchedule.getBellJsonDTO2().getDayJsonDTO4().getLessonsArrayJsonDTOList().get(0);
//            lessonDTOs[1][4] = deserializedSchedule.getBellJsonDTO2().getDayJsonDTO5().getLessonsArrayJsonDTOList().get(0);
//            lessonDTOs[1][5] = deserializedSchedule.getBellJsonDTO2().getDayJsonDTO6().getLessonsArrayJsonDTOList().get(0);
//
//            lessonDTOs[2][0] = deserializedSchedule.getBellJsonDTO3().getDayJsonDTO1().getLessonsArrayJsonDTOList().get(0);
//            lessonDTOs[2][1] = deserializedSchedule.getBellJsonDTO3().getDayJsonDTO2().getLessonsArrayJsonDTOList().get(0);
//            lessonDTOs[2][2] = deserializedSchedule.getBellJsonDTO3().getDayJsonDTO3().getLessonsArrayJsonDTOList().get(0);
//            lessonDTOs[2][3] = deserializedSchedule.getBellJsonDTO3().getDayJsonDTO4().getLessonsArrayJsonDTOList().get(0);
//            lessonDTOs[2][4] = deserializedSchedule.getBellJsonDTO3().getDayJsonDTO5().getLessonsArrayJsonDTOList().get(0);
//            lessonDTOs[2][5] = deserializedSchedule.getBellJsonDTO3().getDayJsonDTO6().getLessonsArrayJsonDTOList().get(0);
//
//            lessonDTOs[3][0] = deserializedSchedule.getBellJsonDTO4().getDayJsonDTO1().getLessonsArrayJsonDTOList().get(0);
//            lessonDTOs[3][1] = deserializedSchedule.getBellJsonDTO4().getDayJsonDTO2().getLessonsArrayJsonDTOList().get(0);
//            lessonDTOs[3][2] = deserializedSchedule.getBellJsonDTO4().getDayJsonDTO3().getLessonsArrayJsonDTOList().get(0);
//            lessonDTOs[3][3] = deserializedSchedule.getBellJsonDTO4().getDayJsonDTO4().getLessonsArrayJsonDTOList().get(0);
//            lessonDTOs[3][4] = deserializedSchedule.getBellJsonDTO4().getDayJsonDTO5().getLessonsArrayJsonDTOList().get(0);
//            lessonDTOs[3][5] = deserializedSchedule.getBellJsonDTO4().getDayJsonDTO6().getLessonsArrayJsonDTOList().get(0);
//
//            lessonDTOs[4][0] = deserializedSchedule.getBellJsonDTO5().getDayJsonDTO1().getLessonsArrayJsonDTOList().get(0);
//            lessonDTOs[4][1] = deserializedSchedule.getBellJsonDTO5().getDayJsonDTO2().getLessonsArrayJsonDTOList().get(0);
//            lessonDTOs[4][2] = deserializedSchedule.getBellJsonDTO5().getDayJsonDTO3().getLessonsArrayJsonDTOList().get(0);
//            lessonDTOs[4][3] = deserializedSchedule.getBellJsonDTO5().getDayJsonDTO4().getLessonsArrayJsonDTOList().get(0);
//            lessonDTOs[4][4] = deserializedSchedule.getBellJsonDTO5().getDayJsonDTO5().getLessonsArrayJsonDTOList().get(0);
//            lessonDTOs[4][5] = deserializedSchedule.getBellJsonDTO5().getDayJsonDTO6().getLessonsArrayJsonDTOList().get(0);
//
//            lessonDTOs[5][0] = deserializedSchedule.getBellJsonDTO6().getDayJsonDTO1().getLessonsArrayJsonDTOList().get(0);
//            lessonDTOs[5][1] = deserializedSchedule.getBellJsonDTO6().getDayJsonDTO2().getLessonsArrayJsonDTOList().get(0);
//            lessonDTOs[5][2] = deserializedSchedule.getBellJsonDTO6().getDayJsonDTO3().getLessonsArrayJsonDTOList().get(0);
//            lessonDTOs[5][3] = deserializedSchedule.getBellJsonDTO6().getDayJsonDTO4().getLessonsArrayJsonDTOList().get(0);
//            lessonDTOs[5][4] = deserializedSchedule.getBellJsonDTO6().getDayJsonDTO5().getLessonsArrayJsonDTOList().get(0);
//            lessonDTOs[5][5] = deserializedSchedule.getBellJsonDTO6().getDayJsonDTO6().getLessonsArrayJsonDTOList().get(0);
//
//            lessonDTOs[6][0] = deserializedSchedule.getBellJsonDTO7().getDayJsonDTO1().getLessonsArrayJsonDTOList().get(0);
//            lessonDTOs[6][1] = deserializedSchedule.getBellJsonDTO7().getDayJsonDTO2().getLessonsArrayJsonDTOList().get(0);
//            lessonDTOs[6][2] = deserializedSchedule.getBellJsonDTO7().getDayJsonDTO3().getLessonsArrayJsonDTOList().get(0);
//            lessonDTOs[6][3] = deserializedSchedule.getBellJsonDTO7().getDayJsonDTO4().getLessonsArrayJsonDTOList().get(0);
//            lessonDTOs[6][4] = deserializedSchedule.getBellJsonDTO7().getDayJsonDTO5().getLessonsArrayJsonDTOList().get(0);
//            lessonDTOs[6][5] = deserializedSchedule.getBellJsonDTO7().getDayJsonDTO6().getLessonsArrayJsonDTOList().get(0);
//
//
//            LocalDate localdate = LocalDate.now();
//            Date date = java.sql.Date.valueOf(localdate);
//            BellJsonDTO[] bellDTOs = new BellJsonDTO[7];
//            //bellDTOs[0] = deserializedSchedule.getBellJsonDTO1().getBellHeaderDTO()
//            LocalTime localStartTimeBell1 = LocalTime.parse(deserializedSchedule.getBellJsonDTO1().getBellHeaderDTO().getStartTime());
//            LocalTime localEndTimeBell1 = LocalTime.parse(deserializedSchedule.getBellJsonDTO1().getBellHeaderDTO().getEndTime());
//            Time startTimeBell1 = java.sql.Time.valueOf(localStartTimeBell1);
//            Time endTimeBell1 = java.sql.Time.valueOf(localEndTimeBell1);
//
//            LocalTime localStartTimeBell2 = LocalTime.parse(deserializedSchedule.getBellJsonDTO2().getBellHeaderDTO().getStartTime());
//            LocalTime localEndTimeBell2 = LocalTime.parse(deserializedSchedule.getBellJsonDTO2().getBellHeaderDTO().getEndTime());
//            Time startTimeBell2 = java.sql.Time.valueOf(localStartTimeBell2);
//            Time endTimeBell2 = java.sql.Time.valueOf(localEndTimeBell2);
//
//            LocalTime localStartTimeBell3 = LocalTime.parse(deserializedSchedule.getBellJsonDTO3().getBellHeaderDTO().getStartTime());
//            LocalTime localEndTimeBell3 = LocalTime.parse(deserializedSchedule.getBellJsonDTO3().getBellHeaderDTO().getEndTime());
//            Time startTimeBell3 = java.sql.Time.valueOf(localStartTimeBell3);
//            Time endTimeBell3 = java.sql.Time.valueOf(localEndTimeBell3);
//
//            LocalTime localStartTimeBell4 = LocalTime.parse(deserializedSchedule.getBellJsonDTO4().getBellHeaderDTO().getStartTime());
//            LocalTime localEndTimeBell4 = LocalTime.parse(deserializedSchedule.getBellJsonDTO4().getBellHeaderDTO().getEndTime());
//            Time startTimeBell4 = java.sql.Time.valueOf(localStartTimeBell4);
//            Time endTimeBell4 = java.sql.Time.valueOf(localEndTimeBell4);
//
//            LocalTime localStartTimeBell5 = LocalTime.parse(deserializedSchedule.getBellJsonDTO5().getBellHeaderDTO().getStartTime());
//            LocalTime localEndTimeBell5 = LocalTime.parse(deserializedSchedule.getBellJsonDTO5().getBellHeaderDTO().getEndTime());
//            Time startTimeBell5 = java.sql.Time.valueOf(localStartTimeBell5);
//            Time endTimeBell5 = java.sql.Time.valueOf(localEndTimeBell5);
//
//            LocalTime localStartTimeBell6 = LocalTime.parse(deserializedSchedule.getBellJsonDTO6().getBellHeaderDTO().getStartTime());
//            LocalTime localEndTimeBell6 = LocalTime.parse(deserializedSchedule.getBellJsonDTO6().getBellHeaderDTO().getEndTime());
//            Time startTimeBell6 = java.sql.Time.valueOf(localStartTimeBell6);
//            Time endTimeBell6 = java.sql.Time.valueOf(localEndTimeBell6);
//
//            LocalTime localStartTimeBell7 = LocalTime.parse(deserializedSchedule.getBellJsonDTO7().getBellHeaderDTO().getStartTime());
//            LocalTime localEndTimeBell7 = LocalTime.parse(deserializedSchedule.getBellJsonDTO7().getBellHeaderDTO().getEndTime());
//            Time startTimeBell7 = java.sql.Time.valueOf(localStartTimeBell7);
//            Time endTimeBell7 = java.sql.Time.valueOf(localEndTimeBell7);
//
//            for (int bell = 1; bell <= 7; bell++) {
//                for (int day = 1; day <= 6; day++) {
//                    newSchedule.addAll(updateDtoSchedule(lessonDTOs[bell][day], date, startTimeBell1, endTimeBell1, newSchedule));
//                }
//            }


//            LessonsArrayJsonDTO lessonDTOBell1Day1 = deserializedSchedule.getBellJsonDTO1().getDayJsonDTO1().getLessonsArrayJsonDTOList().get(0);
//            LessonsArrayJsonDTO lessonDTOBell1Day2 = deserializedSchedule.getBellJsonDTO1().getDayJsonDTO2().getLessonsArrayJsonDTOList().get(0);
//            LessonsArrayJsonDTO lessonDTOBell1Day3 = deserializedSchedule.getBellJsonDTO1().getDayJsonDTO3().getLessonsArrayJsonDTOList().get(0);
//            LessonsArrayJsonDTO lessonDTOBell1Day4 = deserializedSchedule.getBellJsonDTO1().getDayJsonDTO4().getLessonsArrayJsonDTOList().get(0);
//            LessonsArrayJsonDTO lessonDTOBell1Day5 = deserializedSchedule.getBellJsonDTO1().getDayJsonDTO5().getLessonsArrayJsonDTOList().get(0);
//            LessonsArrayJsonDTO lessonDTOBell1Day6 = deserializedSchedule.getBellJsonDTO1().getDayJsonDTO6().getLessonsArrayJsonDTOList().get(0);
//
//            LessonsArrayJsonDTO lessonDTOBell2Day1 = deserializedSchedule.getBellJsonDTO2().getDayJsonDTO1().getLessonsArrayJsonDTOList().get(0);
//            LessonsArrayJsonDTO lessonDTOBell2Day2 = deserializedSchedule.getBellJsonDTO2().getDayJsonDTO2().getLessonsArrayJsonDTOList().get(0);
//            LessonsArrayJsonDTO lessonDTOBell2Day3 = deserializedSchedule.getBellJsonDTO2().getDayJsonDTO3().getLessonsArrayJsonDTOList().get(0);
//            LessonsArrayJsonDTO lessonDTOBell2Day4 = deserializedSchedule.getBellJsonDTO2().getDayJsonDTO4().getLessonsArrayJsonDTOList().get(0);
//            LessonsArrayJsonDTO lessonDTOBell2Day5 = deserializedSchedule.getBellJsonDTO2().getDayJsonDTO5().getLessonsArrayJsonDTOList().get(0);
//            LessonsArrayJsonDTO lessonDTOBell2Day6 = deserializedSchedule.getBellJsonDTO2().getDayJsonDTO6().getLessonsArrayJsonDTOList().get(0);
//
//            LessonsArrayJsonDTO lessonDTOBell3Day1 = deserializedSchedule.getBellJsonDTO3().getDayJsonDTO1().getLessonsArrayJsonDTOList().get(0);
//            LessonsArrayJsonDTO lessonDTOBell3Day2 = deserializedSchedule.getBellJsonDTO3().getDayJsonDTO2().getLessonsArrayJsonDTOList().get(0);
//            LessonsArrayJsonDTO lessonDTOBell3Day3 = deserializedSchedule.getBellJsonDTO3().getDayJsonDTO3().getLessonsArrayJsonDTOList().get(0);
//            LessonsArrayJsonDTO lessonDTOBell3Day4 = deserializedSchedule.getBellJsonDTO3().getDayJsonDTO4().getLessonsArrayJsonDTOList().get(0);
//            LessonsArrayJsonDTO lessonDTOBell3Day5 = deserializedSchedule.getBellJsonDTO3().getDayJsonDTO5().getLessonsArrayJsonDTOList().get(0);
//            LessonsArrayJsonDTO lessonDTOBell3Day6 = deserializedSchedule.getBellJsonDTO3().getDayJsonDTO6().getLessonsArrayJsonDTOList().get(0);
//
//            LessonsArrayJsonDTO lessonDTOBell4Day1 = deserializedSchedule.getBellJsonDTO4().getDayJsonDTO1().getLessonsArrayJsonDTOList().get(0);
//            LessonsArrayJsonDTO lessonDTOBell4Day2 = deserializedSchedule.getBellJsonDTO4().getDayJsonDTO2().getLessonsArrayJsonDTOList().get(0);
//            LessonsArrayJsonDTO lessonDTOBell4Day3 = deserializedSchedule.getBellJsonDTO4().getDayJsonDTO3().getLessonsArrayJsonDTOList().get(0);
//            LessonsArrayJsonDTO lessonDTOBell4Day4 = deserializedSchedule.getBellJsonDTO4().getDayJsonDTO4().getLessonsArrayJsonDTOList().get(0);
//            LessonsArrayJsonDTO lessonDTOBell4Day5 = deserializedSchedule.getBellJsonDTO4().getDayJsonDTO5().getLessonsArrayJsonDTOList().get(0);
//            LessonsArrayJsonDTO lessonDTOBell4Day6 = deserializedSchedule.getBellJsonDTO4().getDayJsonDTO6().getLessonsArrayJsonDTOList().get(0);
//
//            LessonsArrayJsonDTO lessonDTOBell5Day1 = deserializedSchedule.getBellJsonDTO5().getDayJsonDTO1().getLessonsArrayJsonDTOList().get(0);
//            LessonsArrayJsonDTO lessonDTOBell5Day2 = deserializedSchedule.getBellJsonDTO5().getDayJsonDTO2().getLessonsArrayJsonDTOList().get(0);
//            LessonsArrayJsonDTO lessonDTOBell5Day3 = deserializedSchedule.getBellJsonDTO5().getDayJsonDTO3().getLessonsArrayJsonDTOList().get(0);
//            LessonsArrayJsonDTO lessonDTOBell5Day4 = deserializedSchedule.getBellJsonDTO5().getDayJsonDTO4().getLessonsArrayJsonDTOList().get(0);
//            LessonsArrayJsonDTO lessonDTOBell5Day5 = deserializedSchedule.getBellJsonDTO5().getDayJsonDTO5().getLessonsArrayJsonDTOList().get(0);
//            LessonsArrayJsonDTO lessonDTOBell5Day6 = deserializedSchedule.getBellJsonDTO5().getDayJsonDTO6().getLessonsArrayJsonDTOList().get(0);
//
//            LessonsArrayJsonDTO lessonDTOBell6Day1 = deserializedSchedule.getBellJsonDTO6().getDayJsonDTO1().getLessonsArrayJsonDTOList().get(0);
//            LessonsArrayJsonDTO lessonDTOBell6Day2 = deserializedSchedule.getBellJsonDTO6().getDayJsonDTO2().getLessonsArrayJsonDTOList().get(0);
//            LessonsArrayJsonDTO lessonDTOBell6Day3 = deserializedSchedule.getBellJsonDTO6().getDayJsonDTO3().getLessonsArrayJsonDTOList().get(0);
//            LessonsArrayJsonDTO lessonDTOBell6Day4 = deserializedSchedule.getBellJsonDTO6().getDayJsonDTO4().getLessonsArrayJsonDTOList().get(0);
//            LessonsArrayJsonDTO lessonDTOBell6Day5 = deserializedSchedule.getBellJsonDTO6().getDayJsonDTO5().getLessonsArrayJsonDTOList().get(0);
//            LessonsArrayJsonDTO lessonDTOBell6Day6 = deserializedSchedule.getBellJsonDTO6().getDayJsonDTO6().getLessonsArrayJsonDTOList().get(0);
//
//            LessonsArrayJsonDTO lessonDTOBell7Day1 = deserializedSchedule.getBellJsonDTO7().getDayJsonDTO1().getLessonsArrayJsonDTOList().get(0);
//            LessonsArrayJsonDTO lessonDTOBell7Day2 = deserializedSchedule.getBellJsonDTO7().getDayJsonDTO2().getLessonsArrayJsonDTOList().get(0);
//            LessonsArrayJsonDTO lessonDTOBell7Day3 = deserializedSchedule.getBellJsonDTO7().getDayJsonDTO3().getLessonsArrayJsonDTOList().get(0);
//            LessonsArrayJsonDTO lessonDTOBell7Day4 = deserializedSchedule.getBellJsonDTO7().getDayJsonDTO4().getLessonsArrayJsonDTOList().get(0);
//            LessonsArrayJsonDTO lessonDTOBell7Day5 = deserializedSchedule.getBellJsonDTO7().getDayJsonDTO5().getLessonsArrayJsonDTOList().get(0);
//            LessonsArrayJsonDTO lessonDTOBell7Day6 = deserializedSchedule.getBellJsonDTO7().getDayJsonDTO6().getLessonsArrayJsonDTOList().get(0);

//            LocalDate localdate = LocalDate.now();
//            Date date = java.sql.Date.valueOf(localdate);
//
//            LocalTime localStartTimeBell1 = LocalTime.parse(deserializedSchedule.getBellJsonDTO1().getBellHeaderDTO().getStartTime());
//            LocalTime localEndTimeBell1 = LocalTime.parse(deserializedSchedule.getBellJsonDTO1().getBellHeaderDTO().getEndTime());
//            Time startTimeBell1 = java.sql.Time.valueOf(localStartTimeBell1);
//            Time endTimeBell1 = java.sql.Time.valueOf(localEndTimeBell1);
//
//            LocalTime localStartTimeBell2 = LocalTime.parse(deserializedSchedule.getBellJsonDTO2().getBellHeaderDTO().getStartTime());
//            LocalTime localEndTimeBell2 = LocalTime.parse(deserializedSchedule.getBellJsonDTO2().getBellHeaderDTO().getEndTime());
//            Time startTimeBell2 = java.sql.Time.valueOf(localStartTimeBell2);
//            Time endTimeBell2 = java.sql.Time.valueOf(localEndTimeBell2);
//
//            LocalTime localStartTimeBell3 = LocalTime.parse(deserializedSchedule.getBellJsonDTO3().getBellHeaderDTO().getStartTime());
//            LocalTime localEndTimeBell3 = LocalTime.parse(deserializedSchedule.getBellJsonDTO3().getBellHeaderDTO().getEndTime());
//            Time startTimeBell3 = java.sql.Time.valueOf(localStartTimeBell3);
//            Time endTimeBell3 = java.sql.Time.valueOf(localEndTimeBell3);
//
//            LocalTime localStartTimeBell4 = LocalTime.parse(deserializedSchedule.getBellJsonDTO4().getBellHeaderDTO().getStartTime());
//            LocalTime localEndTimeBell4 = LocalTime.parse(deserializedSchedule.getBellJsonDTO4().getBellHeaderDTO().getEndTime());
//            Time startTimeBell4 = java.sql.Time.valueOf(localStartTimeBell4);
//            Time endTimeBell4 = java.sql.Time.valueOf(localEndTimeBell4);
//
//            LocalTime localStartTimeBell5 = LocalTime.parse(deserializedSchedule.getBellJsonDTO5().getBellHeaderDTO().getStartTime());
//            LocalTime localEndTimeBell5 = LocalTime.parse(deserializedSchedule.getBellJsonDTO5().getBellHeaderDTO().getEndTime());
//            Time startTimeBell5 = java.sql.Time.valueOf(localStartTimeBell5);
//            Time endTimeBell5 = java.sql.Time.valueOf(localEndTimeBell5);
//
//            LocalTime localStartTimeBell6 = LocalTime.parse(deserializedSchedule.getBellJsonDTO6().getBellHeaderDTO().getStartTime());
//            LocalTime localEndTimeBell6 = LocalTime.parse(deserializedSchedule.getBellJsonDTO6().getBellHeaderDTO().getEndTime());
//            Time startTimeBell6 = java.sql.Time.valueOf(localStartTimeBell6);
//            Time endTimeBell6 = java.sql.Time.valueOf(localEndTimeBell6);
//
//            LocalTime localStartTimeBell7 = LocalTime.parse(deserializedSchedule.getBellJsonDTO7().getBellHeaderDTO().getStartTime());
//            LocalTime localEndTimeBell7 = LocalTime.parse(deserializedSchedule.getBellJsonDTO7().getBellHeaderDTO().getEndTime());
//            Time startTimeBell7 = java.sql.Time.valueOf(localStartTimeBell7);
//            Time endTimeBell7 = java.sql.Time.valueOf(localEndTimeBell7);

//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell1Day1, date, startTimeBell1, endTimeBell1, newSchedule));
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell1Day2, date, startTimeBell1, endTimeBell1, newSchedule));
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell1Day3, date, startTimeBell1, endTimeBell1, newSchedule));
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell1Day4, date, startTimeBell1, endTimeBell1, newSchedule));
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell1Day5, date, startTimeBell1, endTimeBell1, newSchedule));
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell1Day6, date, startTimeBell1, endTimeBell1, newSchedule));
//
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell2Day1, date, startTimeBell2, endTimeBell2, newSchedule));
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell2Day2, date, startTimeBell2, endTimeBell2, newSchedule));
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell2Day3, date, startTimeBell2, endTimeBell2, newSchedule));
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell2Day4, date, startTimeBell2, endTimeBell2, newSchedule));
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell2Day5, date, startTimeBell2, endTimeBell2, newSchedule));
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell2Day6, date, startTimeBell2, endTimeBell2, newSchedule));
//
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell3Day1, date, startTimeBell3, endTimeBell3, newSchedule));
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell3Day2, date, startTimeBell3, endTimeBell3, newSchedule));
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell3Day3, date, startTimeBell3, endTimeBell3, newSchedule));
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell3Day4, date, startTimeBell3, endTimeBell3, newSchedule));
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell3Day5, date, startTimeBell3, endTimeBell3, newSchedule));
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell3Day6, date, startTimeBell3, endTimeBell3, newSchedule));
//
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell4Day1, date, startTimeBell4, endTimeBell4, newSchedule));
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell4Day2, date, startTimeBell4, endTimeBell4, newSchedule));
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell4Day3, date, startTimeBell4, endTimeBell4, newSchedule));
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell4Day4, date, startTimeBell4, endTimeBell4, newSchedule));
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell4Day5, date, startTimeBell4, endTimeBell4, newSchedule));
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell4Day6, date, startTimeBell4, endTimeBell4, newSchedule));
//
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell5Day1, date, startTimeBell5, endTimeBell5, newSchedule));
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell5Day2, date, startTimeBell5, endTimeBell5, newSchedule));
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell5Day3, date, startTimeBell5, endTimeBell5, newSchedule));
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell5Day4, date, startTimeBell5, endTimeBell5, newSchedule));
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell5Day5, date, startTimeBell5, endTimeBell5, newSchedule));
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell5Day6, date, startTimeBell5, endTimeBell5, newSchedule));
//
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell6Day1, date, startTimeBell6, endTimeBell6, newSchedule));
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell6Day2, date, startTimeBell6, endTimeBell6, newSchedule));
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell6Day3, date, startTimeBell6, endTimeBell6, newSchedule));
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell6Day4, date, startTimeBell6, endTimeBell6, newSchedule));
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell6Day5, date, startTimeBell6, endTimeBell6, newSchedule));
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell6Day6, date, startTimeBell6, endTimeBell6, newSchedule));
//
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell7Day1, date, startTimeBell7, endTimeBell7, newSchedule));
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell7Day2, date, startTimeBell7, endTimeBell7, newSchedule));
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell7Day3, date, startTimeBell7, endTimeBell7, newSchedule));
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell7Day4, date, startTimeBell7, endTimeBell7, newSchedule));
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell7Day5, date, startTimeBell7, endTimeBell7, newSchedule));
//            newSchedule.addAll(updateDtoSchedule(lessonDTOBell7Day6, date, startTimeBell7, endTimeBell7, newSchedule));

//            List<Teacher> teachers = lessonDTO.getTeacherList();
//            for (Teacher teacherOnlyExternalID : teachers) {
//                Teacher teacher = teacherRepository.findTeacherByExternalID(teacherOnlyExternalID.getExternalID());
//                Lesson lesson = lessonService.createOrReturnLesson(new Lesson(lessonDTO.getSubjectId(), lessonDTO.getSubjectName()));
//                Group group = groupRepository.findByExternalID(groupExternalID);
//                Schedule schedule = new Schedule(date, startTime, java.sql.Time.valueOf(localEndTime), lesson, teacher, classroomRepository.findClassroomByExternalID(lessonDTO.getClassroomApiID()), group, LessonType.valueOf(lessonDTO.getLessonType()));
//                if (scheduleRepository.findByDateAndStartTimeAndTeacherAndGroupAndLesson(date, startTime, teacher, group, lesson) == null) {
//                    if (scheduleRepository.findByDateAndStartTimeAndGroup(date, startTime, group) == null) {
//                        newSchedule.add(this.createSchedule(schedule));
//                    } else {
//                        newSchedule.add(this.editSchedule(schedule));
//                    }
//                } else {
//                    newSchedule.add(schedule);
//                }
//            }
        //Schedule schedule = new Schedule();
        //return null;
    }

//        return null;
//        return filiationParserJSON.parseJSON().getResponseFromAPIDTO().getClassroomList()
//                .stream().map(this::createClassroom).collect(Collectors.toList());
}

