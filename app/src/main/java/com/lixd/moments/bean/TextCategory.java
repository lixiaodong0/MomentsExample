package com.lixd.moments.bean;

import com.lixd.moments.bean.moments.MomentsBean;
import com.lixd.moments.bean.moments.User;

/**
 * 文本类型
 * 不包含图片
 */
public class TextCategory extends Categroy {
    public String content; //内容

    public TextCategory(int type, long date, User user, MomentsBean bean) {
        super(type, date, user, bean);
    }
}
