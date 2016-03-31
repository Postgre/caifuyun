package com.zepan.caifuyun.adapter;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.entity.ReceiveAddress;

public class ManagerAddressAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private Context context;
	private List<ReceiveAddress> list;//
	public ManagerAddressAdapter(Context context,List<ReceiveAddress>  list){
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
	public ReceiveAddress getItem(int position) {
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
			convertView=inflater.inflate(R.layout.listitem_receive_address, null);
			holder.tv_name=(TextView)convertView.findViewById(R.id.tv_name);
			holder.mobile_phone=(TextView)convertView.findViewById(R.id.mobile_phone);
			holder.address=(TextView)convertView.findViewById(R.id.address);
			holder.zip=(TextView)convertView.findViewById(R.id.zip);
			convertView.setTag(holder);
		}else {
			holder=(HolderView)convertView.getTag();
		}
		if(list.get(position)!=null){
			if(list.get(position).getName()!=null){
				holder.tv_name.setText(list.get(position).getName());
			}
			if(list.get(position).getMobile()!=null){
				holder.mobile_phone.setText(list.get(position).getMobile());
			}
			if(list.get(position).getAddress()!=null){
				holder.address.setText(list.get(position).getAddress());
			}
			if(list.get(position).getZipcode()!=null){
				holder.zip.setText(list.get(position).getZipcode());
			}
			
		}

		return convertView;
	}
	class HolderView{
		TextView tv_name;//名字
		TextView mobile_phone;//手机号
		TextView address;//地址
		TextView zip;//邮编
	}
}
