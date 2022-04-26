package com.voidsow.community.controller;

import com.voidsow.community.dto.Result;
import com.voidsow.community.entity.Post;
import com.voidsow.community.entity.User;
import com.voidsow.community.service.PostService;
import com.voidsow.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/post")
public class PostController {
    PostService postService;
    HostHolder hostHolder;

    @Autowired
    public PostController(PostService postService, HostHolder hostHolder) {
        this.postService = postService;
        this.hostHolder = hostHolder;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result addPost(@RequestBody Post post) {
        User user = hostHolder.user.get();
        if (post.getTitle() == null || post.getTitle().isBlank())
            return new Result(400, "标题不能为空", null);
        else if (post.getContent() == null || post.getContent().isBlank())
            return new Result(400, "内容不能为空", null);
        else if (user == null)
            return new Result(403, "尚未登录", null);
        post.setUid(user.getId());
        Date curTime = new Date();
        post.setGmtCreate(curTime);
        post.setGmtModified(curTime);
        postService.add(post);
        return new Result(0, "发布成功", null);
    }
}
