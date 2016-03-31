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
import com.zepan.android.widget.AlphaTextView;
import com.zepan.android.widget.CheckableEditText;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.application.MyApplication;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.constants.Url;

import android.R.integer;
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
 * 认证公司信息
 * @author duanjie
 *
 */
public class CertificationCpInfoActivity extends BaseActivity implements OnClickListener,OnPhotoDeleteCallBack, Callback{
	private String company;
	private OSSBucket bucket= MyApplication.ossService.getOssBucket("canaanwealth");
	
	private Map<Integer,String> pictureMap=new ArrayMap<Integer, String>();
	private Handler mHandler=new Handler(this);
	
	private static final int LICENSE=0x2004;
	private static final int DELETELICENSE=0x2005;
	private static final int CARD=0x2006;
	private static final int DELETECARD=0x2007;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_certification_cp_info);
		setHeaderFields(0,R.string.certification_cp_info,R.string.title_skip,
				R.drawable.ic_action_back, 0, 0);
		initData();
		initView();
	}

	private void initData() {
		Intent intent=getIntent();
		company=intent.getStringExtra("company");

	}

	private void initView() {
		((AlphaImageView)findViewById(R.id.iv_left)).setClickAlpha(150, true, false);
		((AlphaTextView)findViewById(R.id.tv_right)).setClickAlpha(100);
		((TextView)findViewById(R.id.et_company_name)).setText(company);
		((AlphaTextView)findViewById(R.id.tv_right)).setOnClickListener(this);
		findViewById(R.id.submit_certification).setOnClickListener(this);
		AchievePhotoImageView corporateImageView= (AchievePhotoImageView)findViewById(R.id.iv_corporate_charter);
		corporateImageView.setOnPhotoDeleteCallBack(this);
		((AchievePhotoImageView)findViewById(R.id.iv_legal_person_id_card)).setOnPhotoDeleteCallBack(this);
	}
	//ApplycertificateTenant


	private void  getApplycertificateTenant(){

		request(Url.ApplycertificateTenant,getApplycertificateTenantParamJson(), Method.POST,new IRequestCallBack() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				getApplycertificateTenantResult(response);
			}

			@Override
			public void onErrorResponse(String errorMessage) {
				// TODO Auto-generated method stub

			}
		});
	}



	private JSONObject getApplycertificateTenantParamJson(){
		JSONObject paramJson=new JSONObject();
		try {
				paramJson.put("tenantId",getStringLocalData("tenantId"));//公司id
				paramJson.put("tenantName",
						((TextView)findViewById(R.id.et_company_name)).getText().toString());//公司名字
				paramJson.put("orgCode",
						((CheckableEditText)findViewById(R.id.et_organization_code_name)).getText().toString());//机构代码
				paramJson.put("tenantAddress",
						((CheckableEditText)findViewById(R.id.et_company_address_name)).getText().toString());//公司地址
				paramJson.put("tenantTel",
						((CheckableEditText)findViewById(R.id.et_phone_company)).getText().toString());//公司电话
				if(pictureMap.get(R.id.iv_corporate_charter)!=null){
					paramJson.put("tenantLicenseImg",pictureMap.get(R.id.iv_corporate_charter));//公司执照
				}else{
					paramJson.put("tenantLicenseImg","");//公司执照
				}
				if(pictureMap.get(R.id.iv_legal_person_id_card)!=null){
					paramJson.put("legalPersonImg",pictureMap.get(R.id.iv_legal_person_id_card));//法人身份证
				}else{
					paramJson.put("legalPersonImg","");//法人身份证
				}
				paramJson.put("loginName",getStringLocalData("passport"));//手机号码loginName

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return paramJson;
	}


	private void getApplycertificateTenantResult(JSONObject response){
		try {
			if(response.getInt("status")==0){
				Intent intent=new Intent(CertificationCpInfoActivity.this,HomeActivity.class);
				startActivity(intent);
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
		case R.id.tv_right:
			Intent intent=new Intent(CertificationCpInfoActivity.this,HomeActivity.class);
			startActivity(intent);
			break;
		case R.id.submit_certification:
			if(!((CheckableEditText)findViewById(R.id.et_organization_code_name)).check()){
				return;
			}
			if(!((CheckableEditText)findViewById(R.id.et_company_address_name)).check()){
				return;
			}
			if(!((CheckableEditText)findViewById(R.id.et_phone_company)).check()){
				return;
			}
			if(pictureMap.get(R.id.iv_corporate_charter)==null){
				printToast("公司执照没有选择");
                return;
			}
			if(pictureMap.get(R.id.iv_legal_person_id_card)==null){
				printToast("法人身份证没有选择");
				return;
			}
			getApplycertificateTenant();
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
			case R.id.iv_corporate_charter:
				loadAndShowPiture(uri,R.id.iv_corporate_charter);
				break;
			case R.id.iv_legal_person_id_card:
				loadAndShowPiture(uri,R.id.iv_legal_person_id_card);
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
			//thumb/
			OSSFile bigfFile = MyApplication.ossService.getOssFile(bucket, id+"_"+System.currentTimeMillis()+"_cropped.jpg");
			bigfFile.enableUploadCheckMd5sum();
			bigfFile.setUploadFilePath(file.getPath(), "raw/binary");
			bigfFile.ResumableUploadInBackground(new SaveCallback() {

				@Override
				public void onSuccess(String objectKey) {
					pictureMap.put(id, objectKey);
					switch (id) {
					case R.id.iv_corporate_charter:
						mHandler.sendEmptyMessage(LICENSE);
						break;
					case R.id.iv_legal_person_id_card:
						mHandler.sendEmptyMessage(CARD);
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
                Log.d(TAG, "[onSuccess] - delete " + objectKey + " success!");
                switch (id) {
				case R.id.iv_corporate_charter:
					mHandler.sendEmptyMessage(DELETELICENSE);
					break;

                case R.id.iv_legal_person_id_card:
					mHandler.sendEmptyMessage(DELETECARD);
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
	public void deleteItemClick(int viewId, String imageUrl) {
		// TODO Auto-generated method stub
		switch (viewId) {
		case R.id.iv_corporate_charter:
			String key1=pictureMap.get(R.id.iv_corporate_charter);
			asyncDelete(key1,R.id.iv_corporate_charter);
			break;
		case R.id.iv_legal_person_id_card:
			String key2=pictureMap.get(R.id.iv_legal_person_id_card); 
			asyncDelete(key2,R.id.iv_legal_person_id_card);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch (msg.what) {
		case LICENSE:
			String licenseUrl=Url.PictureAddress+pictureMap.get(R.id.iv_corporate_charter);
			((ImageView)findViewById(R.id.iv_corporate_charter)).setImageURI(Uri.parse(licenseUrl));
			break;
		case CARD:
			String cardUrl=Url.PictureAddress+pictureMap.get(R.id.iv_legal_person_id_card);
			((ImageView)findViewById(R.id.iv_legal_person_id_card)).setImageURI(Uri.parse(cardUrl));
		    break;
		case DELETELICENSE:
			((ImageView) findViewById(R.id.iv_corporate_charter)).setImageURI(null);
			pictureMap.put(R.id.iv_corporate_charter, null);
			break;
		case DELETECARD:
			((ImageView) findViewById(R.id.iv_legal_person_id_card)).setImageURI(null);
			pictureMap.put(R.id.iv_legal_person_id_card, null);
			break;
		default:
			break;
		}
		return false;
	}


}
