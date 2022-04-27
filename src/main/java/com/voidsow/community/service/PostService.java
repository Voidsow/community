package com.voidsow.community.service;

import com.voidsow.community.dto.Page;
import com.voidsow.community.entity.Post;
import com.voidsow.community.entity.PostExample;
import com.voidsow.community.mapper.PostMapper;
import com.voidsow.community.mapper.UserMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostService {
    PostMapper postMapper;
    UserMapper userMapper;

    @Value("${community.page.size}")
    int pageSize;

    @Value("${community.page.num}")
    int pageNum;

    @Autowired
    public PostService(PostMapper postMapper, UserMapper userMapper) {
        this.postMapper = postMapper;
        this.userMapper = userMapper;
    }

    public Map<String, Object> getPosts(Integer uid, int pageNo) {
        PostExample postExample = new PostExample();
        if (uid != null)
            postExample.createCriteria().andUidEqualTo(uid);
        Map<String, Object> map = new HashMap<>();
        List<Post> posts = postMapper.selectByExampleWithBLOBsWithRowbounds(
                postExample, new RowBounds((pageNo - 1) * pageSize, pageSize));
        ArrayList<Object> users = new ArrayList<>();
        posts.forEach(x -> users.add(userMapper.selectByPrimaryKey(x.getUid())));
        map.put("posts", posts);
        map.put("users", users);
        map.put("page", new Page(postMapper.countByExample(postExample),
                pageNo, pageSize, pageNum));
        return map;
    }

    public long getCount(Integer uid) {
        PostExample postExample = new PostExample();
        if (uid != null)
            postExample.createCriteria().andUidEqualTo(uid);
        return postMapper.countByExample(postExample);
    }

    public Post get(Integer id) {
        return postMapper.selectByPrimaryKey(id);
    }

    public void add(Post post) {
        //转义标签，防止脚本攻击
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));
        postMapper.insertSelective(post);
    }
}
