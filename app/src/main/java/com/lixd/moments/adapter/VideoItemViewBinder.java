package com.lixd.moments.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lixd.moments.R;
import com.lixd.moments.bean.VideoCategory;
import com.lixd.moments.callback.MomentsCallback;

public class VideoItemViewBinder extends TextItemViewBinder<VideoCategory> {

    private MomentsCallback.VideoItemCallback callback;

    public VideoItemViewBinder(MomentsCallback.VideoItemCallback callback) {
        super(callback);
        this.callback = callback;
    }


    @Override
    public TextItemViewBinder.ViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.moments_item_text_and_video, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void bindViewHolder(TextItemViewBinder.ViewHolder textHolder, VideoCategory item) {
        super.bindViewHolder(textHolder, item);

        if (textHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) textHolder;

            String previewUrl = item.videos.get(0).preview_url;
            final String videoUrl = item.videos.get(0).url;
            Glide.with(holder.imgVideoPreview.getContext())
                    .asBitmap()
                    .load(previewUrl)
                    .into(holder.imgVideoPreview);
            holder.imgVideoPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onClickVideo(videoUrl);
                    }
                }
            });
        }

    }

    static class ViewHolder extends TextItemViewBinder.ViewHolder {
        private final ImageView imgPlayIcon;
        private final ImageView imgVideoPreview;

        public ViewHolder(View itemView) {
            super(itemView);
            imgPlayIcon = itemView.findViewById(R.id.img_play_icon);
            imgVideoPreview = itemView.findViewById(R.id.img_video_preview);

        }
    }
}
