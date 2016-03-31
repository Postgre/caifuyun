package com.zepan.caifuyun.activity;

import android.os.Bundle;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;

//用户设置的关于蜜蜂云页面
public class UserAboutCaifuyunActivity extends BaseActivity {

	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_aboutcaifuyun);
		setHeaderFields(0,R.string.aboutcaifuyun,0,R.drawable.ic_action_back,0,0);
	}
}
