package com.zepan.caifuyun.activity;
import com.zepan.caifuyun.base.BaseCityActivity;
import com.zepan.caifuyun.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CityActivity extends BaseCityActivity implements OnClickListener{
	private ListView regionListView;
	String[] cities;
	private static final int CITY=0x101;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_province);
		initProvinceDatas();
		initView();
		initData();
	}
	private void initData(){
		Intent intent=getIntent();
		int currentPosition=intent.getIntExtra("currentPosition", -1);
		mCurrentProviceName = mProvinceDatas[currentPosition];
		cities = mCitisDatasMap.get(mCurrentProviceName);
		regionListView.setAdapter(new ArrayAdapter<String>(CityActivity.this, 
				android.R.layout.simple_list_item_1,cities));
		regionListView.setOnItemClickListener(listener);
	}


	private void initView(){
		setHeaderFields(0, R.string.please_choice, 
				0,R.drawable.ic_action_back, 0,0);
		regionListView=(ListView) findViewById(R.id.list_province);

	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	private OnItemClickListener listener=new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent=new Intent(CityActivity.this,DistrictActivity.class);
			intent.putExtra("currentProvince", mCurrentProviceName);
			intent.putExtra("currentCity",position);// cities[position]
			startActivityForResult(intent, CITY);

		}
	};


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data!=null){
			switch (requestCode)
			{
			case CITY:
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
				CityActivity.this.setResult(RESULT_OK, intent);
				finish();
				break;
			}
		};

	}
}
