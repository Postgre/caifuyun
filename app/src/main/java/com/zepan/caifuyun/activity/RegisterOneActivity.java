package com.zepan.caifuyun.activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;



import com.zepan.android.widget.AlphaImageView;
import com.zepan.android.widget.AlphaTextView;
import com.zepan.android.widget.CheckableEditText;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;
/**
 * 注册第一个界面
 * @author duanjie
 *
 */
public class RegisterOneActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_one);
		setHeaderFields(0, R.string.title_register,0,
				R.drawable.ic_action_back, 0, 0);
		initView();
	}
	private void initView(){
		((AlphaImageView)findViewById(R.id.iv_left)).setClickAlpha(150, true, false);
		((AlphaTextView)findViewById(R.id.tv_right)).setClickAlpha(100);
		findViewById(R.id.tv_right).setOnClickListener(this);
		findViewById(R.id.btn_send_verification_code).setOnClickListener(this);
		findViewById(R.id.btn_send_verification_code).setEnabled(false);
		((CheckableEditText)findViewById(R.id.edtTx_name)).addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.length()>0){
					findViewById(R.id.btn_send_verification_code).setEnabled(true);
				}else{
					findViewById(R.id.btn_send_verification_code).setEnabled(false);
				}
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {

		super.onPostCreate(savedInstanceState);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_right:
			finish();
			break;
		case R.id.btn_send_verification_code:
			CheckableEditText nameEdtTx=(CheckableEditText)findViewById(R.id.edtTx_name);
			if(nameEdtTx.check()){
				Intent intent=new Intent(this, RegisterTwoActivity.class);
				intent.putExtra("phone", nameEdtTx.getText().toString());
				startActivity(intent);
			}else{
				return;
			}
			break;
		default:
			break;
		}

	}
}
