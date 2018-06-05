package com.lixd.moments.adapter;

import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lixd.moments.R;
import com.lixd.moments.bean.ShareCategory;
import com.lixd.moments.bean.moments.Share;
import com.lixd.moments.callback.MomentsCallback;

public class ShareItemViewBinder extends TextItemViewBinder<ShareCategory> {

    private MomentsCallback.ShareItemCallback callback;

    public ShareItemViewBinder(MomentsCallback.ShareItemCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public TextItemViewBinder.ViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.moments_item_share, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void bindViewHolder(TextItemViewBinder.ViewHolder textHolder, final ShareCategory item) {
        super.bindViewHolder(textHolder, item);

        if(textHolder instanceof ViewHolder){
            ViewHolder holder = (ViewHolder) textHolder;

            switch (item.share.type) {
                case Share.MUSIC_TYPE:
                case Share.VIDEO_TYPE:
                    holder.groupMedia.setVisibility(View.VISIBLE);
                    holder.tvShareContent.setVisibility(View.GONE);
                    holder.tvMediaTitle.setText(item.share.title);
                    holder.tvMediaDes.setText(item.share.content);
                    break;
                default:
                    holder.groupMedia.setVisibility(View.GONE);
                    holder.tvShareContent.setVisibility(View.VISIBLE);
                    holder.tvShareContent.setText(item.share.content);
            }
            holder.tvShareApp.setText(item.share.app);


            Glide.with(holder.imgShareIcon.getContext())
                    .load(item.share.icon)
                    .into(holder.imgShareIcon);

            holder.clShareLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onClickShare(item.share.url);
                    }
                }
            });
        }
    }


    static class ViewHolder extends TextItemViewBinder.ViewHolder{
        private final ImageView imgShareIcon;
        private final TextView tvShareContent;
        private final TextView tvMediaTitle;
        private final TextView tvMediaDes;
        private final ImageView imgPlayIcon;
        private final android.support.constraint.Group groupMedia;
        private final TextView tvShareApp;
        private final ConstraintLayout clShareLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            clShareLayout = itemView.findViewById(R.id.cl_share_layout);
            tvShareApp = itemView.findViewById(R.id.tv_share_app);
            imgShareIcon = itemView.findViewById(R.id.img_share_icon);
            tvShareContent = itemView.findViewById(R.id.tv_share_content);

            tvMediaTitle = itemView.findViewById(R.id.tv_media_title);
            tvMediaDes = itemView.findViewById(R.id.tv_media_des);
            imgPlayIcon = itemView.findViewById(R.id.img_play_icon);
            groupMedia = itemView.findViewById(R.id.group_media);
        }
    }
}
