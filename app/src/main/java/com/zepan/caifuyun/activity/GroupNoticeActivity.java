package com.zepan.caifuyun.activity;

import android.os.Bundle;

import com.yiciyuan.easycomponent.listview.ECListViewLayout;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;

//群公告界面
public class GroupNoticeActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_notice);
		
		initView();
		ECListViewLayout ecListViewLayout = (ECListViewLayout)findViewById(R.id.eclistLayout);
	    ecListViewLayout.setResourceJsonKeyMap(new int[]{R.id.group_name,R.id.duty, R.id.date,R.id.time,R.id.content}, new String[]{"group_name","duty","date","time","content"});
	}

	private void initView() {
		// TODO Auto-generated method stub
		setHeaderFields(0,R.string.group_notice,0,R.drawable.ic_return,0,0);
	}
	
}
