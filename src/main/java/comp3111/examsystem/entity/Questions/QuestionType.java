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
    public String getName() {
        return name;
    }

    public static QuestionType toType(String s) {
        for (QuestionType t : QuestionType.values()) {
            if (t.getName().equals(s)) return t;
        }
        return QuestionType.valueOf(s);
    }
}
