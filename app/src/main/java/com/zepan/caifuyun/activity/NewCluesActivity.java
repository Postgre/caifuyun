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
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.application.MyApplication;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.constants.Url;
import com.zepan.caifuyun.entity.Clues;
import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 新建线索   /修改线索
 * @author duanjie
 *
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class NewCluesActivity extends BaseActivity   implements OnClickListener,OnPhotoDeleteCallBack,Callback{
	private CheckViewManager  checkView=new  CheckViewManager();
	private final static int NewClues=0x100;
	private final static int LABEL=0x101;
	private CheckableEditText mProductName,mCompany,
	mPosition,mCluesSource,mPhone,mEmail,mNote,mMobile;//名字
	private TextView  mLabel;
	private AchievePhotoImageView headView;
	private  Clues clues;
	private Handler mHandler=new Handler(this);
	private OSSBucket bucket= MyApplication.ossService.getOssBucket("canaanwealth");
	String tag;//判断是编辑还是修改
	private Map<Integer, String> avatarMap=new ArrayMap<Integer, String>();
	private static final int DELETEAVATAR=0x102;
	private static final int UPDATEAVATAR=0x103;
    
	private boolean isVisible;//

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_clues);
		setHeaderFields(0,R.string.new_clue, R.string.save,
				R.drawable.ic_action_back, 0,0);
		initView();
		initData();
	}


	private void initData(){
		Intent intent=getIntent();
		tag=intent.getStringExtra("tag");
		clues=intent.getParcelableExtra("clues");
		mLabel.setOnClickListener(this);
		if(tag!=null){
			if(tag.equals("change")){
				((TextView)findViewById(R.id.tv_center)).setText("修改线索");
				//				findViewById(R.id.tv_right).setVisibility(View.GONE);
				getLeadInfo();
			}
		}


	}
	private void initView() {
		((AlphaImageView)findViewById(R.id.iv_left)).setClickAlpha(150, true, false);
		((AlphaTextView)findViewById(R.id.tv_right)).setClickAlpha(100);
		((AlphaTextView)findViewById(R.id.tv_right)).setOnClickListener(this);
		findViewById(R.id.tv_phone).setOnClickListener(this);
		findViewById(R.id.et_label).setOnClickListener(this);

		//展开的按钮--
		findViewById(R.id.layout_more).setOnClickListener(this);
		
		//图片
		headView=(AchievePhotoImageView)findViewById(R.id.iv_head);
		headView.setOnPhotoDeleteCallBack(this);
		//名字
		mProductName=(CheckableEditText)findViewById(R.id.et_product_name);
		//公司名字
		mCompany=(CheckableEditText)findViewById(R.id.ed_company);
		//职位
		mPosition=(CheckableEditText)findViewById(R.id.ed_position);
		// 线索来源
		mCluesSource=(CheckableEditText)findViewById(R.id.et_clues_source);
		//电话
		mPhone=(CheckableEditText)findViewById(R.id.et_phone);
		//标签 
		mLabel=(TextView)findViewById(R.id.et_label);
		//手机号码   
		mMobile=(CheckableEditText)findViewById(R.id.et_mobile);
		//邮箱   
		mEmail=(CheckableEditText)findViewById(R.id.et_email);
		//备注  
		mNote=(CheckableEditText)findViewById(R.id.et_note);

	}
	/**
	 * 	LeadUpdate   修改线索
	 */
	private void  getLeadUpdate(){

		request(Url.LeadUpdate,getLeadUpdateParamJson(), Method.POST,new IRequestCallBack() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				getCreatePrivateLeadResult(response);
			}

			@Override
			public void onErrorResponse(String errorMessage) {
				// TODO Auto-generated method stub

			}
		});
	}


	private JSONObject getLeadUpdateParamJson(){

		//JSONObject paramJson=new JSONObject();
		JSONObject paramJson=checkView.getSubmitJson();
		try {
			String lableString=mLabel.getText().toString();
			String[] labelArray=lableString.split(" ");
			lableString="";
			for(int i=0;i<labelArray.length;i++){
				if(i==labelArray.length-1){
					lableString+=labelArray[i];
				}else{
					lableString+=labelArray[i]+",";
				}

			}
			//paramJson.put("tags",lableString);//标签
			paramJson.put("leadId",clues.getId());//线索id
			paramJson.put("token",getStringLocalData("token"));
			paramJson.put("tid", getStringLocalData("tid"));
			paramJson.put("uid", getStringLocalData("uid"));
			/*paramJson.put("name",mProductName.getText().toString());//名字
			paramJson.put("mobile",mMobile.getText().toString());//手机号码
			paramJson.put("email",mEmail.getText().toString());//邮件
			paramJson.put("phone",mPhone.getText().toString());//电话号码
			paramJson.put("post",mPosition.getText().toString());//职位
			paramJson.put("companyName",mCompany.getText().toString());//公司名字
			paramJson.put("dbcVarchar2",mCluesSource.getText().toString());//线索来源
			paramJson.put("comment",mNote.getText().toString());//备注
			 */	
			if(avatarMap.get(R.id.iv_head)!=null&&!avatarMap.get(R.id.iv_head).equals("")){
				String avatarUrl = Url.PictureAddress+avatarMap.get(R.id.iv_head);
				paramJson.put("dbcVarchar3",avatarUrl);//头像
			}else{
				paramJson.put("dbcVarchar3","");//头像
			}
			

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return paramJson;
	}
	/**
	 *请求    CreatePrivateLead    创建线索
	 * 
	 */

	private void  getCreatePrivateLead(){

		request(Url.CreatePrivateLead,getCreatePrivateLeadParamJson(), Method.POST,new IRequestCallBack() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				getCreatePrivateLeadResult(response);
			}

			@Override
			public void onErrorResponse(String errorMessage) {
				// TODO Auto-generated method stub

			}
		});
	}



	private void getCreatePrivateLeadResult(JSONObject response){
		try {
			if(response.getInt("status")==0){
				printToast("创建线索成功！");
				finish();
				
			}else if(response.getInt("status")==1){
				String message=response.getString("message");
				printToast(message);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


	private JSONObject getCreatePrivateLeadParamJson(){
		JSONObject paramJson=new JSONObject();
		try {
			String lableString=mLabel.getText().toString();
			String[] labelArray=lableString.split(" ");
			lableString="";
			for(int i=0;i<labelArray.length;i++){
				if(i==labelArray.length-1){
					lableString+=labelArray[i];
				}else{
					lableString+=labelArray[i]+",";
				}

			}
			paramJson.put("tags",lableString);//标签
			// getStringLocalData("token")
			paramJson.put("token",getStringLocalData("token"));
			paramJson.put("uid",getStringLocalData("uid"));
			paramJson.put("tid",getStringLocalData("tid"));
			JSONObject changeInfoJson = new JSONObject();
			changeInfoJson.put("name",mProductName.getText().toString());//名字
			changeInfoJson.put("mobile",mMobile.getText().toString());//手机号码
			changeInfoJson.put("email",mEmail.getText().toString());//邮件
			changeInfoJson.put("phone",mPhone.getText().toString());//电话号码
			changeInfoJson.put("post",mPosition.getText().toString());//职位
			changeInfoJson.put("companyName",mCompany.getText().toString());//公司名字
			changeInfoJson.put("dbcVarchar2",mCluesSource.getText().toString());//线索来源
			changeInfoJson.put("comment",mNote.getText().toString());//备注
			if(avatarMap.get(R.id.iv_head)!=null&&!avatarMap.get(R.id.iv_head).equals("")){
				String avatarUrl = Url.PictureAddress+avatarMap.get(R.id.iv_head);
				changeInfoJson.put("dbcVarchar3",avatarUrl);//头像
			}else{
				changeInfoJson.put("dbcVarchar3","");//头像
			}
			
			paramJson.put("leadJson", changeInfoJson);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return paramJson;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_phone://手机
			TextView  tv=(TextView)v;
			//SelectPhoneActivity
			Intent intentSelectPhone=new Intent(NewCluesActivity.this,SelectPhoneActivity.class);
			intentSelectPhone.putExtra("tvPhone",tv.getText().toString());
			startActivityForResult(intentSelectPhone, NewClues);
			break;
		case R.id.et_label://标签
			Intent intentLabel=new Intent(NewCluesActivity.this,LabelActivity.class);
			startActivityForResult(intentLabel,LABEL);
			break;
		case R.id.tv_right://保存按钮
			if(tag!=null){
				if(tag.equals("change")){
					getLeadUpdate();
				}
			}else{
				getCreatePrivateLead();
			}
			break;
			
			//更多
		case R.id.layout_more:
			if(isVisible){
				findViewById(R.id.layout_name).setVisibility(View.GONE);
				((ImageView)findViewById(R.id.iv_more)).setImageResource(R.drawable.ic_action_expand);
				isVisible=false;
			}else{
				findViewById(R.id.layout_name).setVisibility(View.VISIBLE);
				((ImageView)findViewById(R.id.iv_more)).setImageResource(R.drawable.ic_action_collapse);
				isVisible=true;
			}
			break;
		
		default:
			break;
		}

	}



	private void loadAndShowPiture(Uri uri,int id){
		if (uri != null) {
			File file = new File(StringUtil.getPath(this,uri));
			if (file != null && file.exists()) {
				asynUpload(file,id,uri);
			}
		}
	}




	// 断点上传
	public void asynUpload(File file,final int id,final Uri uri) {

		try {
			OSSFile bigfFile =MyApplication.ossService.getOssFile(bucket, id+"_"+System.currentTimeMillis()+"_cropped.jpg");
			bigfFile.enableUploadCheckMd5sum();
			bigfFile.setUploadFilePath(file.getPath(), "raw/binary");
			bigfFile.ResumableUploadInBackground(new SaveCallback() {

				@Override
				public void onSuccess(String objectKey) {
					avatarMap.put(id, objectKey);
					mHandler.sendEmptyMessage(UPDATEAVATAR);
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
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data!=null){
			Uri uri = data.getParcelableExtra("uri");
			int viewId = data.getIntExtra("viewId",0);
			switch (viewId){
			case R.id.iv_head://上传图片
				loadAndShowPiture(uri,R.id.iv_head);
				break;
			}
			switch (requestCode) {
			case NewClues:
				String phone=data.getStringExtra("phone");
				((TextView)findViewById(R.id.tv_phone)).setText(phone);
				break;
			case LABEL:
				String label=data.getStringExtra("label");
				((TextView)findViewById(R.id.et_label)).setText(label);
				break;
			default:
				break;
			}
		}
		super.onActivityResult(requestCode,resultCode,data);
	}




	/**
	 * 获取线索的详情    LeadInfo
	 */
	private void  getLeadInfo(){
		String token;
		try {
			token = URLEncoder.encode(getStringLocalData("token"), "UTF-8");
			String uid=getStringLocalData("uid");
			String tid=getStringLocalData("tid");
			String url=Url.LeadInfo+"?token="+token+"&id="+clues.getId()+"&uid="+uid+"&tid="+tid;
			request(url,null, Method.GET,new IRequestCallBack() {
				@Override
				public void onResponse(JSONObject response) {
					// TODO Auto-generated method stub
					getLeadInfoResult(response);
				}
				@Override
				public void onErrorResponse(String errorMessage) {
					// TODO Auto-generated method stub
				}
			});
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	private void getLeadInfoResult(JSONObject response){
		JSONObject json ;
		try {
			json = response.getJSONObject("lead");
			Clues c=(Clues)JsonUtil.jsonToEntity(json, Clues.class);
			mProductName.setText(c.getName());
			mCompany.setText(c.getCompany());//companyName
			mPosition.setText(c.getPosition());//post
			mCluesSource.setText(c.getSource());//dbcVarchar2
			mPhone.setText(c.getTelphone());//phone
			mMobile.setText(c.getMobile());//mobile
			mEmail.setText(c.getEmail());
			mNote.setText(c.getComment());
			if(c.getDbcVarchar3()!=null&&!"".equals(c.getDbcVarchar3())){//dbcVarchar8
				Uri picturePhotoUrl=Uri.parse(c.getDbcVarchar3());
				headView.setImageURI(picturePhotoUrl);
				String objectKey = c.getDbcVarchar3().substring(c.getDbcVarchar3().lastIndexOf("/")+1, c.getDbcVarchar3().length());
				avatarMap.put(R.id.iv_head, objectKey);
			}else{
				headView.setImageResource(R.drawable.ic_action_add);//当没有图片时，设置默认的图片
			}
			String lableString="";
			if(c.getTags()!=null){
				for (int i=0;i<c.getTags().size();i++) { 
					if(i==c.getTags().size()-1){
						lableString+=c.getTags().get(i).getName();
					}else{
						lableString+=c.getTags().get(i).getName()+" ";
					}
				} 
				((TextView)findViewById(R.id.et_label)).setText(lableString); 
			}


			checkView.addView(mProductName);
			checkView.addView(mCompany);
			checkView.addView(mPosition);
			checkView.addView(mCluesSource);
			checkView.addView(mPhone);
			checkView.addView(mMobile);
			checkView.addView(mEmail);
			checkView.addView(mNote);
			//checkView.addView(iv_head);
			checkView.addView(mLabel);


		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		
	}



	// 异步删除数据
	public void asyncDelete(String objectKey,final int id) {

		OSSData data =MyApplication.ossService.getOssData(bucket,objectKey);
		data.deleteInBackground(new DeleteCallback() {

			@Override
			public void onSuccess(String objectKey) {
				switch (id) {
				case R.id.iv_head:
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
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case DELETEAVATAR:
			((ImageView) findViewById(R.id.iv_head)).setImageURI(null);
			avatarMap.put(R.id.iv_head, null);
           
			break;
		case UPDATEAVATAR:
			String avatarUrl=Url.PictureAddress+avatarMap.get(R.id.iv_head);
		   ((ImageView) findViewById(R.id.iv_head)).setImageURI(Uri.parse(avatarUrl));
		break;
		default:
			break;
		}
		return false;
	}


	@Override
	public void deleteItemClick(int viewId, String imageUrl) {
		switch (viewId) {
		case R.id.iv_head://头像
			String objectKey = avatarMap.get(R.id.iv_head);
			asyncDelete(objectKey,R.id.iv_head);
			break;

		default:
			break;
		}


	}

}
