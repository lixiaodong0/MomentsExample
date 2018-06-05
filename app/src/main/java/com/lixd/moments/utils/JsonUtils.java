package com.lixd.moments.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lixd.moments.App;
import com.lixd.moments.bean.CommentCategory;
import com.lixd.moments.bean.ImageTextCategory;
import com.lixd.moments.bean.ShareCategory;
import com.lixd.moments.bean.TextCategory;
import com.lixd.moments.bean.VideoCategory;
import com.lixd.moments.bean.moments.Comment;
import com.lixd.moments.bean.moments.Like;
import com.lixd.moments.bean.moments.MomentsBean;
import com.lixd.moments.bean.moments.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    private final Gson gson;
    private static JsonUtils sInstance;

    private List<MomentsBean> momentsBeans;

    /**
     * 添加用户评论
     *  @param user    评论人用户信息
     * @param content 内容
     */
    public List<Object> addUserComment(MomentsBean bean, User user, String content) {
        return replyUserComment(bean, user, content, null);
    }

    /**
     * 回复用户评论
     *  @param from    评论人用户信息
     * @param content 内容
     * @param to      被评论人用户信息
     */
    public List<Object> replyUserComment(MomentsBean bean, User from, String content, User to) {
        if (isEmpty(momentsBeans))
            return null;
        int index = findTargetIndex(bean);
        if (index != -1) {
            MomentsBean momentsBean = momentsBeans.get(index);
            if (momentsBean.comment_list == null) {
                momentsBean.comment_list = new ArrayList<>();
            }
            Comment comment = Comment.createComment(from, content, to);
            momentsBean.comment_list.add(comment);
        }
        return foramtData(momentsBeans);
    }

    /**
     * 添加点赞用户
     *
     * @param bean 索引
     * @param user 用户信息
     */
    public List<Object> addLikeUser(MomentsBean bean, User user) {
        if (isEmpty(momentsBeans))
            return null;
        int index = findTargetIndex(bean);
        if (index != -1) {
            MomentsBean momentsBean = momentsBeans.get(index);
            if (momentsBean.like_list == null) {
                momentsBean.like_list = new ArrayList<>();
            }
            Like like = Like.createLike(user);
            momentsBean.like_list.add(like);
        }
        return foramtData(momentsBeans);

    }


    /**
     * 根据匹配用户id,和日期来查询index
     *
     * @param bean
     * @return
     */
    private int findTargetIndex(MomentsBean bean) {
        for (int count = momentsBeans.size(), i = 0; i < count; i++) {
            MomentsBean momentsBean = momentsBeans.get(i);
            if (momentsBean.date == bean.date && momentsBean.user.id == bean.user.id) {
                return i;
            }
        }
        return -1;
    }


    /**
     * 取消点赞用户
     *
     * @param bean 索引
     * @param user 用户信息
     */
    public List<Object> cancelLikeUser(MomentsBean bean, User user) {
        if (isEmpty(momentsBeans))
            return null;
        int index = findTargetIndex(bean);
        if (index != -1) {
            MomentsBean momentsBean = momentsBeans.get(index);
            if (momentsBean.like_list == null) {
                momentsBean.like_list = new ArrayList<>();
            }
            int removeIndex = -1;

            for (int count = momentsBean.like_list.size(), i = 0; i < count; i++) {
                Like like = momentsBean.like_list.get(i);
                if (like.user.id == user.id) {
                    //同一个用户
                    removeIndex = i;
                    break;
                }
            }

            if (removeIndex != -1) {
                momentsBean.like_list.remove(removeIndex);

            }
        }
        return foramtData(momentsBeans);
    }

    /**
     * 删除自己的朋友圈
     *
     * @param myUser
     */
    public List<Object> deleteMyMoments(MomentsBean bean, User myUser) {
        if (isEmpty(momentsBeans))
            return null;
        int index = findTargetIndex(bean);
        if (index != -1) {
            if (myUser.id == momentsBeans.get(index).user.id) {
                momentsBeans.remove(index);
            }
        }
        return foramtData(momentsBeans);
    }

    private JsonUtils() {
        gson = new Gson();
    }

    public static final JsonUtils getInstance() {
        if (sInstance == null) {
            sInstance = new JsonUtils();
        }
        return sInstance;
    }

    public List<Object> getData() {
        try {
            InputStream is = App.sContext.getAssets().open("JSON");
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            os.flush();
            os.close();
            is.close();
            String json = os.toString();
            momentsBeans = parseJson(json);
            if (!isEmpty(momentsBeans)) {
                return foramtData(momentsBeans);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<Object> foramtData(List<MomentsBean> data) {
        if (data == null || data.size() <= 0) {
            return null;
        }
        List<Object> list = new ArrayList<>();
        for (MomentsBean bean : data) {
            switch (bean.type) {
                case MomentsBean.TEXT_TYPE:
                    TextCategory textCategory = new TextCategory(bean.type, bean.date, bean.user, bean);
                    textCategory.content = bean.content;
                    textCategory.isExistCommend = !isEmpty(bean.like_list)  || !isEmpty(bean.comment_list )  ? true : false;
                    list.add(textCategory);
                    break;
                case MomentsBean.IMG_TEXT_TYPE:
                    ImageTextCategory imgCategory = new ImageTextCategory(bean.type, bean.date, bean.user, bean);
                    imgCategory.content = bean.content;
                    imgCategory.isExistCommend = !isEmpty(bean.like_list)  || !isEmpty(bean.comment_list )  ? true : false;
                    imgCategory.images = bean.img_list;
                    list.add(imgCategory);
                    break;
                case MomentsBean.VIDEO_TYPE:
                    VideoCategory videoCategory = new VideoCategory(bean.type, bean.date, bean.user, bean);
                    videoCategory.content = bean.content;
                    videoCategory.isExistCommend = !isEmpty(bean.like_list)  || !isEmpty(bean.comment_list )  ? true : false;
                    videoCategory.videos = bean.video_list;
                    list.add(videoCategory);
                    break;

                case MomentsBean.SHARE_TYPE:
                    ShareCategory shareCategory = new ShareCategory(bean.type, bean.date, bean.user, bean);
                    shareCategory.content = bean.content;
                    shareCategory.isExistCommend = !isEmpty(bean.like_list)  || !isEmpty(bean.comment_list )  ? true : false;
                    shareCategory.share = bean.share;
                    list.add(shareCategory);
                    break;
            }

            if (!isEmpty(bean.like_list) || !isEmpty(bean.comment_list)) {
                CommentCategory commentCategory = new CommentCategory();
                commentCategory.likes = bean.like_list;
                commentCategory.comments = bean.comment_list;
                commentCategory.srcBean = bean;
                list.add(commentCategory);
            }

        }
        return list;
    }


    private boolean isEmpty(List list) {
        if (list == null || list.size() <= 0) {
            return true;
        }
        return false;
    }

    private List<MomentsBean> parseJson(String JSON) {
        List<MomentsBean> list = gson.fromJson(JSON, new TypeToken<List<MomentsBean>>() {
        }.getType());
        return list;
    }
}
