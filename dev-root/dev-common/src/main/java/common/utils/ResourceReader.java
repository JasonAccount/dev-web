package common.utils;

import java.util.ResourceBundle;

public class ResourceReader {
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("application");

    public static String getString(String key){
        return resourceBundle.getString(key);
    }

    public static int getInt(String key){
        return Integer.valueOf(getString(key)).intValue();
    }
}
