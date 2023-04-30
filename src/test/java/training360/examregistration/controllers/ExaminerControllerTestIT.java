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
    subjects = List.of(Subject.LITERATURE, Subject.GERMAN, Subject.HISTORY);
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
            .uri(uriBuilder -> uriBuilder.path("/api/rooms/{roomId}").build(room.getId()))
            .bodyValue(new CreateRoomWithSubjectCommand(Subject.HISTORY))
            .exchange()
            .expectBody(RoomDto.class).returnResult().getResponseBody();

    ExaminerDto result = client.put()
            .uri(uriBuilder -> uriBuilder.path("/api/examiners/{roomId}")
                    .build(room.getId()))
            .bodyValue(new CreateExaminerToRoomCommand(examiner.getId()))
            .exchange()
            .expectStatus().isAccepted()
            .expectBody(ExaminerDto.class).returnResult().getResponseBody();

    assertEquals("1",result.getRoom().getNumber());
}

@Test
void testExaminerAndRoomSubjectNotFits(){
    client.put()
            .uri(uriBuilder -> uriBuilder.path("/api/rooms/{roomId}").build(room.getId()))
            .bodyValue(new CreateRoomWithSubjectCommand(Subject.MATH))
            .exchange()
            .expectBody(RoomDto.class).returnResult().getResponseBody();

    ProblemDetail problemDetail = client.put()
            .uri(uriBuilder -> uriBuilder.path("/api/examiners/{roomId}")
                    .build(room.getId()))
            .bodyValue(new CreateExaminerToRoomCommand(examiner.getId()))
            .exchange()
            .expectStatus().isEqualTo(406)
            .expectBody(ProblemDetail.class).returnResult().getResponseBody();

    assertEquals("Student's subject is not valid in the room", problemDetail.getDetail());


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

@Test
void findExaminersByNamePart(){
    client.post()
            .uri("/api/examiners")
            .bodyValue(new CreateExaminerCommand("X", "Y",subjects))
            .exchange()
            .expectStatus().isCreated()
            .expectBody(ExaminerDto.class).returnResult().getResponseBody();

    client.post()
            .uri("/api/examiners")
            .bodyValue(new CreateExaminerCommand("Anna", "Kovács",subjects))
            .exchange()
            .expectStatus().isCreated()
            .expectBody(ExaminerDto.class).returnResult().getResponseBody();

    List<ExaminerDto> result = client.get()
            .uri(uriBuilder -> uriBuilder.path("/api/examiners").queryParam("namePart","Kovács").build())
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(ExaminerDto.class).returnResult().getResponseBody();

    assertThat(result)
            .hasSize(1)
            .extracting(ExaminerDto::getFirstName)
            .containsExactlyInAnyOrder("Anna");
}

@Test
    void testRemoveExaminerFromRoom(){

    client.put()
            .uri(uriBuilder -> uriBuilder.path("/api/rooms/{roomId}").build(room.getId()))
            .bodyValue(new CreateRoomWithSubjectCommand(Subject.HISTORY))
            .exchange()
            .expectBody(RoomDto.class).returnResult().getResponseBody();

    ExaminerDto result = client.put()
            .uri(uriBuilder -> uriBuilder.path("/api/examiners/{roomId}")
                    .build(room.getId()))
            .bodyValue(new CreateExaminerToRoomCommand(examiner.getId()))
            .exchange()
            .expectStatus().isAccepted()
            .expectBody(ExaminerDto.class).returnResult().getResponseBody();

    assertEquals("1",result.getRoom().getNumber());

    client.put()
            .uri(uriBuilder -> uriBuilder.path("/api/examiners/remove/{examinerId}")
                    .build(result.getId()))
            .exchange()
            .expectStatus().isAccepted()
            .expectBody(ExaminerDto.class).returnResult().getResponseBody();

    assertNull(examiner.getRoom());

}

}