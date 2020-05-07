package plugins.page;

import common.SystemRuntimeException;

import java.util.HashMap;
import java.util.Map;

public class MybatisPluginRuntimeException extends SystemRuntimeException {
    public static final Integer ex_01 = Integer.valueOf(200001);
    public static final Integer ex_02 = Integer.valueOf(200002);
    public static final Integer ex_03 = Integer.valueOf(200003);
    public static final Integer ex_04 = Integer.valueOf(200004);
    public static final Integer ex_05 = Integer.valueOf(200005);
    public static final Integer ex_06 = Integer.valueOf(200006);
    public static final Integer ex_07 = Integer.valueOf(200007);
    public static final Integer ex_08 = Integer.valueOf(200008);
    public static final Integer ex_09 = Integer.valueOf(200009);
    public static final Integer ex_10 = Integer.valueOf(200010);
    public static final Integer ex_11 = Integer.valueOf(200011);


    private static final Map<Integer, String> desc = new HashMap<>();
    private Integer code;
    private String message;

    static {

    }

    public MybatisPluginRuntimeException(Integer code){
        super(desc.get(code));

        this.code = code;
        this.message = desc.get(code);
    }

    public MybatisPluginRuntimeException(Integer code, Throwable cause){
        super(desc.get(code), cause);

        this.code = code;
        this.message = desc.get(code);
    }

    public MybatisPluginRuntimeException(Integer code, String message){
        super(message);

        this.code = code;
        this.message = message;
    }

    public MybatisPluginRuntimeException(Integer code, String message, Throwable cause){
        super(message, cause);

        this.code = code;
        this.message = message;
    }

    public MybatisPluginRuntimeException(String message) {
        super(message);
    }

    public MybatisPluginRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
