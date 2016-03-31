package com.zepan.caifuyun.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.android.volley.Request.Method;
import com.zepan.android.sdk.IRequestCallBack;
import com.zepan.android.sdk.JsonUtil;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.adapter.IntegralAdapter;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.entity.IntegralInfo;
import com.zepan.caifuyun.entity.UserInfo;

//积分商城页面
public class IntegralMallActivity extends BaseActivity {

	private GridView gridView;
	private IntegralAdapter adapter;
	private List<IntegralInfo> list=new ArrayList<IntegralInfo>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_integral_mall);
		initView();
		
	}

	private void initView() {
		// TODO Auto-generated method stub
		setHeaderFields(0,R.string.integral_mall,0,R.drawable.ic_action_back,0,0);
		gridView=(GridView) findViewById(R.id.integral_gridview);
		
		getDataList();
		adapter=new IntegralAdapter(this, list);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(IntegralMallActivity.this,IntegralDetailsActivity.class);
				intent.putExtra("details_url",list.get(position).getUrl());
				intent.putExtra("discount", list.get(position).getDiscount());
				intent.putExtra("content", list.get(position).getContent());
				intent.putExtra("num", list.get(position).getNum());
				startActivity(intent);
			}
		});
	}
	
	private void getDataList(){
		String url="http://120.26.789.31/get_integral_mall";
		JSONObject json=new JSONObject();
		request(url, json, Method.POST, new IRequestCallBack() {
			
			@Override
			public void onResponse(JSONObject response) {
				
				getDataListResult(response);
				
			}
			
			@Override
			public void onErrorResponse(String errorMessage) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	private void getDataListResult(JSONObject response){
		try {
			if(response.getInt("status")==0){
				JSONArray jsonArray=response.getJSONArray("list");
				for(int i=0;i<jsonArray.length();i++){
				
					IntegralInfo info=(IntegralInfo) JsonUtil.jsonToEntity(jsonArray.getJSONObject(i),IntegralInfo.class);
					
					list.add(info);
					
				}
			
				adapter.notifyDataSetChanged();
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
