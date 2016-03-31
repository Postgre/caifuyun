package com.zepan.caifuyun.activity;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.zepan.android.sdk.IRequestCallBack;
import com.zepan.android.widget.AlphaImageView;
import com.zepan.android.widget.CheckableEditText;
import com.zepan.android.widget.CountDownButton;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.constants.Url;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;

public class RegisterTwoActivity extends BaseActivity implements OnClickListener{
	private String phone;
	private CheckableEditText verify;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_two);
		setHeaderFields(0, R.string.title_register,0,
				R.drawable.ic_action_back, 0, 0);
		initView();
		initData();

	}



	private void initView() {
		((AlphaImageView)findViewById(R.id.iv_left)).setClickAlpha(150, true, false);
		findViewById(R.id.btn_continue).setOnClickListener(this);
		findViewById(R.id.btn_continue).setEnabled(false);
		verify=((CheckableEditText)findViewById(R.id.edtTx_name));
		CountDownButton countBtn=(CountDownButton)findViewById(R.id.select_customername);
		countBtn.setOnClickListener(new CountDownButton.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				getPhoneCode();
			}
		});
		verify.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.length()>0){
					findViewById(R.id.btn_continue).setEnabled(true);
				}else{
					findViewById(R.id.btn_continue).setEnabled(false);
				}
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	private void initData(){
		Intent intent=getIntent();
		phone=intent.getStringExtra("phone");
		findViewById(R.id.select_customername).performClick();
		getPhoneCode();
	}

	private void getPhoneCode(){
		String url=Url.GetCaptcha+"?loginName="+phone;
		//Url.UserRootUrl
		request(url,null, Method.GET,new IRequestCallBack() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				getPhoneCodeResult(response);
			}

			@Override
			public void onErrorResponse(String errorMessage) {
				// TODO Auto-generated method stub

			}
		});
	}
	private void getPhoneCodeResult(JSONObject response){
		try {
			if(response.getInt("status")==0){

			}else if(response.getInt("status")==1){
				String message=response.getString("message");
				printToast(message);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);

	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_continue:
			if(verify.check()){
				getVerifyPhoneCode();
			}
			break;

		}
	}
	private void getVerifyPhoneCode(){
		findViewById(R.id.select_customername).performClick();
		String url=Url.CheckCaptcha+"?loginName="+phone+"&captcha="+verify.getText().toString();
		//Url.UserRootUrl
		request(url,null, Method.GET,new IRequestCallBack() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				getVerifyPhoneCodeResult(response);
			}

			@Override
			public void onErrorResponse(String errorMessage) {
				// TODO Auto-generated method stub

			}
		});
	}
	private void getVerifyPhoneCodeResult(JSONObject response){
		try {
			if(response.getInt("status")==0){
				Intent continueIntent=new Intent(RegisterTwoActivity.this,RegisterThreeActivity.class);
				continueIntent.putExtra("phone", phone);
				startActivity(continueIntent);
			}else if(response.getInt("status")==1){
				String message=response.getString("message");
				printToast(message);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


}
