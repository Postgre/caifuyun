package com.zepan.caifuyun.activity;

import java.util.ArrayList;
import java.util.List;

import com.zepan.android.widget.AlphaImageView;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.adapter.OrganizationAdapter;
import com.zepan.caifuyun.adapter.PersonAdapter;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.entity.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 选择客户
 * 个人  和机构 
 * @author duanjie
 *
 */
public class SelectCustomerActivity extends BaseActivity implements OnClickListener{

	private PersonAdapter personAdatper;
	private OrganizationAdapter organizationAdatper;
	private List<User> personList=new ArrayList<User>();
	private List<User> organizationList=new ArrayList<User>();
	private boolean isCheck;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_customer);
		initView();
	}

	private void initView() {
		((AlphaImageView)findViewById(R.id.iv_right)).setImageResource(R.drawable.ic_action_search);
		((AlphaImageView)findViewById(R.id.iv_left)).setClickAlpha(150, true, false);
		((AlphaImageView)findViewById(R.id.iv_right)).setClickAlpha(150, true, false);
		findViewById(R.id.iv_right).setOnClickListener(this);
		findViewById(R.id.rbtn_all).setOnClickListener(this);
		findViewById(R.id.rbtn_anonymous).setOnClickListener(this);
	}
	
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_right:
			Intent SelectCustomerIntent=new Intent(SelectCustomerActivity.this,SelectCustomerSearchActivity.class);
			startActivity(SelectCustomerIntent);
			break;
		case R.id.rbtn_all:
			isCheck=true;
			
			break;
		case R.id.rbtn_anonymous:
			isCheck=false;
			break;

		default:
			break;
		}

	}



}
