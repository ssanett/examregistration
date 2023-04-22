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
import training360.examregistration.dtos.CreateRoomToExaminer;
import training360.examregistration.dtos.ExaminerDto;
import training360.examregistration.services.ExaminerService;

import java.util.List;


@RestController
@RequestMapping("/api/examiners")
@AllArgsConstructor
@Tag(name = "operations on examiners")
public class ExaminerController {

    private ExaminerService examinerService;


    @PostMapping
    @Operation(summary = "create examiner", description = "create examiner")
    @ResponseStatus(HttpStatus.CREATED)
    private ExaminerDto createExaminer(@Valid @RequestBody CreateExaminerCommand command) {
        return examinerService.createExaminer(command);
    }

    @PutMapping("/room/{roomId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "update the room of examiner", description = "update the room of examiner")
    private ExaminerDto updateExaminerRoom(@PathVariable("roomId") long roomId, @RequestBody CreateExaminerToRoomCommand command) {
        return examinerService.updateExaminerByRoomName(roomId, command);
    }

    @PutMapping("/{examinerId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "update the room of examiner", description = "update the room of examiner")
    private ExaminerDto updateExaminerRoomExaminer(@PathVariable("examinerId") long examinerId, @RequestBody CreateRoomToExaminer command) {
        return examinerService.updateRoomByExaminerId(examinerId, command);
    }



    @GetMapping("/{examinerId}")
    @Operation(summary = "find examiner by room", description = "find examiner by room")
    private ExaminerDto findExaminersByRoomNumber(@Positive @PathVariable long examinerId){
        return examinerService.findRoomByExaminerId(examinerId);
    }

    @GetMapping
    @Operation(summary = "find examiners")
    private List<ExaminerDto> findExaminers(){
        return examinerService.findExaminers();
    }

    @DeleteMapping("/{examinerId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "delete idr", description = "delete examiner by  id")
    private void deleteExaminer(@PathVariable long examinerId){
        examinerService.deleteExaminer(examinerId);
    }

}
