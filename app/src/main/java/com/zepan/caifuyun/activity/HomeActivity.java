package com.zepan.caifuyun.activity;

import com.zepan.android.widget.TabGroup;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.application.MyApplication;
import com.zepan.caifuyun.base.BaseActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;
/**
 * home 登入后，项目的入口
 * @author duanjie
 *
 */
public class HomeActivity extends BaseActivity {
	private TabGroup tabHost;
	private static final int BACK_EXIT_DURATION = 2000;
	private long exitTime = 0;
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home);
		tabHost = (TabGroup) findViewById(R.id.tabhost);
		tabHost.attachContent(R.id.content,HomeFragment.class,
				CustomerFragment.class,MessageFragment.class,
				FindFragment.class,UserInfoFragment.class); 
		tabHost.setCurrentView(0);

	}
	/** 用户按返回键操作 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent ke) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& ke.getAction() == KeyEvent.ACTION_DOWN) {

			if (System.currentTimeMillis() - exitTime > BACK_EXIT_DURATION) {
				Toast.makeText(HomeActivity.this, "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				MyApplication.getInstance().finishAllContext();
			}
			return true;
		}
		return super.onKeyDown(keyCode, ke);
	}

}
