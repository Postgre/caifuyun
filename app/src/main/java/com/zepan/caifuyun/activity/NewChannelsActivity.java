package com.zepan.caifuyun.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import com.zepan.android.sdk.CheckViewManager;
import com.zepan.android.sdk.IRequestCallBack;
import com.zepan.android.sdk.JsonUtil;
import com.zepan.android.util.StringUtil;
import com.zepan.android.widget.AchievePhotoImageView;
import com.zepan.android.widget.AchievePhotoImageView.OnPhotoDeleteCallBack;
import com.zepan.android.widget.AlphaImageView;
import com.zepan.android.widget.AlphaTextView;
import com.zepan.android.widget.CheckableEditText;
import com.zepan.android.widget.WheelTextView;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.application.MyApplication;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.constants.Url;
import com.zepan.caifuyun.entity.IdentityInfo;
import com.zepan.caifuyun.entity.ProfessionInfo;
import com.zepan.caifuyun.entity.User;
import com.zepan.caifuyun.widget.DateTimeDialog;
import com.zepan.caifuyun.widget.DateTimeDialog.PriorityListener;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
/**
 * 新建渠道    修改渠道资料
 * @author long
 *
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class NewChannelsActivity extends BaseActivity implements OnClickListener,OnPhotoDeleteCallBack,Callback {
	private int currentTag;
	private int channelType;
	private int id;
	private CheckViewManager  checkView=new  CheckViewManager();
	private TextView birthday;
	private String dateTime=StringUtil.longToFormatTimeUtcStr(System.currentTimeMillis()/1000+"", "yyyy-MM-dd");
	private static final int LABEL=0x100;
	
	private static final int UPDATEINDIVIDUAL=0x2001;
	private static final int NEWCHANNELINDIVIDUAL=0x2002;
	private static final int UPDATEORGANIZATION=0x2003;
	private static final int NEWCHANNELORGANIZATION=0x3001;
	private static final int UPDATECHANNELORGANIZATION=0x3002;
	private OSSBucket bucket= MyApplication.ossService.getOssBucket("canaanwealth");
	private Handler mHandler=new Handler(this);
	private static final int AVATAR=0x2004;
	private static final int DELETEAVATAR=0x2005;
	private static final int CARD=0x2006;
	private static final int DELETECARD=0x2007;
	private Map<Integer, String> pictureMap = new ArrayMap<Integer, String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_channels);
		initView();
		initData();
	}
	private void initView(){
		((AlphaImageView)findViewById(R.id.iv_left)).setClickAlpha(150, true, false);
		((AlphaTextView)findViewById(R.id.tv_right)).setClickAlpha(100);
		findViewById(R.id.tv_right).setOnClickListener(this);

		findViewById(R.id.tv_label).setOnClickListener(this);
		((AchievePhotoImageView)findViewById(R.id.ai_avatar)).setOnPhotoDeleteCallBack(this);
		((AchievePhotoImageView)findViewById(R.id.ai_card)).setOnPhotoDeleteCallBack(this);
		
		((LinearLayout)findViewById(R.id.layout_scale)).setVisibility(View.GONE);
		((LinearLayout)findViewById(R.id.layout_professional)).setVisibility(View.GONE);

	}
	public static int[] getYMDArray(String datetime, String splite) {
		int date[] = { 0, 0, 0, 0, 0 };
		if (datetime != null && datetime.length() > 0) {
			String[] dates = datetime.split(splite);
			int position = 0;
			for (String temp : dates) {
				date[position] = Integer.valueOf(temp);
				position++;
			}
		}
		return date;
	}
	private void initData(){
		Intent intent=getIntent();
		if(intent.hasExtra("newChannelOrganization")){//新建机构渠道
			currentTag=NEWCHANNELORGANIZATION;
			channelType=1;
			setHeaderFields(0, R.string.new_channel_organization, R.string.save,
					R.drawable.ic_action_back, 0,0);
			((LinearLayout)findViewById(R.id.ly_organization)).setVisibility(View.VISIBLE);//公司
			findViewById(R.id.line).setVisibility(View.GONE);
			
			((TextView)findViewById(R.id.tv_is_main)).setText("主要联系人");
			findViewById(R.id.ly_company).setVisibility(View.GONE);//公司
		}

		else if(intent.hasExtra("newChannelIndividualId")){//新建有客渠道
			currentTag=NEWCHANNELINDIVIDUAL;
			channelType=0;
			setHeaderFields(0, R.string.new_channel_individual, R.string.save,
					R.drawable.ic_action_back, 0,0);
			((LinearLayout)findViewById(R.id.ly_organization)).setVisibility(View.GONE);
			findViewById(R.id.line).setVisibility(View.VISIBLE);
			
			((TextView)findViewById(R.id.tv_is_main)).setText("姓名");
			findViewById(R.id.ly_company).setVisibility(View.VISIBLE);
		}
	}
	
	//创建机构渠道和有客渠道
	private void CreateChannels(){
		String url=Url.CreateChannels;
		request(url, getCreateChannelsJson(), Method.POST, new IRequestCallBack() {

			@Override
			public void onResponse(JSONObject response) {
				try {
					if(response.getInt("status")==0){
						printToast("创建成功");
					}else{
						printToast("创建失败");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finish();
			}

			@Override
			public void onErrorResponse(String errorMessage) {
				// TODO Auto-generated method stub

			}
		});
	}
	private JSONObject getCreateChannelsJson(){
		try {
			JSONObject json=new JSONObject();
			json.put("token",getStringLocalData("token"));
			
			JSONObject channelJson=new JSONObject();
			if(currentTag==NEWCHANNELORGANIZATION){//机构
				
					channelJson.put("company",((CheckableEditText)findViewById(R.id.ed_organization)).getText().toString());//机构名称
			
			}
			
            if(currentTag==NEWCHANNELINDIVIDUAL){//有客
                if(!TextUtils.isEmpty(((CheckableEditText)findViewById(R.id.et_company_name)).getText())){
    				channelJson.put("company",((CheckableEditText)findViewById(R.id.et_company_name)).getText().toString());//公司名称
    			}else{
    				channelJson.put("company","");//公司名称
    			}

			}
			channelJson.put("channelType", channelType);
			channelJson.put("isCertification", 1);
			channelJson.put("address","");
			if(pictureMap.get(R.id.ai_avatar)!=null){
				String avatarUrl=Url.PictureAddress+pictureMap.get(R.id.ai_avatar);
				channelJson.put("avatar",avatarUrl);//头像
			}else{
				channelJson.put("avatar","");//头像
			}

			
			if(!TextUtils.isEmpty(((TextView)findViewById(R.id.tv_label)).getText())){ //设置标签
				String lableString=((TextView)findViewById(R.id.tv_label)).getText().toString();
				String[] labelArray=lableString.split(" ");
				lableString="";
				for(int i=0;i<labelArray.length;i++){
					if(i==labelArray.length-1){
						lableString+=labelArray[i];
					}else{
						lableString+=labelArray[i]+",";
					}

				}
				json.put("tags",lableString);//标签
			}else{
				json.put("tags","");//标签
			}
			if(!TextUtils.isEmpty(((CheckableEditText)findViewById(R.id.ed_make_payment)).getText())){  //客户姓名
				channelJson.put("channelName", ((CheckableEditText)findViewById(R.id.ed_make_payment)).getText().toString());
			}else{
				channelJson.put("channelName","");//名称
			}

			
			if(!TextUtils.isEmpty(((CheckableEditText)findViewById(R.id.et_phone)).getText())){ //手机号
				channelJson.put("mobile",((CheckableEditText)findViewById(R.id.et_phone)).getText().toString());
			}else{
				channelJson.put("mobile","");//email
			}
			if(!TextUtils.isEmpty(((CheckableEditText)findViewById(R.id.et_email)).getText())){
				channelJson.put("email",((CheckableEditText)findViewById(R.id.et_email)).getText().toString());//email
			}else{
				channelJson.put("email","");//email
			}
            channelJson.put("address",((CheckableEditText)findViewById(R.id.et_address)).getText().toString());
			
			if(!TextUtils.isEmpty(((CheckableEditText)findViewById(R.id.et_position)).getText())){
				channelJson.put("post",((CheckableEditText)findViewById(R.id.et_position)).getText().toString());//公司职位
			}else{
				channelJson.put("post","");//公司职位
			}
			

			json.put("channelJson",channelJson);
			return json;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_right://保存
			if(currentTag==UPDATEINDIVIDUAL){
				if(((CheckableEditText)findViewById(R.id.ed_make_payment)).check()){
					//postModify();
				}
			}else if(currentTag==NEWCHANNELINDIVIDUAL){//有客
				if(((CheckableEditText)findViewById(R.id.ed_make_payment)).check()
						&&((CheckableEditText)findViewById(R.id.et_address)).check()){
					CreateChannels();
				}
			}else if(currentTag==NEWCHANNELORGANIZATION){//机构
				//必填项
				if(((CheckableEditText)findViewById(R.id.ed_organization)).check()
						&&((CheckableEditText)findViewById(R.id.ed_make_payment)).check()
						&&((CheckableEditText)findViewById(R.id.et_address)).check()){
					CreateChannels();
				}

			}
			else if(currentTag==UPDATEORGANIZATION){
				if(((CheckableEditText)findViewById(R.id.ed_organization)).check()
						&&((CheckableEditText)findViewById(R.id.ed_make_payment)).check()){
					//postModify();
				}

			}
			break;
		
		case R.id.tv_label://设置标签
			Intent intent=new Intent(this, LabelActivity.class);
			startActivityForResult(intent, LABEL);
			break;

		default:
			break;
		}

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data!=null){

			if(data.hasExtra("uri")){
				Uri uri = data.getParcelableExtra("uri");
				int viewId = data.getIntExtra("viewId",0);
				switch (viewId) {
				case R.id.ai_avatar:
					loadAndShowPiture(uri,R.id.ai_avatar);
					break;
				
				default:
					break;
				}
			}else{
				switch (requestCode) {
				case LABEL: 
					String label=data.getStringExtra("label");
					((TextView)findViewById(R.id.tv_label)).setText(label);

					break;

				default:
					break;
				}
			}



		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	private void loadAndShowPiture(Uri uri,int id){
		if (uri != null) {
			///data/data/com.zepan.caifuyun/cache/cropped
			File file = new File(StringUtil.getPath(this,uri));
			// Display the selected image on the image view.
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
					case R.id.ai_avatar:
						mHandler.sendEmptyMessage(AVATAR);
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
				switch (id) {
				case R.id.ai_avatar:
					mHandler.sendEmptyMessage(DELETEAVATAR);
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
		switch (viewId) {
		case R.id.ai_avatar:
			String objectKey1=pictureMap.get(R.id.ai_avatar);
			asyncDelete(objectKey1,R.id.ai_avatar);
			break;
		
		default:
			break;
		}

	}
	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case AVATAR:
			String avatarUrl=Url.PictureAddress+pictureMap.get(R.id.ai_avatar);
			((ImageView) findViewById(R.id.ai_avatar)).setBackground(null);
			((ImageView) findViewById(R.id.ai_avatar)).setImageURI(Uri.parse(avatarUrl));
			break;
		case DELETEAVATAR:
			pictureMap.put(R.id.ai_avatar, null);
			((ImageView) findViewById(R.id.ai_avatar)).setImageURI(null);
			((ImageView) findViewById(R.id.ai_avatar)).setBackgroundResource(R.drawable.ic_form_add);
			break;
		
		default:
			break;
		}
		return false;
	}

}
