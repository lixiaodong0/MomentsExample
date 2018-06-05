package com.lixd.moments.adapter;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lixd.moments.App;
import com.lixd.moments.R;
import com.lixd.moments.bean.TextCategory;
import com.lixd.moments.callback.MomentsCallback;
import com.lixd.moments.utils.TimeUtils;


public class TextItemViewBinder<T extends TextCategory> extends BaseItemViewBinder<T, TextItemViewBinder.ViewHolder> {

    protected MomentsCallback.TextItemCallback callback;

    public TextItemViewBinder(MomentsCallback.TextItemCallback callback) {
        this.callback = callback;
    }


    @Override
    public ViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.moments_item_text, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void bindViewHolder(final ViewHolder holder, final T item) {
        if(holder.tvDelete != null){
            holder.tvDelete.setVisibility(item.user.id == App.getInstance().getUser().id ? View.VISIBLE : View.GONE);
        }
        holder.tvTime.setText(TimeUtils.getFriendlyTimeSpanByNow(item.date));
        holder.tvUserName.setText(item.user.name);
        holder.tvContent.setText(item.content);
        final ImageView img = holder.imgUser;
        Glide.with(img.getContext())
                .load(item.user.img_url)
                .into(img);
        holder.splitLine.setVisibility(item.isExistCommend ? View.GONE : View.VISIBLE);

        holder.tvContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (callback != null) {
                    String text = holder.tvContent.getText().toString();
                    callback.onLongClickText(text);
                }
                return true;
            }
        });


        holder.imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onClickUser();
                }
            }
        });


        holder.tvUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onClickUser();
                }
            }
        });


        holder.imgComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onClickComment(v, holder, item.srcBean);
                }
            }
        });

        if (holder.tvDelete != null) {
            holder.tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onClickDelete(item.srcBean);
                    }
                }
            });
        }
    }

    public static class ViewHolder extends BaseItemViewBinder.ViewHolder {
        private final TextView tvContent;
        private final TextView tvTime;
        private final TextView tvUserName;
        private final ImageView imgUser;
        private final View splitLine;
        private final @Nullable TextView tvDelete;
        private final ImageView imgComment;

        public ViewHolder(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            imgUser = itemView.findViewById(R.id.img_user);
            splitLine = itemView.findViewById(R.id.split_line);
            imgComment = itemView.findViewById(R.id.img_comment);
            tvDelete = itemView.findViewById(R.id.tv_delete);
        }
    }
}
