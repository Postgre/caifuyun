package com.zepan.caifuyun.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.sdk.android.oss.callback.DeleteCallback;
import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.alibaba.sdk.android.oss.storage.OSSBucket;
import com.alibaba.sdk.android.oss.storage.OSSData;
import com.alibaba.sdk.android.oss.storage.OSSFile;
import com.android.volley.Request.Method;
import com.zepan.android.sdk.IRequestCallBack;
import com.zepan.android.util.FileUtil;
import com.zepan.android.util.StringUtil;
import com.zepan.android.widget.AchievePhotoImageView;
import com.zepan.android.widget.AchievePhotoImageView.OnPhotoDeleteCallBack;
import com.zepan.android.widget.AlphaImageView;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.application.MyApplication;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.constants.Url;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 认证的第二个界面  申请认证
 * @author duanjie
 *
 */
public class CertificationTwoActivity extends BaseActivity implements OnClickListener,Callback,OnPhotoDeleteCallBack{
	private String name,loginName,company_name;
	private TextView tv_name,tv_company_name,tv_mobile_phone;
	private String companyId,companyName;
	///private String identityCardUrl,headshotUrl;
	private OSSBucket bucket= MyApplication.ossService.getOssBucket("canaanwealth");
	private Map<Integer, String> pictureMap = new ArrayMap<Integer, String>();
	private Handler mHandler=new Handler(this);
    private static final int DELETEHEAD=0x2005;
	private static final int DELETECARD=0x2007;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_certification_two);
		setHeaderFields(0, R.string.tv_certification,0,
				R.drawable.ic_action_back, 0, 0);
		initData();
		initView();

	}

	private void initData() {
		Intent intent=getIntent();
		companyId=intent.getStringExtra("companyId");
		companyName=intent.getStringExtra("companyName");

	}

	private void initView() {
		((AlphaImageView)findViewById(R.id.iv_left)).setClickAlpha(150, true, false);
		((TextView)findViewById(R.id.et_company_name)).setText(companyName); 
		tv_mobile_phone=(TextView)findViewById(R.id.et_mobile_phone);
		findViewById(R.id.submit_report).setOnClickListener(this);

		((TextView)findViewById(R.id.et_name)).setText(getStringLocalData("name"));
		((TextView)findViewById(R.id.et_mobile_phone)).setText(getStringLocalData("passport"));
		loginName=tv_mobile_phone.getText().toString();
		((AchievePhotoImageView)findViewById(R.id.iv_identity_card_photo)).setOnPhotoDeleteCallBack(this);

	}




	private void  getApplyJoinTenant(){

		request(Url.ApplyJoinTenant,getApplyJoinTenantParamJson(), Method.POST,new IRequestCallBack() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				getApplyJoinTenantResult(response);
			}

			@Override
			public void onErrorResponse(String errorMessage) {
				// TODO Auto-generated method stub

			}
		});
	}

	private JSONObject getApplyJoinTenantParamJson(){
		JSONObject paramJson=new JSONObject();
		try {
				paramJson.put("tenantId",companyId);//公司id
				paramJson.put("token",getStringLocalData("token"));//用户token
				paramJson.put("loginName",getStringLocalData("passport"));//手机号码loginName
				paramJson.put("name",getStringLocalData("name"));//姓名name
				if(pictureMap.get(R.id.iv_identity_card_photo)!=null
						&&!pictureMap.get(R.id.iv_identity_card_photo).equals("")){
					String cardUrl=Url.PictureAddress+pictureMap.get(R.id.iv_identity_card_photo);
					paramJson.put("idCard",cardUrl);//身份证照片（正面）
				}else{
					paramJson.put("idCard","");//身份证照片（正面）
				}
				
				if(pictureMap.get(R.id.iv_headshot)!=null
						&&!pictureMap.get(R.id.iv_headshot).equals("")){
					String headUrl=Url.PictureAddress+pictureMap.get(R.id.iv_headshot);
					paramJson.put("face",headUrl);//面部照片
				}else{
					paramJson.put("face","");//面部照片
				}
				
				
				paramJson.put("uid",getStringLocalData("uid")); 

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return paramJson;
	}



	private void getApplyJoinTenantResult(JSONObject response){
		try {
			if(response.getInt("status")==0){
				Intent ApplyJoinIntent=new Intent(CertificationTwoActivity.this,HomeActivity.class);
				startActivity(ApplyJoinIntent);
			}else if(response.getInt("status")==1){
				String message=response.getString("message");
				printToast(message);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submit_report:
			if(pictureMap.get(R.id.iv_identity_card_photo)==null){
				printToast("身份证没有选择");
				return;
			}
			if(pictureMap.get(R.id.iv_headshot)==null){
				printToast("大头照没有选择");
				return;
			}
			getApplyJoinTenant();
			break;

		default:
			break;
		}

	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub<br>
		if(data != null){
			Uri uri = data.getParcelableExtra("uri");
			int viewId = data.getIntExtra("viewId",0);
			switch (viewId) {
			case R.id.iv_identity_card_photo:
				loadAndShowPiture(uri,R.id.iv_identity_card_photo);
				break;
			case R.id.iv_headshot:
				loadAndShowPiture(uri,R.id.iv_headshot);
				break;
			default:
				break;
			}


		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	private void loadAndShowPiture(Uri uri,int id){
		if (uri != null) {
			///data/data/com.zepan.caifuyun/cache/cropped
			File file = new File(StringUtil.getPath(this,uri));
			
			if (file != null && file.exists()) {
				asynUpload(file,id);
			}
		}
	}
	// 断点上传
	public void asynUpload(File file,final int id) {
		try {
			OSSFile bigfFile = MyApplication.ossService.getOssFile(bucket, id+"_"+System.currentTimeMillis()+"_cropped.jpg");
			bigfFile.enableUploadCheckMd5sum();
			bigfFile.setUploadFilePath(file.getPath(), "raw/binary");
			bigfFile.ResumableUploadInBackground(new SaveCallback() {

				@Override
				public void onSuccess(String objectKey) {
					pictureMap.put(id, objectKey);
					switch (id) {
					case R.id.iv_identity_card_photo:
						mHandler.sendEmptyMessage(R.id.iv_identity_card_photo);
						break;
					case R.id.iv_headshot:
						mHandler.sendEmptyMessage(R.id.iv_headshot);
						break;
					default:
						break;
					}
				}

				@Override
				public void onProgress(String objectKey, int byteCount, int totalSize) {
					Log.d(TAG, "[onProgress] - current upload " + objectKey + " bytes: " + byteCount + " in total: " + totalSize);
				}

				@Override
				public void onFailure(String objectKey, OSSException ossException) {
				    printToast("上传照片失败");
					Log.e(TAG, "[onFailure] - upload " + objectKey + " failed!\n" + ossException.toString());
					ossException.printStackTrace();
					ossException.getException().printStackTrace();
				}
			});
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	// 异步删除数据
    public void asyncDelete(String objectKey,final int id) {
        OSSData data = MyApplication.ossService.getOssData(bucket,objectKey);
        data.deleteInBackground(new DeleteCallback() {

            @Override
            public void onSuccess(String objectKey) {
               switch (id) {
               case R.id.iv_identity_card_photo:
       			mHandler.sendEmptyMessage(DELETECARD);
       			break;
       		case R.id.iv_headshot:
       			mHandler.sendEmptyMessage(DELETEHEAD);
       			break;

			default:
				break;
			}
            }

            @Override
            public void onFailure(String objectKey, OSSException ossException) {
                Log.e(TAG, "[onFailure] - delete " + objectKey + " failed!\n" + ossException.toString());
               // HandleException.handleException(ossException);
            }
        });
    }

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case R.id.iv_identity_card_photo:
			String identityCard=Url.PictureAddress+pictureMap.get(R.id.iv_identity_card_photo);
			((ImageView)findViewById(R.id.iv_identity_card_photo)).setImageURI(Uri.parse(identityCard));
			break;
		case R.id.iv_headshot:
			String headShot=Url.PictureAddress+pictureMap.get(R.id.iv_headshot);
			((ImageView)findViewById(R.id.iv_headshot)).setImageURI(Uri.parse(headShot));
			break;
		case DELETECARD:
			pictureMap.put(R.id.iv_identity_card_photo, null);
			((ImageView)findViewById(R.id.iv_identity_card_photo)).setImageURI(null);
			break;
		case DELETEHEAD:
			pictureMap.put(R.id.iv_headshot, null);
			((ImageView)findViewById(R.id.iv_headshot)).setImageURI(null);
			break;

		default:
			break;
		}
		return false;
	}

	@Override
	public void deleteItemClick(int viewId, String imageUrl) {
		switch (viewId) {
		case R.id.iv_identity_card_photo:
			String objectKey1=pictureMap.get(R.id.iv_identity_card_photo);
			asyncDelete(objectKey1,R.id.iv_identity_card_photo);
			break;
		case R.id.iv_headshot:
			String objectKey2=pictureMap.get(R.id.iv_headshot);
			asyncDelete(objectKey2,R.id.iv_headshot);
			break;

		default:
			break;
		}
		
	}

}
