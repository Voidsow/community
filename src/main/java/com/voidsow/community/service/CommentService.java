package com.voidsow.community.service;

import com.voidsow.community.entity.Comment;
import com.voidsow.community.entity.CommentExample;
import com.voidsow.community.mapper.CommentMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    CommentMapper commentMapper;

    @Autowired
    public CommentService(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    public List<Comment> find(int type, int replyTo, int offset, int limit) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andTypeEqualTo(type).andReplyToEqualTo(replyTo);
        return commentMapper.selectByExampleWithBLOBsWithRowbounds(commentExample,
                new RowBounds(offset, limit));
    }

    public long getCount(int type, int replyTo) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andTypeEqualTo(type).andReplyToEqualTo(replyTo);
        return commentMapper.countByExample(commentExample);
    }
}