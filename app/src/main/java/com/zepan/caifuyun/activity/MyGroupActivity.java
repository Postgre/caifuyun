package com.zepan.caifuyun.activity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.yiciyuan.easycomponent.listview.ECAdapter.ValueBindCallBack;
import com.yiciyuan.easycomponent.listview.ECListViewLayout;
import com.yiciyuan.easycomponent.listview.ECViewHolder;
import com.yiciyuan.easycomponent.listview.IECListView.OnECItemClickListener;
import com.zepan.android.util.StringUtil;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.widget.PolymerImageView;

//我的群组页面
/**
 * update by zhanglei at 2015/08/27 连接融云
 * */
public class MyGroupActivity extends BaseActivity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_mygroup);
    	
    	initView();
    }

	private void initView() {
		// TODO Auto-generated method stub
		setHeaderFields(0,R.string.mygroup,0,R.drawable.ic_action_back,0,0);
		ECListViewLayout ecListViewLayout = (ECListViewLayout)findViewById(R.id.eclistLayout);
		ecListViewLayout.addRequestParams("user_id", getStringLocalData("uid"));
		ecListViewLayout.setResourceJsonKeyMap(new int[]{R.id.name}, new String[]{"group_name"});
	    
		ecListViewLayout.setBindCallBack(new ValueBindCallBack() {
			@Override
			public void bind(ECViewHolder holder, JSONObject json) throws JSONException {
				PolymerImageView imageView = (PolymerImageView)holder.getView(R.id.image);
				JSONArray imageJsonArray = json.getJSONArray("user_img");
				String[] imageArray = new String[imageJsonArray.length()];
				for(int i = 0; i < imageArray.length; i++){
					String imageUrl = imageJsonArray.getString(i);
					if(!StringUtil.isNullOrEmpty(imageUrl)){
						imageArray[i] = imageJsonArray.getString(i);
					}
				}
				imageView.setImageList(imageArray);
			}
		});
		ecListViewLayout.setOnItemClickListener(new OnECItemClickListener() {
			@Override
			public void OnItemClick(AdapterView<?> parent, View view, int position,
					JSONObject itemJson) throws JSONException {
				// 获取讨论组id
				String groupId = itemJson.getString("group_id");
				String groupName = itemJson.getString("group_name");
				Intent intent = new Intent(MyGroupActivity.this,
						ConversationActivity.class);
				intent.putExtra("targetId", groupId);
				intent.putExtra("targetTitle", groupName);
				// 启动融云讨论组界面
				startActivity(intent);
//				RongIM.getInstance().startConversation(MyGroupActivity.this, ConversationType.DISCUSSION, groupId, groupName);
			}
		});
	}
	
	
}
