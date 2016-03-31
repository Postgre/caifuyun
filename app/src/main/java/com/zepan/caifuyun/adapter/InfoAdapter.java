package com.zepan.caifuyun.adapter;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zepan.caifuyun.R;

public class InfoAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private Context context;
	private List<String> list;//
	private String[] array;
	public InfoAdapter(Context context,String[] array,List<String>  list){
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
	@Override
	public View getView(int position,View convertView, ViewGroup parent) {
		HolderView holder=null;
		if(convertView==null){
			holder=new HolderView();
			convertView=inflater.inflate(R.layout.listitem_self_info, null);
			holder.tv_name=(TextView)convertView.findViewById(R.id.tv_name);
			holder.tv_content=(TextView)convertView.findViewById(R.id.tv_content);
			convertView.setTag(holder);
		}else {
			holder=(HolderView)convertView.getTag();
		}
		holder.tv_name.setText(array[position]);
		if(position<list.size()&&list.get(position)!=null){
			holder.tv_content.setText(list.get(position));
		}
		return convertView;
	}
	class HolderView{
		TextView tv_name;// 名字
		TextView tv_content;//内容
	}
}
