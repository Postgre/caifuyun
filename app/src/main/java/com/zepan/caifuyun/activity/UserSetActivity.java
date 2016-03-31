package com.zepan.caifuyun.activity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.zepan.android.widget.IndicatorListView;
import com.zepan.android.widget.ShareView;
import com.zepan.android.widget.ShareView.onStatusListener;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;

import java.io.File;

import io.rong.imkit.RongIM;

//import android.content.pm.IPackageDataObserver;

//设置页面
public class UserSetActivity extends BaseActivity implements OnClickListener{

	private ShareView shareView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_set);
		setHeaderFields(0,R.string.settings,0,R.drawable.ic_action_back,0,0);
		initView();
		
	}
	private void initView() {
		// TODO Auto-generated method stub
		IndicatorListView listview=(IndicatorListView) findViewById(android.R.id.list);
		listview.setDivider(null);
		shareView = (ShareView)findViewById(R.id.shareView_bottom);
		shareView.setEnabled(true);
		shareView.setCurrentView(0);
		//设置遮罩阴影层点击消失该界面
		findViewById(R.id.view_mask_bottom).setOnClickListener(this);
		//设置分享界面状态监听
		shareView.setOnStatusListener(new onStatusListener() {

			@Override
			public void onShow() {
				//显示
				findViewById(R.id.view_mask_bottom).setVisibility(View.VISIBLE);
			}

			@Override
			public void onDismiss() {
				//隐藏
				findViewById(R.id.view_mask_bottom).setVisibility(View.GONE);
			}
		});
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0: //关于蜜蜂云
					Intent intent1=new Intent(UserSetActivity.this,UserAboutCaifuyunActivity.class);
					startActivity(intent1);
					break;
                case 1://新版本检测
                	showDialog();
					break;
                case 2://分享本应用
                	if(shareView.isShow()){
        				shareView.dismiss();
        			}else{
        				shareView.show();
        			}
	                break;
                case 3://意见反馈
                	Intent intent3=new Intent(UserSetActivity.this,UserFeedbackActivity.class);
					startActivity(intent3);
	                break;
                case 4://运营管理办法
	
                	Intent intent4=new Intent(UserSetActivity.this,UserRegulationActivity.class);
                	startActivity(intent4);
	                break;
                case 5://清楚缓存
                	
                	clearCache(UserSetActivity.this);
	                break;

				default:
					break;
				}
			}
		});
		findViewById(R.id.edit).setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			//阴影
		case R.id.view_mask_bottom:
			if(shareView.isShow()){
				shareView.dismiss();
			}
			break;
		case R.id.edit:
			exitMiFengYun();
			break;
	}
	}
	private void exitMiFengYun(){
		final AlertDialog dlg = new AlertDialog.Builder(this)
				.create();
				dlg.show();
				Window window = dlg.getWindow();
				window.setContentView(R.layout.dialog_alert);
				TextView messageView = (TextView) window.findViewById(R.id.tv_message);
				messageView.setText(getString(R.string.tv_dialog_title_logout));
				window.findViewById(R.id.tv_ok).setOnClickListener(
						new OnClickListener() {
							@SuppressWarnings("deprecation")
							@Override
							public void onClick(View v) {
								if (RongIM.getInstance() != null) RongIM.getInstance().disconnect(true);
								Intent intent = new Intent(UserSetActivity.this,
										LoginActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);
								dlg.dismiss();
							}
						});
				window.findViewById(R.id.tv_cancel).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								dlg.dismiss();
							}
						});
	}
	
	
		//清除缓存  
	    public void clearCache(final Context context) {  
	  
//	        try {
//	            PackageManager packageManager = context.getPackageManager();
//	            Method localMethod = packageManager.getClass().getMethod("freeStorageAndNotify", Long.TYPE,
//	                    IPackageDataObserver.class);
//	            Long localLong = Long.valueOf(getEnvironmentSize() - 1L);
//	            Object[] arrayOfObject = new Object[2];
//	            arrayOfObject[0] = localLong;
//	            localMethod.invoke(packageManager, localLong, new IPackageDataObserver.Stub() {
//
//	                @Override
//	                public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
//
//	                }
//
//	            });
//
//	            Toast.makeText(context, "清除缓存成功！", Toast.LENGTH_SHORT).show();
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	        }
	    }  
	  
	    private long getEnvironmentSize() {  
	        File localFile = Environment.getDataDirectory();  
	        long l1;  
	        if (localFile == null)  
	            l1 = 0L;  
	        while (true) {  
	            String str = localFile.getPath();  
	            StatFs localStatFs = new StatFs(str);  
	            long l2 = localStatFs.getBlockSize();  
	            l1 = localStatFs.getBlockCount() * l2;  
	            return l1;  
	        }  
	  
	    } 
	    
	      /**
	         *  检测版本的提示对话框
	         * 
	      */
	         public void showDialog(){
	             new AlertDialog.Builder(UserSetActivity.this)  
	             .setTitle("新版本")
	             .setMessage("当前版本是最新版本")
	             .setPositiveButton("确定", new DialogInterface.OnClickListener(){
	               public void onClick(DialogInterface dialog, int which) {
	                   dialog.dismiss();
	                   
	                }    
	            })
//	            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//	                public void onClick(DialogInterface dialog, int which) {
//	    
//	                }
//	            })
	            .show();
	        }

}
