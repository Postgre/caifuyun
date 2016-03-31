package com.zepan.caifuyun.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseCityActivity;
/**
 * 三级联动  的县城
 * @author duanjie
 *
 */
public class DistrictActivity extends BaseCityActivity{
	private ListView regionListView;
	private static final int DISTRICT=0x102;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_province);
		initProvinceDatas();
		initView();
		initData();
	}
	private void initData(){
		Intent intent=getIntent();//currentPositionCity
		int currentPositionCity=intent.getIntExtra("currentCity", -1);
		mCurrentProviceName=intent.getStringExtra("currentProvince");


		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[currentPositionCity];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);
		regionListView.setAdapter(new ArrayAdapter<String>(DistrictActivity.this, 
				android.R.layout.simple_list_item_1,areas));
		regionListView.setOnItemClickListener(listener);
	}


	private void initView(){
		setHeaderFields(0, R.string.please_choice, 
				0,R.drawable.ic_action_back, 0,0);
		regionListView=(ListView) findViewById(R.id.list_province);
	}
	private OnItemClickListener listener=new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[position];
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
			Intent intent=new Intent();
			intent.putExtra("mCurrentProviceName", mCurrentProviceName);
			intent.putExtra("mCurrentCityName", mCurrentCityName);
			intent.putExtra("mCurrentDistrictName", mCurrentDistrictName);
			intent.putExtra("mCurrentZipCode",mCurrentZipCode);
			DistrictActivity.this.setResult(RESULT_OK, intent);
			finish(); 
			Toast.makeText(DistrictActivity.this, "当前选中:"  
					+mCurrentZipCode, Toast.LENGTH_SHORT).show();
		}
	};


}
