package training360.examregistration.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter

public enum Subject {

    MATH("math"), HISTORY("history"), LITERATURE("literature"), GERMAN("german"), ENGLISH("english"),
    SCIENCE("science"), COMPUTER_SCIENCE("computer_science");


    private String value;



    public String getValue() {
        return value;
    }
}

