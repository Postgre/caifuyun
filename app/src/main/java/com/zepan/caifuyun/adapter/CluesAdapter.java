package com.zepan.caifuyun.adapter;

import java.util.List;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.entity.Clues;

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
public class CluesAdapter  extends BaseAdapter{
	private LayoutInflater inflater;
	private Context context;
	private List<Clues> list;//线索
	public CluesAdapter(Context context,List<Clues>  list){
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
	public Clues getItem(int position) {
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
			convertView=inflater.inflate(R.layout.listitem_clues, null);
			holoderView.tv_name=(TextView)convertView.findViewById(R.id.tv_name);
			holoderView.tv_phone=(TextView)convertView.findViewById(R.id.tv_phone);
			holoderView.tv_position=(TextView)convertView.findViewById(R.id.tv_position);
			holoderView.tv_company=(TextView)convertView.findViewById(R.id.tv_company);
			holoderView.tv_tags=(TextView)convertView.findViewById(R.id.tv_tags);
			holoderView.tv_source=(TextView)convertView.findViewById(R.id.tv_source);
			holoderView.iv_star=(CheckBox)convertView.findViewById(R.id.iv_star);
			convertView.setTag(holoderView);
		}else{
			holoderView=(HoloderView)convertView.getTag();
		}
		holoderView.tv_name.setText(list.get(position).getName());
		holoderView.tv_phone.setText(list.get(position).getMobile());
		holoderView.tv_position.setText(list.get(position).getPosition());
		holoderView.tv_company.setText(list.get(position).getCompany());
		holoderView.tv_source.setText(list.get(position).getSource());//线索来源
		String lableString="";
		if(list.get(position).getTags()!=null){
			for (int i=0;i<list.get(position).getTags().size();i++) { 
				if(i==list.get(position).getTags().size()-1){
					lableString+=list.get(position).getTags().get(i).getName();
				}else{
					lableString+=list.get(position).getTags().get(i).getName()+" ";
				}
			} 
			holoderView.tv_tags.setText(lableString); 
		}
		//holoderView.iv_star.setText(list.get(position).getStatus()+"");//星星
		if((list.get(position).getStatus()==2)){   //Status   1  是普通线索不亮      ic_star_small
			holoderView.iv_star.setBackgroundResource(R.drawable.ic_star_small_marked);  //亮
			//线索状态更新接口
		}else {
			holoderView.iv_star.setBackgroundResource(R.drawable.ic_star_small);//不亮
		}

		return convertView;
	}

	class  HoloderView{
		TextView tv_name;//姓名
		TextView tv_phone;//手机号码
		TextView tv_position;//职位
		TextView  tv_company;//公司
		TextView tv_tags;//标签
		TextView tv_source;//线索来源
		CheckBox iv_star;  //判断星星是否变亮
	}
}
