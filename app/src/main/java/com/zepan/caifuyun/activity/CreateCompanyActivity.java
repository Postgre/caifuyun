package com.zepan.caifuyun.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.zepan.android.sdk.IRequestCallBack;
import com.zepan.android.widget.AlphaImageView;
import com.zepan.android.widget.CheckableEditText;
import com.zepan.android.widget.WheelTextView;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.constants.Url;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
/**
 * 创建新公司
 * @author duanjie
 *
 */
public class CreateCompanyActivity extends BaseActivity implements OnClickListener{
	private String companyName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_company);
		setHeaderFields(0, R.string.create_company,0,
				R.drawable.ic_action_back, 0, 0);
		initData();
		initView();

	}

	private void initData() {
		Intent intent=getIntent();
		companyName=intent.getStringExtra("companyName");

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		return imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
	}
	private void initView() {
		/** 公司名*/
		((CheckableEditText)findViewById(R.id.tv_company_name)).setText(companyName);
		/** 公司规模*/

		((AlphaImageView)findViewById(R.id.iv_left)).setClickAlpha(150, true, false);
		/**提交的按钮*/
		findViewById(R.id.btn_submit).setOnClickListener(this);
	}




	private void  getCreateTenant(){
		request(Url.CreateTenant,getCreateTenantParamJson(), Method.POST,new IRequestCallBack() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				getCreateTenantResult(response);
			}

			@Override
			public void onErrorResponse(String errorMessage) {
				// TODO Auto-generated method stub

			}
		});
	}


	private JSONObject getCreateTenantParamJson(){
		JSONObject paramJson=new JSONObject();
		try {
			paramJson.put("token",getStringLocalData("token"));
			paramJson.put("userName",getStringLocalData("passport"));
			paramJson.put("company",((CheckableEditText)findViewById(R.id.tv_company_name)).getText().toString());
			paramJson.put("size",((WheelTextView)findViewById(R.id.tv_company_size)).getText().toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return paramJson;
	}


	private void getCreateTenantResult(JSONObject response){
		try {
			if(response.getInt("status")==0){
				saveDataToLocal("tenantId", response.getString("tid"));
				Intent createCompanyIntent=new Intent(CreateCompanyActivity.this,CertificationCpInfoActivity.class);
				createCompanyIntent.putExtra("company", response.getString("company"));
				startActivity(createCompanyIntent);
			}else{
				String message=response.getString("message");
				printToast(message);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onClick(View v) {
		if(((CheckableEditText)findViewById(R.id.tv_company_name)).check()){
			/*	&&((WheelTextView)findViewById(R.id.tv_company_size)).check()*/
			getCreateTenant();
		}
	}
}
