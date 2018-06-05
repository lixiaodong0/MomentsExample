package com.lixd.moments.bean;

import com.lixd.moments.bean.moments.MomentsBean;
import com.lixd.moments.bean.moments.User;

public class Categroy {
    public int type;  //类型
    public long date;   //日期
    public User user;                  //用户信息
    public boolean isExistCommend; //是否存在评论列表
    public MomentsBean srcBean;

    public Categroy(int type, long date, User user,MomentsBean bean) {
        this.type = type;
        this.date = date;
        this.user = user;
        this.srcBean = bean;
    }
}
