package com.zepan.caifuyun.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;

//积分商城的商品详情页面
public class IntegralDetailsActivity extends BaseActivity {

	private ImageView detailsImage;//图片
	private TextView contentText;//商品简介内容
	private TextView discountText;//优惠券
	private TextView num;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_integral_details);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		setHeaderFields(0,R.string.integral_details,0,R.drawable.ic_action_back,0,0);
		
		detailsImage=(ImageView) findViewById(R.id.image_head);
		contentText=(TextView) findViewById(R.id.content);
		discountText=(TextView) findViewById(R.id.details_discount);
		num=(TextView) findViewById(R.id.details_num);
		
		Intent intent=getIntent();
		String url=intent.getStringExtra("details_url");
		detailsImage.setImageURI(Uri.parse(url));
        
		contentText.setText(intent.getStringExtra("content"));
		discountText.setText(intent.getStringExtra("discount"));
		num.setText(intent.getIntExtra("num", 0)+"");
		
	}
	
}
