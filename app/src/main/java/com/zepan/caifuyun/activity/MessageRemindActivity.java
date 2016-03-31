package com.zepan.caifuyun.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.yiciyuan.easycomponent.listview.ECListViewLayout;
import com.yiciyuan.easycomponent.listview.IECListView.OnECItemClickListener;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;

//消息的提醒页面
public class MessageRemindActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_remind);
		initView();
		ECListViewLayout ecListViewLayout = (ECListViewLayout)findViewById(R.id.eclistLayout);
	    ecListViewLayout.setResourceJsonKeyMap(new int[]{R.id.content, R.id.date}, new String[]{"content","date"});
	    
	}
	public void initView(){
		setHeaderFields(0,R.string.remind,0,R.drawable.ic_action_back,0,0);
	 
	}
}
