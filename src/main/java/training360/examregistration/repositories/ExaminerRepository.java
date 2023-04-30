package training360.examregistration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import training360.examregistration.model.Examiner;

import java.util.List;
import java.util.Optional;


public interface ExaminerRepository extends JpaRepository<Examiner,Long> {

    @Query("select e from Examiner e where e.firstName like concat('%',:namePart,'%') or e.lastName like concat('%',:namePart,'%')")
    List<Examiner> findExaminersByName(Optional<String> namePart);
}
