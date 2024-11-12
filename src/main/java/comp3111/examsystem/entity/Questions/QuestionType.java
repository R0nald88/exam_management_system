package comp3111.examsystem.entity.Questions;

import java.io.Serializable;

public enum QuestionType implements Serializable {
    SINGLE("single"),
    MULTIPLE("multiple"),
    TRUE_FALSE("true false"),
    SHORT_Q("short");

    private final String name;
    QuestionType(String n) {
        name = n;
    }

    @Override
    public String toString() {
        return name;
    }
}
