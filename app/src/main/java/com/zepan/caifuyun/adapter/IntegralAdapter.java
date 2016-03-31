package com.zepan.caifuyun.adapter;

import java.util.List;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.entity.IntegralInfo;
import com.zepan.caifuyun.entity.UserInfo;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//积分商城
public class IntegralAdapter extends BaseAdapter {

	private Context context;
	private List<IntegralInfo> list;
	public IntegralAdapter(Context context,List<IntegralInfo> list) {
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
		return list==null ? null : list.get(position);
		
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
			convertView=LayoutInflater.from(context).inflate(R.layout.gridviewitem_integral, null);
			holder.imageview=(ImageView) convertView.findViewById(R.id.integral_image);
		    holder.discount=(TextView) convertView.findViewById(R.id.integral_discount);
		    holder.num=(TextView) convertView.findViewById(R.id.integral_num);
		    
		    
		    convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		
		holder.imageview.setImageURI(Uri.parse(list.get(position).getUrl()));
		holder.discount.setText(list.get(position).getDiscount());
		holder.num.setText(list.get(position).getNum()+"");
		return convertView;
	}
	
	class ViewHolder{
		ImageView imageview;
		TextView discount;
		TextView num;
		
		
	}

}
