package training360.examregistration.exceptions;


public class RoomIsFullException extends RuntimeException {

    public RoomIsFullException() {
        super("No more place in this room.");
    }
}
