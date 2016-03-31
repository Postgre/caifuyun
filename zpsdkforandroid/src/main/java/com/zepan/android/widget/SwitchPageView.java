/*
 * Copyright (C) 2014-10-14 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zepan.android.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

/**
 * 底部菜单栏，继承LinearLayout，包含2个按钮分别表示上一页和下一页，点击按钮可实现页面切换，使用时嵌入Activity的
 * XML中。提供的接口attachContent(Fragment ...)可将tab按钮与具体的fragment（内容层）绑定起来。
 * 注意：使用时请确保当前控件下有且只有2个直接控件－按钮。
 * @author zhanglei
 * @version 1.0
 * @date 2015-08-20
 */
public class SwitchPageView extends LinearLayout {

	private static final String TAG = "SwitchPageView";
	private FragmentManager frmMgr = null;
	private Fragment[] mStoredFragments = null;
	/**内容显示区域在XML中的资源id*/
	private int mContentResId = 0;
	/**记录当前显示页面的序号*/
	private int mPage = 0;
	/**
	 * 待显示的Fragment的类集合，不直接存储对象是为了延迟实例化
	 * */
	private List<Class<? extends Fragment>> mFrmCls = new ArrayList<Class<? extends Fragment>>();

	/**
	 * @param context
	 * @param attrs
	 */
	public SwitchPageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		FragmentActivity ac = (FragmentActivity) context;
		frmMgr = ac.getSupportFragmentManager();
	}

	public void attachContent(int contentId,
			Class<? extends Fragment>... frmCls) {
		if (contentId == 0) {
			Log.w(TAG, "invalid content id");
			return;
		}
		this.mContentResId = contentId;
		if (frmCls == null || frmCls.length == 0) {
			Log.w(TAG, "未包含任何Fragment内容块");
		}

		for (Class<? extends Fragment> c : frmCls) {
			mFrmCls.add(c);
		}
		mStoredFragments = new Fragment[mFrmCls.size()];
		defineEvent();
	}
	
	public void setCurrentView(int index) {
		// 如果当前是第一页，则只显示“下一页”控钮
		if(index == 0){
			getChildAt(0).setVisibility(View.GONE);
		}else if(index == mFrmCls.size() - 1){//如果是最后一条，则只显示“上一页”按钮
			getChildAt(1).setVisibility(View.GONE);
		}else{
			getChildAt(0).setVisibility(View.VISIBLE);
			getChildAt(1).setVisibility(View.VISIBLE);
		}
		// 切换Fragment
		if (mStoredFragments[index] == null) {
			try {
				// instantiate the fragment by the class at the [index] of
				// showFrms
				mStoredFragments[index] = mFrmCls.get(index).newInstance();
				// 构建时往Fragment传入序号，有时候会用到
				Bundle data = new Bundle();
				data.putInt("index", index);
				mStoredFragments[index].setArguments(data);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// get a fragment transaction and store it.
		FragmentTransaction trac = frmMgr.beginTransaction().setCustomAnimations(
                android.R.anim.fade_in, android.R.anim.fade_out);
		Fragment currentFragment = mStoredFragments[index];

		if (currentFragment != null) {
			
			// if the fragment is not added,add it to layout,else show it.
			if (!currentFragment.isAdded()) {
				trac.add(mContentResId, currentFragment);
			} else {
				trac.show(currentFragment);
			}
		}
		// hide other fragments.
		for (int i = 0; i < mStoredFragments.length; i++) {
			if (i != index && mStoredFragments[i] != null) {
				trac.hide(mStoredFragments[i]);
			}
		}
//		trac.addToBackStack(null);
		trac.commit();
		
	}
	
	public Fragment getFragment(int index){
		return mStoredFragments[index];
	}
	
	private void defineEvent() {
		for (int i = 0; i < getChildCount(); i++) {
			final View view = getChildAt(i);
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(v == getChildAt(0)){
						if(--mPage < 0){
							mPage = 0;
						}
						setCurrentView(mPage);
					}else{
						if(++mPage > mFrmCls.size() - 1){
							mPage = mFrmCls.size() - 1;
						}
						setCurrentView(mPage);
					}
				}
			});
		}
	}
	
	/**获取当前显示的页序号，从0开始*/
	public int getPage(){
		return mPage;
	}

	/**获取当前显示的fragment*/
	public Fragment getCurrentFragment(){
		return getFragment(this.mPage);
	}
}
