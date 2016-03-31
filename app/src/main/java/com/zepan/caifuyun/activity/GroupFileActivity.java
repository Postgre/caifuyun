package com.zepan.caifuyun.activity;

import android.os.Bundle;

import com.yiciyuan.easycomponent.listview.ECListViewLayout;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;

public class GroupFileActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_group_file);
		
		initView();
		ECListViewLayout ecListViewLayout = (ECListViewLayout)findViewById(R.id.eclistLayout);
	    ecListViewLayout.setResourceJsonKeyMap(new int[]{R.id.title,R.id.size, R.id.name,R.id.date,R.id.time}, new String[]{"title","size","name","date","time"});
	}

	private void initView() {
		// TODO Auto-generated method stub
		setHeaderFields(0,R.string.group_file,0,R.drawable.ic_return,0,0);
	}
	
}
