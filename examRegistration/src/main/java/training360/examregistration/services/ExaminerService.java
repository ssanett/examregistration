package training360.examregistration.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import training360.examregistration.converter.Converter;
import training360.examregistration.dtos.CreateExaminerCommand;
import training360.examregistration.dtos.CreateExaminerToRoomCommand;
import training360.examregistration.dtos.CreateRoomToExaminer;
import training360.examregistration.dtos.ExaminerDto;
import training360.examregistration.exceptions.*;
import training360.examregistration.model.Examiner;
import training360.examregistration.model.Room;
import training360.examregistration.model.Subject;
import training360.examregistration.repositories.ExaminerRepository;
import training360.examregistration.repositories.RoomRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ExaminerService {

    private ExaminerRepository examinerRepository;
    private RoomRepository roomRepository;
    private Converter converter;

    public ExaminerDto createExaminer(CreateExaminerCommand command) {

        Examiner examiner = Examiner.builder()
                .firstName(command.getFirstName())
                .lastName(command.getLastName())
                .taughtSubjects(command.getTaughtSubjects())
                .build();

        return converter.examinerToDto(examinerRepository.save(examiner));
    }

    @Transactional
    public ExaminerDto updateExaminerByRoomName(long roomId, CreateExaminerToRoomCommand command) {
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        Examiner examiner = examinerRepository.findById(command.getExaminerId()).orElseThrow(ExaminerNotFoundException::new);
        checkExaminerFitsRoom(room, examiner);
        examiner.setRoom(room);
        examinerRepository.save(examiner);
        return converter.examinerToDto(examinerRepository.save(examiner));
    }


    public ExaminerDto updateRoomByExaminerId(long examinerId, CreateRoomToExaminer command) {
        Examiner examiner = examinerRepository.findById(examinerId).orElseThrow(ExaminerNotFoundException::new);
        Room room = roomRepository.findById(command.getRoomId()).orElseThrow(RoomNotFoundException::new);
        examiner.setRoom(room);
        return converter.examinerToDto(examinerRepository.save(examiner));

    }

    public ExaminerDto findRoomByExaminerId(long examinerId) {
        Examiner examiner =  examinerRepository.findById(examinerId).orElseThrow(ExaminerNotFoundException::new);
        return converter.examinerToDto(examiner);
    }

    public List<ExaminerDto> findExaminers() {
        return converter.examinerToDto(examinerRepository.findAll());
    }

    public void deleteExaminer(long examinerId) {
        Examiner examiner = examinerRepository.findById(examinerId).orElseThrow(ExaminerNotFoundException::new);
        examiner.setRoom(null);
        examinerRepository.delete(examiner);
    }



    private void checkSubject(List<Subject> examinerSubjects, Subject roomSubject) {
        for (Subject s : examinerSubjects) {
            if (!s.equals(roomSubject)) {
                throw new ExaminerSubjectNotValid(roomSubject);
            }
        }
    }


    private void checkExaminer(Examiner examiner) {
        if (examiner.getRoom() != null) {
            throw new ExaminerAlreadyHasRoom(examiner.getRoom().getNumber());
        }
    }

    private void checkExaminerFitsRoom(Room room, Examiner examiner) {
        checkSubject(examiner.getTaughtSubjects(), room.getSubject());
        checkExaminer(examiner);
    }

}
