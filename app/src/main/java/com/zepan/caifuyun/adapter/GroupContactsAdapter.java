package com.zepan.caifuyun.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.entity.UserInfo;
import com.zepan.caifuyun.util.StringUtil;

//群聊的个人通讯录适配器
public class GroupContactsAdapter extends BaseAdapter {

	private Context context;
	private List<UserInfo> list;
	/**存储选择的用户列表*/
	private List<UserInfo> selectedUserList = new ArrayList<UserInfo>();
	public GroupContactsAdapter(Context context, List<UserInfo> list) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list = list;
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
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.listitem_message_group_contacts, null);
			holder.tv_letter = (TextView) convertView
					.findViewById(R.id.first_letter);
			holder.tv_name = (TextView) convertView.findViewById(R.id.name);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.checkbox = (CheckBox) convertView
					.findViewById(R.id.checkbox);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.checkbox
		.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					list.get(position).setChecked(true);
				} else {
					list.get(position).setChecked(false);
				}
				if (userSelectedListener != null) {
					selectedUserList.clear();
					for(UserInfo userInfo:list){
						if(userInfo.isChecked()){
							selectedUserList.add(userInfo);
						}
					}
					userSelectedListener.onSelected(selectedUserList);
				}
			}
		});
		if(!list.get(position).isClick()){
			holder.checkbox.setVisibility(View.GONE);
		}else{
			holder.checkbox.setVisibility(View.VISIBLE);
		}

		if (list.get(position).getLabel() != null) {
			holder.tv_letter.setText(list.get(position).getLabel());

		} else {
			holder.tv_letter.setVisibility(View.GONE);
		}
		holder.checkbox.setChecked(list.get(position).isChecked());

		holder.tv_name.setText(list.get(position).getUser_name());
		if(StringUtil.isNullOrHttp(list.get(position).getUser_img())){
			holder.image.setImageURI(Uri.parse(list.get(position).getUser_img()));
		}else{
			holder.image.setImageResource(R.drawable.ic_default_head);
		}
		

		return convertView;
	}

	class ViewHolder {
		TextView tv_letter;
		ImageView image;
		TextView tv_name;
		CheckBox checkbox;
	}

	// 回调接口
	public interface OnUserSelectedListener {
		/**
		 * @param userList 选择的用户列表
		 * @see UserInfo
		 * */
		void onSelected(List<UserInfo> userList);
	}

	private OnUserSelectedListener userSelectedListener = null;

	public void setOnUserSelectedListener(OnUserSelectedListener listener) {
		this.userSelectedListener = listener;
	}
}
