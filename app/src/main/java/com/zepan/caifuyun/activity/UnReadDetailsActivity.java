package com.zepan.caifuyun.activity;

import android.os.Bundle;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;

//销售日报 我阅选择进入的未批阅页面
public class UnReadDetailsActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_salesjournal_unread);
		initView();
		
	}
	public void initView(){
		setHeaderFields(0,R.string.unread,0,R.drawable.ic_action_back,0,0);
		
	}
}
