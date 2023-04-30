package training360.examregistration.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import training360.examregistration.dtos.*;

import training360.examregistration.services.RoomService;

import java.util.List;


@RestController
@RequestMapping("/api/rooms")
@AllArgsConstructor
@Tag(name = "operations on rooms")
public class RoomController {

    private RoomService roomService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "create room", description = "create room without examiners and students")
    public RoomDto createRoom(@Valid @RequestBody CreateRoomCommand command) {
        return roomService.createRoom(command);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "find all rooms", description = "find all rooms with students")
    public List<RoomDto> findRooms() {
        return roomService.findRooms();
    }

    @GetMapping("/{number}")
    @Operation(summary = "find room by number", description = "find room by number")
    public RoomDto findRoomByNumber(@Positive @PathVariable String number) {
        return roomService.findRoomByNumber(number);
    }

    @PutMapping("/{roomId}")
    @Operation(summary = "update room with subject", description = "update room with subject")
    public RoomDto updateRoomWithSubject(@PathVariable("roomId") long roomId, @RequestBody CreateRoomWithSubjectCommand command) {
        return roomService.updateRoomWithSubject(roomId, command);
    }


    @DeleteMapping("/{number}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "delete room", description = "delete room by number")
    public void deleteRoomByNumber(@PathVariable String number) {
        roomService.deleteRoomByNumber(number);
    }


    @DeleteMapping("/delete/{roomId}")
    @Operation(summary = "delete room", description = "delete room by id from database")
    public void deleteRoomById(@PathVariable long roomId) {
        roomService.deleteRoomById(roomId);
    }


}
