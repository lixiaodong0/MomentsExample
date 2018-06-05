package com.lixd.moments.bean.moments;

public class Share {
    public static final int WEB_TYPE = 1;
    public static final int VIDEO_TYPE = 2;
    public static final int MUSIC_TYPE = 3;

    //1:网页分享    2: 视频分享     3:音乐分享
    public int type;
    //跳转的链接
    public String url = "";
    //分享的图片
    public String icon = "";
    //标题(只有音乐分享才会出现)
    public String title = "";
    //分享的内容
    public String content = "";
    //分享的App
    public String app="";
}
