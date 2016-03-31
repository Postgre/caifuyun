package com.zepan.caifuyun.activity;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

//通讯录页面
public class MessageContactsActivity extends BaseActivity implements OnClickListener{

	private RelativeLayout phone_relativeLayout;
	private RelativeLayout group_relativeLayout;
	private ImageView iv_add;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_contacts);
		initView();
		
	}
	public void initView(){
		setHeaderFields(0,R.string.contacts,0,R.drawable.ic_action_back,0,0);
		phone_relativeLayout=(RelativeLayout) findViewById(R.id.phone_layout);
		phone_relativeLayout.setOnClickListener(this);
		group_relativeLayout=(RelativeLayout) findViewById(R.id.group_layout);
	    group_relativeLayout.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.phone_layout://手机通讯录
			Intent intent=new Intent(MessageContactsActivity.this,PersonalContactsActivity.class);
			startActivity(intent);
			break;
		case R.id.group_layout://我的群组
			Intent intent2=new Intent(MessageContactsActivity.this,MyGroupActivity.class);
			startActivity(intent2);
			break;

		default:
			break;
		}
		
	}
	
	
}
