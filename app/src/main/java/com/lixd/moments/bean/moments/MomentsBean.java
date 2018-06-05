package com.lixd.moments.bean.moments;


import java.util.List;

public class MomentsBean {
    public static final int TEXT_TYPE = 1; //文本类型      ( type + date + content + user + like_list +  comment_list)
    public static final int IMG_TEXT_TYPE = 2; //图文类型  ( type + date + content + user + img_list + like_list +  comment_list)
    public static final int VIDEO_TYPE = 3;     //视频类型 ( type + date + content + user + video_list + like_list +  comment_list)
    public static final int SHARE_TYPE = 4;     //分享类型 ( type + date + content + user + share + like_list +  comment_list)
    public int type;  //类型
    public long date;   //日期
    public String content; //内容

    public Share share; //分享
    public List<Image> img_list; //图片列表
    public List<Video> video_list;//视频列表(微信采用做法是:一条动态只能放一条视频)
    public List<Comment> comment_list; //评论列表
    public List<Like> like_list;       //点赞列表
    public User user;                  //用户信息
}
