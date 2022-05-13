package com.voidsow.community;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

import java.time.Duration;
import java.util.List;

@SpringBootTest
public class RedisTests {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testString() {
        redisTemplate.opsForValue().set("test", 100);
        System.out.println(redisTemplate.opsForValue().get("test"));
        redisTemplate.opsForValue().increment("test");
        System.out.println(redisTemplate.opsForValue().get("test"));
    }

    @Test
    public void testHash() {
        redisTemplate.opsForHash().put("testHash", "hash", 1);
        System.out.println(redisTemplate.opsForHash().get("testHash", "hash"));
        redisTemplate.expire("test", Duration.ZERO);
    }

    @Test
    public void testTx() {
        List<Object> execute = redisTemplate.execute(new SessionCallback<>() {
            @Override
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                operations.opsForSet().add("testTx", 1, 2, 3);
                operations.opsForSet().members("testTx");
                return operations.exec();
            }
        });
        System.out.println(execute);
    }
}
