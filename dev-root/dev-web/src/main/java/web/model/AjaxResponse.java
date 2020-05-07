package web.model;


import common.SystemCode;

public class AjaxResponse {
    private String code;
    private String message;
    private Object result;

    public static AjaxResponse success(SystemCode code, Object result){
        AjaxResponse r = new AjaxResponse();
        r.code = code.getCode();
        r.message = code.getCode();
        if(result != null){
            r.result = result;
        }
        return r;
    }

    public static AjaxResponse fail(SystemCode code){
        AjaxResponse r = new AjaxResponse();
        r.code = code.getCode();
        r.message = code.getCode();
        return r;
    }
}
