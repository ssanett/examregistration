package training360.examregistration.converter;

import org.mapstruct.Mapper;
import training360.examregistration.dtos.*;
import training360.examregistration.model.Examiner;
import training360.examregistration.model.Room;
import training360.examregistration.model.Student;

import java.util.List;

@Mapper(componentModel = "spring")
public interface Converter {

    RoomDto toDto(Room room);


    StudentDto toDto(Student student);
    List<StudentDto> toDto(List<Student> students);


    List<StudentDtoWithoutRoom> studentToDto(List<Student> students);

    ExaminerDto examinerToDto(Examiner examiner);
    List<ExaminerDto> examinerToDto(List<Examiner> examiners);

    List<RoomDto> roomToDto(List<Room> rooms);
}
