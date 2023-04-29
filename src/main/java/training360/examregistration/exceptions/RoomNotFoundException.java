package training360.examregistration.exceptions;

public class RoomNotFoundException extends RuntimeException {

    public RoomNotFoundException() {
        super("Room is not found");
    }
}
