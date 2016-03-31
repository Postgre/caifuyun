package com.zepan.caifuyun.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.zepan.android.sdk.IRequestCallBack;
import com.zepan.android.widget.AlphaImageView;
import com.zepan.android.widget.AlphaTextView;
import com.zepan.android.widget.CheckableEditText;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.R.id;
import com.zepan.caifuyun.R.layout;
import com.zepan.caifuyun.R.menu;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.constants.Url;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class RegisterThreeActivity extends BaseActivity implements OnClickListener{
	private CheckableEditText nameEditText;
	private CheckableEditText pwdEditText;
	private String phone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_three);
		setHeaderFields(0, R.string.title_register,0,
				R.drawable.ic_action_back, 0, 0);
		initView();
		initData();
	}


	private void initData(){
		Intent intent=getIntent();
		phone=intent.getStringExtra("phone");

	}
	private void initView(){
		((AlphaImageView)findViewById(R.id.iv_left)).setClickAlpha(150, true, false);
		((AlphaTextView)findViewById(R.id.tv_right)).setClickAlpha(100);

		findViewById(R.id.btn_register).setOnClickListener(this);
		nameEditText=(CheckableEditText) findViewById(R.id.tv_name);
		pwdEditText=(CheckableEditText) findViewById(R.id.tv_password);
	}



	private void  getRegisterUser(){

		request(Url.RegisterUser,getRegisterUserParamJson(), Method.POST,new IRequestCallBack() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				getRegisterUserResult(response);
			}

			@Override
			public void onErrorResponse(String errorMessage) {
				// TODO Auto-generated method stub

			}
		});
	}


	private JSONObject getRegisterUserParamJson(){
		JSONObject paramJson=new JSONObject();
		try {
			//getStringLocalData("passport")  phone   "13816715801"
			paramJson.put("loginName",phone);
			paramJson.put("name",nameEditText.getText().toString());
			paramJson.put("password", pwdEditText.getText().toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return paramJson;
	}

	private void getRegisterUserResult(JSONObject response){
		try {
			if(response.getInt("status")==0){
				saveDataToLocal("name", response.getString("name"));
				saveDataToLocal("token", response.getString("token"));
				saveDataToLocal("passport", response.getString("passport"));
				saveDataToLocal("uid", response.getString("uid"));
				Intent registerIntent=new Intent(RegisterThreeActivity.this,CertificationActivity.class);
				startActivity(registerIntent);
			}else if(response.getInt("status")==1){
				String message=response.getString("message");
				printToast(message);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_register:
			if(nameEditText.check() && pwdEditText.check()){
				getRegisterUser();


			}
			break;

		default:
			break;
		}

	}


}
