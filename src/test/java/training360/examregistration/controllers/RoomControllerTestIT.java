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

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = {"delete from student_subjects", "delete from students", "delete from examiner_taught_subjects", "delete  from examiners", "delete  from rooms"})
class RoomControllerTestIT {

    @Autowired
    WebTestClient client;


    @Autowired
    RoomService roomService;

    RoomDto room;

    @BeforeEach
    void init() {
        room = client.post()
                .uri("/api/rooms/create")
                .bodyValue(new CreateRoomCommand("1", 10))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(RoomDto.class).returnResult().getResponseBody();

    }


    @Test
    void testCreateRoom() {
        RoomDto result = client.post()
                .uri("/api/rooms/create")
                .bodyValue(new CreateRoomCommand("2", 10))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(RoomDto.class).returnResult().getResponseBody();

        assertEquals("2", result.getNumber());
        assertEquals(10, result.getCapacity());
        assertNotNull(result.getId());

    }

    @Test
    void testCreateRoomExistedNumber() {

        ProblemDetail problemDetail = client.post()
                .uri("/api/rooms/create")
                .bodyValue(new CreateRoomCommand("1", 10))
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals(URI.create("examregistration/invalid_request"), problemDetail.getType());
        assertEquals(String.format("Room number %s is already exist.", 1), problemDetail.getDetail());
    }

    @Test
    void testCreateRoomWrongNumber() {
        ProblemDetail problemDetail = client.post()
                .uri("/api/rooms/create")
                .bodyValue(new CreateRoomCommand("-4", 10))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals(URI.create("examregistration/invalid_request"), problemDetail.getType());
        assertEquals("room number must be positive", problemDetail.getDetail());
    }


    @Test
    void testCreateRoomWithWrongCapacityMoreThan20() {
        ProblemDetail problemDetail = client.post()
                .uri("/api/rooms/create")
                .bodyValue(new CreateRoomCommand("3", 100))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals(URI.create("examregistration/invalid_request"), problemDetail.getType());
        assertEquals("capacity must be less than 20", problemDetail.getDetail());
    }

    @Test
    void testCreateRoomWithWrongCapacityLessThanExpected() {
        ProblemDetail problemDetail = client.post()
                .uri("/api/rooms/create")
                .bodyValue(new CreateRoomCommand("4", 1))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals(URI.create("examregistration/invalid_request"), problemDetail.getType());
        assertEquals("capacity must be more than 5", problemDetail.getDetail());
    }

    @Test
    void testUpdateRoomWithSubject() {

        RoomDto saved = client.put()
                .uri(uriBuilder -> uriBuilder.path("/api/rooms/{roomId}").build(room.getId()))
                .bodyValue(new CreateRoomWithSubjectCommand(Subject.HISTORY))
                .exchange()
                .expectBody(RoomDto.class).returnResult().getResponseBody();

        assertEquals(Subject.HISTORY, saved.getSubject());
    }

    @Test
    void testUpdateRoomWithSubjectRoomNotFound() {
        ProblemDetail problemDetail = client.put()
                .uri(uriBuilder -> uriBuilder.path("/api/rooms/{roomId}").build(0))
                .bodyValue(new CreateRoomWithSubjectCommand(Subject.HISTORY))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals(URI.create("rooms/room_is_not_found"), problemDetail.getType());
        assertEquals("Room is not found", problemDetail.getDetail());
    }


    @Test
    void testFindRoomByNumber() {
        RoomDto result = client.get()
                .uri(uriBuilder -> uriBuilder.path("/api/rooms/{number}").build(room.getNumber()))
                .exchange()
                .expectStatus().isOk()
                .expectBody(RoomDto.class).returnResult().getResponseBody();

        List<RoomDto> rooms = client.get()
                .uri("/api/rooms")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(RoomDto.class).returnResult().getResponseBody();
        assertEquals(10, result.getCapacity());
        assertThat(rooms).extracting(RoomDto::getCapacity).containsExactly(10);
        assertThat(rooms).extracting(RoomDto::getId).containsExactly(room.getId());
    }

    @Test
    void testRoomNotFound() {
        ProblemDetail problemDetail = client.get()
                .uri(uriBuilder -> uriBuilder.path("/api/rooms/{number}").build(2))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals(URI.create("rooms/room_is_not_found"), problemDetail.getType());
        assertEquals("Room is not found", problemDetail.getDetail());
    }

    @Test
    void testFindRooms() {
        client.post()
                .uri("/api/rooms/create")
                .bodyValue(new CreateRoomCommand("2", 10))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(RoomDto.class).returnResult().getResponseBody();

        List<RoomDto> result = client.get()
                .uri(uriBuilder -> uriBuilder.path("/api/rooms").build(room.getNumber()))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(RoomDto.class).returnResult().getResponseBody();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(RoomDto::getNumber).containsExactly("1", "2");
    }

    @Test
    void testDeleteRoom() {
        client.delete()
                .uri(uriBuilder -> uriBuilder.path("/api/rooms/{number}").build(room.getNumber()))
                .exchange()
                .expectStatus().isAccepted();

        ProblemDetail problemDetail = client.get()
                .uri(uriBuilder -> uriBuilder.path("/api/rooms/{number}").build(room.getNumber()))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals("Room is not found", problemDetail.getDetail());
    }

    @Test
    void testDeleteRoomNumberNotFound() {
        ProblemDetail problemDetail = client.delete()
                .uri(uriBuilder -> uriBuilder.path("/api/rooms/{number}").build(0))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals("Room is not found", problemDetail.getDetail());

    }

    @Test
    void testDeleteRoomById() {
        client.delete()
                .uri(uriBuilder -> uriBuilder.path("/api/rooms/delete/{roomId}").build(room.getId()))
                .exchange()
                .expectStatus().isOk();

        ProblemDetail problemDetail = client.get()
                .uri(uriBuilder -> uriBuilder.path("/api/rooms/{number}").build(room.getNumber()))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals("Room is not found", problemDetail.getDetail());
    }


}