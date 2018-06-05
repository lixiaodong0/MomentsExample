package com.lixd.moments.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.lixd.moments.R;

public class CommentViewUtils {
    private Context context;
    private String content;
    private String fromUserName;
    private String toUserName;
    private OnClickListener listener;
    /**
     * 解决TextView同时设置ClickableSpan, OnClickListener
     * 回调ClickableSpan时,也会触发OnClickListener回调
     */
    private boolean isKeywordClick = false;

    public CommentViewUtils(Builder builder) {
        context = builder.context;
        content = builder.content;
        fromUserName = builder.fromUserName;
        toUserName = builder.toUserName;
        listener = builder.listener;
    }

    public TextView create() {
        TextView textView = new TextView(context);
        textView.setText(buildCommentContent());
        textView.setTextSize(16);
        textView.setTextColor(Color.BLACK);
        textView.setHighlightColor(ContextCompat.getColor(context, R.color.colorAccent));
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setBackgroundResource(R.drawable.text_selector);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null && !isKeywordClick) {
                    listener.onContentClick(v);
                }
                isKeywordClick = false;
            }
        });
        return textView;
    }

    private CharSequence buildCommentContent() {
        StringBuffer buffer = new StringBuffer();
        if (TextUtils.isEmpty(toUserName)) {
            //被评论人是空的
            buffer.append(fromUserName).append(":").append(content);
        } else {
            //被评论人不是空的
            buffer.append(fromUserName).append("回复").append(toUserName).append(":").append(content);
        }
        SpannableString sp = new SpannableString(buffer.toString());
        sp.setSpan(new FromUserClickableSpan(), 0, fromUserName.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.likeColor)), 0, fromUserName.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (!TextUtils.isEmpty(toUserName)) {
            String str = fromUserName + "回复";
            int startIndex = str.length();
            int endIndex = startIndex + toUserName.length();
            sp.setSpan(new ToUserClickableSpan(), startIndex, endIndex, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            sp.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.likeColor)), startIndex, endIndex, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return sp;
    }

    /**
     * 评论人点击
     */
    private class FromUserClickableSpan extends ClickableSpan {

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
            ds.setColor(ContextCompat.getColor(context, R.color.colorAccent));
        }

        @Override
        public void onClick(View widget) {
            if (listener != null) {
                listener.onFromUserClick();
            }
            isKeywordClick = true;
        }
    }

    /**
     * 被评论人点击
     */
    private class ToUserClickableSpan extends ClickableSpan {
        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            if (listener != null) {
                listener.onToUserClick();
            }
            isKeywordClick = true;
        }
    }

    public static class Builder {
        private Context context;
        private String content;
        private String fromUserName;
        private String toUserName;
        private OnClickListener listener;

        public Builder(Context context) {
            this.context = context;
        }

        public CommentViewUtils build() {
            return new CommentViewUtils(this);
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setFromUserName(String fromUserName) {
            this.fromUserName = fromUserName;
            return this;
        }

        public Builder setToUserName(String toUserName) {
            this.toUserName = toUserName;
            return this;
        }

        public Builder setOnClickListener(OnClickListener listener) {
            this.listener = listener;
            return this;
        }
    }

    public interface OnClickListener {
        void onFromUserClick();

        void onToUserClick();

        void onContentClick(View v);
    }

    public class OnClickListenerAdapter implements OnClickListener {
        @Override
        public void onFromUserClick() {

        }

        @Override
        public void onToUserClick() {

        }

        @Override
        public void onContentClick(View v) {

        }
    }
}
