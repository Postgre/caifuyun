package com.zepan.caifuyun.activity;

import android.os.Bundle;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;

//消息的通讯录的详细资料页面
public class MessageDetailsActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_details);
		initView();
		
	}
	public void initView(){
		 setHeaderFields(0,R.string.message_details,0,R.drawable.ic_action_back,0,0);
	}
	
}
