package com.zepan.caifuyun.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.umeng.socialize.net.s;
import com.zepan.android.sdk.IRequestCallBack;
import com.zepan.android.sdk.JsonUtil;
import com.zepan.android.widget.AlphaImageView;
import com.zepan.android.widget.CheckableEditText;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.R.id;
import com.zepan.caifuyun.R.layout;
import com.zepan.caifuyun.R.menu;
import com.zepan.caifuyun.adapter.CertificationAdapter;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.constants.Url;
import com.zepan.caifuyun.entity.Company;

import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
/**
 * 认证   搜索公司
 * @author duanjie
 *
 */
public class CertificationActivity extends BaseActivity implements OnClickListener{

	private CertificationAdapter mCertificationAdapter;
	private ListView mListView;
	private List<Company> mCompanyList;
	private CheckableEditText mSearch;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_certification);
		setHeaderFields(0, R.string.tv_certification,0,
				R.drawable.ic_action_back, 0, 0);
		initView();
	}

	private void initView() {
		mSearch=(CheckableEditText)findViewById(R.id.edtTx_company_name);
		((AlphaImageView)findViewById(R.id.iv_left)).setClickAlpha(150, true, false);
		findViewById(R.id.ry_createcompany).setOnClickListener(this);
		mListView=(ListView) findViewById(android.R.id.list);
		mCompanyList=new ArrayList<Company>();
		mCertificationAdapter=new CertificationAdapter(CertificationActivity.this, mCompanyList);
		mListView.setAdapter(mCertificationAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Company  company=(Company)mCompanyList.get(position);
				Intent  intent=new Intent(CertificationActivity.this,CertificationTwoActivity.class);
				intent.putExtra("companyId",company.getCompanyId()+"");
				intent.putExtra("companyName",company.getCompanyName());
				CertificationActivity.this.startActivity(intent);

			}
		});
		mSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.length() > 0 ){
					((RelativeLayout)findViewById(R.id.ry_createcompany)).setVisibility(View.VISIBLE);
					mListView.setVisibility(View.VISIBLE);
					try {
						getSearchTenant();
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else if(s.length()==0){
					mListView.setVisibility(View.GONE);
					((RelativeLayout)findViewById(R.id.ry_createcompany)).setVisibility(View.GONE);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {


			}

			@Override
			public void afterTextChanged(Editable s) {


			}
		});
	}




	private void  getSearchTenant() throws UnsupportedEncodingException{
		//+getStringLocalData("token")
		//"1aiGgQanppAtyzwSliAFVnYs1l1smiHC5nQ8RoeJlZw="
		String token=URLEncoder.encode(getStringLocalData("token"), "UTF-8");
		String key=URLEncoder.encode(((CheckableEditText)findViewById(R.id.edtTx_company_name)).getText().toString(), "UTF-8");
		String url=Url.SearchTenant+"?token="+token+"&key="+key;
		request(url,null, Method.GET,new IRequestCallBack() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				getSearchTenantResult(response);
			}

			@Override
			public void onErrorResponse(String errorMessage) {
				// TODO Auto-generated method stub

			}
		});
	}
	private void getSearchTenantResult(JSONObject response){
		try {
			if(response.getInt("status")==0){
				if(mCompanyList.size()>0){
					mCompanyList.clear();
				}
				JSONObject json = response;
				JSONArray jsons = json.getJSONArray("tenants");
				for (int i = 0; i < jsons.length(); i++) {
					JSONObject jsonChildParam = jsons.getJSONObject(i);
					Company c=(Company)JsonUtil.jsonToEntity(jsonChildParam, Company.class);
					mCompanyList.add(c);// 添加在存储信息列表
				}
				mCertificationAdapter.notifyDataSetChanged();
			}else if(response.getInt("status")==1){
				String message=response.getString("message");
				printToast(message);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ry_createcompany:
			Intent createIntent=new Intent(CertificationActivity.this,CreateCompanyActivity.class);
			createIntent.putExtra("companyName",((CheckableEditText)findViewById(R.id.edtTx_company_name)).getText().toString());
			startActivity(createIntent);
			break;

		default:
			break;
		}

	}

}
