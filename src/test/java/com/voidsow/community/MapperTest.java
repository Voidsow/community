package com.voidsow.community;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voidsow.community.mapper.ChatMapper;
import com.voidsow.community.mapper.CustomChatMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
public class MapperTest {
    final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ChatMapper chatMapper;
    @Autowired
    CustomChatMapper customChatMapper;

    @Test
    void testChatMapper() {
    }

    @Test
    void debug() {
        customChatMapper.getConversation("1_2", null, null).forEach(v -> {
            try {
                System.out.println(objectMapper.writeValueAsString(v));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }
}
