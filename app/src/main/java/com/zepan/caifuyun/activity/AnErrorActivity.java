package com.zepan.caifuyun.activity;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.android.volley.Request.Method;
import com.zepan.android.sdk.IRequestCallBack;
import com.zepan.android.widget.AlphaImageView;
import com.zepan.android.widget.CheckableEditText;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.constants.Url;
import com.zepan.caifuyun.entity.Clues;
/**
 * 报错
 * @author duanjie
 *
 */
public class AnErrorActivity extends BaseActivity  implements OnClickListener{
	private CheckableEditText  mError;
	private Clues  clues;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_an_error);
		setHeaderFields(0, R.string.error, 
				0,R.drawable.ic_action_back, 0,0);
		initData();
		initView();
	}

	private void initData() {
		Intent intent=getIntent();
		clues=intent.getParcelableExtra("clues");
	}
	private void initView() {
		((AlphaImageView)findViewById(R.id.iv_left)).setClickAlpha(150, true, false);
		mError=(CheckableEditText)findViewById(R.id.ed_error);
		findViewById(R.id.btn_submit).setOnClickListener(this);;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_submit:
			getMarkError();
			break;

		default:
			break;
		}

	}


	/**
	 *删除个人线索（删除公海池线索）接口
	 */
	private void  getMarkError(){
		String token;
		try {
			token = URLEncoder.encode(getStringLocalData("token"), "UTF-8");
			String uid=getStringLocalData("uid");
			String tid=getStringLocalData("tid");
			String url=Url.LeadMarkError+"?token="+token
					+"&leadId="+clues.getId()+""+"&errorReason="+mError.getText().toString()+"&uid="+uid+"&tid="+tid;
			request(url,null, Method.GET,new IRequestCallBack() {//    +"&customData="+json+""  leadId   线索ID   mError
				@Override
				public void onResponse(JSONObject response) {
					// TODO Auto-generated method stub
					getMarkErrorResult(response);
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

	private void getMarkErrorResult(JSONObject response){
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


}
