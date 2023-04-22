package training360.examregistration.exceptions;

import training360.examregistration.model.Subject;

public class ExaminerSubjectNotValid extends RuntimeException {

    public ExaminerSubjectNotValid(Subject subject) {
        super(String.format("Examiners subject %s is not valid in the room",subject));
    }
}
