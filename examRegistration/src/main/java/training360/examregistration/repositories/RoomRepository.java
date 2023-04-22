package training360.examregistration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import training360.examregistration.model.Room;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("select r from Room r left join fetch r.students where r.number = :number")
    Optional<Room> findByNumber(@Param("number") String number);


}
