package com.zepan.caifuyun.adapter;

import java.util.List;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.entity.PersonalContacts;
import com.zepan.caifuyun.entity.UserInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

//个人通讯录适配器
public class PersonalContactsAdapter extends BaseAdapter {

	private Context context;
	private List<UserInfo> list;
	public PersonalContactsAdapter(Context context,List<UserInfo> list) {
		// TODO Auto-generated constructor stub
		this.context=context;
		this.list=list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list == null ? null : list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		ViewHolder holder;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.listitem_message_personal_contacts, null);
			holder.tv_letter=(TextView) convertView.findViewById(R.id.first_letter);
			holder.tv_name=(TextView) convertView.findViewById(R.id.name);
			holder.image=(ImageView) convertView.findViewById(R.id.image);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		
		if(list.get(position).getLabel()!=null){
			holder.tv_letter.setText(list.get(position).getLabel());
		}else{
			holder.tv_letter.setVisibility(View.GONE);
		}
		
		holder.tv_name.setText(list.get(position).getUser_name());
		
		
		return convertView;
	}
	
	class ViewHolder{
		TextView tv_letter;
		ImageView image;
		TextView tv_name;
		
	}

}
