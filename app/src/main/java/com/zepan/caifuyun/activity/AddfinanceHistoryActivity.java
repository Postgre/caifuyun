package com.zepan.caifuyun.activity;


import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.zepan.android.sdk.IRequestCallBack;
import com.zepan.android.widget.AlphaImageView;
import com.zepan.android.widget.AlphaTextView;
import com.zepan.android.widget.CheckableEditText;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.R.drawable;
import com.zepan.caifuyun.R.layout;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.constants.Url;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
/**
 * 新增理财历史
 * 
 *
 */
public class AddfinanceHistoryActivity extends BaseActivity implements OnClickListener{

	private String productName;
	private String beginTime;
	private String endTime;
	private int accountId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addfinance_history);
		setHeaderFields(0,R.string.add_finance_history,0,
				R.drawable.ic_action_back, 0,0);
		initView();
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		Intent intent=getIntent();
		accountId=intent.getIntExtra("accountId", 0);
		beginTime=intent.getStringExtra(beginTime);
		endTime=intent.getStringExtra(endTime);
		
//		if(intent.hasExtra("productName")){
//			((TextView)findViewById(R.id.et_product_name)).setText(productName);
//			
//		}
//		if(intent.hasExtra("beginTime")){
//			((TextView)findViewById(R.id.et_start_time)).setText(beginTime);
//			
//		}
//		
//		if(intent.hasExtra("endTime")){
//			((TextView)findViewById(R.id.et_end_time)).setText(endTime);
//			
//		}
	}

	private void initView() {
		//((AlphaImageView)findViewById(R.id.iv_left)).setClickAlpha(150, true, false);

         findViewById(R.id.btn_save).setOnClickListener(this);
	}
	private void getCreateFinanceHistory(){
		request(Url.CreateFinancialHistory, getCreateFinanceHistoryParamJson(),Method.POST, new IRequestCallBack() {

			@Override
			public void onResponse(JSONObject response) {
				try {
					if(response.getInt("status")==0){
						printToast("创建成功");
					}else{
						printToast(response.getString("message"));
					}

					finish();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void onErrorResponse(String errorMessage) {
				// TODO Auto-generated method stub

			}
		});
	}
	private JSONObject getCreateFinanceHistoryParamJson(){
		JSONObject json=new JSONObject();
		try {
			json.put("token",getStringLocalData("token"));
			json.put("accountId",accountId);//产品id
			//json.put("productName",((TextView)findViewById(R.id.et_product_name)).getText());//产品名称
//			json.put("productName",productName);//产品名称
//			json.put("beginTime",beginTime);//开始时间
//			json.put("endTime",endTime);//结束时间
			/*
			 * 下面数据用于测试 
			 */
			json.put("productName","oo");//产品名称
			json.put("beginTime","2011年12月");//开始时间
			json.put("endTime","2013年3月");//结束时间
			
			json.put("amount",((CheckableEditText)findViewById(R.id.ed_make_payment)).getText());//理财金额
			json.put("expectProfit",((CheckableEditText)findViewById(R.id.ed_make)).getText());//预期收益
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_save:
			if(((CheckableEditText)findViewById(R.id.ed_make)).check()){
				getCreateFinanceHistory();
			}
			
			break;

		default:
			break;
		}
	}


}
