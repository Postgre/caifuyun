package com.zepan.caifuyun.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

//暂时模拟的群组的聊天界面
public class GroupChatActivity extends BaseActivity implements OnClickListener{

	private PopupWindow popupWindow;
	private View view1;
	private ImageView iv_back;
	private ImageView iv_group;
	private ImageView iv_more;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_chat);
		
		initView();
		
	}

	private void initView() {
		// TODO Auto-generated method stub
		iv_back=(ImageView) findViewById(R.id.iv_left);
		iv_group=(ImageView) findViewById(R.id.iv_right);
		iv_more=(ImageView) findViewById(R.id.iv_more);
		iv_back.setOnClickListener(this);
		iv_group.setOnClickListener(this);
		iv_more.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_left:
			finish();
			break;
		case R.id.iv_right: //跳转到群组的聊天信息界面
			Intent intent=new Intent(GroupChatActivity.this,GroupMemberActivity.class);
			startActivity(intent);
			break;
		case R.id.iv_more:
			showpop(v);
			break;
		case R.id.layout_notice://群通知
			//popupwindow的点击样式
			view1.findViewById(R.id.layout_notice).setBackgroundColor(getResources().getColor(R.color.bg_home));
			((TextView)(view1.findViewById(R.id.title_notice))).setTextColor(Color.WHITE);
			((ImageView)(view1.findViewById(R.id.notice_image))).setBackgroundResource(R.drawable.notice2_on);
			
			view1.findViewById(R.id.layout_file).setBackgroundColor(getResources().getColor(R.color.white));
			((TextView)(view1.findViewById(R.id.title_file))).setTextColor(Color.BLACK);
			((ImageView)(view1.findViewById(R.id.file_image))).setBackgroundResource(R.drawable.file);
			
			Intent intent1=new Intent(GroupChatActivity.this,GroupNoticeActivity.class);
			startActivity(intent1);
			break;
		case R.id.layout_file://群文件 
			view1.findViewById(R.id.layout_file).setBackgroundColor(getResources().getColor(R.color.bg_home));
			((TextView)(view1.findViewById(R.id.title_file))).setTextColor(Color.WHITE);
			((ImageView)(view1.findViewById(R.id.file_image))).setBackgroundResource(R.drawable.file_on);
			
			view1.findViewById(R.id.layout_notice).setBackgroundColor(getResources().getColor(R.color.white));
			((TextView)(view1.findViewById(R.id.title_notice))).setTextColor(Color.BLACK);
			((ImageView)(view1.findViewById(R.id.notice_image))).setBackgroundResource(R.drawable.notice2);
			
			Intent intent2=new Intent(GroupChatActivity.this,GroupFileActivity.class);
			startActivity(intent2);
			break;

		default:
			break;
		}
	}

	private void showpop(View v) {
		// TODO Auto-generated method stub
		view1 = LayoutInflater.from(GroupChatActivity.this).inflate(R.layout.popmenu_listview_item, null);
		popupWindow = new PopupWindow(view1,400,LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true); //可以聚焦
        popupWindow.setOutsideTouchable(false);
      //  popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE)); //背景色
        popupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
        
        popupWindow.setContentView(view1);
        popupWindow.showAsDropDown(v);
        
        view1.findViewById(R.id.layout_notice).setOnClickListener(this);
        view1.findViewById(R.id.layout_file).setOnClickListener(this);
	}
	
	
}
