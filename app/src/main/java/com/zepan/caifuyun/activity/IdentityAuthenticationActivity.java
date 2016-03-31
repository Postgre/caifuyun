package com.zepan.caifuyun.activity;

import com.zepan.android.sdk.BaseActivity;
import com.zepan.caifuyun.R;

import android.os.Bundle;
//身份认证
public class IdentityAuthenticationActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_identity_authentication);
		setHeaderFields(0, R.string.identity_authentication, 
				0,R.drawable.ic_action_back, 0,0);
	}

	
}
