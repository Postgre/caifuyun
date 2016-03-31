package com.zepan.caifuyun.base;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.zepan.android.sdk.AsyncActivity;
import com.zepan.android.sdk.IRequestCallBack;
import com.zepan.caifuyun.application.MyApplication;
import com.zepan.caifuyun.widget.LoadingDialog;
/**
 * 自定义Activity,处理了此项目中所有activity的公共操作，如设置标题为空、返回按钮事件等
 * @author long
 *
 */
public class BaseActivity extends AsyncActivity{
	//protected LoadingDialog mLoading;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		MyApplication.getInstance().pushActivity(this);
	}
	public void printToast(String text){
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
	@Override
	protected void onStart() {

		super.onStart();
	}
	@Override
	protected void onStop() {
		super.onStop();
	}

	public static String getCurProcessName(Context context) {
		int pid = android.os.Process.myPid();
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
				.getRunningAppProcesses()) {
			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return null;
	}
	protected void stopLoading(){
		/*if(mLoading!=null){
			mLoading.dismiss();
		}*/
	}
	protected void createLoading(){
		/*if(mLoading==null){
			mLoading=new LoadingDialog(this);
		}*/
	}
	@Override
	protected void onDestroy() {
		/*if(mLoading!=null){
			mLoading.dismiss();
		}*/
		super.onDestroy();
	}
	/**重写请求成功的方法*/
	@Override
	protected void postSuccess(JSONObject response, IRequestCallBack callBack) {
		super.postSuccess(response, callBack);
		try {
			// 判断返回中的status是否为正常返回
			int value = response.getInt("status");
			// 如果是的话，则将返回值传递到子Activity
			if(0 == value){
				callBack.onResponse(response);
			}else{
				// 如果否的话，界面上显示错误信息
				Toast.makeText(this, getErrorInfo(value), Toast.LENGTH_LONG).show();
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected Dialog defineDialog() {
		return new LoadingDialog(this);
	}
}
