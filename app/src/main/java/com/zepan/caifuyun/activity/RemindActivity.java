package com.zepan.caifuyun.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.android.volley.Request.Method;
import com.zepan.android.sdk.IRequestCallBack;
import com.zepan.android.sdk.JsonUtil;


import com.zepan.android.widget.AlphaImageView;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.adapter.RemindAdapter;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.constants.Url;
import com.zepan.caifuyun.entity.Remind;
/**
 * 提醒
 * @author duanjie
 *
 */
public class RemindActivity extends BaseActivity implements OnClickListener{
	private List<Remind> remindList=new ArrayList<Remind>();
	private RemindAdapter remindAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remind);
		setHeaderFields(0, R.string.remind, 
				0,R.drawable.ic_action_back, 0,R.drawable.ic_action_add);
		initView();
		initData();
	}

	private void initView() {
		((AlphaImageView)findViewById(R.id.iv_right)).setOnClickListener(this);
		ListView listView=(ListView) findViewById(android.R.id.list);
		remindAdapter=new RemindAdapter(this, remindList);
		listView.setAdapter(remindAdapter);
	}
	private void initData(){
		String json="";
		getRemindList();
	}
	private void getRemindList(){
		String url="http://119.254/get_remindsgh_list";
		JSONObject json=new JSONObject();
		request(url, json, Method.POST, new IRequestCallBack() {
			
			@Override
			public void onResponse(JSONObject response) {
				getRemindListResult(response);
				
			}
			
			@Override
			public void onErrorResponse(String errorMessage) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	private void getRemindListResult(JSONObject response){
		try {
			if(response.getInt("status")==0){
				JSONArray jsonArray=response.getJSONArray("list");
				for(int i=0;i<jsonArray.length();i++){
					Remind remind=(Remind) JsonUtil.jsonToEntity((JSONObject)jsonArray.get(i), Remind.class);
					remindList.add(remind);
				}
				remindAdapter.notifyDataSetChanged();
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_right://新建提醒
			Intent intentNewRemind=new Intent(RemindActivity.this,NewRemindActivity.class);
			startActivityForResult(intentNewRemind,0);
			break;

		default:
			break;
		}

	}
	@Override
	protected void onActivityResult(int requestId, int resultId, Intent intent) {
		if(intent!=null){
			Remind remind=intent.getParcelableExtra("remind");
			remindList.add(remind);
			remindAdapter.notifyDataSetChanged();
		}
		super.onActivityResult(requestId, resultId, intent);
	}
}
