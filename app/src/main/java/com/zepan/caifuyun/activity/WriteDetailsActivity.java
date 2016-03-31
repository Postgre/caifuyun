package com.zepan.caifuyun.activity;

import android.os.Bundle;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;
//销售日报的写日报页面
public class WriteDetailsActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_writejournal);
		initView();
		
	}
	public void initView(){
		setHeaderFields(0,R.string.write,R.string.savedraft,R.drawable.ic_action_back,0,0);
		
	}
}
