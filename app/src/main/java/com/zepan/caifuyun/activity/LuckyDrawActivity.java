package com.zepan.caifuyun.activity;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
/***
 * 抽奖
 * @author long
 *
 */
@SuppressLint("SetJavaScriptEnabled")
public class LuckyDrawActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lucky_draw);
		setHeaderFields(0, R.string.lucky_draw, 
				0,R.drawable.ic_action_back, 0,0);
		initView();
	}
	private void initView(){
		((WebView)findViewById(R.id.wb_lucky)).loadUrl("http://www.17sucai.com/preview/222076/2015-05-28/turnplate/index.html");
		WebSettings webSettings = ((WebView)findViewById(R.id.wb_lucky)).getSettings();
		webSettings.setJavaScriptEnabled(true);
		 //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
		((WebView)findViewById(R.id.wb_lucky)).setWebViewClient(new WebViewClient(){
	           @Override
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
	            // TODO Auto-generated method stub
	               //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
	            view.loadUrl(url);
	            return true;
	        }
	       });

	}

}


