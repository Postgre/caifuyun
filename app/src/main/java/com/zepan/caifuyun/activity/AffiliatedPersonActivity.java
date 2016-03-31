package com.zepan.caifuyun.activity;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.zepan.android.sdk.IRequestCallBack;
import com.zepan.android.sdk.JsonUtil;
import com.zepan.android.widget.AlphaImageView;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.R.drawable;
import com.zepan.caifuyun.R.layout;
import com.zepan.caifuyun.R.string;
import com.zepan.caifuyun.adapter.ContactAdapter;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.constants.Url;
import com.zepan.caifuyun.entity.Contact;
import com.zepan.caifuyun.widget.DialogBottom;
import com.zepan.caifuyun.widget.DialogBottom.OnItemClick;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
/**
 * 关联人
 * @author duanjie
 *
 */
public class AffiliatedPersonActivity extends BaseActivity implements OnClickListener {
	private int accountId;
	private List<Contact> contactList=new ArrayList<Contact>();
	private ContactAdapter contactAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_affiliated_person);
		setHeaderFields(0, R.string.affiliated_person, 
				0,R.drawable.ic_action_back, 0,R.drawable.ic_action_add);
		initView();
		initData();
	}
	private void initData(){
		Intent intent=getIntent();
		if(intent.hasExtra("accountId")){
			accountId=intent.getIntExtra("accountId", -1);

		}
	}
	@Override
	protected void onStart() {
		getAffiliatedPerson();
		super.onStart();
	}
	private void initView() {
		((AlphaImageView)findViewById(R.id.iv_left)).setClickAlpha(150, true, false);
		findViewById(R.id.iv_right).setOnClickListener(this);
		ListView listView=(ListView) findViewById(android.R.id.list);
		contactAdapter=new ContactAdapter(this, contactList);
		listView.setAdapter(contactAdapter);
		listView.setDivider(null);
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				final DialogBottom dialog=new DialogBottom(AffiliatedPersonActivity.this);
				String[] nameArray={"编辑","删除","取消"};
				dialog.setArray(nameArray);
				dialog.show();
				final int post=position;
				dialog.setOnItemClick(new OnItemClick() {

					@Override
					public void itemClick(AdapterView<?> parent, View view, int position, long id) {
						switch (position) {
						case 0:
							dialog.dismiss();
                            Intent intent=new Intent(AffiliatedPersonActivity.this, NewAssociatesActivity.class);
                            intent.putExtra("updateContact", "updateContact");
                            intent.putExtra("contact",contactList.get(post));
                            startActivity(intent);
							break;
						case 1:
							dialog.dismiss();
                            getDeleteContact(post);
							break;
						case 2:
							dialog.dismiss();
							break;

						default:
							break;
						}

					}
				});
				return false;
			}
		});
	}
	private void getDeleteContact(int post){
		try {
			String token=URLEncoder.encode(getStringLocalData("token"), "UTF-8");
			String uid=getStringLocalData("uid");
			String tid=getStringLocalData("tid");
			String url=Url.ContactDelete+"?token="+token+
					"&contactIds="+contactList.get(post).getId()+"&uid="+uid+"&tid="+tid;
			request(url, null, Method.GET, new IRequestCallBack() {

				@Override
				public void onResponse(JSONObject response) {
					try {
						if(response.getInt("status")==0){
							printToast("删除成功");
							getAffiliatedPerson();
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
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void getAffiliatedPerson(){
		try {
			String token=URLEncoder.encode(getStringLocalData("token"), "UTF-8");
			String uid=getStringLocalData("uid");
			String tid=getStringLocalData("tid");
			String url=Url.ContactGetAll+"?token="+token+"&accountId="+accountId+"&uid="+uid+"&tid="+tid;
			request(url, null, Method.GET, new IRequestCallBack() {

				@Override
				public void onResponse(JSONObject response) {
					affiliatedPersonResult(response);
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
	private void affiliatedPersonResult(JSONObject response){
		try {
			if(contactList.size()>0){
				contactList.clear();
			}
			if(response.getInt("status")==0){
				JSONArray jsonArray=response.getJSONArray("contacts");
				for(int i=0;i<jsonArray.length();i++){
					Contact contact=(Contact) JsonUtil.jsonToEntity(jsonArray.getJSONObject(i), Contact.class);
					contactList.add(contact);
				}
				contactAdapter.notifyDataSetChanged();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_right:
			Intent intent=new Intent(this, NewAssociatesActivity.class);
			intent.putExtra("newContact", "newContact");
			intent.putExtra("accountId", accountId);
			startActivity(intent);
			break;

		default:
			break;
		}

	}


}
