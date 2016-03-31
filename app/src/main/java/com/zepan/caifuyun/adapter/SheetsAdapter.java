package com.zepan.caifuyun.adapter;
import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.R.drawable;

public class SheetsAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private Context context;
	private List<String> list;//
	private String[] array;
	private int color;
	public SheetsAdapter(Context context,String[] array,List<String>  list){
		this.inflater=LayoutInflater.from(context);
		this.list=list;
		this.context=context;
		this.array=array;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return array.length;
	}

	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return list == null ? null : list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	public void setColor(int color){
		this.color=color;
	}
	@Override
	public View getView(int position,View convertView, ViewGroup parent) {
		HolderView holder=null;
		if(convertView==null){
			holder=new HolderView();
			convertView=inflater.inflate(R.layout.listitem_sheets, null);
			holder.tv_name=(TextView)convertView.findViewById(R.id.tv_name);
			holder.progressbar_number=(ProgressBar)convertView.findViewById(R.id.progressbar_number);
			if(color!=0){
				Drawable drawable=context.getResources().getDrawable(color); 
				holder.progressbar_number.setProgressDrawable(drawable);
			}
			holder.tv_money_number=(TextView)convertView.findViewById(R.id.tv_money_number);
			convertView.setTag(holder);
		}else {
			holder=(HolderView)convertView.getTag();
		}
		holder.tv_name.setText(array[position]);
		if(position<list.size()&&list.get(position)!=null){
			holder.progressbar_number.setMax(500);
			int progress=Integer.parseInt(list.get(position));
			holder.progressbar_number.setProgress(progress);
			holder.tv_money_number.setText(list.get(position)+"万");
		}
		return convertView;
	}
	class HolderView{
		TextView tv_name;// 名字
		ProgressBar progressbar_number;//进度条
		TextView tv_money_number;//钱数
	}
}
