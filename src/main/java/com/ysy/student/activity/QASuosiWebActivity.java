package com.ysy.student.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ysy.sourcer_slidingmenu.ProgressWebView;
import com.ysy.sourcer_slidingmenu.R;
import com.ysy.sourcer_slidingmenu.SplashActivity;

public class QASuosiWebActivity extends Activity {

    private ProgressWebView webView;
    private ImageButton btnCloseWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        SharedPreferences dayornight = QASuosiWebActivity.this.getSharedPreferences("dayornight", QASuosiWebActivity.this.MODE_PRIVATE);
        if (dayornight.getBoolean("dayornight", false) == false) {
//			this.setTheme(R.style.AppTheme); // 此处不注释掉的话，会和Mainfest里的theme冲突造车fullscreen失效
        } else if (dayornight.getBoolean("dayornight", false) == true) {
//			this.setTheme(R.style.AppTheme_Night); // 同上
            StudentListActivity.setBrightness(this, 0.0f);
        }
        setContentView(R.layout.activity_qasuosi_web);
        init();

        btnCloseWeb = (ImageButton) findViewById(R.id.btn_close_web);
        btnCloseWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        webView = (ProgressWebView) findViewById(R.id.webView);
        //WebView加载web资源
        webView.loadUrl("http://qa.suosi.org");
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });

        //启用支持javascript
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

    }

    //改写物理按键——返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();//返回上一页面
                return true;
            } else {
//                System.exit(0);//退出程序
                this.finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
