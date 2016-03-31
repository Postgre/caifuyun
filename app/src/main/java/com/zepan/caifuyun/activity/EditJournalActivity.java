package com.zepan.caifuyun.activity;

import android.os.Bundle;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;

//编辑页面
public class EditJournalActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_journal);
		initView();
		
	}
	public void initView(){
	
		setHeaderFields(0,R.string.edit,0,R.drawable.ic_action_back,0,0);
	}
}
