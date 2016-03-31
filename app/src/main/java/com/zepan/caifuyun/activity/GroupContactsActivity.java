package com.zepan.caifuyun.activity;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.OperationCallback;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Conversation.ConversationType;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.zepan.android.sdk.IRequestCallBack;
import com.zepan.android.sdk.JsonUtil;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.adapter.GroupContactsAdapter;
import com.zepan.caifuyun.adapter.GroupContactsAdapter.OnUserSelectedListener;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.cache.CompanyRespository;
import com.zepan.caifuyun.constants.Url;
import com.zepan.caifuyun.entity.PersonalContacts;
import com.zepan.caifuyun.entity.UserInfo;

//群聊的个人通讯录页面
public class GroupContactsActivity extends BaseActivity {

	private List<UserInfo> list = new ArrayList<UserInfo>();
	private GroupContactsAdapter adapter;
	ImageView image_back;
	/** 存储选择的用户列表 */
	private List<UserInfo> selectedUserList = new ArrayList<UserInfo>();
	private boolean isSelect;
	private String targetId;
	private ArrayList<UserInfo> exitMemberList=new ArrayList<UserInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_group_contacts);
		initView();
		initData();
	}

	private void initData(){
		Intent intent=getIntent();
		if(intent.hasExtra("selectPerson")){
			isSelect=true;
			targetId = intent.getStringExtra("targetId");
			exitMemberList=intent.getParcelableArrayListExtra("memberList");
		}
	}
	public void initView() {
		setHeaderFields(0, R.string.companycontacts, R.string.btn_ok,
				R.drawable.ic_action_back, 0, 0);
		ListView listview = (ListView) findViewById(R.id.group_contacts_list);
		final TextView tv_num = (TextView) findViewById(R.id.sure_num);

		image_back = (ImageView) findViewById(R.id.iv_back);
		adapter = new GroupContactsAdapter(this, list);
		listview.setAdapter(adapter);
		adapter.setOnUserSelectedListener(new OnUserSelectedListener() {
			@Override
			public void onSelected(List<UserInfo> userList) {
				tv_num.setText(userList.size() + "");
				selectedUserList = userList;
			}
		});
		// 点击确定按钮后，创建讨论组
		findViewById(R.id.tv_sure).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isSelect){
					if(selectedUserList.size()>0){
						joinDiscussionToRongCloud();
					}else{
						printToast("没有选择");
					}
				}else{
					if(selectedUserList.size()>0){
						// 如果只选择了一个人，只是私聊
						if(selectedUserList.size() == 1){
							startPrivateChat();
						}else{
							createDiscussionToRongCloud();// 选择多个人则创建讨论组
						}
					}else{
						printToast("没有选择");
					}

				}
			}
		});
		getDataList();
	}
	private void joinDiscussionToRongCloud(){
		//加入讨论组
		final List<String> userIdList =new ArrayList<String>();
		for(UserInfo userInfo:selectedUserList){
			userIdList.add(userInfo.getUser_id());
		}
		RongIMClient.getInstance().addMemberToDiscussion(targetId, userIdList, new OperationCallback() {

			@Override
			public void onSuccess() {
				joinDiscussion();

			}

			@Override
			public void onError(ErrorCode arg0) {
				// TODO Auto-generated method stub

			}
		});
	}
	private void joinDiscussion(){
		String tid = getStringLocalData("tid");
		String uid = getStringLocalData("uid");
		JSONObject paramJson = new JSONObject();
		try {
			JSONArray userJsonArray = new JSONArray();
			for (UserInfo user : selectedUserList) {
				JSONObject userJson = new JSONObject();
				userJson.put("user_id", user.getUser_id());
				userJson.put("user_name", user.getUser_name());
				userJsonArray.put(userJson);
			}
			paramJson.put("user_list", userJsonArray);
			paramJson.put("group_id", targetId);
			paramJson.put("uid", uid);
			paramJson.put("tid", tid);
			request(Url.JoinGroup, paramJson, Method.POST,
					new IRequestCallBack() {
				@Override
				public void onResponse(JSONObject response) {
					joinDiscussionResult(response);
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
	private void joinDiscussionResult(JSONObject response){
		try {
			if(response.getInt("status")==0){
				printToast("加入成功");
			}else{
				printToast("加入失败");
			}
			finish();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void getDataList() {
		// String url="http://120.26.789.31/get_message_personal_contacts_list";
		// 构建请求参数
		// 获取租户id
		String tid = getStringLocalData("tid");
		JSONObject paramJson = new JSONObject();
		try {
			paramJson.put("tid", tid);
			request(Url.GetGroupUsers, paramJson, Method.GET,
					new IRequestCallBack() {
				@Override
				public void onResponse(JSONObject response) {
					getDataListResult(response);
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
		// String url = Url.GetGroupUsers + "?tid=" + tid;

	}

	private void getDataListResult(JSONObject response) {
		try {
			if (response.getInt("status") == 0) {
				JSONArray jsonArray = response.getJSONArray("list");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject obj2 = jsonArray.getJSONObject(i);
					PersonalContacts c = (PersonalContacts) JsonUtil
							.jsonToEntity(obj2, PersonalContacts.class);
					for (int j = 0; j < c.getUsers().size(); j++) {
						UserInfo userInfo = c.getUsers().get(j);
						for(int k = 0; k < exitMemberList.size() ; k++){
							if(exitMemberList.get(k)!=null&&exitMemberList.get(k).getUser_id()!=null
									&&userInfo.getUser_id()!=null){
								if(exitMemberList.get(k).getUser_id().equals(userInfo.getUser_id())){
									userInfo.setClick(false);
								}
							}
						}
						if(userInfo.getUser_id().equals(getStringLocalData("uid"))){
							userInfo.setClick(false);
						}
						if (j == 0) {
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

	/**
	 * 向融云发“创建讨论组”请求，调用成功后会调用方法{@link #createDiscussion(String)}}
	 * */
	private void createDiscussionToRongCloud() {
		// 获取讨论组成员用户号
		List<String> userIdList = new ArrayList<String>();
		// 拼接讨论组名，内空为“用户名A、用户名B……”
		for (UserInfo info : selectedUserList) {
			userIdList.add(info.getUser_id());
		}
		userIdList.add(getStringLocalData("uid"));
		final String newDiscussionName = CompanyRespository.getNameList(userIdList);
		RongIMClient.getInstance().createDiscussion(newDiscussionName, userIdList,
				new RongIMClient.CreateDiscussionCallback() {
			@Override
			public void onError(ErrorCode arg0) {
				Log.i(TAG, "融云创建讨论组失败：" + arg0);
			}
			@Override
			public void onSuccess(String arg0) {
				Log.i(TAG, "融云创建讨论组成功：" + arg0);
				createDiscussion(arg0, newDiscussionName);
			}
		});
	}

	/**
	 *  将讨论组数据维护到自己服务器，因为融云不保存数据
	 *  @param groudId 讨论组号，来自融云服务器
	 *  @param discussionName 讨论组名称
	 *  */
	private void createDiscussion(String groudId, String groupName) {
		// 创建请求参数
		JSONObject paramJson = new JSONObject();
		// 设置讨论组创建者
		try {
			paramJson.put("group_id", groudId);
			paramJson.put("group_name", groupName);
			paramJson.put("create_user", getStringLocalData("uid"));
			JSONArray userJsonArray = new JSONArray();
			for (UserInfo user : selectedUserList) {
				JSONObject userJson = new JSONObject();
				userJson.put("user_id", user.getUser_id());
				userJson.put("user_name", user.getUser_name());
				userJson.put("user_img", user.getUser_img());
				userJsonArray.put(userJson);
			}
			// 添加创建者
			JSONObject createUserJson = new JSONObject();
			createUserJson.put("user_id", getStringLocalData("uid"));
			createUserJson.put("user_name", getStringLocalData("name"));
			userJsonArray.put(createUserJson);
			paramJson.put("user_list", userJsonArray);
			// 请求
			request(Url.CreateGroup, paramJson, Method.POST,
					new IRequestCallBack() {

				@Override
				public void onResponse(JSONObject response) {
					try {
						// 获取讨论组id
						String groupId = response.getString("group_id");
						// 创建成功后跳转到讨论组会话列表
						// 拼接讨论组名，内空为“用户名A、用户名B……”
						final StringBuffer discussionName = new StringBuffer();
						for (UserInfo info : selectedUserList) {
							discussionName.append(info.getUser_name() + "、");
						}
						Conversation conversation=new Conversation();
						conversation.setTargetId(groupId);
						conversation.setConversationTitle(discussionName.toString());
						conversation.setConversationType(ConversationType.DISCUSSION);
						Intent intent = new Intent(GroupContactsActivity.this,ConversationActivity.class);
						intent.putExtra("conversation", conversation);
						startActivity(intent);

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onErrorResponse(String errorMessage) {
					Log.i(TAG, "创建讨论组失败：" + errorMessage);
				}
			});
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**跳转到私聊界面*/
	private void startPrivateChat(){
		// 获取用户发件人用户号
		String sendUserId = selectedUserList.get(0).getUser_id();
		Conversation conversation=new Conversation();
		conversation.setTargetId(sendUserId);
		conversation.setConversationTitle(selectedUserList.get(0).getUser_name());
		conversation.setConversationType(ConversationType.DISCUSSION);
		Intent intent = new Intent(GroupContactsActivity.this,ConversationActivity.class);
		intent.putExtra("conversation", conversation);
		startActivity(intent);
	}

	public void click(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
		}
	}
}
