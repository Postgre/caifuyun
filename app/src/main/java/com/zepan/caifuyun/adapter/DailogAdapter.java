package com.zepan.caifuyun.adapter;

import com.zepan.caifuyun.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;



public class DailogAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private Context context;
	private String[] array;//Dynamic  Dynamic
	public DailogAdapter(Context context,String[]  array){
		this.inflater=LayoutInflater.from(context);
		this.array=array;
		this.context=context;
	}
	@Override
	public int getCount() {
		return array.length;
	}

	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return array == null ? null : array[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public View getView(final int position,View convertView, ViewGroup parent) {
		HolderView holder=null;
		if(convertView==null){
			holder=new HolderView();
			convertView=inflater.inflate(R.layout.listitem_dailog_bottom, null);
			holder.tv_name=(TextView)convertView.findViewById(R.id.tv_1);
			holder.iv_line=(FrameLayout)convertView.findViewById(R.id.fy_1);
			convertView.setTag(holder);
		}else {
			holder=(HolderView)convertView.getTag();
		}
		if(array!=null){
			if(array[position]!=null){
				holder.tv_name.setText(array[position]);
			}
		}
		return convertView;
	}
	class HolderView{
		TextView tv_name;//名字
		FrameLayout iv_line;//线
	}

}
