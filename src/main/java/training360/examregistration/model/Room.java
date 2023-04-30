package training360.examregistration.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import training360.examregistration.exceptions.RoomIsFullException;

import java.util.ArrayList;

import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rooms")
public class Room {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "number")
    private String number;

    @NotNull
    @Max(20)
    @Column(name = "capacity")
    private int capacity;

    @Column(name = "room_subject")
    @Enumerated(EnumType.STRING)
    private Subject subject;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    @Column(name = "students")
    private List<Student> students = new ArrayList<>();


    public Room(String number, int capacity) {
        this.number = number;
        this.capacity = capacity;
    }


    public void addStudent(Student student) {
        if (!(students.size() + 1 <= capacity)) {
            throw new RoomIsFullException();
        }
        students.add(student);
        student.setRoom(this);
    }

    public void removeStudent(Student student){
        students.remove(student);
        student.setRoom(null);
    }


}