package comp3111.examsystem.entity.Personnel;

import java.io.Serializable;

public enum Position implements Serializable {
    INSTRUCTOR("Instructor"),
    PROFESSOR("Professor"),
    ASSISTANT_PROFESSOR("Assistant Professor"),
    ASSOCIATE_PROFESSOR("Associate Professor"),
    TEACHING_ASSISTANT("Teaching Assistant");

    private final String name;
    Position(String s) {
        name = s;
    }

    public String getName() {
        return name;
    }

    public static Position toPosition(String s) {
        for (Position p : Position.values()) {
            if (p.name.equals(s)) return p;
        }

        return Position.valueOf(s);
    }
}
