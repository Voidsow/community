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
import java.util.*;

import static com.voidsow.community.constant.Activation.*;
import static com.voidsow.community.service.Constant.*;

@Service
public class UserService {
    UserMapper userMapper;
    MailClient mailClient;

    @Autowired
    public UserService(UserMapper userMapper, MailClient mailClient) {
        this.userMapper = userMapper;
        this.mailClient = mailClient;
    }

    public User findById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
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

    String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}

final class Constant {
    //用户激活状态
    static int UNACTIVATED = 0;
    static int NORMAL = 1;

    //用户类型
    static int COMMON = 0;
}