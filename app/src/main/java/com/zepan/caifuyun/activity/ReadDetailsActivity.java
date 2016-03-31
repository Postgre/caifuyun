package com.zepan.caifuyun.activity;

import android.os.Bundle;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;

//销售日报我阅选择进入的已批阅页面
public class ReadDetailsActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_salesjournal_read);
		initView();
		
	}
	public void initView(){
		setHeaderFields(0,R.string.read,0,R.drawable.ic_action_back,0,0);
		
	}
}
