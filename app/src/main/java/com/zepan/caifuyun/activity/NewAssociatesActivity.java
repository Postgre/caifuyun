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
import com.zepan.caifuyun.entity.Contact;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
/**
 * 新建关联人
 * @author duanjie
 *
 */
public class NewAssociatesActivity extends BaseActivity implements OnClickListener {
	private int accountId;
	private final static int UPDATE=0x2000;
	private final static int NEW=0x2001;
	private int currentTag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_associates);

		initView();
		initData();
	}

	private void initView() {
		((AlphaImageView)findViewById(R.id.iv_left)).setClickAlpha(150, true, false);
		findViewById(R.id.btn_save).setOnClickListener(this);

	}
	private void initData(){
		Intent intent=getIntent();
		if(intent.hasExtra("newContact")){
			setHeaderFields(0, R.string.new_affiliated, 
					0,R.drawable.ic_action_back, 0,0);
			accountId=intent.getIntExtra("accountId", -1);
			currentTag=NEW;
			
		}
		if(intent.hasExtra("updateContact")){
			setHeaderFields(0, R.string.update_affiliated, 
					0,R.drawable.ic_action_back, 0,0);
			Contact contact=intent.getParcelableExtra("contact");
			accountId=contact.getId();
			currentTag=UPDATE;
			((CheckableEditText)findViewById(R.id.ed_name)).setText(contact.getName());
			((CheckableEditText)findViewById(R.id.ed_phone)).setText(contact.getMobile());
			((CheckableEditText)findViewById(R.id.ed_relation)).setText(contact.getDbcVarchar1());
			((CheckableEditText)findViewById(R.id.ed_note)).setText(contact.getComment());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_save:
			if(((CheckableEditText)findViewById(R.id.ed_name)).check()&&
					((CheckableEditText)findViewById(R.id.ed_phone)).check()&&
					((CheckableEditText)findViewById(R.id.ed_relation)).check()&&
					((CheckableEditText)findViewById(R.id.ed_note)).check()){
				postNewAssociates();
			}
			break;

		default:
			break;
		}

	}
	private void postNewAssociates(){
		try {
			String url="";
			JSONObject paramJson=new JSONObject();
			paramJson.put("token", getStringLocalData("token"));
			paramJson.put("uid", getStringLocalData("uid"));
			paramJson.put("tid", getStringLocalData("tid"));
			JSONObject contactJson=new JSONObject();
			if(currentTag==NEW){
				contactJson.put("accountId", accountId);
				url=Url.ContactCreate;
			}else if(currentTag==UPDATE){
				contactJson.put("id", accountId);
				url=Url.ContactUpdate;
			}

			contactJson.put("contactName",((CheckableEditText)findViewById(R.id.ed_name)).getText().toString());//联系人姓名
			contactJson.put("mobile",((CheckableEditText)findViewById(R.id.ed_phone)).getText().toString());//手机
			contactJson.put("comment",((CheckableEditText)findViewById(R.id.ed_note)).getText().toString());//备注
			contactJson.put("dbcVarchar1",((CheckableEditText)findViewById(R.id.ed_relation)).getText().toString());//关联人
			paramJson.put("contactJson",contactJson);
			
			request(url, paramJson, Method.POST, new IRequestCallBack() {

				@Override
				public void onResponse(JSONObject response) {
					newAssociatesResult(response);
				}

				@Override
				public void onErrorResponse(String errorMessage) {
					// TODO Auto-generated method stub

				}
			});
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void newAssociatesResult(JSONObject response){
		try {
			if(response.getInt("status")==0){
				if(currentTag==NEW){
					printToast("创建成功"); 
				}else if(currentTag==UPDATE){
					printToast("修改成功"); 
				}
				
				finish();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
