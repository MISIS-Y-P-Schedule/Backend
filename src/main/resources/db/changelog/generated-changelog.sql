-- liquibase formatted sql

-- changeset dimka:1710177151033-1
CREATE TABLE public.bells (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, end_time time(6) WITHOUT TIME ZONE, start_time time(6) WITHOUT TIME ZONE, CONSTRAINT "bellsPK" PRIMARY KEY (id));

-- changeset dimka:1710177151033-2
CREATE TABLE public.classrooms (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, external_id INTEGER, name VARCHAR(255), CONSTRAINT "classroomsPK" PRIMARY KEY (id));

-- changeset dimka:1710177151033-3
CREATE TABLE public.groups (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, external_id INTEGER, name VARCHAR(255), CONSTRAINT "groupsPK" PRIMARY KEY (id));

-- changeset dimka:1710177151033-4
CREATE TABLE public.lessons (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, external_id INTEGER, name VARCHAR(255), CONSTRAINT "lessonsPK" PRIMARY KEY (id));

-- changeset dimka:1710177151033-5
CREATE TABLE public.lessonsyp (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, lesson_link VARCHAR(255), name VARCHAR(255), teacheryp_id BIGINT, CONSTRAINT "lessonsypPK" PRIMARY KEY (id));

-- changeset dimka:1710177151033-6
CREATE TABLE public.news (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, content VARCHAR(5000), created_at TIMESTAMP(6) WITHOUT TIME ZONE, CONSTRAINT "newsPK" PRIMARY KEY (id));

-- changeset dimka:1710177151033-7
CREATE TABLE public.news_link_attachments (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, news_link VARCHAR(800), news_links_type VARCHAR(255), news_id BIGINT, CONSTRAINT "news_link_attachmentsPK" PRIMARY KEY (id));

-- changeset dimka:1710177151033-8
CREATE TABLE public.schedule (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, date TIMESTAMP(6) WITHOUT TIME ZONE, end_time time(6) WITHOUT TIME ZONE, lesson_type VARCHAR(255), start_time time(6) WITHOUT TIME ZONE, classroom_id BIGINT, group_id BIGINT, lesson_id BIGINT, teacher_id BIGINT, CONSTRAINT "schedulePK" PRIMARY KEY (id));

-- changeset dimka:1710177151033-9
CREATE TABLE public.schedule_changelog (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, change_time TIMESTAMP(6) WITHOUT TIME ZONE, change_type VARCHAR(255), date TIMESTAMP(6) WITHOUT TIME ZONE, end_time time(6) WITHOUT TIME ZONE, lesson_type VARCHAR(255), start_time time(6) WITHOUT TIME ZONE, classroom_id BIGINT, group_id BIGINT, lesson_id BIGINT, teacher_id BIGINT, CONSTRAINT "schedule_changelogPK" PRIMARY KEY (id));

-- changeset dimka:1710177151033-10
CREATE TABLE public.scheduled_tasks (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, active BOOLEAN NOT NULL, cron_expression VARCHAR(255), date TIMESTAMP(6) WITHOUT TIME ZONE, description VARCHAR(255), lesson_link VARCHAR(255), CONSTRAINT "scheduled_tasksPK" PRIMARY KEY (id));

-- changeset dimka:1710177151033-11
CREATE TABLE public.scheduleyp (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, date TIMESTAMP(6) WITHOUT TIME ZONE, end_time time(6) WITHOUT TIME ZONE, lesson_record_link VARCHAR(255), lesson_type VARCHAR(255), start_time time(6) WITHOUT TIME ZONE, lessonyp_id BIGINT, CONSTRAINT "scheduleypPK" PRIMARY KEY (id));

-- changeset dimka:1710177151033-12
CREATE TABLE public.scheduleyp_changelog (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, change_time TIMESTAMP(6) WITHOUT TIME ZONE, change_type VARCHAR(255), date TIMESTAMP(6) WITHOUT TIME ZONE, end_time time(6) WITHOUT TIME ZONE, lesson_record_link VARCHAR(255), lesson_type VARCHAR(255), start_time time(6) WITHOUT TIME ZONE, lessonyp_id BIGINT, CONSTRAINT "scheduleyp_changelogPK" PRIMARY KEY (id));

-- changeset dimka:1710177151033-13
CREATE TABLE public.tasks (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, deadline date, task_description VARCHAR(2500), task_link VARCHAR(255), group_id BIGINT, lessonyp_id BIGINT, CONSTRAINT "tasksPK" PRIMARY KEY (id));

-- changeset dimka:1710177151033-14
CREATE TABLE public.teachers (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, external_id INTEGER, firstname VARCHAR(255), lastname VARCHAR(255), midname VARCHAR(255), CONSTRAINT "teachersPK" PRIMARY KEY (id));

-- changeset dimka:1710177151033-15
CREATE TABLE public.teachersyp (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, firstname VARCHAR(255), lastname VARCHAR(255), midname VARCHAR(255), CONSTRAINT "teachersypPK" PRIMARY KEY (id));

-- changeset dimka:1710177151033-16
CREATE TABLE public.tg_users (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, tg_id BIGINT, user_state VARCHAR(255), CONSTRAINT "tg_usersPK" PRIMARY KEY (id));

-- changeset dimka:1710177151033-17
CREATE TABLE public.tokens (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, expired BOOLEAN NOT NULL, revoked BOOLEAN NOT NULL, token VARCHAR(255), user_id BIGINT, CONSTRAINT "tokensPK" PRIMARY KEY (id));

-- changeset dimka:1710177151033-18
CREATE TABLE public.user_tasks (readiness BOOLEAN, user_id BIGINT NOT NULL, task_id BIGINT NOT NULL);

-- changeset dimka:1710177151033-19
CREATE TABLE public.users (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, firstname VARCHAR(50), lastname VARCHAR(50), midname VARCHAR(50), password VARCHAR(255) DEFAULT '{noop}123', role VARCHAR(255), teacher_external_id INTEGER, username VARCHAR(255), group_id BIGINT, CONSTRAINT "usersPK" PRIMARY KEY (id));

-- changeset dimka:1710177151033-20
ALTER TABLE public.user_tasks ADD CONSTRAINT "user_tasksPK" PRIMARY KEY (task_id, user_id);

-- changeset dimka:1710177151033-21
ALTER TABLE public.tokens ADD CONSTRAINT UC_TOKENSTOKEN_COL UNIQUE (token);

-- changeset dimka:1710177151033-22
ALTER TABLE public.tokens ADD CONSTRAINT "FK2dylsfo39lgjyqml2tbe0b0ss" FOREIGN KEY (user_id) REFERENCES public.users (id);

-- changeset dimka:1710177151033-23
ALTER TABLE public.news_link_attachments ADD CONSTRAINT "FK83h97lr86dvurgh0ouuirwt6n" FOREIGN KEY (news_id) REFERENCES public.news (id);

-- changeset dimka:1710177151033-24
ALTER TABLE public.schedule ADD CONSTRAINT "FK84itfrvrvbie87o2iufdphi94" FOREIGN KEY (teacher_id) REFERENCES public.teachers (id);

-- changeset dimka:1710177151033-25
ALTER TABLE public.schedule_changelog ADD CONSTRAINT "FKb9ub5l7q4icslya6icuno63ej" FOREIGN KEY (classroom_id) REFERENCES public.classrooms (id);

-- changeset dimka:1710177151033-26
ALTER TABLE public.schedule ADD CONSTRAINT "FKbr3mo0shkit5dpgtdeij6a9a8" FOREIGN KEY (classroom_id) REFERENCES public.classrooms (id);

-- changeset dimka:1710177151033-27
ALTER TABLE public.schedule ADD CONSTRAINT "FKbwvsft5mb4gikoi29wxh24hii" FOREIGN KEY (lesson_id) REFERENCES public.lessons (id);

-- changeset dimka:1710177151033-28
ALTER TABLE public.tasks ADD CONSTRAINT "FKc34n6dul26wyud4358qawkxg2" FOREIGN KEY (lessonyp_id) REFERENCES public.lessonsyp (id);

-- changeset dimka:1710177151033-29
ALTER TABLE public.user_tasks ADD CONSTRAINT "FKd365e5kqm9ekl0dhslnnck8f7" FOREIGN KEY (task_id) REFERENCES public.tasks (id);

-- changeset dimka:1710177151033-30
ALTER TABLE public.schedule_changelog ADD CONSTRAINT "FKdgw1y8qdurrmal4wrxwkgnsdk" FOREIGN KEY (group_id) REFERENCES public.groups (id);

-- changeset dimka:1710177151033-31
ALTER TABLE public.users ADD CONSTRAINT "FKemfuglprp85bh5xwhfm898ysc" FOREIGN KEY (group_id) REFERENCES public.groups (id);

-- changeset dimka:1710177151033-32
ALTER TABLE public.tasks ADD CONSTRAINT "FKgo5e9jsb7dv0e6qcvmx7iuhpk" FOREIGN KEY (group_id) REFERENCES public.groups (id);

-- changeset dimka:1710177151033-33
ALTER TABLE public.schedule_changelog ADD CONSTRAINT "FKhrdt10ov27vb8efjbomofsryl" FOREIGN KEY (lesson_id) REFERENCES public.lessons (id);

-- changeset dimka:1710177151033-34
ALTER TABLE public.schedule ADD CONSTRAINT "FKkdul19ou5sp7di9s7r9kx7jjy" FOREIGN KEY (group_id) REFERENCES public.groups (id);

-- changeset dimka:1710177151033-35
ALTER TABLE public.lessonsyp ADD CONSTRAINT "FKkwpoeoypl5m69chjwp009myf2" FOREIGN KEY (teacheryp_id) REFERENCES public.teachersyp (id);

-- changeset dimka:1710177151033-36
ALTER TABLE public.scheduleyp_changelog ADD CONSTRAINT "FKl96xpoao60t0pb9v32er1l9bb" FOREIGN KEY (lessonyp_id) REFERENCES public.lessonsyp (id);

-- changeset dimka:1710177151033-37
ALTER TABLE public.schedule_changelog ADD CONSTRAINT "FKnnlqlv4qtm1oka0vjaihfoxna" FOREIGN KEY (teacher_id) REFERENCES public.teachers (id);

-- changeset dimka:1710177151033-38
ALTER TABLE public.scheduleyp ADD CONSTRAINT "FKpwd8diu09kwrfp8svv9g9cqxi" FOREIGN KEY (lessonyp_id) REFERENCES public.lessonsyp (id);

-- changeset dimka:1710177151033-39
ALTER TABLE public.user_tasks ADD CONSTRAINT "FKsrpyfa9asu2ymkcqr7jol85o2" FOREIGN KEY (user_id) REFERENCES public.users (id);

