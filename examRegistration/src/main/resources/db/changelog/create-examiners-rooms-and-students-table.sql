
create table rooms
(
    id           bigint auto_increment
        primary key,
    capacity     int          not null
        check (`capacity` <= 20),
    number       varchar(255) not null,
    room_subject varchar(255) null
);

create table examiners
(
    id         bigint auto_increment
        primary key,
    first_name varchar(255) null,
    last_name  varchar(255) null,
    room_id    bigint       null,
    constraint FKejaqjp9yrc87qnsbq3xwu4p95
        foreign key (room_id) references rooms (id)
);

create table examiner_taught_subjects
(
    examiner_id      bigint       not null,
    examiner_subject varchar(255) null,
    constraint FKnxsxh47wr5qcgp795s076i0xt
        foreign key (examiner_id) references examiners (id)
);

create table students
(
    id         bigint auto_increment
        primary key,
    first_name varchar(255) null,
    last_name  varchar(255) null,
    room_id    bigint       null,
    constraint FKq8l9dnbc3y02t87sh1d88408j
        foreign key (room_id) references rooms (id)
);

create table student_subjects
(
    student_id       bigint       not null,
    student_subjects varchar(255) null,
    constraint FKjb6x19uwrg0tewtrgv2o7ec2r
        foreign key (student_id) references students (id)
);





