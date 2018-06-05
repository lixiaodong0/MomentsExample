package com.lixd.moments.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lixd.moments.R;
import com.lixd.moments.utils.KeyboardUtils;
import com.lixd.moments.utils.SoftKeyBoardListener;

public class CommentInputLayout extends ConstraintLayout {
    /**
     * 记录输入的文本
     */
    private static String SAVE_TEXT = "";

    private TextView tvSend;
    private EditText editComment;
    private ImageView imgEmoji;
    private Activity activity;
    private boolean isShow;
    private android.os.Handler handler;

    public CommentInputLayout(Context context) {
        this(context, null);
    }

    public CommentInputLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.comment_input_layout, this);
        init();
    }

    private void init() {
        handler = new Handler();
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        tvSend = findViewById(R.id.tv_send);
        editComment = findViewById(R.id.edit_comment);
        imgEmoji = findViewById(R.id.img_emoji);
    }

    private void initData() {

    }

    private void initEvent() {
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editComment.getText().toString();
                clearTextAndHide();
                SAVE_TEXT = "";
                if (listener != null) {
                    listener.onClick(text);
                }
            }
        });

        editComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvSend.setEnabled(TextUtils.isEmpty(s.toString()) ? false : true);
            }
        });
    }

    /**
     * 清除文本并隐藏控件
     */
    private void clearTextAndHide() {
        editComment.setText("");
        hideKeyboard();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideLayout();
            }
        }, 300);
    }

    private int translationY = 0;

    public void bindActivity(@NonNull final Activity activity) {
        this.activity = activity;
        SoftKeyBoardListener.setListener(activity, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                RelativeLayout root = activity.findViewById(R.id.root);
                if (root != null) {
                    /*
                     * 当软键盘弹起的时候,此时parentHeight指的是当前布局在屏幕可见的高度
                     * 不是指的布局的高度
                     * 布局的高度 = 可见的高度 + height(键盘高度)
                     */
                    int visibleHeight = root.getHeight();
                    //计算平移的y值,基于控件左上角
                    translationY = visibleHeight - getHeight();
                    setTranslationY(translationY);
                    editComment.setFocusable(true);
                    editComment.setFocusableInTouchMode(true);
                    editComment.requestFocus();
                    showLayout();
                }

                if (onKeyboardShowListener != null) {
                    onKeyboardShowListener.show(height);
                }
            }

            @Override
            public void keyBoardHide(int height) {
                RelativeLayout root = activity.findViewById(R.id.root);
                if (root != null) {
                    /*
                     * 当软键盘弹起的时候,此时parentHeight指的是当前布局在屏幕可见的高度
                     * 不是指的布局的高度
                     * 布局的高度 = 可见的高度 + height(键盘高度)
                     */
                    int visibleHeight = root.getHeight();
                    //计算平移的y值,基于控件左上角
                    translationY = visibleHeight - getHeight();
                    setTranslationY(translationY);
                }

                SAVE_TEXT = editComment.getText().toString();

                if (onKeyboardShowListener != null) {
                    onKeyboardShowListener.hide();
                }
            }
        });
    }

    public void unbindActivity() {
        this.activity = null;
        handler.removeCallbacksAndMessages(null);
        handler = null;
    }

    private int lastBottom = 0;

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        /*
         *当控件内容发生变化时,会重新执行onLayout方法布局子类
         *经过每次增加编辑框的文字,没增加一行,bootom都会增加一行的高度
         *所以,每次高度变化时,平移一行的高度,解决布局往下延伸被键盘挡住的问题
         */
        int y = bottom - lastBottom;
        super.onLayout(changed, left, top, right, bottom);
        //必须初始化过后才行,否则导致位置错乱
        if (translationY != 0) {
            translationY -= y;
            setTranslationY(translationY);
        }
        lastBottom = bottom;
    }

    private OnClickSendListener listener;

    public void setOnClickSendListener(OnClickSendListener listener) {
        this.listener = listener;
    }

    public interface OnClickSendListener {
        void onClick(String text);
    }

    public void showKeyboard(boolean isSameLoctionClick) {
        showKeyboard(isSameLoctionClick, "");
    }


    public void showKeyboard(boolean isSameLoctionClick, String editHiht) {
        if (!isSameLoctionClick) {
            SAVE_TEXT = "";
            editComment.setText(SAVE_TEXT);
        }
        editComment.setHint(editHiht);
        KeyboardUtils.showSoftInput(activity);
    }

    public void hideKeyboard() {
        KeyboardUtils.hideSoftInput(activity);
    }

    public void showLayout() {
        setVisibility(VISIBLE);
        isShow = true;
    }

    public void hideLayout() {
        setVisibility(GONE);
        isShow = false;
    }

    public void hideLayoutAndHideKeyboard() {
        hideKeyboard();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideLayout();
            }
        }, 300);
    }


    public boolean isShow() {
        return isShow;
    }

    private OnKeyboardShowListener onKeyboardShowListener;

    public void setOnKeyboardShowListener(OnKeyboardShowListener listener) {
        onKeyboardShowListener = listener;
    }

    public static abstract class OnKeyboardShowListener {
        public abstract void show(int height);

        public void hide() {
        }
    }
}
