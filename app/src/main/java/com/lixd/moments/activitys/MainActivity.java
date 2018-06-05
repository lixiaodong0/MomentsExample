package com.lixd.moments.activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.Toast;

import com.lixd.moments.App;
import com.lixd.moments.R;
import com.lixd.moments.adapter.CommentItemViewBinder;
import com.lixd.moments.adapter.HeadherItemViewBinder;
import com.lixd.moments.adapter.ImageTextItemViewBinder;
import com.lixd.moments.adapter.ShareItemViewBinder;
import com.lixd.moments.adapter.TextItemViewBinder;
import com.lixd.moments.adapter.VideoItemViewBinder;
import com.lixd.moments.bean.CommentCategory;
import com.lixd.moments.bean.HeaderCategory;
import com.lixd.moments.bean.ImageTextCategory;
import com.lixd.moments.bean.ShareCategory;
import com.lixd.moments.bean.TextCategory;
import com.lixd.moments.bean.VideoCategory;
import com.lixd.moments.bean.moments.MomentsBean;
import com.lixd.moments.bean.moments.User;
import com.lixd.moments.callback.MomentsCallback;
import com.lixd.moments.utils.CommonUtils;
import com.lixd.moments.utils.ItemViewHelper;
import com.lixd.moments.utils.JsonUtils;
import com.lixd.moments.view.AppBarLayout;
import com.lixd.moments.view.CommentInputLayout;
import com.lixd.moments.view.popup.CommentLikePopupWindow;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

public class MainActivity extends AppCompatActivity {

    private MultiTypeAdapter adapter;
    private Context context;
    private CommentInputLayout commentInputLayout;
    private RecyclerView recyclerView;
    private AppBarLayout appBarLayout;
    private HeaderCategory header;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        initView();
        initData();
        initEvent();
    }

    private void initView() {
        appBarLayout = findViewById(R.id.app_bar_layout);
        commentInputLayout = findViewById(R.id.comment_input_layout);
        commentInputLayout.bindActivity(this);
        recyclerView = findViewById(R.id.recy_list);
    }

    private void initData() {
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MultiTypeAdapter();
        adapter.register(HeaderCategory.class, new HeadherItemViewBinder(new HeaderItemCallback()));
        CommonCallback commonCallback = new CommonCallback();
        adapter.register(TextCategory.class, new TextItemViewBinder(commonCallback));
        adapter.register(ImageTextCategory.class, new ImageTextItemViewBinder(commonCallback));
        adapter.register(VideoCategory.class, new VideoItemViewBinder(commonCallback));
        adapter.register(ShareCategory.class, new ShareItemViewBinder(commonCallback));
        adapter.register(CommentCategory.class, new CommentItemViewBinder(new CommentItemCallback()));
        recyclerView.setAdapter(adapter);

        List<Object> data = JsonUtils.getInstance().getData();
        header = HeaderCategory.createTest();
        data.add(0, header);
        adapter.setItems(data);
        adapter.notifyDataSetChanged();

    }


    private void initEvent() {
        appBarLayout.setOnClickListener(new AppBarLayout.OnClickListener() {
            @Override
            public void onClickLeft() {
                finish();
            }

            @Override
            public void onCickRight() {
                recyclerView.scrollBy(0, 100);
                Toast.makeText(MainActivity.this, "相册", Toast.LENGTH_SHORT).show();
            }
        });

        commentInputLayout.setOnKeyboardShowListener(new CommentInputLayout.OnKeyboardShowListener() {
            @Override
            public void show(int height) {
                smartScrollRecyclerView();
            }

            @Override
            public void hide() {
                if (scrollPosition == adapter.getItemCount() - 1) {
                    recyclerView.setTranslationY(0);
                }
            }
        });
    }

    /**
     * 微信做法:
     * 当键盘弹起,要求键盘不挡住点击的Item布局,要完全展示出来
     * 实现思路:
     * LinearLayoutManager.scrollToPositionWithOffset(position, offset)
     * 将指定的position置顶显示
     * 注意事项:
     * position如果等于最后一个条目将无法置顶,因为高度已经无法滚动了
     * 这里将采取平移RecyclerView来实现滚动Item
     */
    private void smartScrollRecyclerView() {

        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //RecyclerView可见的高度(RecyclerView高度 - 键盘高度)
                int visibleHeight = recyclerView.getHeight();

                View itemView = linearLayoutManager.findViewByPosition(scrollPosition);
                int itemHeight;

                if (itemView != null) {
                    itemHeight = itemView.getHeight();

                    Log.e("smartScrollRecyclerView", " ItemVirew != null ," +
                            "itemHeight: " + itemHeight);
                } else {
                    /**
                     * findViewByPosition()方法有可能获取View是null的, 具体什么原因导致的,
                     * 还处于懵逼状态,
                     * 为了每次都能获取到高度,Adapter创建Item的时候,将高度保存起来
                     */
                    itemHeight = ItemViewHelper.getInstance().findItemViewHeight(scrollPosition);
                    Log.e("smartScrollRecyclerView", "ItemVirew == null , 从Map中取值 " +
                            "itemHeight: " + itemHeight);
                }

                if (itemHeight != 0) {
                    //如果条目高度 > 可见高度  需要向上移动偏移量
                    if (itemHeight > visibleHeight) {
                        int offset = itemHeight - visibleHeight + commentInputLayout.getHeight();
                        linearLayoutManager.scrollToPositionWithOffset(scrollPosition, -offset);
                    } else {
                        //需要向下移动偏移量
                        int offset = visibleHeight - itemHeight - commentInputLayout.getHeight();
                        linearLayoutManager.scrollToPositionWithOffset(scrollPosition, offset);
                    }
                } else {
                    linearLayoutManager.scrollToPositionWithOffset(scrollPosition, 0);
                    Log.e("smartScrollRecyclerView", "itemHeight == 0 先将要滚动的条目置顶显示,让ItemView初始化完毕," +
                            "在去调用滚动方法");
                    smartScrollRecyclerView();
                }

                /**
                 *  如果是最后一条目,是无法调用滚动方法,进行滚动的
                 *  只能采取平移Y轴来达到滚动的目的
                 *  这里只平移输入框的高度:
                 *      是因为当键盘弹起,布局会自动布局,适应键盘高度,
                 *      我们只需要平移被输入框遮挡的部分就行!
                 */
                if (scrollPosition == adapter.getItemCount() - 1) {
                    recyclerView.setTranslationY(-commentInputLayout.getHeight());
                }
            }
        });
    }

    private class HeaderItemCallback implements MomentsCallback.HeaderItemCallback {
        @Override
        public void onClickMyIcon() {

        }

        @Override
        public void onClickBg() {

        }
    }

    private class CommentItemCallback implements MomentsCallback.CommentItemCallback {

        @Override
        public void onClickCommentUser() {

        }

        @Override
        public void onReplyCommentUser(View targetView, RecyclerView.ViewHolder holder, final User to, final MomentsBean bean) {
            int adapterPosition = holder.getAdapterPosition();
            scrollPosition = adapterPosition;

            commentInputLayout.setOnClickSendListener(new CommentInputLayout.OnClickSendListener() {
                @Override
                public void onClick(String text) {
                    List<Object> newData = JsonUtils.getInstance().replyUserComment(bean, App.getInstance().getUser(), text, to);
                    //刷新界面
                    newData.add(0, header);
                    adapter.setItems(newData);
                    //刷新界面
                    adapter.notifyDataSetChanged();
                }
            });

            if (commentLikePopupWindow == null) {
                commentLikePopupWindow = new CommentLikePopupWindow(context);
            }
            commentLikePopupWindow.updateClickLocation(targetView, holder);
            String editHiht = "回复" + to.name + "：";
            commentInputLayout.showKeyboard(commentLikePopupWindow.isSameLoctionClick(), editHiht);
        }

        @Override
        public void onClickLikeUser() {

        }
    }

    private CommentLikePopupWindow commentLikePopupWindow;
    private int scrollPosition;

    private class CommonCallback implements MomentsCallback.TextItemCallback,
            MomentsCallback.ImageTextItemCallback, MomentsCallback.VideoItemCallback, MomentsCallback.ShareItemCallback {

        @Override
        public void onClickUser() {

        }

        @Override
        public void onClickDelete(final MomentsBean bean) {
            new AlertDialog.Builder(context)
                    .setTitle("提示")
                    .setMessage("确认要删除吗?")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            List<Object> list = JsonUtils.getInstance().deleteMyMoments(bean, App.getInstance().getUser());
                            list.add(0, header);
                            adapter.setItems(list);
                            adapter.notifyDataSetChanged();
                        }
                    })
                    .create()
                    .show();
        }

        @Override
        public void onClickComment(View target, final RecyclerView.ViewHolder holder, final MomentsBean bean) {
            int adapterPosition = holder.getAdapterPosition();
            if (!CommonUtils.isEmpty(bean.comment_list) || !CommonUtils.isEmpty(bean.like_list)) {
                adapterPosition++;
            }
            scrollPosition = adapterPosition;

            if (commentLikePopupWindow == null) {
                commentLikePopupWindow = new CommentLikePopupWindow(context);
            }

            commentLikePopupWindow.initData(bean);
            commentLikePopupWindow.setOnClickListener(new CommentLikePopupWindow.OnClickListener() {
                @Override
                public void onLike(boolean isLike) {
                    List<Object> newData;
                    if (isLike) {
                        newData = JsonUtils.getInstance().cancelLikeUser(bean, App.getInstance().getUser());
                    } else {
                        newData = JsonUtils.getInstance().addLikeUser(bean, App.getInstance().getUser());
                    }
                    //重新添加头部
                    newData.add(0, header);
                    adapter.setItems(newData);
                    //刷新界面
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onComment(boolean isSameLoctionClick) {
                    commentInputLayout.showKeyboard(isSameLoctionClick);
                    commentInputLayout.setOnClickSendListener(new CommentInputLayout.OnClickSendListener() {
                        @Override
                        public void onClick(String text) {
                            List<Object> newData = JsonUtils.getInstance().addUserComment(bean, App.getInstance().getUser(), text);
                            //重新添加头部
                            newData.add(0, header);
                            adapter.setItems(newData);
                            //刷新界面
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            });
            commentLikePopupWindow.toggle(target, holder);
        }

        @Override
        public void onLongClickText(String text) {

        }

        @Override
        public void onClickImg(View view, int index, List<String> urlList) {
            Intent intent = PreviewActivity.createIntent(context, index, (ArrayList<String>) urlList);
            /**
             *  设置元素共享动画!
             */
            Pair<View, String>[] pars = new Pair[urlList.size()];
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            if (viewGroup != null) {
                int childCount = viewGroup.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    pars[i] = new Pair<>(viewGroup.getChildAt(i), getString(R.string.preview_transition_name) + "_" + i);
                }
            }
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, pars);
            startActivity(intent, optionsCompat.toBundle());
        }

        @Override
        public void onClickVideo(String videoUrl) {

        }

        @Override
        public void onClickShare(String url) {
            WebViewActivity.startActivity(context, url);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (commentInputLayout.isShow()) {
                commentInputLayout.hideLayout();
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        commentInputLayout.unbindActivity();
        if (commentLikePopupWindow != null && commentLikePopupWindow.isShowing()) {
            commentLikePopupWindow.dismiss();
            commentLikePopupWindow = null;
        }
    }

    /**
     * 点击空白处关闭键盘
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                commentInputLayout.hideLayoutAndHideKeyboard();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

}
