package com.zepan.caifuyun.activity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;

//发现界面的web页面
public class FindWebActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_web);
		setHeaderFields(0,R.string.find_web,0,R.drawable.ic_action_back,0,0);
		
		Intent intent=this.getIntent();
        
        WebView webView = (WebView) findViewById(R.id.focus_webview);
        String url=intent.getStringExtra("url");
        webView.loadUrl(url);
       // webView.loadUrl("http://sh.qq.com/zhuanti/wxpage/fashion17.htm?from=singlemessage&isappinstalled=0");
       
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
         WebSettings webSettings =webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

	}
	
	//Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
	
}
