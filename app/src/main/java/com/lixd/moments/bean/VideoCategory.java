package com.lixd.moments.bean;

import com.lixd.moments.bean.moments.MomentsBean;
import com.lixd.moments.bean.moments.User;
import com.lixd.moments.bean.moments.Video;

import java.util.List;

public class VideoCategory extends TextCategory {

    public List<Video> videos;

    public VideoCategory(int type, long date, User user,MomentsBean bean) {
        super(type, date, user,bean);
    }
}
