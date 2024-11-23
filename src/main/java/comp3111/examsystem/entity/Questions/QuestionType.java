package comp3111.examsystem.entity.Questions;

import java.io.Serializable;

/**
 * Enum representing question type
 * @author Cheung Tuen King
 */
public enum QuestionType implements Serializable {

    /**
     * Single question enum
     */
    SINGLE("single"),

    /**
     * Multiple question enum
     */
    MULTIPLE("multiple"),

    /**
     * True/ false question enum
     */
    TRUE_FALSE("true false"),

    /**
     * Short question enum
     */
    SHORT_Q("short");

    /**
     * Proper case text for displaying question type
     */
    private final String name;

    /**
     * Constructor of question type enum
     * @author Cheung Tuen King
     * @param n Proper case text for type
     */
    QuestionType(String n) {
        name = n;
    }

    /**
     * Access the proper name of the type
     * @author Cheung Tuen King
     * @return Proper case representation for gender
     */
    public String getName() {
        return name;
    }

    /**
     * Convert question type proper name representation to corresponding enum
     * @author Cheung Tuen King
     * @return Question type enum
     * @param s Question type name for converting
     */
    public static QuestionType toType(String s) {
        for (QuestionType t : QuestionType.values()) {
            if (t.getName().equals(s)) return t;
        }
        return QuestionType.valueOf(s);
    }
}
