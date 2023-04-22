package training360.examregistration.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import training360.examregistration.converter.Converter;
import training360.examregistration.dtos.*;
import training360.examregistration.exceptions.RoomNotFoundException;
import training360.examregistration.exceptions.StudentIsNotFoundException;
import training360.examregistration.exceptions.StudentSubjectNotFitsException;
import training360.examregistration.model.Room;
import training360.examregistration.model.Student;
import training360.examregistration.repositories.RoomRepository;
import training360.examregistration.repositories.StudentRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class StudentService {

    private StudentRepository studentRepository;
    private RoomRepository roomRepository;

    private Converter converter;


    public List<StudentDtoWithoutRoom> findStudentsByRoom(String roomNumber) {
        List<Student> students = studentRepository.findStudentsByRoom(roomNumber);
        return converter.studentToDto(students);
    }

    public StudentDto createStudent(CreateStudentCommand command) {
        Student student = Student.builder().
                firstName(command.getFirstName())
                .lastName(command.getLastName())
                .subjects(command.getSubjects()).build();
        return converter.toDto(studentRepository.save(student));
    }


    public void deleteStudentById(long studentId) {
        studentRepository.deleteById(studentId);
    }

    @Transactional
    public StudentDto updateRoomWithStudent(long roomId, CreateStudentToRoomCommand command) {
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        Student student = studentRepository.findById(command.getStudentId()).orElseThrow(StudentIsNotFoundException::new);
        checkStudentAndRoomSubject(student,room);
        room.addStudent(student);
        return converter.toDto(student);
    }


    @Transactional
    public StudentDto removeStudentFromRoom(long studentId, long roomId) {
        Student student = studentRepository.findById(studentId).orElseThrow(StudentIsNotFoundException::new);
        Room room = roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
        checkStudentInRoom(student, room);
        room.removeStudent(student);
        return converter.toDto(student);
    }

    public void checkStudentInRoom(Student student, Room room) {
        if (!room.getStudents().contains(student)) {
            throw new StudentIsNotFoundException(student.getId());
        }
    }

    private void checkStudentAndRoomSubject(Student student, Room room){
        if(room.getSubject()!=null && !student.getSubjects().contains(room.getSubject())){
            throw new StudentSubjectNotFitsException();
        }
    }

}
