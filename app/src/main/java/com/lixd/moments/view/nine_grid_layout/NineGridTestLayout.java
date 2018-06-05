package com.lixd.moments.view.nine_grid_layout;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class NineGridTestLayout extends NineGridLayout {

    protected static final int MAX_W_H_RATIO = 3;

    public NineGridTestLayout(Context context) {
        super(context);
    }

    public NineGridTestLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private OnClickListener listener;

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected boolean displayOneImage(final RatioImageView imageView, String url, final int parentWidth) {

        //        ImageLoaderUtil.displayImage(mContext, imageView, url, ImageLoaderUtil.getPhotoImageOption(), new ImageLoadingListener() {
        //            @Override
        //            public void onLoadingStarted(String imageUri, View view) {
        //
        //            }
        //
        //            @Override
        //            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
        //
        //            }
        //
        //            @Override
        //            public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
        //                int w = bitmap.getWidth();
        //                int h = bitmap.getHeight();
        //
        //                int newW;
        //                int newH;
        //                if (h > w * MAX_W_H_RATIO) {//h:w = 5:3
        //                    newW = parentWidth / 2;
        //                    newH = newW * 5 / 3;
        //                } else if (h < w) {//h:w = 2:3
        //                    newW = parentWidth * 2 / 3;
        //                    newH = newW * 2 / 3;
        //                } else {//newH:h = newW :w
        //                    newW = parentWidth / 2;
        //                    newH = h * newW / w;
        //                }
        //                setOneImageLayoutParams(imageView, newW, newH);
        //            }
        //
        //            @Override
        //            public void onLoadingCancelled(String imageUri, View view) {
        //
        //            }
        //        });
        Glide.with(imageView.getContext())
                .asBitmap()
                .load(url)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        int w = resource.getWidth();
                        int h = resource.getHeight();

                        int newW;
                        int newH;
                        if (h > w * MAX_W_H_RATIO) {//h:w = 5:3
                            newW = parentWidth / 2;
                            newH = newW * 5 / 3;
                        } else if (h < w) {//h:w = 2:3
                            newW = parentWidth * 2 / 3;
                            newH = newW * 2 / 3;
                        } else {//newH:h = newW :w
                            newW = parentWidth / 2;
                            newH = h * newW / w;
                        }
                        setOneImageLayoutParams(imageView, newW, newH);
                        return false;
                    }
                })
                .into(imageView);
        return false;
    }

    @Override
    protected void displayImage(RatioImageView imageView, String url) {
        //        ImageLoaderUtil.getImageLoader(mContext).displayImage(url, imageView, ImageLoaderUtil.getPhotoImageOption());
        Glide.with(imageView.getContext())
                .load(url)
                .into(imageView);
    }

    @Override
    protected void onClickImage(View view, int i, String url, List<String> urlList) {
        if (listener != null) {
            listener.onClick(view,i, urlList);
        } else {
            Toast.makeText(mContext, "点击了图片" + url, Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnClickListener {
        void onClick(View view,int index, List<String> urlList);
    }
}