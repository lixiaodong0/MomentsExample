package com.lixd.moments.adapter;

import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lixd.moments.App;
import com.lixd.moments.R;
import com.lixd.moments.bean.CommentCategory;
import com.lixd.moments.bean.moments.Comment;
import com.lixd.moments.bean.moments.Like;
import com.lixd.moments.callback.MomentsCallback;
import com.lixd.moments.utils.CommentViewUtils;
import com.lixd.moments.utils.CommonUtils;
import com.lixd.moments.utils.LikeViewUtils;

import java.util.List;

public class CommentItemViewBinder extends BaseItemViewBinder<CommentCategory, CommentItemViewBinder.ViewHolder> {

    private MomentsCallback.CommentItemCallback callback;

    public CommentItemViewBinder(MomentsCallback.CommentItemCallback callback) {
        this.callback = callback;
    }

    @Override
    public ViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.moments_item_comment, parent, false);
        return new CommentItemViewBinder.ViewHolder(view);
    }

    @Override
    public void bindViewHolder(final ViewHolder holder, final CommentCategory item) {
        if (isEmpty(item.likes)) {
            holder.tvLinkeContainer.setVisibility(View.GONE);
            holder.likeSplitLine.setVisibility(View.GONE);
        } else if (!isEmpty(item.likes) && isEmpty(item.comments)) {
            holder.tvLinkeContainer.setVisibility(View.VISIBLE);
            holder.likeSplitLine.setVisibility(View.GONE);
        } else {
            holder.tvLinkeContainer.setVisibility(View.VISIBLE);
            holder.likeSplitLine.setVisibility(View.VISIBLE);
        }
        holder.llCommentContainer.setVisibility(isEmpty(item.comments) ? View.GONE : View.VISIBLE);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) holder.llCommentContainer.getLayoutParams();
        if (!isEmpty(item.likes)) {
            new LikeViewUtils.Builder(holder.tvLinkeContainer.getContext())
                    .setLikes(item.likes)
                    .setLikeClickListener(new LikeViewUtils.LikeClickListener() {
                        @Override
                        public void onClick(Like like) {
                            Toast.makeText(App.sContext, "" + like.user.name, Toast.LENGTH_SHORT).show();
                            Log.e("LikeClickListener", "user = " + like.user.name);
                        }
                    })
                    .build()
                    .setContent(holder.tvLinkeContainer);

            layoutParams.topMargin = CommonUtils.dp2px(holder.llCommentContainer.getContext(), 5);
        } else {
            layoutParams.topMargin = CommonUtils.dp2px(holder.llCommentContainer.getContext(), 10);
        }
        holder.llCommentContainer.setLayoutParams(layoutParams);

        if (!isEmpty(item.comments)) {
            holder.llCommentContainer.removeAllViews();
            for (int count = item.comments.size(), i = 0; i < count; i++) {
                final Comment comment = item.comments.get(i);
                TextView view = new CommentViewUtils.Builder(holder.llCommentContainer.getContext())
                        .setContent(comment.text)
                        .setFromUserName(comment.from == null ? "" : comment.from.name)
                        .setToUserName(comment.to == null ? "" : comment.to.name)
                        .setOnClickListener(new CommentViewUtils.OnClickListener() {
                            @Override
                            public void onFromUserClick() {
                                Log.e("CommentViewUtils", "onFromUserClick = " + comment.from.name);
                                Toast.makeText(App.sContext, "" + comment.from.name, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onToUserClick() {
                                Log.e("CommentViewUtils", "onToUserClick = " + comment.to.name);
                                Toast.makeText(App.sContext, "" + comment.to.name, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onContentClick(View v) {
                                Log.e("CommentViewUtils", "onContentClick = " + comment.text);
                                if (callback != null) {
                                    if (comment.from.id != App.getInstance().getUser().id) {
                                        callback.onReplyCommentUser(v, holder, comment.from, item.srcBean);
                                    } else {
                                        // TODO: 2018/5/30  不能回复自己的评论,只能删除
                                        Toast.makeText(App.sContext, "不能回复自己的评论,删除功能暂未开通!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        })
                        .build()
                        .create();
                holder.llCommentContainer.addView(view);
            }
        }
    }

    static class ViewHolder extends BaseItemViewBinder.ViewHolder {
        private final LinearLayout llCommentContainer;
        private final TextView tvLinkeContainer;
        private final View likeSplitLine;

        public ViewHolder(View itemView) {
            super(itemView);
            tvLinkeContainer = itemView.findViewById(R.id.tv_like_container);
            likeSplitLine = itemView.findViewById(R.id.like_split_line);
            llCommentContainer = itemView.findViewById(R.id.ll_comment_container);
        }
    }

    private boolean isEmpty(List data) {
        if (data == null || data.size() <= 0) {
            return true;
        }
        return false;
    }
}
