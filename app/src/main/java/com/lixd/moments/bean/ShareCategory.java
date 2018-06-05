package com.lixd.moments.bean;

import com.lixd.moments.bean.moments.MomentsBean;
import com.lixd.moments.bean.moments.Share;
import com.lixd.moments.bean.moments.User;

public class ShareCategory extends TextCategory {

    public Share share;

    public ShareCategory(int type, long date, User user,MomentsBean bean) {
        super(type, date, user,bean);
    }
}
