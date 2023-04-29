package training360.examregistration.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import training360.examregistration.model.Subject;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StudentDtoWithoutRoom {

    @Schema(description = "id of the student")
    private Long id;

    @Schema(description = "firstname of the student")
    private String firstName;

    @Schema(description = "lastname of the student")
    private String lastName;

    @Schema(description = "list of exam subjects")
    private List<Subject> subjects = new ArrayList<>();

}
