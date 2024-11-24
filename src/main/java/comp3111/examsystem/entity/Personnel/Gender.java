package comp3111.examsystem.entity.Personnel;

import java.io.Serializable;

/**
 * Enum representing gender of personnel
 * @author Cheung Tuen King
 */
public enum Gender implements Serializable {
    /**
     * Male enum
     */
    MALE("Male"),

    /**
     * Female enum
     */
    FEMALE("Female"),

    /**
     * Non-binary enum
     */
    NON_BINARY("Non-binary");

    /**
     * Proper case text for displaying gender
     */
    private final String name;

    /**
     * Constructor of gender enum
     * @author Cheung Tuen King
     * @param s Proper case text for gender
     */
    Gender(String s) {
        name = s;
    }

    /**
     * Access the proper name of the gender
     * @author Cheung Tuen King
     * @return Proper case representation for gender
     */
    public String getName() {
        return name;
    }

    /**
     * Convert gender proper name representation to corresponding enum
     * @author Cheung Tuen King
     * @return Gender enum
     * @param s Gender name for converting
     */
    public static Gender toGender(String s) {
        if (s == null)  throw new RuntimeException("Please select your Gender.");
        for (Gender g : Gender.values()) {
            if (g.name.equals(s)) return g;
        }

        return Gender.valueOf(s);
    }
}
