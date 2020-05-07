package common.utils;

import java.util.Random;

public class CommonStringUtils {
    public static final String BASE_STR = "abcdefghijklmnopqrstuvwxyz01234546789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String randomString(int length){
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i=0; i < length; i++){
            char c = BASE_STR.charAt(random.nextInt(length));
            sb.append(c);
        }
        return sb.toString();
    }
}
