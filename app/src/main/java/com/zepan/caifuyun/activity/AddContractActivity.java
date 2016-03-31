package com.zepan.caifuyun.activity;
import com.zepan.android.widget.AlphaImageView;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;

import android.app.Activity;
import android.os.Bundle;

/**
 * 添加合同  
 * @author duanjie
 *
 */
public class AddContractActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contract);
		setHeaderFields(0, R.string.addcontract, 
				0,R.drawable.ic_action_back, 0,0);
		((AlphaImageView)findViewById(R.id.iv_left)).setClickAlpha(150, true, false);
	}


}
