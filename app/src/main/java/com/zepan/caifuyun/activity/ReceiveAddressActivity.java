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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.Request.Method;
import com.zepan.android.sdk.IRequestCallBack;
import com.zepan.android.sdk.JsonUtil;
import com.zepan.android.widget.AlphaImageView;
import com.zepan.android.widget.AlphaTextView;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.adapter.ReceiveAddressAdapter;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.constants.Url;
import com.zepan.caifuyun.entity.ReceiveAddress;
/**
 * 
 * @author duanjie
 *收件地址
 */
public class ReceiveAddressActivity extends BaseActivity implements OnClickListener,OnItemClickListener{

	private ReceiveAddressAdapter receiveAddressAdapter;
	private List<ReceiveAddress> receiveAddressList;
	private int defaultId=-1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receive_address);
		setHeaderFields(0, R.string.email_address, 
				R.string.administration,R.drawable.ic_action_back, 0,0);
		initView();
		initData();
	}
	private void initView(){
		((AlphaImageView)findViewById(R.id.iv_left)).setClickAlpha(150, true, false);
		((AlphaTextView)findViewById(R.id.tv_right)).setClickAlpha(100);
		
		findViewById(R.id.tv_right).setOnClickListener(this);
		ListView  listView=(ListView) findViewById(android.R.id.list);
		receiveAddressList=new ArrayList<ReceiveAddress>();
		receiveAddressAdapter=new ReceiveAddressAdapter(this, receiveAddressList);
		listView.setAdapter(receiveAddressAdapter);
		listView.setOnItemClickListener(this);
	}
	private void initData(){
		Intent intent=getIntent();
		defaultId=intent.getIntExtra("defaultId", -1);
	}
	@Override
	protected void onStart() {
		getReceiveAddress();
		super.onStart();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_right:
			Intent intent=new Intent(this, ManagerReceiveAddressActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}

	}
	private void getReceiveAddress(){
		request(Url.GetAddressList, getReceiveAddressParamJson(),Method.POST, new IRequestCallBack() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				getReceiveAddressParamJsonResult(response);
			}

			@Override
			public void onErrorResponse(String errorMessage) {
				// TODO Auto-generated method stub

			}
		});
	}
	private JSONObject getReceiveAddressParamJson(){
		JSONObject json=new JSONObject();
		try {
			json.put("uid",getStringLocalData("uid"));
			json.put("tid",getStringLocalData("tid"));
			json.put("token",getStringLocalData("token"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
	private void getReceiveAddressParamJsonResult(JSONObject response){
		try {
			if(response.getInt("status")==0){
				if(receiveAddressList.size()>0){
					receiveAddressList.clear();
				}
				JSONArray json=response.getJSONArray("return_list");
				for(int i=0;i<json.length();i++){
					ReceiveAddress receiveAddress=(ReceiveAddress) JsonUtil.jsonToEntity(json.getJSONObject(i), ReceiveAddress.class);
					receiveAddressList.add(receiveAddress);
				}
				receiveAddressAdapter.notifyDataSetChanged();
			}else{
				printToast(response.getString("message"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ReceiveAddress receiveAddress=receiveAddressList.get(position);
		getDefaultAdress(receiveAddress.getId());
	}
	private void getDefaultAdress(int addressId){
		request(Url.SetDefaultAddress, getDefaultAdressParamJson(addressId),Method.POST, new IRequestCallBack() {

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
	private JSONObject getDefaultAdressParamJson(int addressId){
		JSONObject json=new JSONObject();
		try {
			json.put("id",addressId);//
			json.put("oid",defaultId);//
			json.put("token", getStringLocalData("token"));
			json.put("is_default",1);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	private void getDefaultAdressResult(JSONObject response){
		try {
			if(response.getInt("status")==0){
				printToast("设置默认成功");
				finish();
			}else{
				printToast(response.getString("message"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}     
	}
}
