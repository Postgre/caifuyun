package com.zepan.caifuyun.adapter;

import java.util.List;

import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.socialize.utils.Log;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.entity.Message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//消息首页
public class MessageAdapter extends BaseAdapter {

	private Context context;
	private List<Message> list;
	public MessageAdapter(Context context,List<Message> list) {
		// TODO Auto-generated constructor stub
		super();
		this.context=context;
		this.list=list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		Log.i("----", list.size()+"");
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
			convertView=LayoutInflater.from(context).inflate(R.layout.listitem_message, null);
			holder.image=(SimpleDraweeView) convertView.findViewById(R.id.iv_message);
			holder.title=(TextView) convertView.findViewById(R.id.title);
			holder.content=(TextView) convertView.findViewById(R.id.content);
			holder.time=(TextView) convertView.findViewById(R.id.time);
			
			convertView.setTag(holder);
			
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		
		holder.title.setText(list.get(position).getName());
		holder.content.setText(list.get(position).getContent());
		holder.time.setText(list.get(position).getTime());
		
		return convertView;
	}
	
	class ViewHolder{
		SimpleDraweeView image;
		TextView title; //消息发送者
		TextView content;//消息内容
		TextView time;//消息发送时间
		
	}

}
