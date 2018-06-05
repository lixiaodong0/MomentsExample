package com.lixd.moments.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lixd.moments.R;
import com.lixd.moments.bean.HeaderCategory;
import com.lixd.moments.callback.MomentsCallback;

public class HeadherItemViewBinder extends BaseItemViewBinder<HeaderCategory, HeadherItemViewBinder.ViewHolder> {
    private MomentsCallback.HeaderItemCallback callback;

    public HeadherItemViewBinder(MomentsCallback.HeaderItemCallback callback) {
        this.callback = callback;
    }

    @Override
    public ViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.moments_item_header, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, HeaderCategory item) {
        Glide.with(holder.imgMyUserIcon.getContext())
                .load(item.userIcon)
                .into(holder.imgMyUserIcon);
        holder.tvMyUserName.setText(item.userName);
        Glide.with(holder.imgHeaderBg.getContext())
                .load(item.bgUrl)
                .into(holder.imgHeaderBg);
    }

    static class ViewHolder extends BaseItemViewBinder.ViewHolder {

        private final ImageView imgHeaderBg;
        private final ImageView imgMyUserIcon;
        private final TextView tvMyUserName;

        public ViewHolder(View itemView) {
            super(itemView);
            imgHeaderBg = itemView.findViewById(R.id.img_header_bg);
            imgMyUserIcon = itemView.findViewById(R.id.img_my_user_icon);
            tvMyUserName = itemView.findViewById(R.id.tv_my_user_name);
        }
    }
}
