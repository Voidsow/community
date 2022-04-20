package com.voidsow.community;

import com.voidsow.community.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CommunityApplication {
    @Autowired
    UserMapper userMapper;

    public static void main(String[] args) {
        SpringApplication.run(CommunityApplication.class, args);
    }
}
