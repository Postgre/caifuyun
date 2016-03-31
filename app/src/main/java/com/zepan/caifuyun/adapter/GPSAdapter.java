package com.zepan.caifuyun.adapter;

import java.util.List;

import com.baidu.mapapi.search.core.PoiInfo;
import com.zepan.caifuyun.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
/**
 * 线索的adapter
 * @author duanjie
 *
 */
public class GPSAdapter  extends BaseAdapter{
	private LayoutInflater inflater;
	private Context context;
	private List<PoiInfo> list;//线索
	public GPSAdapter(Context context,List<PoiInfo>  list){
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
	public PoiInfo getItem(int position) {
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
		HoloderView   holoderView;
		if(convertView==null){
			holoderView=new HoloderView();
			convertView=inflater.inflate(R.layout.listitem_gps, null);
			holoderView.tv_name=(TextView)convertView.findViewById(R.id.tv_name);
			holoderView.tv_adress=(TextView)convertView.findViewById(R.id.tv_adress);
			convertView.setTag(holoderView);
		}else{
			holoderView=(HoloderView)convertView.getTag();
		}
		if(list.get(position)!=null){
			if(position==0){
				holoderView.tv_name.setText(list.get(position).name);
				holoderView.tv_name.setPadding(30, 50, 0, 50);
				holoderView.tv_name.setTextColor(context.getResources().getColor(R.color.text_blue_color));
				holoderView.tv_adress.setVisibility(View.GONE);
			}else if(position==1){
				holoderView.tv_name.setText(list.get(position).name);
				holoderView.tv_name.setPadding(30, 50, 0, 50);
				holoderView.tv_name.setTextColor(context.getResources().getColor(R.color.black));
				holoderView.tv_adress.setVisibility(View.GONE);
			}else {
				holoderView.tv_adress.setVisibility(View.VISIBLE);
				holoderView.tv_name.setText(list.get(position).name);
				holoderView.tv_name.setPadding(30, 50, 0, 0);
				holoderView.tv_name.setTextColor(context.getResources().getColor(R.color.black));
				holoderView.tv_adress.setText(list.get(position).address);
			}

		}


		return convertView;
	}

	class  HoloderView{
		TextView tv_name;//名称
		TextView tv_adress;//地址
	}
}
