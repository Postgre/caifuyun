package com.zepan.caifuyun.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.zepan.android.sdk.IRequestCallBack;
import com.zepan.android.widget.IndicatorListView;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseFragment;
import com.zepan.caifuyun.constants.Url;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
/**
 * 个人中心
 * @author long
 *
 */
public class UserInfoFragment extends BaseFragment implements OnClickListener {
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_user_info,container,false);
		initView();
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		setHeaderFields(0, R.string.me,0,0, 0, 0);
		super.onActivityCreated(savedInstanceState);
	}

	private void initView() {
		((IndicatorListView) view.findViewById(android.R.id.list)).setDivider(null);
		((IndicatorListView) view.findViewById(android.R.id.list)).setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
//				case 0:
//					Intent intentIdentity=new Intent(getActivity(),IdentityAuthenticationActivity.class);
//					startActivity(intentIdentity);
//					break;
				case 0:
					Intent intentAdress=new Intent(getActivity(),ReceiveAddressActivity.class);
					startActivity(intentAdress);
					break;
				case 1:
					Intent intentSet=new Intent(getActivity(),UserSetActivity.class);
					startActivity(intentSet);
					break;
					
				case 2:
					Intent intentLucky=new Intent(getActivity(), LuckyDrawActivity.class);
					startActivity(intentLucky);
					
					break;
				case 3://积分商城
					Intent intentIntegralMall=new Intent(getActivity(),IntegralMallActivity.class);
					startActivity(intentIntegralMall);
					break;

				default:
					break;
				}
				
			}
		});
		view.findViewById(R.id.tv_sign).setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_sign:
			getSign();
			break;

		default:
			break;
		}
		
	}
	private void getSign(){
		String tid=getStringLocalData("tid");
		String uid=getStringLocalData("uid");
		String url=Url.Sign+"?tid="+tid+"&uid="+uid;
		request(url, null, Method.GET, new IRequestCallBack() {
			
			@Override
			public void onResponse(JSONObject response) {
				try {
					if(response.getInt("status")==0){
						printToast("签到成功");
						view.findViewById(R.id.tv_sign).setSelected(true);
						((TextView)(view.findViewById(R.id.tv_sign))).setText("已签到");
						view.findViewById(R.id.tv_sign).setEnabled(false);
					}else{
						printToast(response.getString("message"));
						view.findViewById(R.id.tv_sign).setSelected(false);
						view.findViewById(R.id.tv_sign).setEnabled(true);
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


}
