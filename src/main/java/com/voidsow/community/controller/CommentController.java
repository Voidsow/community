package com.voidsow.community.controller;

import com.voidsow.community.entity.Comment;
import com.voidsow.community.service.CommentService;
import com.voidsow.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/post/{pid}/comment")
public class CommentController {
    CommentService commentService;
    HostHolder hostHolder;

    @Autowired
    public CommentController(CommentService commentService, HostHolder hostHolder) {
        this.commentService = commentService;
        this.hostHolder = hostHolder;
    }

    @PostMapping
    public String getMapping(@PathVariable("pid") int pid, Comment comment) {
        comment.setUid(hostHolder.user.get().getId());
        commentService.add(comment, pid);
        return "redirect:/post/" + pid;
    }
}
