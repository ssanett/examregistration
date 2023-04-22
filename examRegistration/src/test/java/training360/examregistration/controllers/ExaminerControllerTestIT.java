package training360.examregistration.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import training360.examregistration.dtos.*;
import training360.examregistration.model.Subject;
import training360.examregistration.services.ExaminerService;
import training360.examregistration.services.RoomService;
import static org.assertj.core.api.Assertions.assertThat;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = {"delete from student_subjects","delete from students", "delete from examiner_taught_subjects","delete  from examiners","delete  from rooms"})

class ExaminerControllerTestIT {

    @Autowired
    WebTestClient client;

    @Autowired
    ExaminerService examinerService;

    @Autowired
    RoomService roomService;

    List<Subject> subjects;
    RoomDto room;
    StudentDto student;
    ExaminerDto examiner;

    @BeforeEach
    void init(){
    subjects = List.of(Subject.LITERATURE, Subject.GERMAN);
    room = client.post()
            .uri("/api/rooms/create")
                .bodyValue(new CreateRoomCommand("1", 10))
            .exchange()
                .expectStatus().isCreated()
                .expectBody(RoomDto.class).returnResult().getResponseBody();
    student = client.post()
            .uri("/api/students")
            .bodyValue(new CreateStudentCommand("Béla", "Nagy", subjects))
            .exchange()
            .expectBody(StudentDto.class).returnResult().getResponseBody();
    examiner = client.post()
            .uri("/api/examiners")
            .bodyValue(new CreateExaminerCommand("Márta", "Horváth",subjects))
            .exchange()
            .expectStatus().isCreated()
            .expectBody(ExaminerDto.class).returnResult().getResponseBody();
}


    @Test
    void testCreateExaminer(){

        ExaminerDto examinerDto = client.post()
                .uri("/api/examiners")
                .bodyValue(new CreateExaminerCommand("X", "Y",subjects))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ExaminerDto.class).returnResult().getResponseBody();

        assertEquals("X", examinerDto.getFirstName());

    }

    @Test
    void testCreateExaminerWithoutFirstName(){
        ProblemDetail problemDetail = client.post()
                .uri("/api/examiners")
                .bodyValue(new CreateExaminerCommand("", "C",subjects))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals("Examiner must have firstname", problemDetail.getDetail());
    }

    @Test
    void testCreateExaminerWithoutLastName(){
        ProblemDetail problemDetail = client.post()
                .uri("/api/examiners")
                .bodyValue(new CreateExaminerCommand("A", "",subjects))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals("Examiner must have lastname", problemDetail.getDetail());
    }


@Test
    void testUpdateExaminerRoom(){

    client.put()
            .uri(uriBuilder -> uriBuilder.path("/api/examiners/{examinerId}")
                    .build(examiner.getId()))
            .bodyValue(new CreateRoomToExaminer(room.getId()))
            .exchange()
            .expectStatus().isAccepted()
            .expectBody(ExaminerDto.class).returnResult().getResponseBody();

    ExaminerDto result =    client.get()
            .uri(uriBuilder -> uriBuilder.path("/api/examiners/{examinerId}")
                    .build(examiner.getId()))
            .exchange()
            .expectStatus().isOk()
            .expectBody(ExaminerDto.class).returnResult().getResponseBody();

    assertEquals("1",result.getRoom().getNumber());
}

@Test
    void testFindExaminers(){
    client.post()
            .uri("/api/examiners")
            .bodyValue(new CreateExaminerCommand("X", "Y",subjects))
            .exchange()
            .expectStatus().isCreated()
            .expectBody(ExaminerDto.class).returnResult().getResponseBody();

    List<ExaminerDto> result = client.get()
            .uri("/api/examiners")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(ExaminerDto.class).returnResult().getResponseBody();

    assertThat(result)
            .hasSize(2)
            .extracting(ExaminerDto::getFirstName)
            .containsExactlyInAnyOrder("X","Márta");

}

}