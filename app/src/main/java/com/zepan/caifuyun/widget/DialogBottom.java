package com.zepan.caifuyun.widget;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.adapter.DailogAdapter;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

@SuppressLint("Instantiatable")
public class DialogBottom extends Dialog implements OnItemClickListener{
	private DailogAdapter infoDailogAdapter;
	private Context context;
	private String[] array;
	public DialogBottom(Context context) {
		super(context,R.style.dialog_style01);
		this.context=context;
	}
	public void setArray(String[] array){
		this.array=array;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dailog_bottom);
		getWindow().setGravity(Gravity.BOTTOM);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		ListView listView=(ListView) findViewById(android.R.id.list);
		infoDailogAdapter=new DailogAdapter(context, array);
		listView.setAdapter(infoDailogAdapter);
		listView.setDivider(null);
		listView.setOnItemClickListener(this);
		
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(onItemClick!=null){
			onItemClick.itemClick(parent, view, position, id);
		}
		
	}
	/**
	 * 点击回调
	 * @author long
	 *
	 */
	public interface OnItemClick{
		void itemClick(AdapterView<?> parent, View view, int position, long id);
	}
	private OnItemClick onItemClick;
	
	public void setOnItemClick(OnItemClick onItemClick){
		this.onItemClick=onItemClick;
	}


}
