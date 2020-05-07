package common.encryptor;

import common.SystemRuntimeException;

import java.util.HashMap;
import java.util.Map;

public class EncryptorRuntimeException extends SystemRuntimeException {

    public static final Integer ex_01 = Integer.valueOf(100001);
    public static final Integer ex_02 = Integer.valueOf(100002);
    public static final Integer ex_03 = Integer.valueOf(100003);
    public static final Integer ex_04 = Integer.valueOf(100004);
    public static final Integer ex_05 = Integer.valueOf(100005);
    public static final Integer ex_06 = Integer.valueOf(100006);
    public static final Integer ex_07 = Integer.valueOf(100007);
    public static final Integer ex_08 = Integer.valueOf(100008);
    public static final Integer ex_09 = Integer.valueOf(100009);
    public static final Integer ex_10 = Integer.valueOf(100010);
    public static final Integer ex_11 = Integer.valueOf(100011);


    private static final Map<Integer, String> desc = new HashMap<>();
    private Integer code;
    private String message;

    static {
        desc.put(ex_01, "构造AES加解密器异常[请检查密钥或令牌是否为空 | 密钥长度是否符合要求]");
        desc.put(ex_02, "构造AES加解密器异常[请检查密钥是否为空]");
        desc.put(ex_03, "AES加密异常");
        desc.put(ex_04, "AES解密异常");
        desc.put(ex_05, "AES计算签名异常");
        desc.put(ex_06, "AES签名不匹配");
        desc.put(ex_07, "RSA加密异常");
        desc.put(ex_08, "RSA解密异常");
        desc.put(ex_09, "RSA签名[请正确配置PKCS8格式的私钥]");
        desc.put(ex_10, "RSA签名异常");
        desc.put(ex_11, "RSA验证签名异常");
    }

    public EncryptorRuntimeException(Integer code){
        super(desc.get(code));

        this.code = code;
        this.message = desc.get(code);
    }

    public EncryptorRuntimeException(Integer code, Throwable cause){
        super(desc.get(code), cause);

        this.code = code;
        this.message = desc.get(code);
    }

    public EncryptorRuntimeException(String message) {
        super(message);
    }

    public EncryptorRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
