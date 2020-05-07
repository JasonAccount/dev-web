package common;

/**
 * 常量类
 */
public class Constant {
    // 由数字和字母组成，且同时含有数字和字母，且长度在8-16位之间
    public static final String account_regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";

    // 用户登录验证码
    public static final String verify_code_in_session = "verify_code";

    public static final String login_page_path = "login/";
}
