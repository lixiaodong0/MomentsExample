package com.lixd.moments.bean;

import com.lixd.moments.bean.moments.Image;
import com.lixd.moments.bean.moments.MomentsBean;
import com.lixd.moments.bean.moments.User;

import java.util.List;

/**
 * 图文类型
 * 包含图片和文本
 */
public class ImageTextCategory extends TextCategory {
    public List<Image> images; //图片列表

    public ImageTextCategory(int type, long date, User user,MomentsBean bean) {
        super(type, date, user,bean);
    }
}
