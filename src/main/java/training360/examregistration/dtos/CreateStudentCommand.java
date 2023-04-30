package training360.examregistration.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
public class CreateStudentCommand {

    @Schema(description = "firstname of the student", example = "Anna")
    @NotBlank(message = "student must have firstname")
    private String firstName;

    @Schema(description = "lastname of the student", example = "Kiss")
    @NotBlank(message = "student must have lastname")
    private String lastName;

    @Schema(description = "list of exam subjects")
    @NotEmpty(message = "student must have at least one exam subject")
    private List<Subject> subjects = new ArrayList<>();



}
