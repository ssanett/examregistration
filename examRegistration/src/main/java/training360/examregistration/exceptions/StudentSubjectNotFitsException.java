package training360.examregistration.exceptions;

public class StudentSubjectNotFitsException extends RuntimeException {

    public StudentSubjectNotFitsException() {
        super("Student's subject is not valid in the room");
    }
}
