package com.lixd.moments.bean;

import com.lixd.moments.bean.moments.Comment;
import com.lixd.moments.bean.moments.Like;
import com.lixd.moments.bean.moments.MomentsBean;

import java.util.List;

/**
 * 评论类型
 * 包含评论和点赞
 */
public class CommentCategory {
    public List<Like> likes;
    public List<Comment> comments;
    public MomentsBean srcBean;
}
