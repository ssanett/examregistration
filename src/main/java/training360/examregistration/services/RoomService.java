package training360.examregistration.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import training360.examregistration.converter.Converter;
import training360.examregistration.dtos.*;
import training360.examregistration.exceptions.RoomIsAlreadyExistException;
import training360.examregistration.exceptions.RoomNotFoundException;
import training360.examregistration.model.Examiner;
import training360.examregistration.model.Room;
import training360.examregistration.model.Student;
import training360.examregistration.repositories.ExaminerRepository;
import training360.examregistration.repositories.RoomRepository;
import java.util.List;

@Service
@AllArgsConstructor

public class RoomService {

    private RoomRepository roomRepository;
    private ExaminerRepository examinerRepository;
    private Converter converter;


    public RoomDto createRoom(CreateRoomCommand command) {
        checkRoomNumber(command.getNumber());
        Room room = Room.builder()
                .capacity(command.getCapacity())
                .number(command.getNumber())
                .build();
        return converter.toDto(roomRepository.save(room));
    }

    @Transactional
    public RoomDto updateRoomWithSubject(long roomId, CreateRoomWithSubjectCommand command) {
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        room.setSubject(command.getSubject());
        return converter.toDto(roomRepository.save(room));
    }


    public RoomDto findRoomByNumber(String number) {
        Room room = roomRepository.findByNumber(number).orElseThrow(RoomNotFoundException::new);
        return converter.toDto(room);
    }

    @Transactional
    public void deleteRoomByNumber(String number) {
        Room room = roomRepository.findByNumber(number).orElseThrow(RoomNotFoundException::new);
        List<Student> students = room.getStudents();
        for (Student s : students) {
            s.setRoom(null);
        }
        deleteExaminerFromRoom(room);
        roomRepository.deleteById(room.getId());
    }

    public List<RoomDto> findRooms() {
        return converter.roomToDto(roomRepository.findAll());
    }

    @Transactional
    public void deleteRoomById(long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        List<Student> students = room.getStudents();
        for (Student s : students) {
            s.setRoom(null);
        }
        deleteExaminerFromRoom(room);
        roomRepository.deleteById(roomId);
    }


    private void checkRoomNumber(String number) {
        List<String> numbers = roomRepository.findAll().stream().map(Room::getNumber).toList();
        for (String n : numbers) {
            if (n.equals(number)) {
                throw new RoomIsAlreadyExistException(number);
            }
        }
    }

    private void deleteExaminerFromRoom(Room room) {
        List<Examiner> examiners = examinerRepository.findAll();
        for (Examiner e : examiners) {
            if (e.getRoom() != null && e.getRoom().getId().equals(room.getId())) {
                e.setRoom(null);
            }
        }
    }
}
