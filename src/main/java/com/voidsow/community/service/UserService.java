package com.voidsow.community.service;

import com.voidsow.community.constant.Activation;
import com.voidsow.community.entity.User;
import com.voidsow.community.entity.UserExample;
import com.voidsow.community.mapper.UserMapper;
import com.voidsow.community.utils.MailClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.mail.MessagingException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.voidsow.community.constant.Activation.*;
import static com.voidsow.community.constant.Constant.*;
import static com.voidsow.community.utils.Authorizer.generateUUID;

@Service
public class UserService {
    UserMapper userMapper;
    MailClient mailClient;
    Key key;

    @Autowired
    public UserService(UserMapper userMapper, MailClient mailClient, Key key) {
        this.userMapper = userMapper;
        this.mailClient = mailClient;
        this.key = key;
    }

    public User findById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    public User findByName(String username) {
        UserExample userExample = new UserExample();
        userExample.or().andUsernameEqualTo(username);
        return userMapper.selectByExample(userExample).get(0);
    }

    public Map<String, Object> register(User user, String confirmPsw) throws MessagingException {
        Map<String, Object> map = new HashMap<>();
        if (user.getUsername() == null)
            map.put("usernameMsg", "用户名不能为空");
        if (user.getPassword() == null)
            map.put("passwordMsg", "密码不能为空");
        else if (!user.getPassword().equals(confirmPsw))
            map.put("confirmPswMsg", "两次输入密码不一致");
        if (user.getEmail() == null)
            map.put("emailMsg", "邮箱不能为空");
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameEqualTo(user.getUsername());
        if (!userMapper.selectByExample(userExample).isEmpty())
            map.put("usernameMsg", "用户名已经被使用");
        userExample.clear();
        userExample.createCriteria().andEmailEqualTo(user.getEmail());
        if (!userMapper.selectByExample(userExample).isEmpty())
            map.put("emailMsg", "邮箱已经被注册");
        //登录信息不合法则直接返回
        if (!map.isEmpty())
            return map;

        user.setSalt(generateUUID());
        user.setPassword(DigestUtils.md5DigestAsHex((user.getPassword() + user.getSalt()).getBytes()));
        //普通用户
        user.setType(COMMON);
        //未激活
        user.setStatus(UNACTIVATED);
        user.setActivationCode(generateUUID());
        user.setGmtCreate(new Date());
        user.setGmtModified(user.getGmtCreate());
        userMapper.insertSelective(user);
        mailClient.sendActivationEmail(user.getEmail(), user.getActivationCode());
        return map;
    }

    public Activation activate(String activationCode) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andActivationCodeEqualTo(activationCode);
        List<User> activated = userMapper.selectByExample(userExample);
        if (activated.isEmpty())
            return INVALID;
        else if (activated.get(0).getStatus() == UNACTIVATED) {
            User user = new User();
            user.setStatus(NORMAL);
            userMapper.updateByExampleSelective(user, userExample);
            return SUCCEESS;
        } else
            return ACTIVATED;
    }

    public Map<String, Object> login(String username, String password) {
        Map<String, Object> map = new HashMap<>();
        if (username == null || username.isEmpty()) {
            map.put("usernameMsg", "用户名不能为空");
        } else if (password == null || password.isEmpty()) {
            map.put("passwordMsg", "密码不能为空");
        }
        if (!map.isEmpty())
            return map;
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameEqualTo(username);
        List<User> users = userMapper.selectByExample(userExample);
        if (users.isEmpty()) {
            map.put("usernameMsg", "该账号不存在");
            return map;
        }
        User user = users.get(0);
        if (!DigestUtils.md5DigestAsHex((password + user.getSalt()).getBytes()).
                equals(user.getPassword())) {
            map.put("passwordMsg", "密码错误");
            return map;
        }
        map.put("user", user);
        return map;
    }

    public void updateHeader(int userId, String headerUrl) {
        User user = new User();
        user.setId(userId);
        user.setHeaderUrl(headerUrl);
        userMapper.updateByPrimaryKeySelective(user);
    }
}