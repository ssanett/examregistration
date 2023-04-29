package training360.examregistration.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import training360.examregistration.model.Subject;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoomWithSubjectCommand {

    Subject subject;
}
