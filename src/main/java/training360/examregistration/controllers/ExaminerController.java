package training360.examregistration.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import training360.examregistration.dtos.CreateExaminerCommand;
import training360.examregistration.dtos.CreateExaminerToRoomCommand;
import training360.examregistration.dtos.ExaminerDto;
import training360.examregistration.services.ExaminerService;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/examiners")
@AllArgsConstructor
@Tag(name = "operations on examiners")
public class ExaminerController {

    private ExaminerService examinerService;


    @PostMapping
    @Operation(summary = "create examiner", description = "create examiner")
    @ResponseStatus(HttpStatus.CREATED)
    public ExaminerDto createExaminer(@Valid @RequestBody CreateExaminerCommand command) {
        return examinerService.createExaminer(command);
    }

    @PutMapping("/remove/{examinerId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "delete examiner from room", description = "delete examiner from room by id")
    public ExaminerDto removeExaminerFromRoom(@PathVariable("examinerId") long examinerId) {
        return examinerService.removeExaminerFromRoom(examinerId);
    }

    @PutMapping("/{roomId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "update the room of examiner", description = "update the room of examiner")
    public ExaminerDto updateExaminerRoom(@PathVariable("roomId") long roomId, @RequestBody CreateExaminerToRoomCommand command) {
        return examinerService.updateExaminerByRoomName(roomId, command);
    }


    @GetMapping("/{examinerId}")
    @Operation(summary = "find examiner by id", description = "find examiner by id")
    public ExaminerDto findExaminersByRoomNumber(@Positive @PathVariable long examinerId) {
        return examinerService.findRoomByExaminerId(examinerId);
    }

    @GetMapping
    @Operation(summary = "find all examiners", description = "find examiners by giving part of their name")
    public List<ExaminerDto> findAllExaminers(@RequestParam Optional<String> namePart) {
        return examinerService.findAllExaminersByName(namePart);
    }

    @DeleteMapping("/{examinerId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "delete examiner by id", description = "delete examiner by  id")
    public void deleteExaminer(@PathVariable long examinerId) {
        examinerService.deleteExaminer(examinerId);
    }

}
