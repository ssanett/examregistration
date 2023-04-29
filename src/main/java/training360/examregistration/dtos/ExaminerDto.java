package training360.examregistration.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import training360.examregistration.model.Subject;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ExaminerDto {

    @Schema(description = "examiner's id")
    private Long id;

    @Schema(description = "firstname of the examiner")
    private String firstName;

    @Schema(description = "lastname of the examiner")
    private String lastName;

    @Schema(description = "list of taught subjects by the examiner")
    private List<Subject> taughtSubjects = new ArrayList<>();

    @Schema(description = "number of the exam room")
    private RoomDto room;

}
