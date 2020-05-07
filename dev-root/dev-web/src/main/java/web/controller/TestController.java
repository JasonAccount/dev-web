package web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);
    @RequestMapping(value = "/testWeb.do")
    @ResponseBody
    public String testWeb(){
        return "web success";
    }

    @RequestMapping(value = "/helloWorld.do")
    public String helloWorld(Model model, HttpServletResponse response) throws IOException {
        logger.info("thymeleaf");
        model.addAttribute("message", "hello thymeleaf!");
        return "test";
    }
}