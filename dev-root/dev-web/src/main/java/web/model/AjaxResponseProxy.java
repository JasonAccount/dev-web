package web.model;

import common.SystemCode;

public class AjaxResponseProxy {

    public static void before(){

    }

    public static AjaxResponse success(SystemCode code, Object result){
        before();

        return AjaxResponse.success(code, result);
    }

    public static AjaxResponse fail(SystemCode code){
        before();

        return AjaxResponse.fail(code);
    }
}
