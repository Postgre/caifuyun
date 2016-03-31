package com.zepan.caifuyun.activity;

import java.util.ArrayList;
import java.util.List;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.adapter.ConversationAdapter;
import com.zepan.caifuyun.base.BaseFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.OnReceiveMessageListener;
import io.rong.imlib.RongIMClient.ResultCallback;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * 第二个tab，即消息 update by zhanglei at 2015/08/26 连接融云
 * 
 */
public class MessageFragment extends BaseFragment implements OnClickListener,
		OnReceiveMessageListener {

	private ConversationAdapter adapter = null;
	public List<Conversation> dataList = new ArrayList<Conversation>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_message, null);
		view.findViewById(R.id.tv_left).setOnClickListener(this);
		view.findViewById(R.id.iv_right).setOnClickListener(this);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		// 获取会话列表
		requestConversationList();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		setHeaderFields(R.string.contacts, R.string.notification, 0, 0, 0,
				R.drawable.ic_action_add);
		// initView();
		ListView listView = (ListView) getView()
				.findViewById(android.R.id.list);
		adapter = new ConversationAdapter(getActivity(), dataList);
		listView.setAdapter(adapter);
		
		View headView=LayoutInflater.from(getActivity()).inflate(R.layout.message_listview_head, null);
		headView.findViewById(R.id.layout_notice).setOnClickListener(this);
	    headView.findViewById(R.id.layout_remind).setOnClickListener(this);
		listView.addHeaderView(headView);
		// 列表点击事件
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(arg2>0){
					Conversation cov = dataList.get(arg2-1);
					Intent intent = new Intent(getActivity(),
							ConversationActivity.class);
					intent.putExtra("conversation", cov);
					getActivity().startActivity(intent);
				}
				
			}
		});
		RongIM.setOnReceiveMessageListener(this);
		super.onActivityCreated(savedInstanceState);
	}

	/** 通过融云api获取会话列表数据 */
	public void requestConversationList() {
		RongIMClient.getInstance().getConversationList(
				new ResultCallback<List<Conversation>>() {

					@Override
					public void onError(ErrorCode arg0) {
						Log.i(TAG, "获取会话列表失败：" + arg0);
					}

					@Override
					public void onSuccess(List<Conversation> list) {
						if (list == null) {
							return;
						}
						dataList.clear();
						for (Conversation cov : list) {
							dataList.add(cov);
						}
						adapter.notifyDataSetChanged();
						Log.i(TAG, "获取会话列表失败：" + list);

					}
				});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_left:
			Intent intent1 = new Intent(getActivity(),
					MessageContactsActivity.class);
			startActivity(intent1);
			break;
		case R.id.iv_right:
			Intent intent2 = new Intent(getActivity(),
					GroupContactsActivity.class);
			startActivity(intent2);
			break;
		case R.id.layout_notice:
			Intent intent3 = new Intent(getActivity(),
					MessageNoticeActivity.class);
			startActivity(intent3);
			break;

		case R.id.layout_remind:
			Intent intent4 = new Intent(getActivity(),
					MessageRemindActivity.class);
			startActivity(intent4);
			break;
		default:
			break;
		}

	}

	/**
	 * 接收消息的监听器：OnReceiveMessageListener 的回调方法，接收到消息后执行。
	 * 
	 * @param message
	 *            接收到的消息的实体信息。
	 * @param left
	 *            剩余未拉取消息数目。
	 */
	@Override
	public boolean onReceived(Message message, int left) {
		requestConversationList();
		return false;
	}

}
