package training360.examregistration.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import training360.examregistration.dtos.*;
import training360.examregistration.services.StudentService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
@AllArgsConstructor
@Tag(name = "operations on students")
public class StudentController {

    private StudentService studentService;


    @GetMapping("/room/{roomNumber}")
    @Operation(summary = "find students", description = "find students by room number")
    public List<StudentDtoWithoutRoom> findStudentsByRoom(@PathVariable("roomNumber") String roomNumber) {
        return studentService.findStudentsByRoom(roomNumber);
    }

    @GetMapping
    @Operation(summary = "find all students")
    public List<StudentDto> findAllStudents(@RequestParam Optional<String> namePart){
        return studentService.findAllStudentsByName(namePart);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "create student", description = "create student without room")
    public StudentDto createStudent(@Valid @RequestBody CreateStudentCommand command) {
        return studentService.createStudent(command);
    }

    @PutMapping("/student/{roomId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "update room ", description = "update room with existing Student")
    public StudentDto updateRoomWithStudent(@PathVariable("roomId") long roomId, @RequestBody CreateStudentToRoomCommand command) {
        return studentService.updateRoomWithStudent(roomId, command);
    }


    @PutMapping("/remove/{roomId}/{studentId}/")
    @Operation(summary = "remove student from room", description = "remove student from room")
    public StudentDto removeStudentFromRoom(@PathVariable("studentId") long studentId, @PathVariable("roomId") long roomId) {
        return studentService.removeStudentFromRoom(studentId, roomId);
    }


    @DeleteMapping("/{studentId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "delete student", description = "delete student")
    public void deleteStudentById(@PathVariable long studentId) {
        studentService.deleteStudentById(studentId);
    }
}

