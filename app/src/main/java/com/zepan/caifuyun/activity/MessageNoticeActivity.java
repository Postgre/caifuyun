package com.zepan.caifuyun.activity;

import android.os.Bundle;

import com.yiciyuan.easycomponent.listview.ECListViewLayout;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;

//消息的通知页面
public class MessageNoticeActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_notice);
		
		initView();
		
		ECListViewLayout ecListViewLayout = (ECListViewLayout)findViewById(R.id.eclistLayout);
	    ecListViewLayout.setResourceJsonKeyMap(new int[]{R.id.title,R.id.content, R.id.date}, new String[]{"title","content","date"});
		
	}
	public void initView(){
		setHeaderFields(0,R.string.notice,0,R.drawable.ic_action_back,0,0);
	}
}
