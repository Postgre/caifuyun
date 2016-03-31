package com.zepan.caifuyun.adapter;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zepan.caifuyun.entity.Picture;

import android.content.Context;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;



public class SlidePictureAdapter extends PagerAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<Picture> imgList;

	public SlidePictureAdapter(Context context,List<Picture> imgList){
		this.context=context;
		this.inflater=LayoutInflater.from(context);
		this.imgList=imgList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imgList.size();
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {
		SimpleDraweeView imageView = new SimpleDraweeView(context);
		imageView.setTag(imgList.get(arg1).getProductPicture());
		imageView.setScaleType(ScaleType.FIT_XY);
		if(imgList.get(arg1)!=null&&imgList.get(arg1).getProductPicture()!=null&&!"".equals(imgList.get(arg1).getProductPicture())){
			String decodeUrl;
			try {
				decodeUrl = java.net.URLDecoder.decode(imgList.get(arg1).getProductPicture(),"utf-8");
				if(!TextUtils.isEmpty(decodeUrl)){
					imageView.setImageURI(Uri.parse(decodeUrl));
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		((ViewPager) arg0).addView(imageView);
		return imageView;
	}
	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {

	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {

	}

	@Override
	public void finishUpdate(View arg0) {

	}

	
}
