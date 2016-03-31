package com.zepan.caifuyun.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.zepan.android.sdk.IRequestCallBack;
import com.zepan.android.widget.AlphaImageView;
import com.zepan.android.widget.CheckableEditText;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.constants.Url;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
/**
 * 预约
 * @author duanjie
 *
 */
public class OrderActivity extends BaseActivity implements OnClickListener{
	private int productId;
	private int orderStyle=0;
	private int customId;
	private int channelId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);
		setHeaderFields(0, R.string.order, 
				0,R.drawable.ic_action_back, 0,0);
		initView();
		initData();
	}
	private void initView(){
		((AlphaImageView)findViewById(R.id.iv_left)).setClickAlpha(150, true, false);
		findViewById(R.id.Submit_Booking).setOnClickListener(this);
		findViewById(R.id.select_customername).setOnClickListener(this);
		findViewById(R.id.tv_border_choice_channel).setOnClickListener(this);
		findViewById(R.id.rl_choice_channel).setVisibility(View.GONE); 
		((RadioGroup)findViewById(R.id.rg_channel_order)).setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_direct_entry:
					findViewById(R.id.rl_choice_channel).setVisibility(View.GONE);
					//findViewById(R.id.ly_customer).setVisibility(View.VISIBLE);
					findViewById(R.id.line).setVisibility(View.VISIBLE);
					orderStyle=1;
					break;
				case R.id.rb_channel_entry:
					findViewById(R.id.rl_choice_channel).setVisibility(View.VISIBLE); 
					///findViewById(R.id.ly_customer).setVisibility(View.GONE);
					findViewById(R.id.line).setVisibility(View.GONE);
					orderStyle=2;
					break;

				default:
					break;
				}

			}
		});

	}
	private void initData(){
		Intent intent=getIntent();
		if(intent.hasExtra("productId")){
			productId=intent.getIntExtra("productId",-1);
		}
		if(intent.hasExtra("productName")){
			((TextView)findViewById(R.id.tv_product_name)).setText(intent.getStringExtra("productName"));
			
		}
		((TextView)findViewById(R.id.tv_your_name)).setText(getStringLocalData("name"));
		((TextView)findViewById(R.id.tv_mobile_phone)).setText(getStringLocalData("phone"));


	}
	private void getCreateOrder(){
		request(Url.createOrder, getCreateOrderParamJson(),Method.POST, new IRequestCallBack() {

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
	private JSONObject getCreateOrderParamJson(){
		JSONObject json=new JSONObject();
		try {
			json.put("token",getStringLocalData("token"));
			json.put("product_id",productId);//产品id
			json.put("account_id",customId);//客户id
			//			json.put("uid", getStringLocalData("uid"));//uid
			//			json.put("tid", getStringLocalData("tid"));//tid
			json.put("amount",((CheckableEditText)findViewById(R.id.tv_reservation_amount)).getText().toString());//预约金额
			json.put("appoint_pattern",orderStyle);//预约方式 1､直客预约 2､渠道预约
			json.put("channel_id",channelId);//渠道ID
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Submit_Booking:
			if(((CheckableEditText)findViewById(R.id.tv_reservation_amount)).check()){
				getCreateOrder();
			}

			break;
		case R.id.select_customername:
			Intent selectCustomerIntent=new Intent(OrderActivity.this,SelectCustomerTwoActivity.class);
			selectCustomerIntent.putExtra("selectCustom", "selectCustom");
			startActivityForResult(selectCustomerIntent, 0);
			break;
		case R.id.tv_border_choice_channel:
			//			Intent selectChannelIntent=new Intent(OrderActivity.this,SelectCustomerTwoActivity.class);
			//			selectChannelIntent.putExtra("selectOriganization", "selectOriganization");
			//			startActivityForResult(selectChannelIntent, 0);
			break;

		default:
			break;
		}

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data!=null){
			if(data.hasExtra("customId")){
				customId= data.getIntExtra("customId",-1);
				((TextView)findViewById(R.id.select_customername)).setText(data.getStringExtra("customName"));
				//((TextView)findViewById(R.id.select_customername)).setBackground(null);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
