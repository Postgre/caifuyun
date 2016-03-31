package com.zepan.caifuyun.adapter;

import java.util.List;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.cache.HistoryDBManager;
import com.zepan.caifuyun.entity.History;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class HistoryAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private Context context;
	private List<History> list;//Dynamic  Dynamic
	public HistoryAdapter(Context context,List<History>  list){
		this.inflater=LayoutInflater.from(context);
		this.list=list;
		this.context=context;
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public History getItem(int position) {
		// TODO Auto-generated method stub
		return list == null ? null : list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public View getView(final int position,View convertView, ViewGroup parent) {
		HolderView holder=null;
		if(convertView==null){
			holder=new HolderView();
			convertView=inflater.inflate(R.layout.listitem_search_history, null);
			holder.tv_name=(TextView)convertView.findViewById(R.id.tv_search_history);
			holder.iv_delete=(ImageView)convertView.findViewById(R.id.iv_delete);
			convertView.setTag(holder);
		}else {
			holder=(HolderView)convertView.getTag();
		}
		if(list.get(position).getText()!=null){
			holder.tv_name.setText(list.get(position).getText());
		}
		
		holder.iv_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mOnDeleteListener!=null){
					mOnDeleteListener.refreshHotWordDBManager(list.get(position).getId());
				}
				
			}
		});
		return convertView;
	}
	class HolderView{
		TextView tv_name;//名字
		TextView product_name;//产品的 名字
		TextView tv_relation;//关注或者好友关系
		ImageView  iv_delete;//
		TextView tv_content;//回复的内容
		TextView tv_time;//时间
		TextView tv_reply_num;//回复数
		TextView tv_praise_num;//点赞的数
	}
	public interface onDeleteListener{
		void refreshHotWordDBManager(String id);   
	}
	private onDeleteListener mOnDeleteListener=null;
	public void setOnDeleteListener(onDeleteListener mOnDeleteListener){
		this.mOnDeleteListener=mOnDeleteListener;
	}

}
