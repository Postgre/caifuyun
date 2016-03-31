package com.zepan.caifuyun.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zepan.caifuyun.R;

import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.zepan.caifuyun.base.BaseCityActivity;
import com.zepan.caifuyun.entity.ProvinceModel;
//mProvinceDatas   所有省
//市mCitisDatasMap
// 区  mDistrictDatasMap
//邮编   mZipcodeDatasMap
public class ProvinceActivity extends BaseCityActivity implements OnClickListener{
	private ListView regionListView;
	private static final int PROVINCE=0x100;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_province);
		initProvinceDatas();
		initView();
	}


	private void initView(){
		setHeaderFields(0, R.string.please_choice, 
				0,R.drawable.ic_action_back, 0,0);
		regionListView=(ListView) findViewById(R.id.list_province);
		regionListView.setAdapter(new ArrayAdapter<String>(ProvinceActivity.this, 
				android.R.layout.simple_list_item_1, mProvinceDatas));
		regionListView.setOnItemClickListener(listener);
	}
	@Override
	public void onClick(View v) {

	}

	private OnItemClickListener listener=new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ProvinceModel province=(ProvinceModel) regionListView.getTag(position);
			Intent intent=new Intent(ProvinceActivity.this, CityActivity.class);
			intent.putExtra("currentPosition", position);
			startActivityForResult(intent, PROVINCE);
		}
	};


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data!=null){
			switch (requestCode)
			{
			case PROVINCE:
				String provice=data.getStringExtra("mCurrentProviceName");
				String city=data.getStringExtra("mCurrentCityName");
				String district=data.getStringExtra("mCurrentDistrictName");
				String zipcode=data.getStringExtra("mCurrentZipCode");
				Intent intent=new Intent();
				intent.putExtra("mCurrentProviceName", provice);
				intent.putExtra("mCurrentCityName", city);
				intent.putExtra("mCurrentDistrictName", district);
				intent.putExtra("mCurrentZipCode", zipcode);
				//设置返回数据
				ProvinceActivity.this.setResult(RESULT_OK, intent);
				finish();
				break;
			}
		}

	}
}
