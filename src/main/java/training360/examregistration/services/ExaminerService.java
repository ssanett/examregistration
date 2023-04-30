package training360.examregistration.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import training360.examregistration.converter.Converter;
import training360.examregistration.dtos.*;
import training360.examregistration.exceptions.*;
import training360.examregistration.model.Examiner;
import training360.examregistration.model.Room;
import training360.examregistration.model.Subject;
import training360.examregistration.repositories.ExaminerRepository;
import training360.examregistration.repositories.RoomRepository;

import java.util.List;
import java.util.Optional;

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
        return converter.examinerToDto(examinerRepository.save(examiner));
    }

    public ExaminerDto findRoomByExaminerId(long examinerId) {
        Examiner examiner = examinerRepository.findById(examinerId).orElseThrow(ExaminerNotFoundException::new);
        return converter.examinerToDto(examiner);
    }

    public List<ExaminerDto> findAllExaminersByName(Optional<String> namePart) {
        if (namePart.isPresent()) {
            return converter.examinerToDto(examinerRepository.findExaminersByName(namePart));
        }
        return converter.examinerToDto(examinerRepository.findAll());
    }

    public void deleteExaminer(long examinerId) {
        Examiner examiner = examinerRepository.findById(examinerId).orElseThrow(ExaminerNotFoundException::new);
        examiner.setRoom(null);
        examinerRepository.delete(examiner);
    }

    public ExaminerDto removeExaminerFromRoom(long examinerId) {
        Examiner examiner = examinerRepository.findById(examinerId).orElseThrow(ExaminerNotFoundException::new);
        examiner.setRoom(null);
        return converter.examinerToDto(examinerRepository.save(examiner));
    }


    private boolean checkSubject(List<Subject> examinerSubjects, Subject roomSubject) {
        boolean result = false;
        for (Subject s : examinerSubjects) {
            if (s.equals(roomSubject)) {
                result = true;
            }
        }
        return result;
    }

    private void checkExaminer(Examiner examiner) {
        if (examiner.getRoom() != null) {
            throw new ExaminerAlreadyHasRoom(examiner.getRoom().getNumber());
        }
    }

    private void checkExaminerFitsRoom(Room room, Examiner examiner) {
        if (!checkSubject(examiner.getTaughtSubjects(), room.getSubject())) {
            throw new StudentSubjectNotFitsException();
        }
        checkExaminer(examiner);
    }

}
