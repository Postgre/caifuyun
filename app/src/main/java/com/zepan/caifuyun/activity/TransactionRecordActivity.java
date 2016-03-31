package com.zepan.caifuyun.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.zepan.android.sdk.IRequestCallBack;
import com.zepan.android.sdk.JsonUtil;
import com.zepan.android.widget.AlphaImageView;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.adapter.TransactionRecordAdapter;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.constants.Code;
import com.zepan.caifuyun.constants.Url;
import com.zepan.caifuyun.entity.PersonalContacts;
import com.zepan.caifuyun.entity.Product;
import com.zepan.caifuyun.entity.User;
import com.zepan.caifuyun.entity.UserInfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
/**
 * 交易记录     Listview嵌套listview
 * @author duanjie
 *
 */
public class TransactionRecordActivity extends BaseActivity implements OnClickListener {
	ListView listview;
	static List<Product> list = new ArrayList<Product>();
	private int accountId;
	private String productName;
	private String beginTime;
	private String endTime;
	//初始化数据
	static{
		Product c = new Product("哈哈","呵呵呵呵呵呵1呵");
		list.add(c);
		Product c1 = new Product("呵呵","呵呵呵呵呵呵2呵呵");
		list.add(c1);
		Product b = new Product("哈哈","呵呵呵呵呵呵3呵呵");
		list.add(b);
		Product b1 = new Product("呵呵","呵呵呵呵呵呵4呵呵呵");
		list.add(b1);
		

	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transaction_record);
		setHeaderFields(0, R.string.title_activity_transaction_record,0,
				R.drawable.ic_action_back, 0,R.drawable.ic_action_add);
		

		listview = (ListView) findViewById(android.R.id.list);

		listview.setAdapter(new TransactionRecordAdapter(this,list));

		//initData();
		initView();
		
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		((AlphaImageView)findViewById(R.id.iv_right)).setClickAlpha(150, true, false);
		findViewById(R.id.iv_right).setOnClickListener(this);
	}

	private void initData(){
		Intent intent=getIntent();
		accountId=intent.getIntExtra("accountId", 0);
		if(accountId!=0){
			getTransactionRecord();
			
		}

	}

		private void getTransactionRecord(){
			  
			request(Url.GetTransactionRecords, getTransactionRecordJson(),Method.POST, new IRequestCallBack() {

				@Override
				public void onResponse(JSONObject response) {
					// TODO Auto-generated method stub
					getResult(response);
				}

				@Override
				public void onErrorResponse(String errorMessage) {
					// TODO Auto-generated method stub

				}
			});	
		}
		private JSONObject getTransactionRecordJson(){
			JSONObject json=new JSONObject();
			try {
				json.put("token",getStringLocalData("token"));//token
				json.put("accountId", 10000650);//客户ID
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return json;
		}
		
		private void getResult(JSONObject response){
			try {
				if(response.getInt("status")==0){
					JSONObject jsonInfo=response.getJSONObject("info");
					JSONObject jsonBody=jsonInfo.getJSONObject("body");
					
					JSONArray jsonArray=jsonBody.getJSONArray("manageWealths");
					for(int i=0;i<jsonArray.length();i++){

						JSONObject obj2=jsonArray.getJSONObject(i);
//						User user=(User) JsonUtil.jsonToEntity(obj2, User.class);
//						    list.add(user);
						}


					}
				//	adapter.notifyDataSetChanged();
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}     
		}
		
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.iv_right:
			 Intent intent=new Intent(TransactionRecordActivity.this,AddfinanceHistoryActivity.class);
			 intent.putExtra("accountId", accountId);
			 //intent.putExtra("productName", productName);
			 intent.putExtra("beginTime", beginTime);
			 intent.putExtra("endTime", endTime);
			 startActivity(intent);
			break;

		default:
			break;
		}
	}


}
