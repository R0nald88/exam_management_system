package comp3111.examsystem.entity.Personnel;

import java.io.Serializable;

/**
 * Enum representing position of teachers
 * @author Cheung Tuen King
 */
public enum Position implements Serializable {
    /**
     * Instructor position
     */
    INSTRUCTOR("Instructor"),

    /**
     * Professor position
     */
    PROFESSOR("Professor"),

    /**
     * Assistance professor position
     */
    ASSISTANT_PROFESSOR("Assistant Professor"),

    /**
     * Associate professor position
     */
    ASSOCIATE_PROFESSOR("Associate Professor"),

    /**
     * Teaching assistant position
     */
    TEACHING_ASSISTANT("Teaching Assistant");

    /**
     * Proper case text for displaying teacher position
     */
    private final String name;

    /**
     * Constructor of teacher position
     * @author Cheung Tuen King
     * @param s Proper case text for teacher position
     */
    Position(String s) {
        name = s;
    }

    /**
     * Access the proper name of the position
     * @author Cheung Tuen King
     * @return Proper case representation for teacher position
     */
    public String getName() {
        return name;
    }

    /**
     * Convert position name representation to corresponding enum
     * @author Cheung Tuen King
     * @return Position enum
     * @param s Position name for converting
     */
    public static Position toPosition(String s) {
        for (Position p : Position.values()) {
            if (p.name.equals(s)) return p;
        }

        return Position.valueOf(s);
    }
}
