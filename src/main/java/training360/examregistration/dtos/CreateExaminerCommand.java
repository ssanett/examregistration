package training360.examregistration.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import training360.examregistration.model.Subject;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateExaminerCommand {

    @Schema(description = "firstname of the examiner", example = "Anna")
    @NotBlank(message = "Examiner must have firstname")
    private String firstName;

    @Schema(description = "lastname of the examiner", example = "Kiss")
    @NotBlank(message = "Examiner must have lastname")
    private String lastName;

    @Schema(description = "list of subject taught by the examiner")
    private List<Subject> taughtSubjects = new ArrayList<>();


}
