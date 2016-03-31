package com.zepan.caifuyun.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.yiciyuan.easycomponent.listview.ECListViewLayout;
import com.yiciyuan.easycomponent.listview.IECListView.OnECItemClickListener;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;

//会员管理页面
public class VipManagerActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vip_manager);
		initView();
		
	}

	private void initView() {
		// TODO Auto-generated method stub
		setHeaderFields(0,R.string.vip_manager,0,R.drawable.ic_action_back,0,0);
		ECListViewLayout ecListViewLayout = (ECListViewLayout)findViewById(R.id.eclistLayout);
	    ecListViewLayout.setResourceJsonKeyMap(new int[]{R.id.image,R.id.vip_name, R.id.sign,R.id.company}, new String[]{"image_url","vip_name","state","company"});
	    
	    ecListViewLayout.setOnItemClickListener(new OnECItemClickListener() {
			
			@Override
			public void OnItemClick(AdapterView<?> parent, View view, int position,
					JSONObject itemJson) throws JSONException {
				// TODO Auto-generated method stub
				if(itemJson.get("state").equals("未报备")){
					Intent intent=new Intent(VipManagerActivity.this,VipManagerDetailsUnreport.class);
					startActivity(intent);
				}else if(itemJson.get("state").equals("已报备")){
					//
				}
			}
		});
	}
	
}
