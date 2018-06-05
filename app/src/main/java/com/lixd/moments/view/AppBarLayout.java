package com.lixd.moments.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lixd.moments.R;

public class AppBarLayout extends RelativeLayout {
    private static final String TAG = AppBarLayout.class.getSimpleName();
    private OnClickListener listener;
    private ImageView imgLeft;
    private ImageView imgRight;
    private TextView tvName;

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public AppBarLayout(Context context) {
        this(context, null);
    }

    public AppBarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AppBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.view_app_bar_layout, this);

        initView();
        initData(context, attrs);
        initEvent();
    }

    private void initView() {
        imgLeft = findViewById(R.id.img_left);
        imgRight = findViewById(R.id.img_right);
        tvName = findViewById(R.id.tv_app_bar_name);
    }

    private void initData(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AppBarLayout);

        String name = ta.getString(R.styleable.AppBarLayout_title);
        setTitle(name);
        int leftRes = ta.getResourceId(R.styleable.AppBarLayout_leftRes, R.mipmap.back);
        setLeftImgRes(leftRes);
        int rightRes = ta.getResourceId(R.styleable.AppBarLayout_rightRes, R.mipmap.camera);
        setRightImgRes(rightRes);
        boolean rightVisible = ta.getBoolean(R.styleable.AppBarLayout_rightVisible, true);
        setRightVisible(rightVisible);
        ta.recycle();
    }


    public void setRightVisible(boolean isVisible) {
        if (imgRight != null) {
            imgRight.setVisibility(isVisible ? VISIBLE: GONE);
        }
    }

    public void setTitle(String title) {
        if (tvName != null) {
            tvName.setText(title);
        }
    }

    public void setLeftImgRes(@DrawableRes int res) {
        if (imgLeft != null) {
            imgLeft.setImageResource(res);
        }
    }

    public void setRightImgRes(@DrawableRes int res) {
        if (imgRight != null) {
            imgRight.setImageResource(res);
        }
    }

    private void initEvent() {

        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickLeft();
                } else {
                    Log.d(TAG, "click left");
                }
            }
        });

        imgRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCickRight();
                } else {
                    Log.d(TAG, "click right");
                }
            }
        });
    }


    public interface OnClickListener {
        void onClickLeft();

        void onCickRight();
    }
}
