package com.lixd.moments.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.lixd.moments.R;
import com.lixd.moments.utils.CommonUtils;

import java.util.ArrayList;

public class PreviewActivity extends AppCompatActivity {
    private static final String DATA_KEY = "DATA";
    private static final String INDEX_KEY = "INDEX";
    private ViewPager vpPreview;
    private LinearLayout llIndicatorContainer;
    private ArrayList<String> data;

    public static Intent createIntent(Context context, int selectedIndex, ArrayList<String> data) {
        Intent intent = new Intent(context, PreviewActivity.class);
        intent.putExtra(DATA_KEY, data);
        intent.putExtra(INDEX_KEY, selectedIndex);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        initView();
        initData();
        initEvent();
    }

    private void initView() {
        vpPreview = findViewById(R.id.vp_preview);
        llIndicatorContainer = findViewById(R.id.ll_indicator_container);
    }


    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            data = (ArrayList) intent.getSerializableExtra(DATA_KEY);
            final int selectedIndex = intent.getIntExtra(INDEX_KEY, 0);
            vpPreview.setTransitionName(getString(R.string.preview_transition_name) + "_" + selectedIndex);
            if (data != null && data.size() > 0) {
                vpPreview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        vpPreview.setCurrentItem(selectedIndex, false);
                        vpPreview.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
                vpPreview.setAdapter(new ImagePageAdapter());
                vpPreview.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        vpPreview.setTransitionName(getString(R.string.preview_transition_name) + "_" + position);
                        updateIndicatorState(position);
                    }
                });
                initIndicator();
                updateIndicatorState(selectedIndex);
            }
        }
    }

    private void initIndicator() {
        if (data == null || data.size() <= 0) {
            return;
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                CommonUtils.dp2px(this, 10), CommonUtils.dp2px(this, 10));
        llIndicatorContainer.removeAllViews();
        for (int i = 0; i < data.size(); i++) {
            ImageView view = createIndicatorView();
            if (i != data.size() - 1) {
                params.rightMargin = CommonUtils.dp2px(this, 5);
            }
            llIndicatorContainer.addView(view, params);
        }
    }

    private void updateIndicatorState(int selectedIndex) {
        if (selectedIndex < 0 || selectedIndex > llIndicatorContainer.getChildCount()) {
            return;
        }
        int childCount = llIndicatorContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = llIndicatorContainer.getChildAt(i);
            childAt.setSelected(selectedIndex == i);
        }
    }

    private ImageView createIndicatorView() {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(R.drawable.indicator_selector);
        return imageView;
    }

    private void initEvent() {

    }


    class ImagePageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = new ImageView(PreviewActivity.this);
            imageView.setTransitionName(getString(R.string.preview_transition_name));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            Glide.with(PreviewActivity.this)
                    .load(data.get(position))
                    .into(imageView);
            container.addView(imageView, params);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
