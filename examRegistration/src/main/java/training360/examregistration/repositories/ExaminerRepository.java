package training360.examregistration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import training360.examregistration.model.Examiner;

import java.util.Optional;


public interface ExaminerRepository extends JpaRepository<Examiner,Long> {


}
