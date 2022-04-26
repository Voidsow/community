package com.voidsow.community;

import com.voidsow.community.service.UserService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;

@SpringBootTest
class CommunityApplicationTests {
    Logger logger = LoggerFactory.getLogger(CommunityApplicationTests.class);

    @Value("${community.token.duration.long-term}")
    int LONG_TERM;

    @Autowired
    UserService userService;

    @Test
    void contextLoads() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, LONG_TERM);
        System.out.println(calendar.toString());
    }
}