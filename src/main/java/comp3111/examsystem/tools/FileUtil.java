package comp3111.examsystem.tools;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for accessing and writing files
 */
public class FileUtil {

    /**
     * Write content to the file specified
     * @param content File content to be written
     * @param fileName File to be written
     * @return Boolean determining if the writing is successful
     */
    public static boolean writeTxtFile(String content, File fileName) {
        boolean flag = false;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(fileName);
            fileOutputStream.write(content.getBytes("UTF-8"));
            fileOutputStream.close();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * Read the content in file line by line
     * @param fileName File for reading
     * @return List of string, each item representing a line in the file
     */
    public static List<String> readFileByLines(String fileName) {
        File file = new File(fileName);
        List<String> list = new ArrayList<String>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                list.add(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return list;
    }
}
