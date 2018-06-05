package com.lixd.moments.bean.moments;

/**
 * 评论列表
 */
public class Comment {
    public String text;  //内容
    public User from;   //评论人
    public User to;     //被评论人


    public static Comment createComment(User from, String text, User to) {
        Comment comment = new Comment();
        comment.from = from;
        comment.to = to;
        comment.text = text;
        return comment;
    }
}
