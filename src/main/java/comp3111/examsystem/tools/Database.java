package comp3111.examsystem.tools;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import comp3111.examsystem.entity.Entity;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the database of entity. Used for accessing and retrieving entity form files
 * @param <T> Entity for storage and accessing in this database
 */
public class Database<T> {

    /**
     * Class sample of entity
     */
    Class<T> entitySample;

    /**
     * File name for storage, also representing the table name of the database
     */
    String tableName;

    /**
     * File for storing database table in .txt format
     */
    String jsonFile;

    /**
     * Constructor for Database.
     * Initializing the file for storing the database table.
     * @param entity Class of entity T
     */
    public Database(Class<T> entity) {
        entitySample = entity;
        tableName = entitySample.getSimpleName().toLowerCase();
        jsonFile = Paths.get("src", "main", "resources", "database", tableName + ".txt").toString();
        File file = new File(jsonFile);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Search for the specific entity based on its id (key)
     * @param key Id of the entity
     * @return The searched entity; null if no such id exist
     */
    public T queryByKey(String key) {
        List<String> slist = FileUtil.readFileByLines(jsonFile);

        T res = null;
        for (int i = 0; i < slist.size(); i++) {
            T t = txtToEntity(slist.get(i));
            Object tvalue = getValue(t, "id");
            if (tvalue.toString().equals(key)) {
                res = t;
                break;
            }
        }
        return res;
    }

    /**
     * Search for a list of entity based on the list of id
     * @param keys List of entity ids
     * @return A list of searched entity
     */
    public List<T> queryByKeys(List<String> keys) {
        List<String> slist = FileUtil.readFileByLines(jsonFile);

        List<T> res = new ArrayList<>();
        for (int i = 0; i < slist.size(); i++) {
            T t = txtToEntity(slist.get(i));
            Object tvalue = getValue(t, "id");
            for (String key : keys) {
                if (tvalue.toString().equals(key)) {
                    res.add(t);
                    break;
                }
            }
        }
        return res;
    }

    /**
     * Search for entities with specific field with value exactly same as the field value provided
     * @param fieldName The field for searching
     * @param fieldValue The value of the field that exactly matches
     * @return List of entity as the searching result
     */
    public List<T> queryByField(String fieldName, String fieldValue) {
        List<T> list = getAll();
        List<T> resList = new ArrayList<>();
        for (T e : list) {
            Object value = getValue(e, fieldName);
            if ((value == null && fieldValue != null) || (value != null && fieldValue == null) || !value.toString().equals(fieldValue)) {
                continue;
            }
            resList.add(e);
        }
        list.clear();
        list.addAll(resList);
        return list;
    }

    /**
     * Search for entities with specific field with value as string containing the field value provided
     * @param fieldName The field for searching
     * @param fieldValue The value of the field that fuzzily matches
     * @return List of entity as the searching result
     */
    public List<T> queryFuzzyByField(String fieldName, String fieldValue) {
        List<T> list = getAll();
        List<T> resList = new ArrayList<>();
        for (T e : list) {
            Object value = getValue(e, fieldName);
            if (fieldValue == null || value.toString().contains(fieldValue)) {
                resList.add(e);
            }
        }
        list.clear();
        list.addAll(resList);
        return list;
    }

    /**
     * Search for entities based on the entity provided,
     * with field values exactly same as those declared in the entity
     * @param entity The entity with declared field for searching
     * @return List of entity as the searching result
     */
    public List<T> queryByEntity(T entity) {
        List<T> list = getAll();
        List<String> prolist = new ArrayList<>();
        Class<?> clazz = entitySample;
        while (true) {
            for (Field field : clazz.getDeclaredFields()) {
                if (!field.getName().equals("id") && !field.getName().equals("dbutil")) {
                    Object obj = getValue(entity, field.getName());
                    if (obj != null && !obj.toString().isEmpty()) {
                        prolist.add(field.getName());
                    }
                }
            }
            if (clazz.equals(Entity.class)) {
                break;
            }
            else {
                clazz = clazz.getSuperclass();
            }
        }
        List<T> resList = new ArrayList<>();
        for (T e : list) {
            boolean flag = true;
            for (int i = 0; i < prolist.size(); i++) {
                String filterProp = prolist.get(i);
                String queryValue = getValue(entity, filterProp).toString();
                Object value = getValue(e, filterProp);
                if ((queryValue == null && value != null) || (queryValue != null && value == null) || !value.toString().equals(queryValue)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                resList.add(e);
            }
        }
        list.clear();
        list.addAll(resList);
        return list;
    }

    /**
     * Access all entity in the database
     * @return List of all entity in the database
     */
    public List<T> getAll() {
        List<String> slist = FileUtil.readFileByLines(jsonFile);
        List<T> tlist = new ArrayList<>();
        for (int i = 0; i < slist.size(); i++) {
            tlist.add(txtToEntity(slist.get(i)));
        }
        return tlist;
    }

    /**
     * Inner join 2 list of the entity
     * @param list1 First list for joining
     * @param list2 Second list for joining
     * @return The list inner-joined
     */
    public List<T> join(List<T> list1, List<T> list2) {
        List<T> resList = new ArrayList<>();
        for (int i = 0; i < list1.size(); i++) {
            for (int j = 0; j < list2.size(); j++) {
                Long id1 = (Long) getValue(list1.get(i), "id");
                Long id2 = (Long) getValue(list2.get(j), "id");
                if (id1.toString().equals(id2.toString())) {
                    resList.add(list1.get(i));
                    break;
                }
            }
        }
        return resList;
    }

    /**
     * Delete an entity from the database based on its id
     * @param key Id of the entity
     */
    public void delByKey(String key) {
        List<T> tlist = getAll();
        for (int i = 0; i < tlist.size(); i++) {
            Object value = getValue(tlist.get(i), "id");
            if (value.toString().equals(key)) {
                tlist.remove(i);
                break;
            }
        }
        try {
            FileUtil.writeTxtFile(listToStr(tlist), new File(jsonFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete all entity with specific field name exactly same as the field value provided
     * @param fieldName Name of the field for searching
     * @param fieldValue Value of the field that matches exactly
     */
    public void delByFiled(String fieldName, String fieldValue) {
        List<T> tlist = getAll();
        for (int i = 0; i < tlist.size(); i++) {
            Object value = getValue(tlist.get(i), fieldName);
            if (value.toString().equals(fieldValue)) {
                tlist.remove(i);
                break;
            }
        }
        try {
            FileUtil.writeTxtFile(listToStr(tlist), new File(jsonFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Update the entity based on its id in the database.
     * No update is performed if id is not found
     * @param entity Entity for updating
     */
    public void update(T entity) {
        Long key1 = (Long) getValue(entity, "id");
        List<T> tlist = getAll();
        for (int i = 0; i < tlist.size(); i++) {
            Long key = (Long) getValue(tlist.get(i), "id");

            if (key.toString().equals(key1.toString())) {
                Class<?> clazz = entitySample;
                while (true) {
                    for (Field field : clazz.getDeclaredFields()) {
                        if (!field.getName().equals("id") && !field.getName().equals("dbutil")) {
                            Object o = getValue(entity, field.getName());
                            setValue(tlist.get(i), field.getName(), o);
                        }
                    }
                    if (clazz.equals(Entity.class)) {
                        break;
                    }
                    else {
                        clazz = clazz.getSuperclass();
                    }
                }
                break;
            }
        }
        try {
            FileUtil.writeTxtFile(listToStr(tlist), new File(jsonFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add an entity to the database.
     * @param entity Entity for Adding
     */
    public void add(T entity) {
        setValue(entity, "id", System.currentTimeMillis());
        List<T> tlist = getAll();
        tlist.add(entity);
        try {
            FileUtil.writeTxtFile(listToStr(tlist), new File(jsonFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Access the value of specific field of a specific entity
     * @param entity Entity to get the value
     * @param fieldName Name of field to get the value
     * @return Value of specific field of the specific entity
     */
    private Object getValue(Object entity, String fieldName) {
        Object value;
        Class<?> clazz = entity.getClass();
        while (true) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                value = field.get(entity);
                break;
            }
            catch (NoSuchFieldException e) {
                if (clazz.equals(Object.class))
                    throw new RuntimeException(e);
            }
            catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            clazz = clazz.getSuperclass();
        }
        return value;
    }

    /**
     * Set the value of specific field of a specific entity to the value given.<br>
     * ps: Gson is used instead of given code to set the value for preventing error when setting enum, array, list or primitives.
     * @param entity Entity to set the value
     * @param fieldName Name of field to set the value
     * @param fieldValue Value to be set
     * @author Cheung Tuen King
     */
    private void setValue(Object entity, String fieldName, Object fieldValue) {
        Class<?> clazz = entity.getClass();
        while (true) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);

                if (fieldValue == null || "null".equals(fieldValue.toString())) {
                    field.set(entity, null);
                } else if (field.getType().equals(String.class)) {
                    field.set(entity, fieldValue.toString());
                } else {
                    Gson gson = new Gson();
                    field.set(entity, gson.fromJson(fieldValue.toString(), field.getType()));
                }
                break;
            }
            catch (NoSuchFieldException e) {
                if (clazz.equals(Object.class))
                    throw new RuntimeException(e);
            }
            catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            clazz = clazz.getSuperclass();
        }
    }

    /**
     * Convert a list of entity to string representation to be stored in database file
     * @param tlist List of entity for storing
     * @return Converted string
     */
    private String listToStr(List<T> tlist) {
        StringBuilder sbf = new StringBuilder();
        for (T t : tlist) {
            sbf.append(entityToTxt(t)).append("\r\n");
        }
        return sbf.toString();
    }

    /**
     * Convert a json string in database file to its java object representation in entity type<br>
     * ps: Gson is used in here for conversion to prevent suspicious error in conversion
     * @param txt Json string representation of the entity
     * @return Java object representation of the entity
     * @author Cheung Tuen King
     */
    private T txtToEntity(String txt) {
        Gson gson = new Gson();
        return gson.fromJson(txt, entitySample);
    }

    /**
     * Convert java object representation of entity to its json string representation in database file<br>
     * ps: Gson is used in here for conversion to prevent suspicious error in conversion
     * @param t Java object representation of the entity
     * @return Json string representation of the entity
     * @author Cheung Tuen King
     */
    private String entityToTxt(T t) {
        Gson gson = new Gson();
        return gson.toJson(t);
    }

    /**
     * Validate the integer such that it is in range (lower, upper) given
     * @param lower Lower limit of the integer
     * @param upper Upper limit of the integer
     * @param input Input integer
     * @param fieldName Name/ Representation of the integer
     * @param unit Unit used for the integer
     * @throws RuntimeException Integer out of range
     * @author Cheung Tuen King
     */
    public static void validateNumberRange(int lower, int upper, int input, String fieldName, String unit) {
        String s = "";
        if (upper > lower && lower >= 0) {
            s = " between " + lower + " and " + upper;
        } else if (upper > 0) {
            s = " less than " + upper;
        } else if (lower >= 0) {
            s = " larger than " + lower;
        }

        if ((lower >= 0 && input < lower) || (upper > 0 && input > upper)) {
            throw new RuntimeException(
                    "Please enter a valid " +
                    fieldName.toLowerCase().trim() + s +
                    (unit == null || unit.trim().isEmpty() ? "." : " " + unit.trim() + "."));
        }
    }

    /**
     * Convert the string to an integer and validate if it is in range (lower, upper) given
     * @param lower Lower limit of the integer
     * @param upper Upper limit of the integer
     * @param input Input integer in string format
     * @param fieldName Name/ Representation of the integer
     * @param unit Unit used for the integer
     * @throws RuntimeException Integer out of range, or input string is not an integer
     * @author Cheung Tuen King
     */
    public static void validateNumberRange(int lower, int upper, String input, String fieldName, String unit) {
        validateTextLength(-1, input, fieldName);
        int a;
        try {
            a = Integer.parseInt(input);
        } catch (Exception e) {
            throw new RuntimeException("The " + fieldName.trim().toLowerCase() + " should be an integer.");
        }

        validateNumberRange(lower, upper, a, fieldName, unit);
    }

    /**
     * Validate if the string given has length in range (0, length)
     * @param length Length limit of the input string
     * @param input Input string
     * @param fieldName Name/ Representation of the string
     * @throws RuntimeException Length out of range, or input is empty or null
     * @author Cheung Tuen King
     */
    public static void validateTextLength(int length, String input, String fieldName) {
        if (input == null || input.trim().isEmpty()) {
            throw new RuntimeException("Please enter the " + fieldName.trim().toLowerCase() + ".");
        }

        if (length > 0 && input.length() > length) {
            throw new RuntimeException("Length of " + fieldName.trim().toLowerCase() + " should not be larger than " + length + ".");
        }
    }
}
