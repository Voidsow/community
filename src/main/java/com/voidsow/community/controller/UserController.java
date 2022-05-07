package com.voidsow.community.controller;

import com.voidsow.community.annotation.LoginRequire;
import com.voidsow.community.service.UserService;
import com.voidsow.community.utils.Authorizer;
import com.voidsow.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {
    UserService userService;
    HostHolder hostHolder;
    Authorizer authorizer;

    @Value("${community.path.upload}")
    private String UPLOAD_PATH;

    @Value("${community.domain}")
    private String DOMAIN;

    @Value("${server.servlet.context-path}")
    private String CONTEXT_PATH;

    @Autowired
    public UserController(UserService userService, HostHolder hostHolder, Authorizer authorizer) {
        this.userService = userService;
        this.hostHolder = hostHolder;
        this.authorizer = authorizer;
    }

    @LoginRequire
    @GetMapping("/setting")
    public String getPage() {
        return "setting";
    }

    @LoginRequire
    @PostMapping("/upload")
    public String uploadHeader(MultipartFile headerImage,
                               Model model) throws IOException {
        if (headerImage == null) {
            model.addAttribute("errorMsg", "未选中图片");
            return "/setting";
        }
        String imageName = headerImage.getOriginalFilename();
        String suffix = imageName.substring(imageName.lastIndexOf('.'));
        if (suffix.isBlank() || !isImage(suffix)) {
            model.addAttribute("errorMsg", "图片格式不正确");
            return "/setting";
        }
        imageName = UUID.randomUUID().toString().replaceAll("-", "") + imageName;
        headerImage.transferTo(new File(UPLOAD_PATH + "/" + imageName));
        String headerUrl = DOMAIN + CONTEXT_PATH + "user/header/" + imageName;
        userService.updateHeader(hostHolder.user.get().getId(), headerUrl);
        hostHolder.user.get().setHeaderUrl(headerUrl);
        return "redirect:/";
    }

    @GetMapping(value = "/header/{imageName}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE})
    @ResponseBody
    byte[] getHeader(@PathVariable("imageName") String fileName) throws IOException {
        fileName = UPLOAD_PATH + "/" + fileName;
        try (FileInputStream fileInputStream = new FileInputStream(fileName);
             ByteArrayOutputStream byteOs = new ByteArrayOutputStream()) {
            fileInputStream.transferTo(byteOs);
            return byteOs.toByteArray();
        }
    }

    boolean isImage(String suffix) {
        suffix = suffix.toLowerCase();
        return suffix.equals(".jpg") || suffix.equals(".png") || suffix.equals(".jpeg");
    }
}