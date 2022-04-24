package com.voidsow.community.utils;

import com.voidsow.community.entity.User;
import org.springframework.stereotype.Component;

@Component
public class HostHolder {
    public ThreadLocal<User> user = new ThreadLocal<>();
}
