package com.zepan.caifuyun.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.zepan.android.sdk.IRequestCallBack;
import com.zepan.android.sdk.JsonUtil;
import com.zepan.android.widget.AlphaImageView;
import com.zepan.android.widget.AlphaTextView;
import com.zepan.android.widget.CheckableEditText;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.constants.Code;
import com.zepan.caifuyun.constants.Url;
import com.zepan.caifuyun.entity.ReceiveAddress;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
/**
 * 申请合同
 * @author duanjie
 *
 */
public class ForContractActivity extends BaseActivity implements OnClickListener{
	private int productId;
	private ReceiveAddress receiveAddress;
	private CheckableEditText contractNum;
	private int defaultId=-1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_for_contract);
		setHeaderFields(0, R.string.for_contract, 
				0,R.drawable.ic_action_back, 0,0);
		initView();
		initData();
	}
	private void initView(){
		((AlphaImageView)findViewById(R.id.iv_left)).setClickAlpha(150, true, false);
		((AlphaTextView)findViewById(R.id.tv_new)).setClickAlpha(100);
		contractNum=(CheckableEditText)findViewById(R.id.et_contract_number);
		//新增地址
		findViewById(R.id.tv_new).setOnClickListener(this);
		findViewById(R.id.listitem_forcontract).setOnClickListener(this);
		findViewById(R.id.for_contract).setOnClickListener(this);

	}
	@Override
	protected void onStart() {
		getDefaultAdress();
		super.onStart();
	}
	private void initData(){
		Intent intent=getIntent();
		if(intent.hasExtra("productId")){
			productId=intent.getIntExtra("productId",-1);
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_new:
			Intent newIntent=new Intent(this, NewAddressActivity.class);
			newIntent.putExtra("tag", Code.create);
			startActivity(newIntent);
			break;
		case R.id.listitem_forcontract:
			Intent receiveIntent=new Intent(this, ReceiveAddressActivity.class);
			receiveIntent.putExtra("defaultId", defaultId);
			startActivity(receiveIntent);
			break;
		case R.id.for_contract:
			if(!contractNum.check()){
				return;
			}
			if(receiveAddress!=null){
				getCreateContract();
			}else{
				printToast("没有默认地址");
				return;
			}

			break;
		default:
			break;
		}

	}
	private void getCreateContract(){
		request(Url.CreateContractApplyRecord, getCreateContractParamJson(),Method.POST, new IRequestCallBack() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				getCreateContractJsonResult(response);
			}

			@Override
			public void onErrorResponse(String errorMessage) {
				// TODO Auto-generated method stub

			}
		});
	}
	private JSONObject getCreateContractParamJson(){
		JSONObject json=new JSONObject();
		try {
			json.put("uid",getStringLocalData("uid"));//所有用户token
			json.put("tid",getStringLocalData("tid"));//所有用户token
			json.put("token",getStringLocalData("token"));//所有用户token
			json.put("product_id",productId);//产品号
			json.put("address",receiveAddress.getRegion()+receiveAddress.getAddress());//合同寄件地址
			json.put("num",Integer.parseInt(contractNum.getText().toString()));//合同申领份数
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
	private void getCreateContractJsonResult(JSONObject response){
		try {
			if(response.getInt("status")==0){
				printToast("提交申请成功");
				finish();
			}else{
				printToast(response.getString("message"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getDefaultAdress(){
		try {
			String url = Url.GetDefaultAddress;
			JSONObject jsonParams=new JSONObject();
			jsonParams.put("token", getStringLocalData("token"));
			jsonParams.put("uid", getStringLocalData("uid"));
			jsonParams.put("tid", getStringLocalData("tid"));
			request(url, jsonParams,Method.POST, new IRequestCallBack() {

				@Override
				public void onResponse(JSONObject response) {
					// TODO Auto-generated method stub
					getDefaultAdressParamJsonResult(response);
				}

				@Override
				public void onErrorResponse(String errorMessage) {
					// TODO Auto-generated method stub

				}
			});
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private void getDefaultAdressParamJsonResult(JSONObject response){
		try {
			if(response.getInt("status")==0){
				JSONArray jsonArray=response.getJSONArray("return_list");
				if(jsonArray.length()>0){
					JSONObject json=jsonArray.getJSONObject(0);
					receiveAddress=(ReceiveAddress) JsonUtil.jsonToEntity(json, ReceiveAddress.class);
					defaultId=receiveAddress.getId();
					if(receiveAddress.getName()!=null){
						((TextView)findViewById(R.id.tv_name)).setText(receiveAddress.getName());
					}
					if(receiveAddress.getMobile()!=null){
						((TextView)findViewById(R.id.mobile_phone)).setText(receiveAddress.getMobile());
					}
					if(receiveAddress.getRegion()!=null){
						((TextView)findViewById(R.id.address)).setText(receiveAddress.getRegion());
					}
					if(receiveAddress.getZipcode()!=null){
						((TextView)findViewById(R.id.zip)).setText(receiveAddress.getZipcode());
					}
					findViewById(R.id.iv_choice_right).setVisibility(View.VISIBLE);
				}else{
					((TextView)findViewById(R.id.tv_name)).setText("");
					((TextView)findViewById(R.id.mobile_phone)).setText("");
					((TextView)findViewById(R.id.address)).setText("");
					((TextView)findViewById(R.id.zip)).setText("");
					findViewById(R.id.iv_choice_right).setVisibility(View.GONE);
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
