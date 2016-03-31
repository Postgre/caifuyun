package com.zepan.caifuyun.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;



import com.zepan.android.widget.AlphaImageView;
import com.zepan.android.widget.AlphaTextView;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.R.drawable;
import com.zepan.caifuyun.R.id;
import com.zepan.caifuyun.R.layout;
import com.zepan.caifuyun.R.string;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.widget.DialogBottom;
/**
 * 报单详情
 * @author duanjie
 *
 */
public class FormDetailActivity extends BaseActivity  implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_form_detail);
		setHeaderFields(0, R.string.title_activity_form_detail, 
				0,R.drawable.ic_action_back, 0,R.drawable.ic_action_more);
		initView();
	}



	private void initView() {
		((AlphaImageView)findViewById(R.id.iv_left)).setClickAlpha(150, true, false);
		((AlphaImageView)findViewById(R.id.iv_right)).setClickAlpha(150, true, false);
		((AlphaTextView)findViewById(R.id.tv_right)).setOnClickListener(this);

	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_right:
			Intent intentAnError=new Intent(FormDetailActivity.this,DialogBottom.class);
			startActivity(intentAnError);
			break;

		default:
			break;
		}

	}
}
