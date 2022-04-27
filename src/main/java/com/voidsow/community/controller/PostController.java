package com.voidsow.community.controller;

import com.voidsow.community.dto.Page;
import com.voidsow.community.dto.Result;
import com.voidsow.community.entity.Comment;
import com.voidsow.community.entity.Post;
import com.voidsow.community.entity.User;
import com.voidsow.community.service.CommentService;
import com.voidsow.community.service.PostService;
import com.voidsow.community.service.UserService;
import com.voidsow.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.voidsow.community.constant.Constant.POST_LEVEL_ONE;
import static com.voidsow.community.constant.Constant.POST_LEVEL_TWO;

@Controller
@RequestMapping("/post")
public class PostController {
    PostService postService;
    UserService userService;
    CommentService commentService;
    HostHolder hostHolder;

    @Value("${community.page.size}")
    int pageSize;

    @Value("${community.page.num}")
    int pageNum;

    @Autowired
    public PostController(PostService postService, UserService userService,
                          CommentService commentService, HostHolder hostHolder) {
        this.postService = postService;
        this.userService = userService;
        this.commentService = commentService;
        this.hostHolder = hostHolder;
    }

    @GetMapping("/{id}")
    public String getPost(@PathVariable("id") Integer id, @RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        Post post = postService.get(id);
        model.addAttribute("post", post);
        //一级评论列表
        List<Map<String, Object>> commentVOList = new ArrayList<>();
        List<Comment> comments = commentService.find(POST_LEVEL_ONE, id, (page - 1) * pageSize, pageSize);
        for (var comment : comments) {
            Map<String, Object> commentVo = new HashMap<>();
            commentVo.put("comment", comment);
            commentVo.put("user", userService.findById(comment.getUid()));
            //二级评论列表
            List<Comment> secondComments = commentService.find(POST_LEVEL_TWO,
                    comment.getId(), 0, Integer.MAX_VALUE);
            List<Map<String, Object>> secondCommentVOList = new ArrayList<>();
            for (var secondComment : secondComments) {
                Map<String, Object> secondVO = new HashMap<>();
                secondVO.put("secondComment", secondComment);
                secondVO.put("user", userService.findById(secondComment.getUid()));
                User targetUser = secondComment.getReplyToUid() == null ? null :
                        userService.findById(secondComment.getReplyToUid());
                secondVO.put("targetUser", targetUser);
                secondCommentVOList.add(secondVO);
            }
            commentVo.put("secondComments", secondCommentVOList);
            commentVo.put("replyNum", secondComments.size());
            commentVOList.add(commentVo);
        }
        model.addAttribute("comments", commentVOList);
        model.addAttribute("user", userService.findById(post.getUid()));
        model.addAttribute("page",
                new Page(commentService.getCount(POST_LEVEL_ONE, id), page, pageSize, pageNum));
        return "discuss-detail";
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
