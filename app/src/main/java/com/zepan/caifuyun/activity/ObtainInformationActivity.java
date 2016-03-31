package com.zepan.caifuyun.activity;


import android.os.Bundle;

import com.zepan.android.widget.AlphaImageView;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;
/**
 * 获取资料
 * @author duanjie
 *
 */
public class ObtainInformationActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_obtain_information);
		setHeaderFields(0, R.string.obtain_information, 
				0,R.drawable.ic_action_back, 0,0);
		((AlphaImageView)findViewById(R.id.iv_left)).setClickAlpha(150, true, false);
	}
}
