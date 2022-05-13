package com.voidsow.community.controller;

import com.voidsow.community.service.PostService;
import com.voidsow.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class IndexController {
    PostService postService;
    UserService userService;

    @Autowired
    public IndexController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping
    public String hello(@RequestParam(value = "page", defaultValue = "1") Integer page,
                        Model model) {
        model.addAllAttributes(postService.getPosts(null, page));
        return "index";
    }

    @GetMapping("/error")
    public String error() {
        return "error/500";
    }
}
