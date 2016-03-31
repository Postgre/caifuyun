package com.zepan.caifuyun.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
//com.zepan.caifuyun.widget.TransFrameLayout
public class TransFrameLayout extends FrameLayout{
	public TransFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.getBackground().setAlpha(50);
	}

}
