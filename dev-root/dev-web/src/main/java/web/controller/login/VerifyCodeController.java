package web.controller.login;

import common.Constant;
import common.utils.CommonStringUtils;
import common.utils.CommonUtils;
import common.utils.ResourceReader;
import core.service.login.LoginService;
import core.service.login.LoginType;
import core.vo.AccountVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
@RequestMapping(value = "/verify/")
public class VerifyCodeController {
    private static Logger logger = LoggerFactory.getLogger(VerifyCodeController.class);

    @RequestMapping(value = "/verifyCode.do", method = RequestMethod.GET)
    public void verifyCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String words = CommonStringUtils.randomString(ResourceReader.getInt("verifyCodeLength"));
        BufferedImage bufferedImage = CommonUtils.generateRandowImg(words, ResourceReader.getInt("verifyCodeWidth"), ResourceReader.getInt("verifyCodeHeight"));

        // 设置浏览器禁止图像缓存
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        // 将生成的随机验证码存入会话
        request.getSession().setAttribute(Constant.verify_code_in_session, words);

        // 响应验证码
        ServletOutputStream os = response.getOutputStream();
        try {
            ImageIO.write(bufferedImage, ResourceReader.getString("verifyCodeImgFormatName"), os);
            os.flush();
        } catch (IOException e) {
            logger.error("[exception]生成验证码异常", e);
        } finally {
            os.close();
        }
    }
}
