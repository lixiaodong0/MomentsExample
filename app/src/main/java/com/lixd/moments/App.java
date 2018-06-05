package com.lixd.moments;

import android.app.Application;
import android.content.Context;

import com.lixd.moments.bean.moments.User;

public class App extends Application {
    public static Context sContext;
    public static App sApp;
    private User currntUser;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        sApp = this;
        //默认用户
        currntUser = new User();
        currntUser.id = 973166944;
        currntUser.name = "落魄的安卓开发";
        currntUser.img_url = "http://5b0988e595225.cdn.sohucs.com/images/20171219/7d8a94b9450e47808e5048cffd60a9b1.jpeg";
    }

    public void upadteUser(User user) {
        currntUser = user;
    }

    public User getUser() {
        return currntUser;
    }

    public static App getInstance() {
        return sApp;
    }
}
