package com.zepan.caifuyun.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.entity.Company;
public class CertificationAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private Context context;
	private List<Company> list;

	public CertificationAdapter(Context context,List<Company>  list){
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
	public Company getItem(int position) {
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
			convertView=inflater.inflate(R.layout.listitem_certification, null);
			holder.tv_company_name=(TextView)convertView.findViewById(R.id.tv_company_name);
			holder.tv_date=(TextView)convertView.findViewById(R.id.tv_date);
			holder.tv_company_num=(TextView)convertView.findViewById(R.id.tv_company_num);
			convertView.setTag(holder);
		} else {
			holder=(HolderView)convertView.getTag();
		}
   	    holder.tv_company_name.setText(list.get(position).getCompanyName());
		return convertView;


	}


	
	class HolderView{
		TextView tv_company_name;//公司的 名字
		TextView tv_date;//年月日
		TextView tv_company_num;//成员人数

	}
}
