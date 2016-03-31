package com.zepan.caifuyun.adapter;

import java.io.IOException;
import java.util.List;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zepan.android.util.StringUtil;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.entity.Record;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/***
 * 跟踪纪录adapter
 * @author long
 *
 */

public class RecordAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private Context context;
	private List<Record> list;//Dynamic  Dynamic
	public RecordAdapter(Context context,List<Record>  list){
		this.inflater=LayoutInflater.from(context);
		this.list=list;
		this.context=context;
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Record getItem(int position) {
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
			convertView=inflater.inflate(R.layout.listitem_individualcustomer, null);
			holder.tv_content=(TextView)convertView.findViewById(R.id.tv_item_content);
			holder.ly_picture=(LinearLayout)convertView.findViewById(R.id.ly_item_picture);
			holder.iv_delete=(ImageView)convertView.findViewById(R.id.iv_item_delete);
			holder.fy_voice=(FrameLayout)convertView.findViewById(R.id.fy_item_voice);
			holder.tv_voice=(TextView)convertView.findViewById(R.id.tv_item_voice);
			holder.iv_voice_animation=(ImageView)convertView.findViewById(R.id.iv_item_voice_animation);
			holder.iv_voice_hide=(ImageView)convertView.findViewById(R.id.iv_item_voice_hide);
			holder.tv_gps_adress=(TextView)convertView.findViewById(R.id.tv_item_gps_adress);
			holder.tv_item_time=(TextView)convertView.findViewById(R.id.tv_item_time);
			convertView.setTag(holder);
		}else {
			holder=(HolderView)convertView.getTag();
		}
		if(list.get(position)!=null){
			holder.tv_content.setText(list.get(position).getContent());
			if(list.get(position).getImgFilesList()!=null){
				if(holder.ly_picture.getChildCount()>0){
					holder.ly_picture.removeAllViews();
				}
				for(String fileStr :list.get(position).getImgFilesList()){
					SimpleDraweeView imageView=new SimpleDraweeView(context);
					imageView.setPadding(30, 30, 0, 0);
					imageView.setLayoutParams(new LinearLayout.LayoutParams(250,250));
					imageView.setImageURI(Uri.parse(fileStr));
					holder.ly_picture.addView(imageView);
				}
			}
			final ImageView hideView=holder.iv_voice_hide;
			final ImageView voiceAnimationView=holder.iv_voice_animation;
			if(list.get(position).getSoundUrl()!=null&&!list.get(position).getSoundUrl().equals("")){
				holder.fy_voice.setVisibility(View.VISIBLE);
				holder.tv_voice.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						//播放声音
						try {
							MediaPlayer mPlayer = new MediaPlayer();
							voiceAnimationView.setVisibility(View.VISIBLE);
							final AnimationDrawable animationDrawable = (AnimationDrawable)
									voiceAnimationView.getBackground();
				            hideView.setVisibility(View.GONE);
							mPlayer.setDataSource(list.get(position).getSoundUrl());
							mPlayer.prepare();
							mPlayer.start();
							animationDrawable.start();
							mPlayer.setOnCompletionListener(new OnCompletionListener() {

								@Override
								public void onCompletion(MediaPlayer mp) {
									animationDrawable.stop();
									voiceAnimationView.setVisibility(View.GONE);
									hideView.setVisibility(View.VISIBLE);
								}
							});
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});


			}else{
				holder.fy_voice.setVisibility(View.GONE);
			}
			if(list.get(position).getAccountName()!=null&&!list.get(position).getAccountName().equals("")){
				holder.tv_gps_adress.setVisibility(View.VISIBLE);
				holder.tv_gps_adress.setText(list.get(position).getAccountName());
			}else{
				holder.tv_gps_adress.setVisibility(View.GONE);
			}
			holder.iv_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(deleteRecord!=null){
						deleteRecord.deleteRecord(list.get(position).getId());
					}
				}
			});
			if(list.get(position).getCreatedAt()!=0){
				String time = StringUtil.longToFormatTimeStr(list.get(position).getCreatedAt()+"", "yyyy-MM-dd");
				holder.tv_item_time.setText(time);
			}
		}
		return convertView;
	}
	class HolderView{
		TextView tv_content;//纪录名称
		LinearLayout ly_picture;//图片
		ImageView iv_delete;
		FrameLayout fy_voice;
		TextView tv_voice;//
		ImageView iv_voice_animation;
		ImageView iv_voice_hide;
		TextView tv_gps_adress;
		TextView tv_item_time;
	}
	public interface DeleteRecord{
		void deleteRecord(int id);
	}
	private DeleteRecord deleteRecord;
	public void setDeleteRecord(DeleteRecord deleteRecord){
		this.deleteRecord=deleteRecord;
	}


}
