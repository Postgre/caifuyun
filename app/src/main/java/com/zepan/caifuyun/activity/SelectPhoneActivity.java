package com.zepan.caifuyun.activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;



import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;
/**
 * 选择电话
 * @author duanjie
 *
 */
public class SelectPhoneActivity extends BaseActivity  implements  OnClickListener{
	private Handler handler = new Handler();
	private String tvPhone,tv_phone,tv_work_phone,tv_address_phone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_phone);
		setHeaderFields(0,0,0,
				R.drawable.ic_action_back, 0,0);
		initView();
		initData();

	}

	private void initData() {
		Intent  intent=getIntent();
		tvPhone=intent.getStringExtra("tvPhone"); 
		tv_phone=((RadioButton)findViewById(R.id.tv_phone)).getText().toString();
		tv_work_phone=((RadioButton)findViewById(R.id.tv_work_phone)).getText().toString();
		tv_address_phone=((RadioButton)findViewById(R.id.tv_address_phone)).getText().toString();
		if(tvPhone.equals(tv_phone)){
			findViewById(R.id.tv_phone).setSelected(true); 
		}else if(tvPhone.equals(tv_work_phone)){
			findViewById(R.id.tv_work_phone).setSelected(true); 
		}else {
			findViewById(R.id.tv_address_phone).setSelected(true); 
		}

	}

	private void initView() {
		findViewById(R.id.tv_phone).setOnClickListener(this); 
		findViewById(R.id.tv_work_phone).setOnClickListener(this); 
		findViewById(R.id.tv_address_phone).setOnClickListener(this); 
	}

	@Override
	public void onClick(View v) {
		findViewById(R.id.tv_phone).setSelected(false);
		findViewById(R.id.tv_work_phone).setSelected(false);
		findViewById(R.id.tv_address_phone).setSelected(false);
		RadioButton  tv=(RadioButton)v;
		Intent intentphone=new Intent();
		intentphone.putExtra("phone",tv.getText().toString());
		SelectPhoneActivity.this.setResult(RESULT_OK, intentphone);
		finish(); 
		// 设置选中样式
		tv.setSelected(true);
		handler.postDelayed(new Runnable() {
			public void run() {
				finish();
			}}, 300);


	}



	/*switch (v.getId()) {

		case R.id.tv_phone:
			Intent intentphone=new Intent();
			intentphone.putExtra("phone", findViewById(id));
			SelectPhoneActivity.this.setResult(RESULT_OK, intentphone);
			finish(); 
			break;
		case R.id.tv_work_phone:
			Intent intentworkPhone=new Intent();
			intentworkPhone.putExtra("phone", "工作电话");
			SelectPhoneActivity.this.setResult(RESULT_OK, intentworkPhone);
			finish(); 
			break;

		case R.id.tv_address_phone:
			Intent intentaddressPhone=new Intent();
			intentaddressPhone.putExtra("phone", "住宅电话");
			SelectPhoneActivity.this.setResult(RESULT_OK, intentaddressPhone);
			break;

		default:
			break;*/
}


