package com.lixd.moments.bean.moments;

public class Like {
    public User user;


    public static Like createLike(User user){
        Like like = new Like();
        like.user = user;
        return like;
    }
}
