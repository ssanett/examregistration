package training360.examregistration.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name= "examiners")
@Builder
@AllArgsConstructor

public class Examiner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Fistname cannot be blank")
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message = "Lastname cannot be blank")
    @Column(name = "last_name")
    private String lastName;

    @ElementCollection
    @Column(name = "examiner_subject")
    @Enumerated(EnumType.STRING)
    private List<Subject> taughtSubjects = new ArrayList<>();

    @OneToOne
    private Room room;


}
