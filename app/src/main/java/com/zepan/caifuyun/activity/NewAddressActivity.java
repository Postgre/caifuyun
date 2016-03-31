package com.zepan.caifuyun.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.zepan.android.sdk.IRequestCallBack;
import com.zepan.android.widget.CheckableEditText;
import com.zepan.android.widget.ToggleButton;
import com.zepan.android.widget.ToggleButton.OnToggleChanged;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.constants.Code;
import com.zepan.caifuyun.constants.Url;
import com.zepan.caifuyun.entity.ReceiveAddress;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 
 * @author duanjie
 *新建地址
 */
public class NewAddressActivity extends BaseActivity implements OnClickListener,OnToggleChanged{

	private final static int Address=0x100;
	private CheckableEditText  adressName;
	private CheckableEditText  mobilePhone;
	private TextView  area;
	private CheckableEditText  address;
	private TextView  zip;
	private ToggleButton  toggDefault;
	private ReceiveAddress receiveAdress;
	private String tag;
	private boolean switchDefault;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_address);
		initView();
		initData();
	}
	private void initView(){
		setHeaderFields(0, R.string.new_address, 
				0,R.drawable.ic_action_back, 0,0);
		findViewById(R.id.tv_area).setOnClickListener(this);
		findViewById(R.id.save).setOnClickListener(this);
		findViewById(R.id.tv_right).setOnClickListener(this);
		adressName=(CheckableEditText) findViewById(R.id.et_addressee_name);
		mobilePhone=(CheckableEditText) findViewById(R.id.et_mobile_phone);
		area=(TextView) findViewById(R.id.tv_area);
		address=(CheckableEditText) findViewById(R.id.tv_address);
		zip=(TextView) findViewById(R.id.tv_zip);
		toggDefault=(ToggleButton) findViewById(R.id.togg_default);
		toggDefault.setOnToggleChanged(this);
	}
	private void initData(){
		Intent intent=getIntent();
		if(intent.hasExtra("tag")){
			tag=intent.getStringExtra("tag");
			if(tag.equals(Code.modify)){
				setHeaderFields(0, R.string.modify_address, 
						R.string.delete,R.drawable.ic_action_back, 0,0);
			}else if(tag.equals(Code.create)){
				setHeaderFields(0, R.string.new_address, 
						0,R.drawable.ic_action_back, 0,0);
			}
		}
		if(intent.hasExtra("address")){
			receiveAdress=intent.getParcelableExtra("address");
			if(receiveAdress.getName()!=null){
				adressName.setText(receiveAdress.getName());
			}
			if(receiveAdress.getMobile()!=null){
				mobilePhone.setText(receiveAdress.getMobile());
			}
			if(receiveAdress.getRegion()!=null){
				area.setText(receiveAdress.getRegion());
			}
			if(receiveAdress.getAddress()!=null){
				address.setText(receiveAdress.getAddress());
			}
			if(receiveAdress.getZipcode()!=null){
				zip.setText(receiveAdress.getZipcode());
			}
			if(receiveAdress.getDefault_address()==1){
				toggDefault.setToggleOn();
			}else{
				toggDefault.setToggleOff();
			}

		}
	}
	private void getAdress(){
		String url=null;
		if(Code.modify.equals(tag)){
			url=Url.ModifyAddress;
		}else if(Code.create.equals(tag)){
			url=Url.GreateAddress;
		}
		request(url, getAdressParamJson(),Method.POST, new IRequestCallBack() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				getAdressResult(response);
			}

			@Override
			public void onErrorResponse(String errorMessage) {
				// TODO Auto-generated method stub

			}
		});
	}
	private JSONObject getAdressParamJson(){
		JSONObject json=new JSONObject();
		try {
			json.put("token",getStringLocalData("token"));//token
			json.put("uid",getStringLocalData("uid"));//uid
			json.put("tid",getStringLocalData("tid"));//tid
			json.put("address",address.getText().toString());//详细地址
			json.put("region",area.getText().toString());//地区
			json.put("zipcode",zip.getText().toString());//邮编
			json.put("name",adressName.getText().toString());//收件人姓名
			json.put("mobile",mobilePhone.getText().toString());//收件人手机
			if(Code.modify.equals(tag)){
				json.put("id",receiveAdress.getId());//地址id
			}else if(Code.create.equals(tag)){
				json.put("is_default",switchDefault?1:0);//地址id
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	private void getAdressResult(JSONObject response){
		try {
			if(response.getInt("status")==0){
				finish();
			}else{
				printToast(response.getString("message"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}     
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data!=null){
			switch (requestCode){
			case Address:
				String provice=data.getStringExtra("mCurrentProviceName");
				String city=data.getStringExtra("mCurrentCityName");
				String district=data.getStringExtra("mCurrentDistrictName");
				String zipcode=data.getStringExtra("mCurrentZipCode");
				((TextView)findViewById(R.id.tv_area)).setText(provice+""+city+""+district);
				((TextView)findViewById(R.id.tv_zip)).setText(zipcode);

				break;
			}
		}
		super.onActivityResult(requestCode,resultCode,data);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save:
			if(TextUtils.isEmpty(area.getText())){
				printToast("地区不能为空");
				return;
			}
			if(TextUtils.isEmpty(zip.getText())){
				printToast("邮编不能为空");
				return;
			}
			if(adressName.check()&&mobilePhone.check()&&address.check()){
				getAdress();
			}
			break;
		case R.id.tv_area:
			Intent intent=new Intent(NewAddressActivity.this, ProvinceActivity.class);
			startActivityForResult(intent, Address);
			break;
		case R.id.tv_right:
			getDeleteAdress();
			break;

		default:
			break;
		}

	}
	private void getDeleteAdress(){
		request(Url.DeleteAddress, getDeleteAdressParamJson(),Method.POST, new IRequestCallBack() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				getDeleteAdressResult(response);
			}

			@Override
			public void onErrorResponse(String errorMessage) {
				// TODO Auto-generated method stub

			}
		});
	}
	private JSONObject getDeleteAdressParamJson(){
		JSONObject json=new JSONObject();
		try {
			json.put("id",receiveAdress.getId());//
			json.put("token", getStringLocalData("token"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	private void getDeleteAdressResult(JSONObject response){
		try {
			if(response.getInt("status")==0){
				printToast("删除成功");
				finish();
			}else{
				printToast(response.getString("message"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}     
	}

	@Override
	public void onToggle(boolean on) {
		switchDefault=on;
		if(tag!=null){
			if(tag.equals(Code.modify)){
				getDefaultAdress();
			}
		}

	}
	private void getDefaultAdress(){
		request(Url.SetDefaultAddress, getDefaultAdressParamJson(),Method.POST, new IRequestCallBack() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				getDefaultAdressResult(response);
			}

			@Override
			public void onErrorResponse(String errorMessage) {
				// TODO Auto-generated method stub

			}
		});
	}
	private JSONObject getDefaultAdressParamJson(){
		JSONObject json=new JSONObject();
		try {
			json.put("id",receiveAdress.getId());//
			json.put("token", getStringLocalData("token"));
			json.put("is_default",switchDefault?1:0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	private void getDefaultAdressResult(JSONObject response){
		try {
			if(response.getInt("status")==0){
				if(switchDefault){
					printToast("设置默认成功");
				}else{
					printToast("取消默认成功");
				}
			}else{
				printToast(response.getString("message"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}     
	}

}
