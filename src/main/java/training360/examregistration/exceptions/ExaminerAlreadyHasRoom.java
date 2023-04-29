package training360.examregistration.exceptions;

import jakarta.validation.constraints.NotNull;

public class ExaminerAlreadyHasRoom extends RuntimeException {
    public ExaminerAlreadyHasRoom(@NotNull String number) {
        super(String.format("Examiner is already in room %s",number));
    }
}
