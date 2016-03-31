package com.zepan.caifuyun.adapter;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.entity.File;
import com.zepan.caifuyun.entity.File;

public class FileAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private Context context;
	private List<File> list;//
	public FileAdapter(Context context,List<File>  list){
		this.inflater=LayoutInflater.from(context);
		this.list=list;
		this.context=context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public File getItem(int position) {
		// TODO Auto-generated method stub
		return list == null ? null : list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public View getView(int position,View convertView, ViewGroup parent) {
		HolderView holder=null;
		if(convertView==null){
			holder=new HolderView();
			convertView=inflater.inflate(R.layout.listitem_remind_file, null);
			holder.tv_remind_file_name=(TextView) convertView.findViewById(R.id.tv_remind_file_name);
			convertView.setTag(holder);
		}else {
			holder=(HolderView)convertView.getTag();
		}
		if(list.get(position)!=null){
			holder.tv_remind_file_name.setText(list.get(position).getFileName());
			
		}

		return convertView;
	}
	class HolderView{
		TextView tv_remind_file_name;//名字
		TextView mobile_phone;//手机号
		TextView address;//地址
		TextView zip;//邮编
	}
}
