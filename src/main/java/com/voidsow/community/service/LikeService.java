package com.voidsow.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    private RedisTemplate<String, Object> redisTemplate;
    private final String LIKE = "like";

    private String getKey(int type, int id) {
        StringBuilder builder = new StringBuilder();
        builder.append(LIKE);
        builder.append(':');
        builder.append(type);
        builder.append(':');
        builder.append(id);
        return builder.toString();
    }

    @Autowired
    public LikeService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String likeOrNot(int type, int id, int userId) {
        String key = getKey(type, id);
        Boolean member = redisTemplate.opsForSet().isMember(key, userId);
        if (member) {
            redisTemplate.opsForSet().remove(key, userId);
            return "no";
        } else {
            redisTemplate.opsForSet().add(key, userId);
            return "yes";
        }
    }

    public long likeNum(int type, int id) {
        return redisTemplate.opsForSet().size(getKey(type, id));
    }

    public boolean like(int type, int id, int uid) {
        return redisTemplate.opsForSet().isMember(getKey(type, id), uid);
    }
}
