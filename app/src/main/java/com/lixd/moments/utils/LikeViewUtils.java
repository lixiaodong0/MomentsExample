package com.lixd.moments.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.lixd.moments.R;
import com.lixd.moments.bean.moments.Like;

import java.util.List;

public class LikeViewUtils {
    private Context context;
    private List<Like> likes;
    private LikeClickListener listener;

    public LikeViewUtils(Builder builder) {
        context = builder.context;
        likes = builder.likes;
        listener = builder.listener;
    }

    public TextView create() {
        TextView textView = new TextView(context);
        textView.setText(buildCommentContent());
        textView.setTextSize(16);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundResource(R.drawable.text_selector);
        textView.setHighlightColor(ContextCompat.getColor(context, R.color.colorAccent));
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        return textView;
    }

    public void setContent(TextView textView) {
        if (textView != null) {
            textView.setText(buildCommentContent());
            textView.setHighlightColor(ContextCompat.getColor(context, R.color.colorAccent));
            textView.setBackgroundResource(R.drawable.text_selector);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    private CharSequence buildCommentContent() {
        if (CommonUtils.isEmpty(likes)) {
            return "";
        }

        String loveFlag = "<img>，";
        StringBuilder builder = new StringBuilder(loveFlag);
        for (int count = likes.size(), i = 0; i < count; i++) {
            Like like = likes.get(i);
            builder.append(like.user.name);
            if (i != count - 1) {
                builder.append("，");
            }
        }
        SpannableString sp = new SpannableString(builder.toString());
        sp.setSpan(new ImageSpan(context, R.mipmap.love), 0, loveFlag.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        int lastIndex = 0 + loveFlag.length();
        for (int count = likes.size(), i = 0; i < count; i++) {
            Like like = likes.get(i);
            int startIndex = lastIndex;
            int endIndex = startIndex + like.user.name.length();
            sp.setSpan(new LikeClickableSpan(i), startIndex, endIndex, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            sp.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.likeColor)), startIndex, endIndex, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

            lastIndex = endIndex + "，".length();
        }
        return sp;
    }


    private class LikeClickableSpan extends ClickableSpan {
        private int index;

        public LikeClickableSpan(int index) {
            this.index = index;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(ContextCompat.getColor(context, R.color.colorAccent));
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            if (listener != null) {
                Like like = likes.get(index);
                listener.onClick(like);
            }
        }
    }

    public static class Builder {
        private Context context;
        private List<Like> likes;
        private LikeClickListener listener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setLikes(List<Like> data) {
            this.likes = data;
            return this;
        }

        public LikeViewUtils build() {
            return new LikeViewUtils(this);
        }

        public Builder setLikeClickListener(LikeClickListener listener) {
            this.listener = listener;
            return this;
        }
    }


    public interface LikeClickListener {
        void onClick(Like like);
    }

}
