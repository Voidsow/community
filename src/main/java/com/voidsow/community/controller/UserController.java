package com.voidsow.community.controller;

import com.google.code.kaptcha.Producer;
import com.voidsow.community.constant.Activation;
import com.voidsow.community.entity.User;
import com.voidsow.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import static com.voidsow.community.constant.Activation.ACTIVATED;
import static com.voidsow.community.constant.Activation.SUCCEESS;

@Controller
public class UserController {
    UserService userService;
    Producer captchaProducer;

    @Autowired
    public UserController(UserService userService, Producer captchaProducer) {
        this.userService = userService;
        this.captchaProducer = captchaProducer;
    }

    @GetMapping("/register")
    public String getRegister() {
        return "register";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @GetMapping(value = "/captcha", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public byte[] getCaptcha(HttpSession session) throws IOException {
        String text = captchaProducer.createText();
        session.setAttribute("captcha", text);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(captchaProducer.createImage(text), "png", outputStream);
        return outputStream.toByteArray();
    }

    @PostMapping("/register")
    public String register(User user, @RequestParam("confirm-psw") String confirmPsw, Model model) throws MessagingException {
        Map<String, Object> result = userService.register(user, confirmPsw);
        if (result.isEmpty()) {
            model.addAttribute("msg", "注册成功，激活邮件已发往您的邮箱，请前往邮箱查看！");
            model.addAttribute("targetLink", "/");
            return "operate-result";
        } else {
            model.addAllAttributes(result);
            model.addAttribute("user", user);
            return "register";
        }
    }

    @GetMapping("/activate/{activationCode}")
    public String activate(@PathVariable("activationCode") String activationCode,
                           Model model) {
        Activation result = userService.activate(activationCode);
        if (result == SUCCEESS) {
            model.addAttribute("msg", "激活成功，跳转到登录页面");
            model.addAttribute("targetLink", "/login");
        } else if (result == ACTIVATED) {
            model.addAttribute("msg", "该账号已被激活！");
            model.addAttribute("targetLink", "/index");
        } else {
            model.addAttribute("msg", "激活链接无效！");
            model.addAttribute("targetLink", "/index");
        }
        return "operate-result";
    }
}
