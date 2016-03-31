package com.zepan.caifuyun.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.widget.DialogBottom;
import com.zepan.caifuyun.widget.DialogBottom.OnItemClick;

//点击草稿进入的未批阅选择页面
public class UnReadEditActivity extends BaseActivity {

	private Button button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_salesjournal_unread);
		initView();

	}
	public void initView(){
		setHeaderFields(0,R.string.unread,0,R.drawable.ic_action_back,0,R.drawable.ic_action_more);
		button=(Button) findViewById(R.id.sure);
		button.setVisibility(View.GONE);
		//未批阅的编辑
		ImageView iv_more=(ImageView) findViewById(R.id.iv_right);
		iv_more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final DialogBottom infoDailog=new DialogBottom(UnReadEditActivity.this);
				String[] nameArray={"编辑","删除"};
				infoDailog.setArray(nameArray);
				infoDailog.setOnItemClick(new OnItemClick() {

					@Override
					public void itemClick(AdapterView<?> parent, View view, int position, long id) {
						switch (position) {
						case 0:
							infoDailog.dismiss();
							Intent intent=new Intent(UnReadEditActivity.this,EditJournalActivity.class);
							startActivity(intent);
							break;
						case 1:
							infoDailog.dismiss();
							break;

						default:
							break;
						}

					}
				});
				infoDailog.show();


			}
		});
	}
}
