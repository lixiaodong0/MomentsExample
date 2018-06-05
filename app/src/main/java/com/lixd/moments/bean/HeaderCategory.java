package com.lixd.moments.bean;

import com.lixd.moments.R;

public class HeaderCategory {
    public int bgUrl;
    public String userName;
    public String userIcon;


    public static final HeaderCategory createTest() {
        HeaderCategory header = new HeaderCategory();
//        header.bgUrl = "http://img3.duitang.com/uploads/item/201504/27/20150427220929_ZXRCk.jpeg";
        header.bgUrl = R.mipmap.header_bg;
        header.userName = "落魄的安卓开发";
        header.userIcon = "http://5b0988e595225.cdn.sohucs.com/images/20171219/7d8a94b9450e47808e5048cffd60a9b1.jpeg";
        return header;
    }
}
