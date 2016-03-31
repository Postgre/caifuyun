package com.zepan.caifuyun.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;

import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Conversation.ConversationType;

/**
 * 单聊界面
 * */
public class ConversationActivity extends BaseActivity implements
		OnClickListener{

	private PopupWindow popupWindow;
	private View view1;
	private ImageView iv_back;
	private ImageView iv_group;
	private ImageView iv_more;
	/**从上个页面传回的会话对像*/
	private Conversation conversation;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conversation);
		initView();
		// 调用融云聊天界面
		Intent intent = getIntent();
		if (intent != null) {
			conversation = intent.getParcelableExtra("conversation");
			if(conversation != null){
				// 如是会话类型为讨论组
				if (ConversationType.DISCUSSION == conversation.getConversationType()) {// 讨论组
					((TextView) findViewById(R.id.tv_left)).setText(conversation.getConversationTitle());
				}else if(ConversationType.DISCUSSION == conversation.getConversationType()){// 私聊
					((TextView) findViewById(R.id.tv_left)).setText(conversation.getSenderUserName());
				}
				ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager()
						.findFragmentById(R.id.conversation);
				Uri uri = Uri
						.parse("rong://" + getApplicationInfo().packageName)
						.buildUpon()
						.appendPath("conversation")
						.appendPath(
								conversation.getConversationType()
										.getName().toLowerCase())
						.appendQueryParameter("targetId", conversation.getTargetId())
						.appendQueryParameter("title", "hello").build();
				fragment.setUri(uri);
			}
		}
	}

	private void initView() {
		// TODO Auto-generated method stub
		iv_back = (ImageView) findViewById(R.id.iv_left);
		iv_group = (ImageView) findViewById(R.id.iv_right);
		iv_more = (ImageView) findViewById(R.id.iv_more);
		iv_back.setOnClickListener(this);

		iv_group.setOnClickListener(this);
		iv_more.setOnClickListener(this);
		// ==========================事件==========================

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_left:
			finish();
			break;
		case R.id.iv_right: // 跳转到群组的群成员界面
			Intent intent = new Intent(ConversationActivity.this,
					GroupMemberActivity.class);
			if(conversation != null){
				intent.putExtra("targetId", conversation.getTargetId());
				intent.putExtra("targetTitle", conversation.getConversationTitle());
			}else{// 如果是从“我的群组”跳转
				Intent srcIntent = getIntent();
				if(srcIntent != null){
					intent.putExtra("targetId", srcIntent.getStringExtra("targetId"));
					intent.putExtra("targetTitle", srcIntent.getStringExtra("targetTitle"));
				}
			}
			startActivityForResult(intent, 0);
			break;
		case R.id.iv_more:
			showpop(v);
			break;
		case R.id.layout_notice:// 群通知
			// popupwindow的点击样式
			view1.findViewById(R.id.layout_notice).setBackgroundColor(
					getResources().getColor(R.color.bg_home));
			((TextView) (view1.findViewById(R.id.title_notice)))
					.setTextColor(Color.WHITE);
			((ImageView) (view1.findViewById(R.id.notice_image)))
					.setBackgroundResource(R.drawable.notice2_on);

			view1.findViewById(R.id.layout_file).setBackgroundColor(
					getResources().getColor(R.color.white));
			((TextView) (view1.findViewById(R.id.title_file)))
					.setTextColor(Color.BLACK);
			((ImageView) (view1.findViewById(R.id.file_image)))
					.setBackgroundResource(R.drawable.file);

			Intent intent1 = new Intent(ConversationActivity.this,
					GroupNoticeActivity.class);
			startActivity(intent1);
			break;
		case R.id.layout_file:// 群文件
			view1.findViewById(R.id.layout_file).setBackgroundColor(
					getResources().getColor(R.color.bg_home));
			((TextView) (view1.findViewById(R.id.title_file)))
					.setTextColor(Color.WHITE);
			((ImageView) (view1.findViewById(R.id.file_image)))
					.setBackgroundResource(R.drawable.file_on);

			view1.findViewById(R.id.layout_notice).setBackgroundColor(
					getResources().getColor(R.color.white));
			((TextView) (view1.findViewById(R.id.title_notice)))
					.setTextColor(Color.BLACK);
			((ImageView) (view1.findViewById(R.id.notice_image)))
					.setBackgroundResource(R.drawable.notice2);

			Intent intent2 = new Intent(ConversationActivity.this,
					GroupFileActivity.class);
			startActivity(intent2);
			break;
		default:
			break;
		}

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data!=null){
			if(data.hasExtra("quitDiscussion")){
				finish();
			}
			if(data.hasExtra("targetTitle")){
				((TextView) findViewById(R.id.tv_left)).setText(data.getStringExtra("targetTitle"));
			}
			
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void showpop(View v) {
		// TODO Auto-generated method stub
		view1 = LayoutInflater.from(ConversationActivity.this).inflate(
				R.layout.popmenu_listview_item, null);
		popupWindow = new PopupWindow(view1, 400, LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true); // 可以聚焦
		popupWindow.setOutsideTouchable(false);
		// popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
		// //背景色
		popupWindow.setBackgroundDrawable(new ColorDrawable(getResources()
				.getColor(R.color.transparent)));

		popupWindow.setContentView(view1);
		popupWindow.showAsDropDown(v);

		view1.findViewById(R.id.layout_notice).setOnClickListener(this);
		view1.findViewById(R.id.layout_file).setOnClickListener(this);
	}

	

}
