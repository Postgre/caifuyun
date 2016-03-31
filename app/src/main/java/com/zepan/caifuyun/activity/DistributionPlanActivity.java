package com.zepan.caifuyun.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;

import android.widget.TextView;

import com.android.volley.Request.Method;

import com.zepan.android.sdk.IRequestCallBack;
import com.zepan.android.sdk.JsonUtil;
import com.zepan.android.util.StringUtil;
import com.zepan.caifuyun.R;

import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.constants.Url;
import com.zepan.caifuyun.entity.Product;

//发行方案
public class DistributionPlanActivity extends BaseActivity{

	
	private int productId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_distribution_plan);
		initData();
		initView();
		
	}
	private void initData(){
		Intent intent=getIntent();
		productId=intent.getIntExtra("productId", 0);
		if(productId!=0){
			getDataList();
		}

	}

	private void initView() {
		// TODO Auto-generated method stub
		setHeaderFields(0, R.string.distribution_plan, 
				0,R.drawable.ic_action_back, 0,0);
		
		
	}
	
	private void getDataList(){

        try {
			String token = URLEncoder.encode(getStringLocalData("token"), "UTF-8");

			String uid = getStringLocalData("uid");
			String tid = getStringLocalData("tid");
			
			String url = Url.GetProductInfo+"?token="+token+"&id="+productId+"&uid="+uid+"&tid="+tid;
			
			request(url, null,Method.GET, new IRequestCallBack() {

				@Override
				public void onResponse(JSONObject response) {
					DataListResult(response);

				}

				@Override
				public void onErrorResponse(String errorMessage) {
					// TODO Auto-generated method stub

				}
			});
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void DataListResult(JSONObject response){
		try {
			if(response.getInt("status")==0){
				JSONObject jsonInfo = response.getJSONObject("info");
				JSONObject jsonBody=jsonInfo.getJSONObject("body");
				JSONObject jsonProduct=jsonBody.getJSONObject("product");
				JSONObject jsonScheme=jsonProduct.getJSONObject("scheme");

				Product product=(Product) JsonUtil.jsonToEntity(jsonScheme, 
						Product.class);
				if(product!=null){
					//开放日
					((TextView)findViewById(R.id.open_day)).setText(StringUtil.longToFormatTimeStr(product.getOpenDate()+"", "yyyy年MM月dd日"));
					//本期募集总额
					((TextView)findViewById(R.id.collect_num)).setText(product.getTheRaiseTotal()+"");
					
					//募集预警通知
					((TextView)findViewById(R.id.waring_notice)).setText(product.getWarningLimit()+"");
					//成立规模
					((TextView)findViewById(R.id.found_scale)).setText(product.getHoldTotal()+"");
					//大单金额
					((TextView)findViewById(R.id.big_money)).setText(product.getLargeAmount()+"");
					
					if(product.getIsCustomerUpload()==1){
						((TextView)findViewById(R.id.upload)).setText("是");
					}else{
						((TextView)findViewById(R.id.upload)).setText("否");
					}
					//佣金起点
					((TextView)findViewById(R.id.salary_start_num)).setText(product.getCommissionPoint()+"万元");
					//佣金比率
					((TextView)findViewById(R.id.salary_ratio_num)).setText(product.getCommissionRatio());
				}
				
				JSONObject jsonBase=jsonProduct.getJSONObject("base");

				Product product2=(Product) JsonUtil.jsonToEntity(jsonBase, 
						Product.class);
				if(product2!=null){
					//截止抢购时间
					((TextView)findViewById(R.id.deadline)).setText(StringUtil.longToFormatTimeStr(product2.getLastPayTime()+"","yyyy年MM月dd日"));
				}

				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
