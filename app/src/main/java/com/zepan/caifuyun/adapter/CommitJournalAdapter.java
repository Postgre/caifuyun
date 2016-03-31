package com.zepan.caifuyun.adapter;

import java.util.List;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.activity.UnReadDetailsActivity;
import com.zepan.caifuyun.activity.UnReadEditActivity;
import com.zepan.caifuyun.adapter.InstitutionsAcceptAdapter.HolderView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TextView;

//我提交的日报
public class CommitJournalAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Context context;
	private List<String> list;//
	private String[] array;
	
	 public CommitJournalAdapter(Context context,String[] array,List<String>  list) {
		// TODO Auto-generated constructor stub
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
	public View getView( final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=inflater.inflate(R.layout.listitem_commitjournal, null);
			holder.date=(TextView) convertView.findViewById(R.id.date);
			holder.time=(TextView) convertView.findViewById(R.id.time);
			holder.order= (RadioButton) convertView.findViewById(R.id.order);
			
			convertView.setTag(holder);
			
		}else{
			holder=(ViewHolder) convertView.getTag();
			
		}
		
		//暂时数据
		holder.order.setText("草稿");
		
		
		//button暂时模拟草稿的跳转页面
		OnClickListener listener=new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(holder.order.getText().equals("草稿")){
					
					Intent intent=new Intent(context,UnReadEditActivity.class);
					context.startActivity(intent);
				}
			}
		};
		holder.order.setOnClickListener(listener);
		
		return convertView;
	}
	class ViewHolder{
		TextView date;//日期
		TextView time; //时间
		RadioButton order;//未阅
	}

}
