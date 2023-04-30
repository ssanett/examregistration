package training360.examregistration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import training360.examregistration.model.Student;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Long> {

    @Query("select s from Student s left join fetch s.room r where r.number = :roomNumber ")
    List<Student> findStudentsByRoom(String roomNumber);


    @Query("select s from Student s where s.firstName like concat('%',:namePart,'%') or s.lastName like concat('%',:namePart,'%')")
    List<Student> findStudentsByName(Optional<String> namePart);
}
