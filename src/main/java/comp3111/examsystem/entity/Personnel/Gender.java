package comp3111.examsystem.entity.Personnel;

import java.io.Serializable;

public enum Gender implements Serializable {
    MALE("Male"),
    FEMALE("Female"),
    NON_BINARY("Non-binary");

    private final String name;
    Gender(String s) {
        name = s;
    }

    public String getName() {
        return name;
    }

    public static Gender toGender(String s) {
        if (s == null)  throw new RuntimeException("Please select your Gender.");
        for (Gender g : Gender.values()) {
            if (g.name.equals(s)) return g;
        }

        return Gender.valueOf(s);
    }
}
