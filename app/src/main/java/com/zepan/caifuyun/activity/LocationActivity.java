package com.zepan.caifuyun.activity;


import android.os.Bundle;


import java.util.ArrayList;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.core.SearchResult;
import com.zepan.caifuyun.R;

import com.zepan.caifuyun.adapter.GPSAdapter;

import com.zepan.caifuyun.base.BaseActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

//定位显示地址列表界面
public class LocationActivity extends BaseActivity implements OnGetGeoCoderResultListener,BDLocationListener {



	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private String tempcoor="gcj02";

	private LocationClient mLocationClient;

	private GeoCoder mPoiSearch = null;
	private List<PoiInfo> poiInfoList=new ArrayList<PoiInfo>();
	private GPSAdapter gpsAdapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		initView();
	}


	private void initView(){
		mLocationClient = new LocationClient(this.getApplicationContext());
		mLocationClient.registerLocationListener(this);
		initLocation();
		mLocationClient.start();//定位SDK start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
		mPoiSearch = GeoCoder.newInstance();
		mPoiSearch.setOnGetGeoCodeResultListener(this);
		findViewById(R.id.iv_left).setOnClickListener(new OnClickListener() {



			@Override
			public void onClick(View v) {
				finish();

			}
		});
		ListView gpsListView=(ListView) findViewById(android.R.id.list);
		PoiInfo poiInfo=new PoiInfo();
		poiInfo.name="不显示位置";
		poiInfoList.add(poiInfo);
		gpsAdapter=new GPSAdapter(this, poiInfoList);
		gpsListView.setAdapter(gpsAdapter);
		gpsListView.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("static-access")
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent=new Intent();
				if(poiInfoList.get(position)!=null){
					if(position!=0){
						intent.putExtra("gpsName", poiInfoList.get(position).name);
					}
					if(position!=0&&position!=1){
						intent.putExtra("gpsAdress", poiInfoList.get(position).address);
						intent.putExtra("latitude", poiInfoList.get(position).location.latitude);//纬度
						intent.putExtra("longitude", poiInfoList.get(position).location.longitude);//经度
					}
				}
				LocationActivity.this.setResult(LocationActivity.this.RESULT_OK, intent);
				finish();

			}
		});

	}
	private void initLocation(){
		LocationClientOption option = new LocationClientOption();
		tempcoor="gcj02";//国家测绘局标准
		//		 tempcoor="bd09ll";//百度经纬度标准
		//		 tempcoor="bd09";//百度墨卡托标准
		option.setLocationMode(tempMode);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType(tempcoor);//可选，默认gcj02，设置返回的定位结果坐标系，
		option.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);//可选，默认false,设置是否使用gps
		option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

		mLocationClient.setLocOption(option);
	}

	@Override

	public void onReceiveLocation(BDLocation location) {
		//Receive Location
		StringBuffer sb = new StringBuffer(256);
		sb.append("time : ");
		sb.append(location.getTime());
		sb.append("\nerror code : ");
		sb.append(location.getLocType());
		sb.append("\nlatitude : ");
		sb.append(location.getLatitude());
		sb.append("\nlontitude : ");
		sb.append(location.getLongitude());
		sb.append("\nradius : ");
		sb.append(location.getRadius());
		if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS定位结果
			sb.append("\nspeed : ");
			sb.append(location.getSpeed());// 单位：公里每小时
			sb.append("\nsatellite : ");
			sb.append(location.getSatelliteNumber());
			sb.append("\nheight : ");
			sb.append(location.getAltitude());// 单位：米
			sb.append("\ndirection : ");
			sb.append(location.getDirection());
			sb.append("\naddr : ");
			sb.append(location.getAddrStr());
			sb.append("\ndescribe : ");
			sb.append("gps定位成功");
			mLocationClient.stop();

		} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// 网络定位结果
			sb.append("\naddr : ");
			sb.append(location.getCity());
			//location.getProvince()
			sb.append("\n"+location.getAddress());
			//运营商信息
			sb.append("\noperationers : ");
			sb.append(location.getOperators());
			sb.append("\ndescribe : ");
			sb.append("网络定位成功");
			mLocationClient.stop();
		} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
			sb.append("\ndescribe : ");
			sb.append("离线定位成功，离线定位结果也是有效的");
		} else if (location.getLocType() == BDLocation.TypeServerError) {
			sb.append("\ndescribe : ");
			sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
		} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
			sb.append("\ndescribe : ");
			sb.append("网络不同导致定位失败，请检查网络是否通畅");
		} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
			sb.append("\ndescribe : ");
			sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
		}
		PoiInfo poiInfo=new PoiInfo();
		poiInfo.name=location.getCity();
		poiInfoList.add(poiInfo);
		mPoiSearch.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(location.getLatitude(),location.getLongitude())));  
		//		mPoiSearch.searchNearby(new PoiNearbySearchOption().
		//				radius(1000).location(new LatLng(location.getLatitude(),location.getLongitude())).keyword("公司").pageNum(1));
		Log.i("BaiduLocationApiDem", sb.toString());
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		mLocationClient.stop();
		mPoiSearch.destroy();
		super.onStop();
	}
	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null
				|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			Toast.makeText(this, "未找到结果", Toast.LENGTH_LONG)
			.show();
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {

			List<PoiInfo> poiInfoResultList=result.getPoiList();
			for(PoiInfo poiInfo:poiInfoResultList){
				poiInfoList.add(poiInfo);
			}
			gpsAdapter.notifyDataSetChanged();
			return;
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

			// 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
			String strInfo = "在";
			for (PoiInfo poiInfo : result.getPoiList()) {
				strInfo += poiInfo.address;
				strInfo += ",";
			}
			strInfo += "找到结果";
			Toast.makeText(this, strInfo, Toast.LENGTH_LONG)
			.show();
		}

	}

}

