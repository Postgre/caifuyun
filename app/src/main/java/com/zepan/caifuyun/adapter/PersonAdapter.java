package com.zepan.caifuyun.adapter;

import java.util.List;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.entity.User;
import com.zepan.caifuyun.util.StringUtil;
import com.zepan.caifuyun.entity.User;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
/**
 * 个人 adapter
 * @author duanjie
 *
 */
public class PersonAdapter  extends BaseAdapter{
	private LayoutInflater inflater;
	private Context context;
	private List<User> list;//个人

	public PersonAdapter(Context context,List<User>  list){
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
	public User getItem(int position) {
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
			convertView=inflater.inflate(R.layout.listitem_personal_two, null);
			holoderView.tv_name=(TextView)convertView.findViewById(R.id.tv_person_name);
			holoderView.tv_position=(TextView)convertView.findViewById(R.id.tv_person_position);
			holoderView.tv_company=(TextView)convertView.findViewById(R.id.tv_person_company);
			holoderView.iv_head=(ImageView)convertView.findViewById(R.id.iv_person_head);
			holoderView.tv_maturity=((RatingBar)convertView.findViewById(R.id.rb_person_ratingBar1));
			convertView.setTag(holoderView);
		}else{
			holoderView=(HoloderView)convertView.getTag();
		}
		if(list.get(position)!=null){
			if(list.get(position).getUserName()!=null){
				holoderView.tv_name.setText(list.get(position).getUserName());
			}
			if(list.get(position).getProfessionalInformation()!=null&&list.get(position).getProfessionalInformation().getCompany()!=null){
				holoderView.tv_company.setText(list.get(position).getProfessionalInformation().getCompany());
			}
			if(list.get(position).getProfessionalInformation()!=null&&list.get(position).getProfessionalInformation().getPosition()!=null){
				holoderView.tv_position.setText(list.get(position).getProfessionalInformation().getPosition());
			}
			holoderView.tv_maturity.setRating(list.get(position).getMaturity());//成熟度
			if(StringUtil.isNullOrHttp(list.get(position).getAvatar())){
				Uri uri=Uri.parse(list.get(position).getAvatar());
				holoderView.iv_head.setImageURI(uri);
			}else{
				holoderView.iv_head.setImageResource(R.drawable.ic_default_head);
			}
		}

		return convertView;
	}

	class  HoloderView{
		TextView tv_name;//姓名
		TextView tv_position;//职位
		TextView  tv_company;//公司
		ImageView iv_head;//头像
		RatingBar tv_maturity; //成熟度
	}
}
