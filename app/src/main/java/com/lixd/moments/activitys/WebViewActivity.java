package com.lixd.moments.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lixd.moments.R;
import com.lixd.moments.view.AppBarLayout;

public class WebViewActivity extends AppCompatActivity {
    private static final String TAG = WebViewActivity.class.getSimpleName();
    private static final String URL_KEY = "URL";
    private AppBarLayout appBarLayout;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        initView();
        initData();
        initEvent();
    }

    public static final void startActivity(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(URL_KEY, url);
        context.startActivity(intent);
    }

    private void initView() {
        appBarLayout = findViewById(R.id.app_bar_layout);
        webView = findViewById(R.id.web_view);
    }

    private String url;

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(URL_KEY)) {
                url = intent.getStringExtra(URL_KEY);
                Log.d(TAG, "webview load url  = " + url);
            } else {
                Log.d(TAG, "webview load url is null");
            }
        }

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String title = view.getTitle();
                if (!TextUtils.isEmpty(title)) {
                    appBarLayout.setTitle(title);
                } else {
                    appBarLayout.setTitle("");
                }
            }
        });
        webView.loadUrl(url);
    }

    private void initEvent() {
        appBarLayout.setOnClickListener(new AppBarLayout.OnClickListener() {
            @Override
            public void onClickLeft() {
                finish();
            }

            @Override
            public void onCickRight() {

            }
        });
    }
}
