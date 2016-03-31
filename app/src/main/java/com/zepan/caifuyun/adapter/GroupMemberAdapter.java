package com.zepan.caifuyun.adapter;

import java.util.List;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.adapter.GroupContactsAdapter.ViewHolder;
import com.zepan.caifuyun.entity.UserInfo;
import com.zepan.caifuyun.util.StringUtil;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class GroupMemberAdapter extends BaseAdapter {

	private Context context;
	private List<UserInfo> list;
	public GroupMemberAdapter(Context context,List<UserInfo> list) {
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
		return list == null ? null : list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.gridview_item, null);
			holder.avatar=(ImageView) convertView.findViewById(R.id.avatar);
			holder.name=(TextView) convertView.findViewById(R.id.name);
			holder.delete=(ImageView) convertView.findViewById(R.id.iv_delete2);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		if(position==list.size()-2){
			if(list.get(position).getUser_id()!=null){
				defaultShow(position,holder);
			}else{
				holder.avatar.setImageResource(R.drawable.add);
				holder.delete.setVisibility(View.GONE);
				holder.name.setText("");
			}
		}else if(position==list.size()-1){
			if(list.get(position).getUser_id()!=null){
				holder.avatar.setImageResource(R.drawable.delete);
				holder.delete.setVisibility(View.GONE);
				holder.name.setText("");
			}else{
				holder.avatar.setImageResource(R.drawable.add);
				holder.delete.setVisibility(View.GONE);
				holder.name.setText("");
			}
			
		}else{
			defaultShow(position,holder);
		}
		return convertView;
	}
	private void defaultShow(final int position,ViewHolder holder){
		if(list.get(position).isDelete()){
			holder.delete.setVisibility(View.VISIBLE);
		}else{
			holder.delete.setVisibility(View.GONE);
		}
		holder.delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(deleteUser!=null){
					deleteUser.deleteUser(position);
				}
			}
		});
		if(StringUtil.isNullOrHttp(list.get(position).getUser_img())){
			Uri uri=Uri.parse(list.get(position).getUser_img());
			holder.avatar.setImageURI(uri);
		}else{
			holder.avatar.setImageResource(R.drawable.ic_default_head);
		}
		holder.name.setText(list.get(position).getUser_name());
	}

	class ViewHolder{
		ImageView avatar;//头像
		TextView name; //名字
		ImageView delete;//删除
	}
	public interface DeleteUser{
		void deleteUser(int position);
	}
	private DeleteUser deleteUser;
	public  void setOnDeleteUser(DeleteUser deleteUser){
		this.deleteUser=deleteUser;
	}
	
	
}
