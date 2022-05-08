package com.voidsow.community.controller;

import com.voidsow.community.dto.Page;
import com.voidsow.community.dto.Result;
import com.voidsow.community.entity.Chat;
import com.voidsow.community.entity.User;
import com.voidsow.community.service.ChatService;
import com.voidsow.community.service.UserService;
import com.voidsow.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.voidsow.community.constant.Constant.UNREAD;

@Controller
@RequestMapping("/chat")
public class ChatController {
    HostHolder hostHolder;
    ChatService chatService;
    UserService userService;

    @Value("${community.page.size}")
    int pageSize;

    @Value("${community.page.num}")
    int pageNum;

    @Autowired
    public ChatController(HostHolder hostHolder, ChatService chatService, UserService userService) {
        this.hostHolder = hostHolder;
        this.chatService = chatService;
        this.userService = userService;
    }

    @GetMapping
    public String getChatList(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        User user = hostHolder.user.get();
        List<Map<String, Object>> conversations = chatService.getConversations(user.getId(), (page - 1) * pageSize, pageSize);
        int totalUnread = 0;
        for (var v : conversations) {
            Chat message = (Chat) v.get("message");
            int oppositeUid = message.getSpeaker().equals(user.getId()) ? message.getListener() : message.getSpeaker();
            v.put("talkTo", userService.findById(oppositeUid));
            totalUnread += (int) v.get("unread");
        }
        model.addAttribute("conversations", conversations);
        model.addAttribute("totalUnread", totalUnread);
        model.addAttribute("page", new Page(chatService.countConversations(user.getId()), page, pageSize, pageNum));
        return "chat";
    }

    @GetMapping("/{id}")
    public String getChatDetail(Model model, @PathVariable("id") String conversationId,
                                @RequestParam(value = "page", defaultValue = "1") int page) {
        User user = hostHolder.user.get();
        List<Chat> conversation = chatService.getConversation(conversationId, (page - 1) * pageSize, pageSize);
        String[] uids = conversationId.split("_");
        User user0 = userService.findById(Integer.parseInt(uids[0]));
        User user1 = userService.findById(Integer.parseInt(uids[1]));
        List<User> users = new ArrayList<>();
        List<Integer> setReadIds = new ArrayList<>();
        for (var message : conversation) {
            users.add(message.getSpeaker().equals(user0.getId()) ? user0 : user1);
            //更新消息读状态
            if (message.getStatus().equals(UNREAD) && message.getListener().equals(user.getId()))
                setReadIds.add(message.getId());
        }
        if (!setReadIds.isEmpty())
            chatService.setRead(setReadIds);
        model.addAttribute("users", users);
        model.addAttribute("messages", conversation);
        model.addAttribute("talkTo", user0.getId().equals(user.getId()) ? user1 : user0);
        model.addAttribute("page", new Page(chatService.countConversation(conversationId), page, pageSize, pageNum));
        return "message";
    }

    @PostMapping(produces = "application/json")
    @ResponseBody
    public Result sendMessage(@RequestBody Map<String, String> map) {
        User to = userService.findByName(map.get("toUsername"));
        if (to == null)
            return new Result(404, "收信人不存在！", null);
        chatService.sendMessage(hostHolder.user.get().getId(), to.getId(), map.get("content"));
        return new Result(0, "成功", null);
    }
}
