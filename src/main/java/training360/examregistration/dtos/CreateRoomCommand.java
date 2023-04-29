package training360.examregistration.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoomCommand {

    @Schema(description = "room number of the room")
    @Positive(message = "room number must be positive")
    private String number;

    @Schema(description = "capacity of the room")
    @Max(value = 20, message = "capacity must be less than 20")
    @Min(value = 5, message = "capacity must be more than 5")
    private int capacity;

    private List<StudentDto> students = new ArrayList<>();

    public CreateRoomCommand(String number, int capacity) {
        this.number = number;
        this.capacity = capacity;
    }
}
