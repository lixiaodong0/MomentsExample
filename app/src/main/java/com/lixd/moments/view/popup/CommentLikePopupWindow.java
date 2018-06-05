package com.lixd.moments.view.popup;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lixd.moments.App;
import com.lixd.moments.R;
import com.lixd.moments.bean.moments.Like;
import com.lixd.moments.bean.moments.MomentsBean;
import com.lixd.moments.utils.CommonUtils;

/**
 * 点赞评论PopupWindow
 */
public class CommentLikePopupWindow extends PopupWindow {
    /**
     * 用来确保点击的View是同一个
     */
    private float uniquenessItemViewValue;
    private float uniquenessChildViewValue;
    private Context context;
    private final View rootView;
    private View clickLeft;
    private View clicRight;
    private ImageView imgLike;
    private TextView tvLike;
    private ImageView imgComment;
    private TextView tvComment;
    //是否是打开状态
    private boolean isOpen = false;
    //是否是同一个位置点击
    private boolean isSameLoctionClick = false;
    //是否点赞过
    private boolean isLike;

    public CommentLikePopupWindow(Context context) {
        this.context = context;
        rootView = LayoutInflater.from(context).inflate(R.layout.popup_comment, null, false);
        initWindowParams();
        setContentView(rootView);
        initView(rootView);
        initEvent();
    }

    private void initWindowParams() {
        // 设置PopupWindow的背景
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        setTouchable(true);
        setWidth(CommonUtils.dp2px(context, 200));
        setHeight(CommonUtils.dp2px(context, 40));
    }

    public void toggle(View view, RecyclerView.ViewHolder holder) {
        float value = holder.itemView.getBottom();
        if (uniquenessItemViewValue != value) {
            //点击的不是同一个View
            open(view);
            isOpen = true;
            isSameLoctionClick = false;
        } else {
            isSameLoctionClick = true;
            //点击的是相同的View
            if (isOpen) {
                close();
                isOpen = false;
            } else {
                open(view);
                isOpen = true;
            }
        }
        uniquenessItemViewValue = value;
    }

    public void updateClickLocation(View targetView, RecyclerView.ViewHolder holder) {
        float value = holder.itemView.getBottom();
        int childValue = targetView.getBottom();
        if ((uniquenessItemViewValue == value && uniquenessChildViewValue != childValue)
                || uniquenessItemViewValue != value) {
            //点击的不是同一个View
            isSameLoctionClick = false;
        } else {
            //点击的是相同的View
            isSameLoctionClick = true;
        }
        uniquenessItemViewValue = value;
        uniquenessChildViewValue = childValue;
    }


    private void open(View view) {
        int offsetX = -(getWidth() + CommonUtils.dp2px(context, 10));
        int offsetY = -(getHeight() + view.getMeasuredHeight()) / 2;
        showAsDropDown(view, offsetX, offsetY, Gravity.START);

        //开启平移动画
        ValueAnimator animator = ValueAnimator.ofInt(0, CommonUtils.dp2px(context, 200));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                rootView.setTranslationX(CommonUtils.dp2px(context, 200) - value);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                rootView.setTranslationX(CommonUtils.dp2px(context, 200));
                rootView.setVisibility(View.VISIBLE);
            }
        });
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }

    /**
     * 是否是同一个位置点击
     *
     * @return
     */
    public boolean isSameLoctionClick() {
        return isSameLoctionClick;
    }

    private void close() {
        dismiss();
    }

    private void initView(View rootView) {
        clickLeft = rootView.findViewById(R.id.click_left);
        clicRight = rootView.findViewById(R.id.click_right);
        imgLike = rootView.findViewById(R.id.img_like);
        tvLike = rootView.findViewById(R.id.tv_like);
        imgComment = rootView.findViewById(R.id.img_comment);
        tvComment = rootView.findViewById(R.id.tv_comment);
    }


    public void initData(MomentsBean bean) {
        if (bean.like_list == null) {
            isLike = false;
        } else {
            isLike = false;
            for (Like like : bean.like_list) {
                if (like.user.id == App.getInstance().getUser().id) {
                    isLike = true;
                    break;
                }
            }
        }
        tvLike.setText(isLike ? "取消" : "赞");
    }

    private void initEvent() {
        clickLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
                if (listener != null) {
                    listener.onLike(isLike);
                }
            }
        });
        clicRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
                if (listener != null) {
                    listener.onComment(isSameLoctionClick);
                }
            }
        });
    }

    private OnClickListener listener;

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnClickListener {
        void onLike(boolean isLike);

        void onComment(boolean isSameLoctionClick);
    }
}
