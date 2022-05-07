package com.voidsow.community.service;

import com.voidsow.community.entity.Comment;
import com.voidsow.community.entity.CommentExample;
import com.voidsow.community.entity.Post;
import com.voidsow.community.mapper.CommentMapper;
import com.voidsow.community.mapper.PostMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.List;

import static com.voidsow.community.constant.Constant.POST_LEVEL_ONE;

@Service
public class CommentService {
    CommentMapper commentMapper;
    PostMapper postMapper;

    @Autowired
    public CommentService(CommentMapper commentMapper, PostMapper postMapper) {
        this.commentMapper = commentMapper;
        this.postMapper = postMapper;
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

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void add(Comment comment, int postId) {
        Date curTime = new Date();
        comment.setGmtCreate(curTime);
        comment.setGmtModified(curTime);
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        commentMapper.insertSelective(comment);
        Post newValPost = new Post();
        newValPost.setId(postId);
        //修改最后回帖时间
        newValPost.setGmtModified(new Date());
        //若为一级评论则更新Post的commentNum字段
        if (comment.getType() == POST_LEVEL_ONE)
            newValPost.setCommentNum(postMapper.selectByPrimaryKey(
                    comment.getReplyTo()).getCommentNum() + 1);
        postMapper.updateByPrimaryKeySelective(newValPost);
     }
}