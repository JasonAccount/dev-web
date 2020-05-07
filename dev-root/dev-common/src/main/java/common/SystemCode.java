package common;

public enum SystemCode {

    Success("000000", "ok"),
    LoginTypeError("000001", "登录类型不匹配"),
    VerifyCodeNotMatch("000002", "验证码不匹配"),
    AccountNotExists("000003", "账号不存在"),
    PasswordNotMatch("000004", "账号密码不匹配"),
    PasswordIsEmpty("000005", "请输入密码");

    private String code;
    private String message;
    private SystemCode(String code, String message){
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
