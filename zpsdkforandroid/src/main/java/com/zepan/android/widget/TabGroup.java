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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 底部菜单栏，继承LinearLayout，包含若干个Tab按钮，通常用于带有底部菜单的Activity，使用时嵌入Activity的
 * XML中。提供的接口attachContent(Fragment ...)可将tab按钮与具体的fragment（内容层）绑定起来。
 * 
 * @author zhanglei
 * @version 1.0
 * @date 2014-10-14
 */
public class TabGroup extends LinearLayout {

	private static final String TAG = "TabGroup";
	private FragmentManager frmMgr = null;
	private Fragment[] mStoredFragments = null;
	private int mContentResId = 0;
	/**
	 * 待显示的Fragment的类集合，不直接存储对象是为了延迟实例化
	 * */
	private List<Class<? extends Fragment>> mFrmCls = new ArrayList<Class<? extends Fragment>>();

	/**
	 * @param context
	 * @param attrs
	 */
	public TabGroup(Context context, AttributeSet attrs) {
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

	public Fragment getFragment(int index){
		return mStoredFragments[index];
	}
	
	public void setCurrentView(int index) {
		if (mStoredFragments[index] == null) {
			try {
				// instantiate the fragment by the class at the [index] of
				// showFrms
				mStoredFragments[index] = mFrmCls.get(index).newInstance();
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
		setSelected(getChildAt(index));
	}

	private void defineEvent() {
		for (int i = 0; i < getChildCount(); i++) {
			final View view = getChildAt(i);
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setCurrentView(getIndex(v));
				}
			});
		}
	}

	private void setSelected(View view) {
		for (int i = 0; i < getChildCount(); i++) {
			View v = getChildAt(i);
			v.setSelected(v == view);
		}
	}

	public int getSelectedIndex() {
		for (int i = 0; i < getChildCount(); i++) {
			if (getChildAt(i).isSelected()) {
				return i;
			}
		}
		return 0;
	}

	public int getIndex(View view) {
		for (int i = 0; i < getChildCount(); i++) {
			View v = getChildAt(i);
			if (v == view) {
				return i;
			}
		}
		return 0;
	}
}
