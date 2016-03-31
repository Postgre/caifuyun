package com.zepan.caifuyun.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.zepan.android.sdk.IRequestCallBack;
import com.zepan.android.sdk.JsonUtil;
import com.zepan.android.util.StringUtil;
import com.zepan.android.widget.AlphaImageView;
import com.zepan.android.widget.ShareView;
import com.zepan.android.widget.ShareView.onStatusListener;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.constants.Url;
import com.zepan.caifuyun.entity.Product;
import com.zepan.caifuyun.widget.ProgressTextView;
import com.zepan.caifuyun.widget.RoundProgressBar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;


/**
 * 产品介绍
 * @author long
 *
 */

public class ProductIntroductionActivity extends BaseActivity implements OnClickListener{

	private int productId;
	private ShareView shareView;
	private String productName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_introduction);
		setHeaderFields(0, R.string.product_introduce, 
				0,R.drawable.ic_action_back, 0,R.drawable.ic_action_share);
		initView();
		initData();

	}
	private void initData(){
		Intent intent=getIntent();
		productId=intent.getIntExtra("productId", 0);
		if(productId!=0){
			getProducDetail();
			getProductSubscription();
		}

	}
	private void initView(){
		((AlphaImageView)findViewById(R.id.iv_left)).setClickAlpha(150, true, false);
		((AlphaImageView)findViewById(R.id.iv_right)).setClickAlpha(150, true, false);
		//分享按钮
		findViewById(R.id.iv_right).setOnClickListener(this);
		shareView = (ShareView)findViewById(R.id.shareView_bottom);
		shareView.setEnabled(true);
		shareView.setCurrentView(0);
		//设置遮罩阴影层点击消失该界面
		findViewById(R.id.view_mask_bottom).setOnClickListener(this);
		//设置分享界面状态监听
		shareView.setOnStatusListener(new onStatusListener() {

			@Override
			public void onShow() {
				//显示
				findViewById(R.id.view_mask_bottom).setVisibility(View.VISIBLE);
			}

			@Override
			public void onDismiss() {
				//隐藏
				findViewById(R.id.view_mask_bottom).setVisibility(View.GONE);
			}
		});
		//申请合同
		findViewById(R.id.rabtn_apply_contract).setOnClickListener(this);
		//获取资料
		findViewById(R.id.rabtn_get_info).setOnClickListener(this);
		//发型方案
		findViewById(R.id.rabtn_distribution_plan).setOnClickListener(this);
		//我要预约
		findViewById(R.id.rabtn_order).setOnClickListener(this);
		//我要报单
		findViewById(R.id.rabtn_form).setOnClickListener(this);
	}
	//产品详情
	private void getProducDetail(){


		try {
			String token = URLEncoder.encode(getStringLocalData("token"), "UTF-8");

			String uid = getStringLocalData("uid");
			String tid = getStringLocalData("tid");
			String url = Url.GetProductInfo+"?token="+token+"&id="+productId+"&uid="+uid+"&tid="+tid;

			request(url, null,Method.GET, new IRequestCallBack() {

				@Override
				public void onResponse(JSONObject response) {
					producDetailResult(response);

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

	private void producDetailResult(JSONObject response){
		try {
			if(response.getInt("status")==0){
				JSONObject jsonInfo = response.getJSONObject("info");
				JSONObject jsonBody=jsonInfo.getJSONObject("body");
				JSONObject jsonProduct=jsonBody.getJSONObject("product");
				JSONObject jsonBase=jsonProduct.getJSONObject("base");

				Product product=(Product) JsonUtil.jsonToEntity(jsonBase, 
						Product.class);
				if(product!=null){
					if(product.getProductName()!=null){
						((TextView)findViewById(R.id.tv_product_name)).setText(product.getProductName());
						productName = product.getProductName();

					}

					//融资方
					//((TextView)findViewById(R.id.et_product_name)).setText();
					//投资方向
					if(product.getInvestType()==1){
						((TextView)findViewById(R.id.investment_direction_name)).setText("房地产");
					}else if(product.getInvestType()==2){
						((TextView)findViewById(R.id.investment_direction_name)).setText("征信");
					}else if(product.getInvestType()==3){
						((TextView)findViewById(R.id.investment_direction_name)).setText("工商企业");
					}else if(product.getInvestType()==4){
						((TextView)findViewById(R.id.investment_direction_name)).setText("PE/VC");
					}else if(product.getInvestType()==5){
						((TextView)findViewById(R.id.investment_direction_name)).setText("新三板");
					}else if(product.getInvestType()==6){
						((TextView)findViewById(R.id.investment_direction_name)).setText("打新");
					}else if(product.getInvestType()==7){
						((TextView)findViewById(R.id.investment_direction_name)).setText("定增");
					}else if(product.getInvestType()==8){
						((TextView)findViewById(R.id.investment_direction_name)).setText("二级市场－股票型 ");
					}else if(product.getInvestType()==9){
						((TextView)findViewById(R.id.investment_direction_name)).setText("其他");
					}
					
					//投资期限
					((TextView)findViewById(R.id.tv_deadline)).setText(product.getInvestmentDeadline()+"");
					//起投金额
					((TextView)findViewById(R.id.¥)).setText(product.getTranchePoint());
					//返佣比例
					((TextView)findViewById(R.id.proportion)).setText(product.getRebateRatio()+"");

					//产品亮点介绍
					((TextView)findViewById(R.id.feature_1)).setText(product.getFeature());


					//剩余时间
					//((TextView)findViewById(R.id.tv_surplus_time)).setText(product.getProductName());
					//认购金额
					//((TextView)findViewById(R.id.certified_amount_num)).setText(product.getTrancheTotal()+"");
					


				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	//产品认购
	private void getProductSubscription(){


		try {
			String token = URLEncoder.encode(getStringLocalData("token"), "UTF-8");

			String url = Url.GetProductSubscription+"?token="+token+"&product_id="+productId;

			request(url, null,Method.GET, new IRequestCallBack() {

				@Override
				public void onResponse(JSONObject response) {
					
					ProductSubscriptionResult(response);
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
	
	private void ProductSubscriptionResult(JSONObject response){
		try {
			if(response.getInt("status")==0){
				JSONObject jsonInfo = response.getJSONObject("info");
				JSONObject jsonBody=jsonInfo.getJSONObject("body");
				JSONObject jsonProduct=jsonBody.getJSONObject("product");
				
				Product product=(Product) JsonUtil.jsonToEntity(jsonProduct, 
						Product.class);
				if(product!=null){
					//剩余时间
					//((TextView)findViewById(R.id.tv_surplus_time)).setText(StringUtil.longToFormatTimeStr(product.getRemainTime()+"","yyyy-MM-dd"));
//					long time1=System.currentTimeMillis();
//					Log.i("===", "系统当前时间："+time1);
//					long time2=product.getLastPayTime();
//					Log.i("===", "打款结束日期："+time2);
//					Log.i("===", "剩余时间(当前时间与产品打款截止时间之差)："+(time1-time2));
					 long remaintime=product.getRemainTime();
					//计算出相差天数  
					int days=(int) Math.floor(remaintime/(24*3600*1000));  
					//计算出小时数  
					int leave1=(int) (remaintime%(24*3600*1000));//计算天数后剩余的毫秒数    
					int hours=(int) Math.floor(leave1/(3600*1000));  

					((TextView)findViewById(R.id.tv_surplus_time)).setText("剩余"+days+"天 "+hours+"小时");
					
					 
					//募集规模
					((TextView)findViewById(R.id.no_subscription_amount_num)).setText(product.getRaiseTotal()+"");
					//已认购金额
					((TextView)findViewById(R.id.certified_amount_num)).setText(product.getAmountSum()+"");
	                //已进款金额
					((TextView)findViewById(R.id.amount_income)).setText(product.getPaySum()+"");
					
					//认购百分比
					final float raisePercent=(float) (product.getAmountSum()/product.getRaiseTotal());
					DecimalFormat df = new DecimalFormat("#");
					final String percent=df.format((raisePercent*100));
					((RoundProgressBar)findViewById(R.id.roundProgressBar3)).setMax(100);
					if(!percent.equals("NaN")){
						((ProgressTextView)findViewById(R.id.tv_already_buy)).setProgress("", Integer.parseInt(percent));
						new Thread(new Runnable() {

							@Override
							public void run() {
								int progress=0;
								if(Integer.parseInt(percent)!=0){
									while(progress <= Integer.parseInt(percent)){
										progress += 1;
										((RoundProgressBar)findViewById(R.id.roundProgressBar3)).setProgress(progress);
										try {
											Thread.sleep(20);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
									}
								}
							}
						}).start();

				}else{
					((ProgressTextView)findViewById(R.id.tv_already_buy)).setProgress("", 0);
					((RoundProgressBar)findViewById(R.id.roundProgressBar3)).setProgress(0);

					}
					
					//进款百分比
					final float payPercent=(float) (product.getPaySum()/product.getRaiseTotal());
					DecimalFormat df2 = new DecimalFormat("#");
					final String percent2=df2.format((payPercent*100));
					((RoundProgressBar)findViewById(R.id.roundProgressBar4)).setMax(100);
					if(!percent2.equals("NaN")){
						((ProgressTextView)findViewById(R.id.tv_already_pay)).setProgress("", Integer.parseInt(percent2));
						new Thread(new Runnable() {

							@Override
							public void run() {
								int progress=0;
								if(Integer.parseInt(percent2)!=0){
									while(progress <= Integer.parseInt(percent2)){
										progress += 1;
										((RoundProgressBar)findViewById(R.id.roundProgressBar4)).setProgress(progress);
										try {
											Thread.sleep(20);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
									}
								}
							}
						}).start();

				}else{
					((ProgressTextView)findViewById(R.id.tv_already_pay)).setProgress("", 0);
					((RoundProgressBar)findViewById(R.id.roundProgressBar4)).setProgress(0);

					}

					
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//分享
		case R.id.iv_right:
			if(shareView.isShow()){
				shareView.dismiss();
			}else{
				shareView.show();
			}
			break;
			//阴影
		case R.id.view_mask_bottom:
			if(shareView.isShow()){
				shareView.dismiss();
			}
			break;
			//获取资料
		case R.id.rabtn_get_info:
			Intent infoIntent=new Intent(this, ObtainInformationActivity.class);
			startActivity(infoIntent);
			break;
			//申请合同
		case R.id.rabtn_apply_contract:
			Intent applyIntent=new Intent(this, ForContractActivity.class);
			applyIntent.putExtra("productId", productId);
			startActivity(applyIntent);
			break;
			//发行方案
		case R.id.rabtn_distribution_plan:
			Intent planIntent=new Intent(this, DistributionPlanActivity.class);
			planIntent.putExtra("productId", productId);
			startActivity(planIntent);
			break;
			//预约
		case R.id.rabtn_order:
			Intent orderIntent=new Intent(this, OrderActivity.class);
			orderIntent.putExtra("productId", productId);
			orderIntent.putExtra("productName", productName);
			startActivity(orderIntent);
			break;
			//报单
		case R.id.rabtn_form:
			Intent formIntent=new Intent(this, FormActivity.class);
			formIntent.putExtra("productId", productId);
			formIntent.putExtra("productName", productName);
			startActivity(formIntent);
			break;

		default:
			break;
		}

	}

}

