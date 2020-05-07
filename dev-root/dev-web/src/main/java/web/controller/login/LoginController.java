package web.controller.login;

import common.Constant;
import common.SystemCode;
import common.utils.CommonStringUtils;
import common.utils.CommonUtils;
import common.utils.ResourceReader;
import core.common.ServiceResult;
import core.service.login.LoginService;
import core.service.login.LoginType;
import core.vo.AccountVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
@RequestMapping(value = "/login/")
public class LoginController {
    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "/signin.htm", method = RequestMethod.GET)
    public String signin(HttpServletRequest request, Model model){

        // TODO 验证用户

        model.addAttribute("message", "welcome to our site!(欢迎)");
        return Constant.login_page_path + "signin";
    }

    @RequestMapping(value = "/checkLogin.do", method = RequestMethod.POST)
    public void checkLogin(HttpServletRequest req, Model model){
        String loginType = req.getParameter("type");// 用户选择登录方式
        LoginType loginTypeEnum = LoginType.valueOf(loginType);
        switch (loginTypeEnum){
            case PHONE:// 手机验证码登录

                break;
            case PWD:// 账户密码登录
                String account = req.getParameter("account");
                if(StringUtils.isNotBlank(account) && CommonUtils.isMatch(Constant.account_regex, account)){
                    model.addAttribute("msg", SystemCode.AccountNotExists.getMessage());
                    return;
                }

                String pwd = req.getParameter("pwd");
                if(StringUtils.isNotBlank(pwd)){
                    model.addAttribute("msg", SystemCode.PasswordIsEmpty.getMessage());
                    return;
                }

                String verifyCode = req.getParameter("verifyCode");
                String verifyCodeInSession = (String) req.getSession().getAttribute(Constant.verify_code_in_session);
                if(StringUtils.isNotBlank(verifyCode) || verifyCode.equals(verifyCodeInSession)){
                    model.addAttribute("msg", SystemCode.VerifyCodeNotMatch.getMessage());
                    return;
                }

                AccountVo accountVo = new AccountVo();
                accountVo.setAccount(account);
                accountVo.setPwd(pwd);
                ServiceResult<Void> serviceResult = loginService.checkLogin(loginTypeEnum, accountVo);
                model.addAttribute("msg", serviceResult.getMessage());
                break;
            case WECHAT:// 微信登录

                break;
            default:
                model.addAttribute("msg", SystemCode.LoginTypeError.getMessage());
        }
    }

    @RequestMapping(value = "/login.do", method = RequestMethod.POST)
    public String login(HttpServletRequest req, Model model){
        String loginType = req.getParameter("type");// 用户选择登录方式
        LoginType loginTypeEnum = LoginType.valueOf(loginType);
        switch (loginTypeEnum){
            case PHONE:// 手机验证码登录

                break;
            case PWD:// 账户密码登录
                String account = req.getParameter("account");
                if(StringUtils.isNotBlank(account) && CommonUtils.isMatch(Constant.account_regex, account)){
                    model.addAttribute("msg", SystemCode.AccountNotExists.getMessage());
                    return Constant.login_page_path + "signin";
                }

                String pwd = req.getParameter("pwd");
                if(StringUtils.isNotBlank(pwd)){
                    model.addAttribute("msg", SystemCode.PasswordIsEmpty.getMessage());
                    return Constant.login_page_path + "signin";
                }

                AccountVo accountVo = new AccountVo();
                accountVo.setAccount(account);
                accountVo.setPwd(pwd);
                ServiceResult<Void> serviceResult = loginService.login(loginTypeEnum, accountVo);
                model.addAttribute("msg", serviceResult.getMessage());
                if(!serviceResult.isSucceed()){
                    return Constant.login_page_path + "signin";
                }
                break;
            case WECHAT:// 微信登录

                break;
            default:
                model.addAttribute("msg", SystemCode.LoginTypeError.getMessage());
        }
        return Constant.login_page_path + "success";
    }


}
