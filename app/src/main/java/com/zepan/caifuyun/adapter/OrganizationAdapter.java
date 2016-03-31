package com.zepan.caifuyun.adapter;

import java.util.List;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.entity.User;
import com.zepan.caifuyun.util.StringUtil;

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
 * 机构的adapter
 * @author duanjie
 *
 */
public class OrganizationAdapter  extends BaseAdapter{
	private LayoutInflater inflater;
	private Context context;
	private List<User> list;//
	public OrganizationAdapter(Context context,List<User>  list){
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
			convertView=inflater.inflate(R.layout.listitem_organization_two, null);
			holoderView.tv_company_name=(TextView)convertView.findViewById(R.id.tv_company_name);
			holoderView.tv_relative_name=(TextView)convertView.findViewById(R.id.tv_relative_name);
			holoderView.iv_picture=(ImageView)convertView.findViewById(R.id.iv_company_head);
			holoderView.tv_maturity=((RatingBar)convertView.findViewById(R.id.rb_company_ratingBar1));
			convertView.setTag(holoderView);
		}else{
			holoderView=(HoloderView)convertView.getTag();
		}
		if(position<list.size()){
			if(list.get(position)!=null){
				if(list.get(position).getUserName()!=null){
					holoderView.tv_company_name.setText(list.get(position).getUserName());
				}
				if(list.get(position).getRelationUser()!=null){
					holoderView.tv_relative_name.setText(list.get(position).getRelationUser());
				}
				holoderView.tv_maturity.setRating(list.get(position).getMaturity());//成熟度
				if(StringUtil.isNullOrHttp(list.get(position).getAvatar())){
					Uri uri=Uri.parse(list.get(position).getAvatar());
					holoderView.iv_picture.setImageURI(uri);
				}else{
					holoderView.iv_picture.setImageResource(R.drawable.ic_default_institutions);
				}

			}

		}


		return convertView;
	}

	class  HoloderView{
		TextView tv_company_name;//公司名称
		TextView tv_relative_name;//联系人
		ImageView iv_picture;//职位
		RatingBar tv_maturity; //成熟度
	}
}
