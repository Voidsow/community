package com.voidsow.community.controller;

import com.voidsow.community.dto.Follow;
import com.voidsow.community.dto.Page;
import com.voidsow.community.dto.Result;
import com.voidsow.community.entity.User;
import com.voidsow.community.exception.ResourceNotFoundException;
import com.voidsow.community.service.FollowService;
import com.voidsow.community.service.UserService;
import com.voidsow.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FollowController {
    HostHolder hostHolder;
    UserService userService;
    FollowService followService;

    @Value("${community.page.size}")
    private int pageSize;

    @Value("${community.page.num}")
    private int pageNum;

    @Autowired
    public FollowController(UserService userService, FollowService followService, HostHolder hostHolder) {
        this.followService = followService;
        this.userService = userService;
        this.hostHolder = hostHolder;
    }

    @GetMapping("/user/{id}/follower")
    public String follwer(Model model, @PathVariable("id") int id,
                          @RequestParam(value = "page", defaultValue = "1") int page) {
        User user = hostHolder.user.get();
        User observed = userService.findById(id);
        if (observed == null)
            throw new ResourceNotFoundException();
        model.addAttribute("observed", observed);
        model.addAttribute("page", new Page(followService.countFollower(observed.getId()), page, pageSize, pageNum));
        List<User> followers = followService.findFollowers(observed.getId(), (page - 1) * pageSize, pageSize);
        if (user != null)
            model.addAttribute("selfFollowed", selfFollowed(followers, user.getId()));
        model.addAttribute("followers", followers);
        return "follower";
    }

    @GetMapping("/user/{id}/followee")
    public String follwee(Model model, @PathVariable("id") int id,
                          @RequestParam(value = "page", defaultValue = "1") int page) {
        User user = hostHolder.user.get();
        User observed = userService.findById(id);
        if (observed == null)
            throw new ResourceNotFoundException();
        model.addAttribute("observed", observed);
        model.addAttribute("page", new Page(followService.countFollowee(observed.getId()), page, pageSize, pageNum));
        List<User> followees = followService.findFollowees(observed.getId(), (page - 1) * pageSize, pageSize);
        if (user != null)
            model.addAttribute("selfFollowed", selfFollowed(followees, user.getId()));
        model.addAttribute("followees", followees);
        return "followee";
    }

    @PostMapping(value = "/follow", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result follow(@RequestBody Follow follow) {
        int userId = hostHolder.user.get().getId();
        if (follow.isFollowed())
            followService.follow(userId, follow.getFollowee());
        else
            followService.cancelFollow(userId, follow.getFollowee());
        return new Result(0, follow.isFollowed() ? "关注成功" : "取消关注成功", null);
    }

    private List<Boolean> selfFollowed(List<User> followers, int uid) {
        List<Boolean> followed = new ArrayList<>();
        followers.forEach(follower -> followed.add(followService.isFollower(follower.getId(), uid)));
        return followed;
    }
}
