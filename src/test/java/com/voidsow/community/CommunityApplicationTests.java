package com.voidsow.community;

import com.voidsow.community.service.UserService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CommunityApplicationTests {
    Logger logger = LoggerFactory.getLogger(CommunityApplicationTests.class);

    @Autowired
    UserService userService;

    @Test
    void contextLoads() {
    }
}