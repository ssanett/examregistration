package training360.examregistration.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import training360.examregistration.model.Subject;


import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RoomDto {

    @Schema(description = "id of the room")
    private Long id;

    @Schema(description = "number of the room")
    private String number;

    @Schema(description = "capacity of the room")
    private int capacity;

    @Schema(description = "exam subject in the room")
    private Subject subject;

    @Schema(description = "list of the students in the room")
    private List<StudentDto> students;

}
