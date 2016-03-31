package com.zepan.caifuyun.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.Request.Method;
import com.zepan.android.sdk.IRequestCallBack;
import com.zepan.android.sdk.JsonUtil;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.adapter.PersonalContactsAdapter;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.entity.PersonalContacts;
import com.zepan.caifuyun.entity.UserInfo;

//个人通讯录页面
public class PersonalContactsActivity extends BaseActivity implements OnItemClickListener{

	private List<UserInfo> list=new ArrayList<UserInfo>();
	private PersonalContactsAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_personal_contacts);

		initView();
		

	}
	

	private void initView(){

		setHeaderFields(0,R.string.personalcontacts,0,R.drawable.ic_action_back,0,0);
		ListView listview=(ListView) findViewById(R.id.personal_contacts_list);
		listview.setOnItemClickListener(this);
		adapter=new PersonalContactsAdapter(this,list);
		getDataList();
		listview.setAdapter(adapter);
	}

	private void getDataList(){
		String url="http://120.26.789.31/get_message_personal_contacts_list";
		JSONObject json=new JSONObject();
		request(url, json, Method.POST, new IRequestCallBack() {

			@Override
			public void onResponse(JSONObject response) {

				getDataListResult(response);

			}

			@Override
			public void onErrorResponse(String errorMessage) {
				// TODO Auto-generated method stub

			}
		});
	}
	private void getDataListResult(JSONObject response){
		try {
			if(response.getInt("status")==0){
				JSONArray jsonArray=response.getJSONArray("list");
				for(int i=0;i<jsonArray.length();i++){

					JSONObject obj2=jsonArray.getJSONObject(i);
					PersonalContacts c=(PersonalContacts) JsonUtil.jsonToEntity(obj2, PersonalContacts.class);
					for(int j=0;j<c.getUsers().size();j++){
						UserInfo userInfo = c.getUsers().get(j);
						if(j==0){
							userInfo.setLabel(c.getFirst_letter());
						}
						list.add(userInfo);
					}


				}
				adapter.notifyDataSetChanged();

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
			Intent intent=new Intent(PersonalContactsActivity.this,MessageDetailsActivity.class);
			startActivity(intent);
		
		
	}

}
