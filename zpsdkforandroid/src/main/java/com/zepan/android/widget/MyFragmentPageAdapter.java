
/*
 * 文件名:MyFragmentPageAdapter.java
 * 包含类名列表:MyFragmentPageAdapter.java
 * 版本号:1.0
 * 创建日期:2014/12/1
 * Copyright (c) 2014 ZePan.Co.Ltd. All rights reserved.
 */   
  
package com.zepan.android.widget;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;


/** 
 * <请描述这个类是干什么的> 
 * @author  name 
 * @date:  2014-12-8 下午10:54:16 
 * @version:  V1.0 
 */

public class MyFragmentPageAdapter extends FragmentPagerAdapter {
	
	private static final String TAG = "MyFragmentPageAdapter";

	/**
	 * 待显示的Fragment的类集合，不直接存储对象是为了延迟实例化
	 * */
	private List<Class<? extends Fragment>> mFrmCls = new ArrayList<Class<? extends Fragment>>();

	/**
	 * @param fm
	 */
	public MyFragmentPageAdapter(FragmentManager fm) {
		super(fm);
	}

	/**
	 * @see FragmentPagerAdapter#getItem(int)
	 */
	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		try {
			return mFrmCls.get(position).newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 向当前ViewPager添加待显示的Fragment
	 * 
	 * @param frmCls
	 *            Fragment类数组
	 * */
	@SuppressWarnings("unchecked")
	public void fillFragments(Class<? extends Fragment>... frmCls) {
		if (frmCls == null || frmCls.length == 0) {
			Log.w(TAG, "未包含任何Fragment内容块");
		}
		for(Class<? extends Fragment> c : frmCls){
			mFrmCls.add(c);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mFrmCls.size();
	}
	
}

