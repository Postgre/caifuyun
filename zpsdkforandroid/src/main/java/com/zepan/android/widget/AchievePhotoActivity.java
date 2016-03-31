package com.zepan.android.widget;

import java.io.File;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;
import com.zepan.android.sdk.R;
import com.zepan.android.widget.AchievePhotoActivity;
import com.zepan.android.widget.AchievePhotoDialog;
import com.zepan.android.widget.AchievePhotoImageView;

/**
 * 拍照或相册中转的Activity，使用时请务必保证activity在AndroidManifest.xml中声明。
 * 
 * @see AchievePhotoDialog
 * @see AchievePhotoImageView
 * */
public class AchievePhotoActivity extends Activity {
	/**
	 * 布局文件，在xml中设置，如果不提供，默认使用dialog_achieve_photo.xml。
	 * 自定义xml中三个按钮的id必须与dialog_achieve_photo.xml保持一致。
	 * @see AchievePhotoImageView
	 * */
	private int layoutResId = 0;
	/**
	 * 裁剪框形状
	 * */
	private int cropShape = 1;
	/**
	 * 拍照存储位置
	 * */
	private Uri takeDestUri;
	/**
	 * 事件源的id
	 * */
	private int viewId;
	/**
	 * 裁剪存储位置
	 * */
	private Uri cropUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//setTheme(R.style.dialog_style01);
		super.onCreate(savedInstanceState);
		//this.setTheme(R.style.dialog_style01);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		cropShape = getIntent().getIntExtra("cropShape", 1);
		layoutResId = getIntent().getIntExtra("layoutResId", R.layout.dialog_achieve_photo);
		viewId = getIntent().getIntExtra("viewId", 0);
		setContentView(layoutResId);
		getWindow().setGravity(Gravity.BOTTOM);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// 设置属性值
		takeDestUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory() ,"taken.jpg"));
		cropUri = Uri.fromFile(new File(getCacheDir(), "cropped.jpg"));
		// 设置点击事件
		findViewById(R.id.take_photo).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Crop.takeImage(AchievePhotoActivity.this, takeDestUri);
					}
				});
		// 相册
		findViewById(R.id.select_photo).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Crop.pickImage(AchievePhotoActivity.this);
					}
				});
		// 取消
		findViewById(R.id.tv_cancel).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						AchievePhotoActivity.this.finish();
					}
				});
	}
	/**
	 * 接收拍照和选择照片的返回
	 * */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(RESULT_OK == resultCode){
			// 返回到之前Activity的uri
			Uri resultUri = null;
			// 存储裁剪后图片的uri
			if(requestCode == Crop.REQUEST_PICK){
				resultUri = data.getData();
			}else if(requestCode == Crop.REQUEST_TAKE){
				resultUri = takeDestUri;
			}else if (requestCode == Crop.REQUEST_CROP) {
				resultUri = Crop.getOutput(data);
				Intent intent = new Intent();
				intent.putExtra("uri", resultUri);
				intent.putExtra("viewId", viewId);
				this.setResult(requestCode, intent);
				this.finish();
				return;
			}
			// 如果没有裁剪框
			if(cropShape == 1){
				Intent intent = new Intent();
				intent.putExtra("viewId", viewId);
				intent.putExtra("uri", resultUri);
				this.setResult(requestCode, intent);
				this.finish();
				return;
			}else if(cropShape == 2){//如果是正文形裁剪框
				Crop.of(resultUri, cropUri).asSquare().start(this);
			}else if(cropShape == 3){//如果是长方形裁剪框
				Crop.of(resultUri, cropUri).start(this);
			}
		}else if (resultCode == Crop.RESULT_ERROR) {
			Toast.makeText(this, Crop.getError(data).getMessage(), Toast.LENGTH_SHORT).show();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
