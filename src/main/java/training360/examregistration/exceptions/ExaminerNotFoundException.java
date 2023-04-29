package training360.examregistration.exceptions;

public class ExaminerNotFoundException extends RuntimeException{

    public ExaminerNotFoundException(String firstName,String lastName) {
        super(String.format("Examiner not found: %s %s",firstName,lastName));
    }

    public ExaminerNotFoundException() {
        super("Examiner not found");
    }
}
