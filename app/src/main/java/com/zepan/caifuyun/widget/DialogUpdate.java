package com.zepan.caifuyun.widget;

import com.zepan.android.widget.CheckableEditText;
import com.zepan.caifuyun.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;


public class DialogUpdate extends Dialog implements View.OnClickListener{
	private Context context;
	private String name;
	public DialogUpdate(Context context,String name) {
		super(context,R.style.dialog_style01);
		this.context=context;
		this.name=name;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dailog_update);
		findViewById(R.id.tv_cancel).setOnClickListener(this);
		findViewById(R.id.tv_sure).setOnClickListener(this);
		((CheckableEditText)findViewById(R.id.et_name)).setText(name);

	}
	/**
	 * 点击回调
	 * @author long
	 *
	 */
	public interface OnButtonClick{
		void sure(String name);
		void cancel();
	}
	private OnButtonClick onButtonClick;

	public void setOnButtonClick(OnButtonClick onButtonClick){
		this.onButtonClick=onButtonClick;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_cancel:
			if(onButtonClick!=null){
				onButtonClick.cancel();
			}
			break;
		case R.id.tv_sure:
			if(!((CheckableEditText)findViewById(R.id.et_name)).check()){
				return;
			}
			if(!((CheckableEditText)findViewById(R.id.et_name)).getText().toString().equals(name)){
				if(onButtonClick!=null){
					onButtonClick.sure(((CheckableEditText)findViewById(R.id.et_name)).getText().toString());
				}
			}else{
				this.dismiss();
			}
			
			break;

		default:
			break;
		}

	}


}
