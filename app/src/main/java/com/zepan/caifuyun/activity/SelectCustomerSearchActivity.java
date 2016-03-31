package com.zepan.caifuyun.activity;


import com.zepan.caifuyun.R;
import com.zepan.caifuyun.R.id;
import com.zepan.caifuyun.R.layout;
import com.zepan.caifuyun.R.string;
import com.zepan.caifuyun.base.BaseActivity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
/**
 * 个人和机构的搜索页面
 * @author duanjie
 *
 */
public class SelectCustomerSearchActivity extends BaseActivity {
	/** 取消 */
	private TextView cancel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_customer_search);
		cancel = (TextView) findViewById(R.id.tv_right);
		cancel.setText(R.string.head_title_register_right);
	}

}
