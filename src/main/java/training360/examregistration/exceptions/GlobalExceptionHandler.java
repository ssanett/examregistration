package training360.examregistration.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException e){
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        detail.setType(URI.create("examregistration/invalid_request"));
        detail.setDetail(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
        return detail;}

    @ExceptionHandler(RoomIsFullException.class)
    public ProblemDetail handleValidationException(RoomIsFullException e){
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        detail.setType(URI.create("rooms/room_is_full"));
        return detail;}

    @ExceptionHandler(StudentIsNotFoundException.class)
    public ProblemDetail handleValidationException(StudentIsNotFoundException e){
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        detail.setType(URI.create("students/student_is_not_found"));
        return detail;}

    @ExceptionHandler(RoomNotFoundException.class)
    public ProblemDetail handleValidationException(RoomNotFoundException e){
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        detail.setType(URI.create("rooms/room_is_not_found"));
        detail.setDetail(Objects.requireNonNull(e.getMessage()));
        return detail;}


    @ExceptionHandler(ExaminerSubjectNotValid.class)
    public ProblemDetail handleValidationException(ExaminerSubjectNotValid e){
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        detail.setType(URI.create("examiners/subject_is_not_valid"));
        detail.setDetail(Objects.requireNonNull(e.getMessage()));
        return detail;}

    @ExceptionHandler(ExaminerNotFoundException.class)
    public ProblemDetail handleValidationException(ExaminerNotFoundException e){
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        detail.setType(URI.create("examiners/examiner_not_found"));
        detail.setDetail(Objects.requireNonNull(e.getMessage()));
        return detail;}

    @ExceptionHandler(RoomIsAlreadyExistException.class)
    public ProblemDetail handleValidationException(RoomIsAlreadyExistException e){
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        detail.setType(URI.create("examregistration/invalid_request"));
        detail.setDetail(Objects.requireNonNull(e.getMessage()));
        return detail;}

    @ExceptionHandler(StudentSubjectNotFitsException.class)
    public ProblemDetail handleValidationException(StudentSubjectNotFitsException e){
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        detail.setDetail(Objects.requireNonNull(e.getMessage()));
        detail.setType(URI.create("students/studentsubject_is_not_valid"));
        return detail;}

}
