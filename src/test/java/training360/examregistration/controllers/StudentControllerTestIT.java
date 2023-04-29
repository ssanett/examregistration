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
import training360.examregistration.services.RoomService;
import training360.examregistration.services.StudentService;
import static org.assertj.core.api.Assertions.assertThat;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = {"delete from student_subjects", "delete from students", "delete from examiner_taught_subjects", "delete  from examiners", "delete  from rooms"})
class StudentControllerTestIT {

    @Autowired
    WebTestClient client;

    @Autowired
    StudentService studentService;

    @Autowired
    RoomService roomService;

    RoomDto room;
    StudentDto student;
    List<Subject> subjects;

    @BeforeEach
    void init() {
        room = client.post()
                .uri("/api/rooms/create")
                .bodyValue(new CreateRoomCommand("15", 10))
                .exchange()
                .expectBody(RoomDto.class).returnResult().getResponseBody();

        subjects = List.of(Subject.LITERATURE, Subject.GERMAN);
        student = client.post()
                .uri("/api/students")
                .bodyValue(new CreateStudentCommand("Anna", "Kiss", subjects))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(StudentDto.class).returnResult().getResponseBody();
    }

    @Test
    void testCreateStudent() {

        StudentDto studentDto = client.post()
                .uri("/api/students")
                .bodyValue(new CreateStudentCommand("Béla", "Nagy", subjects))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(StudentDto.class).returnResult().getResponseBody();

        assertEquals("Béla", studentDto.getFirstName());
        assertEquals(2, studentDto.getSubjects().size());
    }

    @Test
    void testCreateStudentWithoutFirstName() {
        ProblemDetail problemDetail = client.post()
                .uri("/api/students")
                .bodyValue(new CreateStudentCommand("", "Nagy", subjects))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals("student must have firstname", problemDetail.getDetail());
    }

    @Test
    void testCreateStudentWithoutLastName() {
        ProblemDetail problemDetail = client.post()
                .uri("/api/students")
                .bodyValue(new CreateStudentCommand("Béla", "", subjects))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals("student must have lastname", problemDetail.getDetail());
    }

    @Test
    void testCreateStudentWithoutSubject() {
        ProblemDetail problemDetail = client.post()
                .uri("/api/students")
                .bodyValue(new CreateStudentCommand("Béla", "Nagy", null))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals("student must have at least one exam subject", problemDetail.getDetail());
    }

    @Test
    void testFindStudentsByRoomNumber() {
        RoomDto roomDto = client.post()
                .uri("/api/rooms/create")
                .bodyValue(new CreateRoomCommand("5", 13))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(RoomDto.class).returnResult().getResponseBody();

        StudentDto one = client.post()
                .uri("/api/students")
                .bodyValue(new CreateStudentCommand("Alma", "Nagy", subjects))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(StudentDto.class).returnResult().getResponseBody();

        client.put()
                .uri(uriBuilder -> uriBuilder.path("/api/students/student/{roomId}").build(roomDto.getId()))
                .bodyValue(new CreateStudentToRoomCommand(one.getId()))
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(StudentDto.class).returnResult().getResponseBody();

        StudentDto studentDto = client.post()
                .uri("/api/students")
                .bodyValue(new CreateStudentCommand("Béla", "Nagy", subjects))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(StudentDto.class).returnResult().getResponseBody();

        client.put()
                .uri(uriBuilder -> uriBuilder.path("/api/students/student/{roomId}").build( roomDto.getId()))
                .bodyValue(new CreateStudentToRoomCommand(studentDto.getId()))
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(StudentDto.class).returnResult().getResponseBody();
        List<StudentDto> result =
                client.get()
                        .uri(uriBuilder -> uriBuilder.path("api/students/room/{roomNumber}").build(roomDto.getNumber()))
                        .exchange()
                        .expectBodyList(StudentDto.class).returnResult().getResponseBody();

        assertEquals(2, result.size());
    }


    @Test
    void testUpdateRoomWithStudentCommand() {

        StudentDto anotherStudent = client.post()
                .uri("/api/students")
                .bodyValue(new CreateStudentCommand("Béla", "Nagy", subjects))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(StudentDto.class).returnResult().getResponseBody();

        client.put()
                .uri(uriBuilder -> uriBuilder.path("/api/students/student/{roomId}").build(room.getId()))
                .bodyValue(new CreateStudentToRoomCommand(anotherStudent.getId()))
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(RoomDto.class).returnResult().getResponseBody();

        List<StudentDto> result = client.get().uri(uriBuilder -> uriBuilder.path("api/students/room/{roomNumber}").build(room.getNumber()))
                .exchange()
                .expectBodyList(StudentDto.class).returnResult().getResponseBody();

        assertEquals(1,result.size());
        assertThat(result).extracting(StudentDto::getFirstName).containsExactly("Béla");
    }

    @Test
    void testUpdateRoomWithStudentWrongRoomId(){

        StudentDto anotherStudent = client.post()
                .uri("/api/students")
                .bodyValue(new CreateStudentCommand("Béla", "Nagy", subjects))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(StudentDto.class).returnResult().getResponseBody();

       ProblemDetail problemDetail = client.put()
                .uri(uriBuilder -> uriBuilder.path("/api/students/student/{roomId}").build(0))
                .bodyValue(new CreateStudentToRoomCommand(anotherStudent.getId()))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals("Room is not found", problemDetail.getDetail());
    }

    @Test
    void testUpdateRoomWithStudentWrongStudentId(){

        ProblemDetail problemDetail = client.put()
                .uri(uriBuilder -> uriBuilder.path("/api/students/student/{roomId}").build(room.getId()))
                .bodyValue(new CreateStudentToRoomCommand(0))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals("Student is not found", problemDetail.getDetail());

    }

    @Test
    void testUpdateRoomWithStudentWrongSubject(){

        client.put()
                .uri(uriBuilder -> uriBuilder.path("/api/rooms/{roomId}").build(room.getId()))
                .bodyValue(new CreateRoomWithSubjectCommand(Subject.HISTORY))
                .exchange()
                .expectBody(RoomDto.class).returnResult().getResponseBody();


        ProblemDetail problemDetail = client.put()
                .uri(uriBuilder -> uriBuilder.path("/api/students/student/{roomId}").build(room.getId()))
                .bodyValue(new CreateStudentToRoomCommand(student.getId()))
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals("Student's subject is not valid in the room", problemDetail.getDetail());

    }


    @Test
    void testDeleteStudent() {
        client.delete()
                .uri(uriBuilder -> uriBuilder.path("/api/students/{studentId}").build(student.getId()))
                .exchange()
                .expectStatus().isAccepted();
    }

    @Test
    void testRemoveStudentFromRoom(){
        StudentDto anotherStudent = client.post()
                .uri("/api/students")
                .bodyValue(new CreateStudentCommand("Béla", "Nagy", subjects))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(StudentDto.class).returnResult().getResponseBody();

        client.put()
                .uri(uriBuilder -> uriBuilder.path("/api/students/student/{roomId}").build(room.getId()))
                .bodyValue(new CreateStudentToRoomCommand(anotherStudent.getId()))
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(RoomDto.class).returnResult().getResponseBody();


        client.put()
                .uri(uriBuilder -> uriBuilder.path("/api/students/student/{roomId}").build(room.getId()))
                .bodyValue(new CreateStudentToRoomCommand(student.getId()))
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(RoomDto.class).returnResult().getResponseBody();

        List<StudentDto> result = client.get().uri(uriBuilder -> uriBuilder.path("api/students/room/{roomNumber}").build(room.getNumber()))
                .exchange()
                .expectBodyList(StudentDto.class).returnResult().getResponseBody();

        client.put()
                .uri(uriBuilder -> uriBuilder.path("/api/students/remove/{roomId}/{studentId}/").build(room.getId(),student.getId()))
                .exchange()
                .expectStatus().isOk()
                .returnResult(StudentDto.class).getResponseBody();

        List<StudentDto> expected = client.get().uri(uriBuilder -> uriBuilder.path("api/students/room/{roomNumber}").build(room.getNumber()))
                .exchange()
                .expectBodyList(StudentDto.class).returnResult().getResponseBody();

        assertEquals(2,result.size());
        assertEquals(1,expected.size());
        assertThat(expected).extracting(StudentDto::getFirstName)
                .containsExactly("Béla");

    }

    @Test
    void testRemoveStudentNotInRoom(){
        StudentDto anotherStudent = client.post()
                .uri("/api/students")
                .bodyValue(new CreateStudentCommand("Béla", "Nagy", subjects))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(StudentDto.class).returnResult().getResponseBody();

        client.put()
                .uri(uriBuilder -> uriBuilder.path("/api/students/student/{roomId}").build(room.getId()))
                .bodyValue(new CreateStudentToRoomCommand(anotherStudent.getId()))
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(RoomDto.class).returnResult().getResponseBody();



        ProblemDetail problemDetail = client.put()
                .uri(uriBuilder -> uriBuilder.path("/api/students/remove/{roomId}/{studentId}/").build(room.getId(),student.getId()))
                .exchange()
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals("Student is not found in room with id: " + student.getId(), problemDetail.getDetail());

    }

    @Test
    void testRemoveStudentWithWrongRoomId(){

        StudentDto anotherStudent = client.post()
                .uri("/api/students")
                .bodyValue(new CreateStudentCommand("Béla", "Nagy", subjects))
                .exchange()
                .expectBody(StudentDto.class).returnResult().getResponseBody();

        client.put()
                .uri(uriBuilder -> uriBuilder.path("/api/students/student/{roomId}").build(room.getId()))
                .bodyValue(new CreateStudentToRoomCommand(anotherStudent.getId()))
                .exchange()
                .expectBody(RoomDto.class).returnResult().getResponseBody();

        ProblemDetail problemDetail = client.put()
                .uri(uriBuilder -> uriBuilder.path("/api/students/remove/{roomId}/{studentId}/").build(0,student.getId()))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals("Room is not found", problemDetail.getDetail());
    }
}