package com.voidsow.community.controller;

import com.voidsow.community.dto.Like;
import com.voidsow.community.dto.Result;
import com.voidsow.community.entity.User;
import com.voidsow.community.service.LikeService;
import com.voidsow.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/like")
public class LikeController {
    LikeService likeService;
    HostHolder hostHolder;

    @Autowired
    public LikeController(LikeService likeService, HostHolder hostHolder) {
        this.likeService = likeService;
        this.hostHolder = hostHolder;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result like(@RequestBody Like like) {
        User user = hostHolder.user.get();
        boolean status = likeService.likeOrNot(like.getType(), like.getId(), user.getId(), like.getLikedUid());
        long likeNum = likeService.likeNum(like.getType(), like.getId());
        Map<String, Object> result = new HashMap<>();
        result.put("num", likeNum);
        result.put("like", status);
        return new Result(0, "success", result);
    }

}
