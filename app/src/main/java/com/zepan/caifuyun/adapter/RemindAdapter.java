package com.zepan.caifuyun.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.entity.File;
import com.zepan.caifuyun.entity.Remind;
public class RemindAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private Context context;
	private List<Remind> list;

	public RemindAdapter(Context context,List<Remind>  list){
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
	public Remind getItem(int position) {
		// TODO Auto-generated method stub
		return list == null ? null : list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@SuppressWarnings("deprecation")
	@Override
	public View getView(final int position,View convertView, ViewGroup parent) {
		if(list.get(position)!=null&&list.get(position).getRemindType()!=0){
			if(list.get(position).getRemindType()==4){
				//理财到期
				convertView=inflater.inflate(R.layout.listitem_remind_financial, null);
				final View view=convertView.findViewById(android.R.id.list);
				convertView.findViewById(R.id.rl_remind_title).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if(list.get(position).isOpen()){
							view.findViewById(android.R.id.list).setVisibility(View.VISIBLE);
							list.get(position).setOpen(false);
						}else{
							view.findViewById(android.R.id.list).setVisibility(View.GONE);
							list.get(position).setOpen(true);
						}

					}
				});;
				((TextView)convertView.findViewById(R.id.tv_remind_name)).setText(list.get(position).getRemindName());
				((TextView)convertView.findViewById(R.id.tv_file_num)).setText(list.get(position).getChildFile().size()+"");;
				ListView listView=(ListView) convertView.findViewById(android.R.id.list);
				List<File> fileList=new ArrayList<File>();
				for(int i=0;i<list.get(position).getChildFile().size();i++){
					fileList.add(list.get(position).getChildFile().get(i));
				}
				FileAdapter fileAdapter=new FileAdapter(context, fileList);
				listView.setAdapter(fileAdapter);
				listView.setDivider(null);
			}else{
				convertView=inflater.inflate(R.layout.listitem_remind_single, null);
				TextView tv_reminder_name=(TextView)convertView.findViewById(R.id.tv_reminder_name);
				if(list.get(position).getRemindType()==1){
					//纪念日
					Drawable left=context.getResources().getDrawable(R.drawable.ic_reminder_anniversary);
					/// 这一步必须要做,否则不会显示.
					left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());
					tv_reminder_name.setCompoundDrawables(left, null, null, null);
				}else if(list.get(position).getRemindType()==2){
					//理财到期
					Drawable left=context.getResources().getDrawable(R.drawable.ic_reminder_finance);
					/// 这一步必须要做,否则不会显示.
					left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());
					tv_reminder_name.setCompoundDrawables(left, null, null, null);
				}else if(list.get(position).getRemindType()==3){
					//预约拜访
					Drawable left=context.getResources().getDrawable(R.drawable.ic_reminder_visit);
					/// 这一步必须要做,否则不会显示.
					left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());
					tv_reminder_name.setCompoundDrawables(left, null, null, null);
				}
				if(list.get(position).getRemindName()!=null){
					tv_reminder_name.setText(list.get(position).getRemindName());
				}
				
				
				
				
			}
		}



		return convertView;
	}



	class HolderView{
		TextView tv_company_name;//公司的 名字
		TextView tv_date;//年月日
		TextView tv_company_num;//成员人数

	}
}
