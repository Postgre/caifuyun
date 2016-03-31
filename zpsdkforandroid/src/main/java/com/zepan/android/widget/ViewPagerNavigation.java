package com.zepan.android.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class ViewPagerNavigation extends LinearLayout implements OnClickListener, OnPageChangeListener{

	private static final String TAG = "ViewPagerNavigation";
	private ViewPager mViewPager = null;
	
	public ViewPagerNavigation(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 与ViewPager结合成一个整体，使得当前ViewPager导航滑动时，ViewPager的内容随之改变
	 * @param viewPager ViewPager
	 * */
	public void combine(ViewPager viewPager){
		if(viewPager == null){
			return;
		}
		mViewPager = viewPager;
		// 如果子View的数量与ViewPager中的页数不一致，弹出警告信息，处理完毕
		if(getChildCount() != viewPager.getAdapter().getCount()){
			Log.w(TAG, "ViewPager导航与页面数不对应");
			return;
		}
		// 遍历所有的子View并增加点击事件
		for(int i = 0;i < getChildCount(); i++){
			View view = getChildAt(i);
			if(view != null){
				//设置view的Id与ViewPager中的Page的Id一致
				view.setOnClickListener(this);
				//设置默认选中按钮
				view.setSelected(i == 0);
			}
		}
		// ViewPager页面切换时改变导航当前选中项
		viewPager.setOnPageChangeListener(this);
		
	}

	/**
	 * @see OnClickListener#onClick(View)
	 */
	@Override
	public void onClick(View view) {
		for(int i = 0;i < getChildCount(); i++){
			if(view == getChildAt(i)){
				navClicked(i);
			}
		}
	}
	
	private void navClicked(int position){
		mViewPager.setCurrentItem(position);
		for(int i = 0;i < getChildCount(); i++){
			getChildAt(i).setSelected(false);
		}
		getChildAt(position).setSelected(true);
	}
	
	/**
	 * @see OnPageChangeListener#onPageSelected(int)
	 */
	@Override
	public void onPageSelected(int position) {
//		navClicked(position);
//		if(mOnNavChangedListener != null){
//			mOnNavChangedListener.onNavSelected(position);
//		}
		getChildAt(position).performClick();
	}
	/**
	 * @see OnPageChangeListener#onPageScrollStateChanged(int)
	 */
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see OnPageChangeListener#onPageScrolled(int, float, int)
	 */
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	
	private OnNavChangedListener mOnNavChangedListener = null;
	public void setOnNavChangedListener(OnNavChangedListener listener){
		this.mOnNavChangedListener = listener;
	}
	public interface OnNavChangedListener{
		void onNavSelected(int position);
	}
}
