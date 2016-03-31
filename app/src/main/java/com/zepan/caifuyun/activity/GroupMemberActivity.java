package com.zepan.caifuyun.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.OperationCallback;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.zepan.android.sdk.IRequestCallBack;
import com.zepan.android.sdk.JsonUtil;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.adapter.GroupMemberAdapter;
import com.zepan.caifuyun.adapter.GroupMemberAdapter.DeleteUser;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.constants.Url;
import com.zepan.caifuyun.entity.UserInfo;
import com.zepan.caifuyun.widget.DialogUpdate;
import com.zepan.caifuyun.widget.DialogUpdate.OnButtonClick;
import com.zepan.caifuyun.widget.MyGridView;

//群聊的聊天成员界面
public class GroupMemberActivity extends BaseActivity implements OnClickListener{

	private MyGridView gridview;
	private ArrayList<UserInfo> list;
	private GroupMemberAdapter adapter;
	private ImageView iv_back;
	private String targetId;
	private String targetTitle;
	private String createUserId;
	private int currentTag;
	private final static int QUITDICUSSION =0x100;
	private final static int REMOVEOFCUSSION =0x101;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_member);
		initView();
		initData();
	}
	private void initData(){
		Intent intent=getIntent();
		if(intent!=null){
			if (intent.hasExtra("targetId")){
				targetId = intent.getStringExtra("targetId");
			}
			if(intent.hasExtra("targetTitle")){
				targetTitle = intent.getStringExtra("targetTitle");
				((TextView)findViewById(R.id.tv_discuss_name)).setText(targetTitle);
			}
		}
	}
	@Override
	protected void onStart() {
		getDataList();
		super.onStart();
	}

	private void initView() {
		gridview=(MyGridView) findViewById(R.id.gridview);
		iv_back=(ImageView) findViewById(R.id.iv_left);
		iv_back.setOnClickListener(this);
		findViewById(R.id.sure).setOnClickListener(this);
		findViewById(R.id.rl_discuss_name).setOnClickListener(this);
		list=new ArrayList<UserInfo>();
		adapter=new GroupMemberAdapter(this,list);
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(position==list.size()-2){
					if(list.get(position).getUser_id()==null){
						//添加通讯录用户
						Intent intent=new Intent(GroupMemberActivity.this, GroupContactsActivity.class);
						intent.putExtra("selectPerson", "selectPerson");
						intent.putExtra("targetId", targetId);
						intent.putParcelableArrayListExtra("memberList", list);
						startActivityForResult(intent, 0);
					}
				}else if(position==list.size()-1){
					if(list.get(position).getUser_id()==null){
						//添加通讯录用户
						Intent intent=new Intent(GroupMemberActivity.this, GroupContactsActivity.class);
						intent.putExtra("selectPerson", "selectPerson");
						intent.putExtra("targetId", targetId);
						intent.putParcelableArrayListExtra("memberList", list);
						startActivityForResult(intent, 0);
					}else{
						for(UserInfo userInfo:list){
							if(userInfo.getUser_id()!=null&&userInfo.getUser_id().equals(createUserId)){
								userInfo.setDelete(false);
							}else{
								userInfo.setDelete(true);
							}
						}
						adapter.notifyDataSetChanged();
					}


				}

			}
		});
		adapter.setOnDeleteUser(new DeleteUser() {

			@Override
			public void deleteUser(int position) {
				String userId=list.get(position).getUser_id();
				currentTag=REMOVEOFCUSSION;
				deleteUserInfo(userId);
			}
		});
	}
	private void deleteUserInfo(final String userId){
		RongIMClient.getInstance().removeMemberFromDiscussion(targetId, userId, new OperationCallback() {

			@Override
			public void onSuccess() {
				getDeleteUser(userId);
			}

			@Override
			public void onError(ErrorCode arg0) {
				printToast("删除失败");
			}
		});

	}
	private void getDeleteUser(String userId){
		JSONObject paramJson=new JSONObject();
		try {
			paramJson.put("user_id",userId);
			paramJson.put("group_id",targetId);
			paramJson.put("uid",getStringLocalData("uid"));
			paramJson.put("tid",getStringLocalData("tid"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		request(Url.QuitGroup, paramJson, Method.POST, new IRequestCallBack() {

			@Override
			public void onResponse(JSONObject response) {
				try {
					if(response.getInt("status")==0){
						if(currentTag==QUITDICUSSION){
							printToast("退出成功");
							Intent intent=new Intent();
							intent.putExtra("quitDiscussion", "quitDiscussion");
							finish();
						}else if(currentTag==REMOVEOFCUSSION){
							printToast("删除成功");
							getDataList();
						}
					}else{
						printToast("删除失败");

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
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(data!=null){


		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void getDataList(){
		String url=Url.GroupInfo;
		JSONObject json=new JSONObject();
		try {
			json.put("uid", getStringLocalData("uid"));
			json.put("tid", getStringLocalData("tid"));
			String token = URLEncoder.encode(getStringLocalData("token"), "UTF-8");
			json.put("token", token);
			json.put("group_id", targetId);
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request(url, json, Method.GET, new IRequestCallBack() {

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
				if(list.size()>0){
					list.clear();
				}
				JSONObject jsonGroup=response.getJSONObject("group_info");
				createUserId=jsonGroup.getString("create_user");
				JSONArray jsonArray=jsonGroup.getJSONArray("user_list");
				for(int i=0;i<jsonArray.length();i++){
					UserInfo users=(UserInfo) JsonUtil.jsonToEntity(jsonArray.getJSONObject(i),UserInfo.class);
					list.add(users);
				}

				if(getStringLocalData("uid").equals(createUserId)){
					UserInfo user1=new UserInfo();
					UserInfo user2=new UserInfo();
					user2.setUser_id(createUserId);
					list.add(user1);
					list.add(user2);
				}else{
					UserInfo user1=new UserInfo();
					list.add(user1);
				}

				adapter.notifyDataSetChanged();

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_left:
			if(targetTitle!=null){
				Intent intent=new Intent();
				intent.putExtra("targetTitle", targetTitle);
				setResult(RESULT_OK, intent);
			}
			finish();
			break;
		case R.id.sure:
			currentTag=QUITDICUSSION;
			quitDiscussion();
			break;
		case R.id.rl_discuss_name:
			if(createUserId!=null&&createUserId.equals(getStringLocalData("uid"))){
				final DialogUpdate dialogUpdate=new DialogUpdate(this,targetTitle);
				dialogUpdate.setOnButtonClick(new OnButtonClick() {
					
					@Override
					public void sure(String name) {
						dialogUpdate.dismiss();
						updateDiscussName(name);
					}
					
					@Override
					public void cancel() {
						dialogUpdate.dismiss();
						
					}
				});
				dialogUpdate.show();
			}else{
				printToast("没有权限");
			}
			
			break;

		default:
			break;
		}
	}
	private void updateDiscussName(final String name){
		RongIMClient.getInstance().setDiscussionName(targetId, name, new OperationCallback() {

			@Override
			public void onSuccess() {
				requestUpdateDiscussName(name);

			}

			@Override
			public void onError(ErrorCode arg0) {
				// TODO Auto-generated method stub

			}
		});
	}
	private void requestUpdateDiscussName(final String name){
		String url =Url.GroupUpdate;
		JSONObject paramJson = new JSONObject();
		try {
			paramJson.put("group_id", targetId);
			String encodeName = URLEncoder.encode(name, "UTF-8");
			paramJson.put("group_name", encodeName);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		request(url, paramJson, Method.GET, new IRequestCallBack() {

			@Override
			public void onResponse(JSONObject response) {
				try {
					if(response.getInt("status")==0){
						printToast("修改成功");
						targetTitle=name;
						((TextView)findViewById(R.id.tv_discuss_name)).setText(name);
						
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
	private void quitDiscussion(){
		RongIMClient.getInstance().quitDiscussion(targetId, new OperationCallback() {

			@Override
			public void onSuccess() {
				getDeleteUser(getStringLocalData("uid"));
			}

			@Override
			public void onError(ErrorCode arg0) {
				printToast("退出失败");

			}
		});
	}

}
