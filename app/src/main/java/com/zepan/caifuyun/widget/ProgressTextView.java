package com.zepan.caifuyun.widget;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.zepan.caifuyun.R;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

public class ProgressTextView extends TextView implements Callback {
	ExecutorService executor;
	Handler mHandler=new Handler(this);
	String endStr;//尾部字符串
	public ProgressTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		// 建立一个容量为5的固定尺寸的线程池   
		executor = Executors.newFixedThreadPool(5);   

	}
	public void setProgress(String text,final int max){
		endStr=text;
		executor.execute(new Runnable() {
			
			@Override
			public void run() {
				int progress=0;
				if(max!=0){
					while(progress <= max){
						progress += 1;
						Message msg=new Message();
						msg.what=0xa;
						msg.obj=progress;
						mHandler.sendMessage(msg);
						try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}else{
					Message msg=new Message();
					msg.what=0xa;
					msg.obj=progress;
					mHandler.sendMessage(msg);
				}
				
				
			}
		});   
	}
	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case 0xa:
			this.setText(msg.obj.toString()+endStr);
			break;

		default:
			break;
		}
		return false;
	}
 
}
