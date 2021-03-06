package com.voidsow.community.service;

import com.voidsow.community.entity.User;
import com.voidsow.community.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class FollowService {
    UserMapper userMapper;
    RedisTemplate<String, Integer> redisTemplate;
    private static final String USER = "user:";
    private static final String FOLLOWER = ":follower";
    private static final String FOLLOWEE = ":followee";

    //uid的关注者
    private String getKey(int uid, String type) {
        StringBuilder builder = new StringBuilder();
        builder.append(USER);
        builder.append(uid);
        builder.append(type);
        return builder.toString();
    }

    //uid关注的人
    private String getFolloweeKey(int uid) {
        StringBuilder builder = new StringBuilder();
        builder.append(USER);
        builder.append(uid);
        builder.append(FOLLOWEE);
        return builder.toString();
    }

    @Autowired
    public FollowService(UserMapper userMapper, RedisTemplate<String, Integer> redisTemplate) {
        this.userMapper = userMapper;
        this.redisTemplate = redisTemplate;
    }

    public void follow(int userId, int followeeId) {
        String followeeKey = getKey(userId, FOLLOWEE);
        String followerKey = getKey(followeeId, FOLLOWER);
        redisTemplate.execute(new SessionCallback<>() {
            @Override
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                Date now = new Date();
                operations.opsForZSet().add(followeeKey, followeeId, now.getTime());
                operations.opsForZSet().add(followerKey, userId, now.getTime());
                return redisTemplate.exec();
            }
        });
    }

    public void cancelFollow(int userId, int followeeId) {
        String followeeKey = getKey(userId, FOLLOWEE);
        String followerKey = getKey(followeeId, FOLLOWER);
        redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                operations.opsForZSet().remove(followeeKey, followeeId);
                operations.opsForZSet().remove(followerKey, userId);
                return redisTemplate.exec();
            }
        });
    }

    public boolean isFollower(int followee, int follower) {
        return redisTemplate.opsForZSet().score(getKey(followee, FOLLOWER), follower) != null;
    }

    public Long countFollower(int uid) {
        return redisTemplate.opsForZSet().zCard(getKey(uid, FOLLOWER));
    }

    public Long countFollowee(int uid) {
        return redisTemplate.opsForZSet().zCard(getKey(uid, FOLLOWEE));
    }

    private List<User> findFollows(int uid, String type, int offset, int limit) {
        Set<Integer> followers = redisTemplate.opsForZSet().reverseRange(getKey(uid, type), offset, offset + limit - 1);
        if (followers == null)
            return null;
        List<User> users = new ArrayList<>();
        followers.forEach(follower -> users.add(userMapper.selectByPrimaryKey(follower)));
        return users;
    }

    public List<User> findFollowers(int uid, int offset, int limit) {
        return findFollows(uid, FOLLOWER, offset, limit);
    }

    public List<User> findFollowees(int uid, int offset, int limit) {
        return findFollows(uid, FOLLOWEE, offset, limit);
    }
}