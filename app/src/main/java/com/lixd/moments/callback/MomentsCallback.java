package com.lixd.moments.callback;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lixd.moments.bean.moments.MomentsBean;
import com.lixd.moments.bean.moments.User;

import java.util.List;

public interface MomentsCallback {


    interface TextItemCallback {
        /**
         * 当点击用户(头像,昵称)
         */
        void onClickUser();

        /**
         * 当点击删除(删除这条动态)
         *
         * @param item 用来索引数据源
         */
        void onClickDelete(MomentsBean item);

        /**
         * 当点击评论(发表评论)
         *
         * @param target 用来显示popup的位置
         * @param holder 用来确保唯一性,如果点击相同位置,需要关闭popup位置
         * @param bean   用来索引数据源
         */
        void onClickComment(View target, RecyclerView.ViewHolder holder, MomentsBean bean);

        /**
         * 当长按文本(复制,翻译等操作)
         *
         * @param text 文本内容
         */
        void onLongClickText(String text);
    }

    interface ImageTextItemCallback extends TextItemCallback {
        /**
         * 当点击图片
         *
         * @param view    被点击的View
         * @param index   点击的下标
         * @param urlList 图片列表
         */
        void onClickImg(View view, int index, List<String> urlList);
    }

    interface VideoItemCallback extends TextItemCallback {
        /**
         * 当点击视频
         *
         * @param videoUrl 视频链接
         */
        void onClickVideo(String videoUrl);
    }

    interface ShareItemCallback extends TextItemCallback {
        /**
         * 当点击分享
         *
         * @param url 分享的链接
         */
        void onClickShare(String url);
    }

    interface CommentItemCallback {
        /**
         * 当点击评论用户
         */
        void onClickCommentUser();

        /**
         * 点答复评论用户
         *
         * @param targetView
         * @param holder
         * @param to         答复用户的信息数据
         * @param bean       用来索引数据源
         */
        void onReplyCommentUser(View targetView, RecyclerView.ViewHolder holder, User to, MomentsBean bean);

        /**
         * 当点击点赞用户
         */
        void onClickLikeUser();
    }

    interface HeaderItemCallback {
        /**
         * 点击自己的头像
         */
        void onClickMyIcon();

        /**
         * 点击背景图片(更换封面)
         */
        void onClickBg();
    }
}
