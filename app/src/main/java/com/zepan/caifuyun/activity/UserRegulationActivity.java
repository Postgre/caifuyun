package com.zepan.caifuyun.activity;

import android.os.Bundle;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;

//设置页面进入的运营管理办法页面
public class UserRegulationActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regulation);
		setHeaderFields(0,R.string.regulations,0,R.drawable.ic_action_back,0,0);
	}
}
