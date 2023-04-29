package training360.examregistration.exceptions;

public class StudentIsNotFoundException extends RuntimeException {

    public StudentIsNotFoundException() {
        super("Student is not found");
    }

    public StudentIsNotFoundException(long id) {
        super("Student is not found in room with id: " + id);
    }
}
