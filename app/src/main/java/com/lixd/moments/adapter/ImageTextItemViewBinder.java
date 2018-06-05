package com.lixd.moments.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lixd.moments.R;
import com.lixd.moments.bean.ImageTextCategory;
import com.lixd.moments.bean.moments.Image;
import com.lixd.moments.callback.MomentsCallback;
import com.lixd.moments.view.nine_grid_layout.NineGridTestLayout;

import java.util.ArrayList;
import java.util.List;

public class ImageTextItemViewBinder extends TextItemViewBinder<ImageTextCategory> {

    private MomentsCallback.ImageTextItemCallback callback;
    public ImageTextItemViewBinder(MomentsCallback.ImageTextItemCallback callback) {
        super(callback);
        this.callback = callback;
    }


    @Override
    public TextItemViewBinder.ViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.moments_item_text_and_img, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(TextItemViewBinder.ViewHolder textHolder, ImageTextCategory item) {
        super.bindViewHolder(textHolder, item);
        if(textHolder instanceof ViewHolder){
            ViewHolder holder = (ViewHolder) textHolder;

            List<String> list = new ArrayList<>();
            if (item.images != null && item.images.size() > 0) {
                for (Image image : item.images) {
                    list.add(image.url);
                }
            }
            holder.imgList.setUrlList(list);
            holder.imgList.setOnClickListener(new NineGridTestLayout.OnClickListener() {
                @Override
                public void onClick(View view,int index, List<String> urlList) {
                    if (callback != null) {
                        callback.onClickImg(view,index, urlList);
                    }
                }
            });

        }
    }

    static class ViewHolder extends TextItemViewBinder.ViewHolder {
        private final NineGridTestLayout imgList;
        public ViewHolder(View itemView) {
            super(itemView);
            imgList = itemView.findViewById(R.id.img_list);
        }
    }
}
