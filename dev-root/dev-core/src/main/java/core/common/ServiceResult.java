package core.common;

import common.SystemCode;

public class ServiceResult<T> {

    private String message;
    private String code;
    private T result;
    private boolean succeed;

    public static <T> ServiceResult<T> success(){
        ServiceResult<T> r = new ServiceResult<>();
        r.code = SystemCode.Success.getCode();
        r.message = SystemCode.Success.getMessage();
        r.succeed = true;
        return r;
    }

    public static <T> ServiceResult<T> success(T result){
        ServiceResult<T> r = new ServiceResult<>();
        r.code = SystemCode.Success.getCode();
        r.message = SystemCode.Success.getMessage();
        r.succeed = true;
        r.result = result;
        return r;
    }

    public static <T> ServiceResult<T> fail(SystemCode code){
        ServiceResult<T> r = new ServiceResult<>();
        r.code = code.getCode();
        r.message = code.getMessage();
        r.succeed = false;
        return r;
    }

    public boolean isSucceed(){
        return succeed;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public T getResult() {
        return result;
    }
}
