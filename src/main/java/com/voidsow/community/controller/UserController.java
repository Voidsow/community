package com.voidsow.community.controller;

import com.google.code.kaptcha.Producer;
import com.voidsow.community.constant.Activation;
import com.voidsow.community.entity.User;
import com.voidsow.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import static com.voidsow.community.constant.Activation.ACTIVATED;
import static com.voidsow.community.constant.Activation.SUCCEESS;

@Controller
public class UserController {
    UserService userService;
    Producer captchaProducer;

    @Value("${server.servlet.context-path}")
    String contextPath;

    @Value("${token.duration.session}")
    int SESSION;

    @Value("${token.duration.long-term}")
    int LONG_TERM;

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

    @PostMapping("/login")
    public String login(String username, String password, String captcha,
                        @RequestParam(value = "long-term", required = false) boolean longTerm,
                        HttpSession session, HttpServletResponse response,
                        Model model) {
        String answer = (String) session.getAttribute("captcha");
        if (answer == null) {
            model.addAttribute("usernameMsg", "非法登录");
            return "login";
        } else if (!answer.equalsIgnoreCase(captcha)) {
            model.addAttribute("captchaMsg", "验证码错误");
            return "login";
        }
        int duration = longTerm ? LONG_TERM : SESSION;
        Map<String, Object> map = userService.login(username, password, duration);
        if (map.containsKey("token")) {
            Cookie token = new Cookie("token", (String) map.get("token"));
            token.setPath(contextPath);
            token.setMaxAge(duration);
            response.addCookie(token);
            session.removeAttribute("captcha");
            return "redirect:/";
        } else {
            model.addAllAttributes(map);
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(@CookieValue(value = "token", required = false) Cookie cookie, HttpServletResponse response) {
        if (cookie != null) {
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        return "redirect:/";
    }
}