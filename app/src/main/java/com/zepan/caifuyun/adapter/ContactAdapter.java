package com.zepan.caifuyun.adapter;

import java.util.List;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.entity.Clues;
import com.zepan.caifuyun.entity.Contact;

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
public class ContactAdapter  extends BaseAdapter{
	private LayoutInflater inflater;
	private Context context;
	private List<Contact> list;//线索
	public ContactAdapter(Context context,List<Contact>  list){
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
	public Contact getItem(int position) {
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
			convertView=inflater.inflate(R.layout.listitem_affiliated_person, null);
			holoderView.tv_name=(TextView)convertView.findViewById(R.id.tv_name);
			holoderView.tv_phone=(TextView)convertView.findViewById(R.id.tv_phone);
			holoderView.tv_relation=(TextView)convertView.findViewById(R.id.tv_relation);
			holoderView.tv_note=(TextView)convertView.findViewById(R.id.tv_note);
			convertView.setTag(holoderView);
		}else{
			holoderView=(HoloderView)convertView.getTag();
		}
		if(list.get(position)!=null){
			holoderView.tv_name.setText(list.get(position).getName());
			holoderView.tv_phone.setText(list.get(position).getMobile());
			holoderView.tv_relation.setText(list.get(position).getDbcVarchar1());
			holoderView.tv_note.setText(list.get(position).getComment());
		}
		
		return convertView;
	}

	class  HoloderView{
		TextView tv_name;//姓名
		TextView tv_phone;//手机号码
		TextView tv_relation;//关系
		TextView tv_note;//备注
	}
}
