package training360.examregistration.exceptions;

public class RoomIsAlreadyExistException extends RuntimeException {
    public RoomIsAlreadyExistException(String number) {
        super(String.format("Room number %s is already exist.",number));
    }
}
