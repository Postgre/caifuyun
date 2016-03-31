package com.zepan.caifuyun.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.zepan.android.sdk.IRequestCallBack;
import com.zepan.android.sdk.JsonUtil;
import com.zepan.android.widget.AlphaImageView;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseFragment;
import com.zepan.caifuyun.constants.Url;
import com.zepan.caifuyun.entity.User;
/**
 * 第二个tab，即客户
 * @author duanjie
 *
 */
public class CustomerFragment extends BaseFragment implements OnClickListener{
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_customer,container,false);
		initView();
		
		return view;
	}
	private void initView() {
		((AlphaImageView)view.findViewById(R.id.iv_right)).setClickAlpha(150, true, false);
		((TextView)view.findViewById(R.id.tv_clues)).setOnClickListener(this);
		((TextView)view.findViewById(R.id.tv_customer)).setOnClickListener(this);
		((TextView)view.findViewById(R.id.tv_channels_manager)).setOnClickListener(this);
		((TextView)view.findViewById(R.id.tv_form_verifying)).setOnClickListener(this);
		((TextView)view.findViewById(R.id.tv_form_finish)).setOnClickListener(this);
		((TextView)view.findViewById(R.id.tv_booking_form)).setOnClickListener(this);
		
		//		((TextView)view.findViewById(R.id.tv_renew_contract)).setOnClickListener(this);
		((TextView)view.findViewById(R.id.tv_customer_remind)).setOnClickListener(this);
		((TextView)view.findViewById(R.id.tv_vip_manager)).setOnClickListener(this);
		view.findViewById(R.id.tv_sales_journal).setOnClickListener(this);

	}
	
	@Override
	public void onResume() {
		CreateUserIndexCounts();
		super.onResume();
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		setHeaderFields(0, R.string.customer,0,
				0, 0, 0); 
		super.onActivityCreated(savedInstanceState);

	}
	private void CreateUserIndexCounts(){
		request(Url.GetIndexCounts, getCreateUserIndexCountsParamJson(),Method.POST, new IRequestCallBack() {

			@SuppressWarnings("static-access")
			@Override
			public void onResponse(JSONObject response) {
				try {
					if(response.getInt("status")==0){
					  JSONObject json=response.getJSONObject("info");
					  JSONObject jsonBody=json.getJSONObject("body");
					  JSONObject jsonCounts=jsonBody.getJSONObject("counts");
					  
					  User user=(User) JsonUtil.jsonToEntity(jsonCounts, User.class);
					  if(user.getOrderCount()!=0){
						  ((TextView)view.findViewById(R.id.booking_form_num)).setVisibility(view.VISIBLE); 
						  ((TextView)view.findViewById(R.id.booking_form_num)).setText(user.getOrderCount()+"");
					  }else{
						  ((TextView)view.findViewById(R.id.booking_form_num)).setVisibility(view.GONE); 
					  }
					  ((TextView)view.findViewById(R.id.clues_num)).setText(user.getLeadCount()+"");
					  ((TextView)view.findViewById(R.id.customer_num)).setText(user.getAccountCount()+"");
					  ((TextView)view.findViewById(R.id.channels_manager_num)).setText(user.getChannelCount()+"");
					  
					}else{
						printToast(response.getString("message"));
					}

					
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
	private JSONObject getCreateUserIndexCountsParamJson(){
		JSONObject json=new JSONObject();
		try {
			json.put("token",getStringLocalData("token"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_clues://线索
			Intent cluesIntent=new Intent(getActivity(),CluesActivity.class);
			startActivity(cluesIntent);
			break;
		case R.id.tv_customer://客户
			Intent selectCustomerTwoIntent=new Intent(getActivity(),SelectCustomerTwoActivity.class);
			startActivity(selectCustomerTwoIntent);
			break;
		case R.id.tv_channels_manager://渠道管理
			Intent vipIntent=new Intent(getActivity(),ChannelsManagerActivity.class);
			startActivity(vipIntent);
			break;
		case R.id.tv_booking_form://预约报单
			Intent OrderOrFormIntent=new Intent(getActivity(),OrderOrFormActivity.class);
			startActivity(OrderOrFormIntent);
			break;
		case R.id.tv_form_verifying://报单审核中
			Intent FormVerifyingIntent=new Intent(getActivity(),FormVerifyingActivity.class);
			startActivity(FormVerifyingIntent);
			break;
		case R.id.tv_form_finish://已成功
			Intent FormFinishIntent=new Intent(getActivity(),FormFinishActivity.class);
			startActivity(FormFinishIntent);
			break;
			/*case R.id.tv_renew_contract://存续合同
			Intent RenewContractIntent=new Intent(getActivity(),RenewContractActivity.class);
			startActivity(RenewContractIntent);
			break;*/
		case R.id.tv_customer_remind://客户提醒
			Intent RemindIntent=new Intent(getActivity(),RemindActivity.class);
			startActivity(RemindIntent);
			break;
		case R.id.tv_vip_manager://会员管理
			Intent VipManagerIntent=new Intent(getActivity(),VipManagerActivity.class);
			startActivity(VipManagerIntent);
			break;
		case R.id.tv_sales_journal://销售日报
			Intent SalesJournalIntent=new Intent(getActivity(),SalesJournalActivity.class);
			startActivity(SalesJournalIntent);
			break;
			
		default:
			break;
		}

	}

}
