package com.voidsow.community.service;

import com.voidsow.community.entity.Chat;
import com.voidsow.community.mapper.ChatMapper;
import com.voidsow.community.mapper.CustomChatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ChatService {
    ChatMapper chatMapper;
    CustomChatMapper customChatMapper;

    @Autowired
    public ChatService(ChatMapper chatMapper, CustomChatMapper customChatMapper) {
        this.chatMapper = chatMapper;
        this.customChatMapper = customChatMapper;
    }

    public List<Map<String, Object>> getConversations(int uid, Integer offset, Integer limit) {
        List<Map<String, Object>> conversations = customChatMapper.getConversations(uid, offset, limit);
        conversations.forEach((v) -> v.put("message", chatMapper.selectByPrimaryKey((int) v.get("last"))));
        return conversations;
    }

    public int countConversations(int uid) {
        return customChatMapper.countConversations(uid);
    }

    public List<Chat> getConversation(String conversationId, int offset, int limit) {
        return customChatMapper.getConversation(conversationId, offset, limit);
    }

    public int countConversation(String conversationId) {
        return customChatMapper.countConversation(conversationId);
    }
}