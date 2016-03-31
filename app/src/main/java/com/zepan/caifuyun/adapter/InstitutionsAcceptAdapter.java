package com.zepan.caifuyun.adapter;
import java.util.List;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.entity.Channel;
import com.zepan.caifuyun.util.StringUtil;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/***
 * 机构收单adapter
 * @author long
 *
 */
public class InstitutionsAcceptAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private Context context;
	private List<Channel> list;//
	private String[] array;
	public InstitutionsAcceptAdapter(Context context,String[] array,List<Channel>  list){
		this.inflater=LayoutInflater.from(context);
		this.list=list;
		this.context=context;
		this.array=array;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Channel getItem(int position) {
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
			convertView=inflater.inflate(R.layout.listitem_institutions_accept, null);
			holder.tv_name=(TextView)convertView.findViewById(R.id.tv_name);
			holder.tv_company=(TextView)convertView.findViewById(R.id.tv_company);
			holder.tv_address=(TextView)convertView.findViewById(R.id.tv_address);
			holder.iv_head=(ImageView) convertView.findViewById(R.id.iv_head);
			convertView.setTag(holder);
		}else {
			holder=(HolderView)convertView.getTag();
		}
		holder.tv_company.setText(list.get(position).getCompany());
		holder.tv_name.setText(list.get(position).getContact());
		holder.tv_address.setText(list.get(position).getAddress());
		
		if(StringUtil.isNullOrHttp(list.get(position).getAvatar())){//判断头像url的格式
			holder.iv_head.setImageURI(Uri.parse(list.get(position).getAvatar()));
		}else{
			holder.iv_head.setImageResource(R.drawable.ic_default_head);
		}
		return convertView;
	}
	class HolderView{
		TextView tv_name;// 名字
		TextView tv_address;//地址
		TextView tv_company;//公司
		ImageView iv_head;//头像
	}
}
