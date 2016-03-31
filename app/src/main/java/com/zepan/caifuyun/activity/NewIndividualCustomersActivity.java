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
import android.widget.RatingBar;
import android.widget.TextView;
/**
 * 新建个人客户    修改个人客户资料
 * @author long
 *
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class NewIndividualCustomersActivity extends BaseActivity implements OnClickListener,OnPhotoDeleteCallBack,Callback {
	private int currentTag;
	private int id;
	private CheckViewManager  checkView=new  CheckViewManager();
	private TextView birthday;
	private String dateTime=StringUtil.longToFormatTimeUtcStr(System.currentTimeMillis()/1000+"", "yyyy-MM-dd");
	private static final int LABEL=0x100;
	private static final int NEWINDIVIDUAL=0x2000;
	private static final int UPDATEINDIVIDUAL=0x2001;
	private static final int NEWORGANIZATION=0x2002;
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
		setContentView(R.layout.activity_new_individual_customers);
		initView();
		initData();
	}
	private void initView(){
		((AlphaImageView)findViewById(R.id.iv_left)).setClickAlpha(150, true, false);
		((AlphaTextView)findViewById(R.id.tv_right)).setClickAlpha(100);
		findViewById(R.id.tv_right).setOnClickListener(this);
		birthday=(TextView)findViewById(R.id.tv_birthday);
		birthday.setOnClickListener(this);
		findViewById(R.id.tv_label).setOnClickListener(this);
		((AchievePhotoImageView)findViewById(R.id.ai_avatar)).setOnPhotoDeleteCallBack(this);
		((AchievePhotoImageView)findViewById(R.id.ai_card)).setOnPhotoDeleteCallBack(this);

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
		if(intent.hasExtra("updateIndividual")){
			currentTag=UPDATEINDIVIDUAL;
			setHeaderFields(0, R.string.update_self_custom, R.string.save,
					R.drawable.ic_action_back, 0,0);
			id=intent.getIntExtra("updateIndividualId", -1);
			findViewById(R.id.ly_organization).setVisibility(View.GONE);
			((TextView)findViewById(R.id.tv_is_main)).setText("姓名");
			getInfo();
		}else if(intent.hasExtra("newIndividual")){
			currentTag=NEWINDIVIDUAL;
			setHeaderFields(0, R.string.new_self_custom, R.string.save,
					R.drawable.ic_action_back, 0,0);
			findViewById(R.id.ly_organization).setVisibility(View.GONE);
			((TextView)findViewById(R.id.tv_is_main)).setText("姓名");
		}
		else if(intent.hasExtra("updateOrganization")){
			currentTag=UPDATEORGANIZATION;
			setHeaderFields(0, R.string.update_organization, R.string.save,
					R.drawable.ic_action_back, 0,0);
			id=intent.getIntExtra("updateOrganizationId", -1);
			findViewById(R.id.ly_organization).setVisibility(View.VISIBLE);
			((TextView)findViewById(R.id.tv_is_main)).setText("主要联系人");
			getInfo();
		}else if(intent.hasExtra("newOrganization")){
			currentTag=NEWORGANIZATION;
			setHeaderFields(0, R.string.new_organization, R.string.save,
					R.drawable.ic_action_back, 0,0);
			findViewById(R.id.ly_organization).setVisibility(View.VISIBLE);
			((TextView)findViewById(R.id.tv_is_main)).setText("主要联系人");
		}
		/*else if(intent.hasExtra("newChannelOrganization")){//新建机构渠道
			currentTag=NEWCHANNELORGANIZATION;
			setHeaderFields(0, R.string.new_channel_organization, R.string.save,
					R.drawable.ic_action_back, 0,0);
			findViewById(R.id.ly_organization).setVisibility(View.VISIBLE);
			((TextView)findViewById(R.id.tv_is_main)).setText("主要联系人");
		}*/
		
//		else if(intent.hasExtra("updateChannelOrganization")){
//			currentTag=UPDATEORGANIZATION;
//			setHeaderFields(0, R.string.update_organization, R.string.save,
//					R.drawable.ic_action_back, 0,0);
//			id=intent.getIntExtra("updateOrganizationId", -1);
//			findViewById(R.id.ly_organization).setVisibility(View.VISIBLE);
//			((TextView)findViewById(R.id.tv_is_main)).setText("主要联系人");
//			getInfo();
//		}
//		else if(intent.hasExtra("updateChannelIndividualId")){
//			currentTag=UPDATEORGANIZATION;
//			setHeaderFields(0, R.string.update_organization, R.string.save,
//					R.drawable.ic_action_back, 0,0);
//			id=intent.getIntExtra("updateOrganizationId", -1);
//			findViewById(R.id.ly_organization).setVisibility(View.VISIBLE);
//			((TextView)findViewById(R.id.tv_is_main)).setText("主要联系人");
//			getInfo();
//		}
		
		/*else if(intent.hasExtra("newChannelIndividualId")){ //新建有客渠道
			currentTag=NEWORGANIZATION;
			setHeaderFields(0, R.string.new_channel_individual, R.string.save,
					R.drawable.ic_action_back, 0,0);
			findViewById(R.id.ly_organization).setVisibility(View.GONE);
			((TextView)findViewById(R.id.tv_is_main)).setText("姓名");
		}*/
		
	}
	// 获得用户信息 或者机构信息
	private void getInfo(){
		try {
			String token = URLEncoder.encode(getStringLocalData("token"), "UTF-8");
			String uid=getStringLocalData("uid");
			String tid=getStringLocalData("tid");
			String url=Url.GetInfo+"?token="+token+"&id="+id+"&uid="+uid+"&tid="+tid;
			request(url, null, Method.GET, new IRequestCallBack() {

				@Override
				public void onResponse(JSONObject response) {
					getInfoResult(response);

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
	private void getInfoResult(JSONObject response){
		try {
			if(response.getInt("status")==0){
				JSONObject jsonInfo=response.getJSONObject("info");
				JSONObject jsonBody=jsonInfo.getJSONObject("body");
				JSONObject jsonAccount=jsonBody.getJSONObject("account");
				User user=(User) JsonUtil.jsonToEntity(jsonAccount, User.class);
				if(user!=null){
					if(jsonAccount.has("dbcVarchar1")){
						((CheckableEditText)findViewById(R.id.ed_organization)).setText(jsonAccount.getString("dbcVarchar1"));
					}
					if(jsonAccount.has("dbcVarchar2")){
						if(jsonAccount.getString("dbcVarchar2")!=null&&!"".equals(jsonAccount.getString("dbcVarchar2").trim())){
							String dbcVarchar2 =jsonAccount.getString("dbcVarchar2");
							if(dbcVarchar2.substring(0, 4).equals("http")
									||dbcVarchar2.substring(0, 4).equals("HTTP")){
								String objectKey=dbcVarchar2.substring(
										dbcVarchar2.lastIndexOf("/")+1,
										dbcVarchar2.length());
								pictureMap.put(R.id.ai_card,objectKey);
								((ImageView)findViewById(R.id.ai_card)).setImageURI(Uri.parse(dbcVarchar2));
								((ImageView)findViewById(R.id.ai_card)).setBackground(null);
							}else{
								pictureMap.put(R.id.ai_card,null);
								((ImageView)findViewById(R.id.ai_card)).setBackgroundResource(R.drawable.ic_form_add);
							}

						}else{
							pictureMap.put(R.id.ai_card,null);
							((ImageView)findViewById(R.id.ai_card)).setBackgroundResource(R.drawable.ic_form_add);
						}

					}
					if(jsonAccount.has("dbcVarchar5")){
						if(jsonAccount.getString("dbcVarchar5")!=null&&!"".equals(jsonAccount.getString("dbcVarchar5").trim())){
							String dbcVarchar5 =jsonAccount.getString("dbcVarchar5");
							if(dbcVarchar5.substring(0, 4).equals("http")
									||dbcVarchar5.substring(0, 4).equals("HTTP")){
								
								String objectKey=dbcVarchar5.substring(
										dbcVarchar5.lastIndexOf("/")+1,
										dbcVarchar5.length());
								pictureMap.put(R.id.ai_avatar,objectKey);
								((ImageView)findViewById(R.id.ai_avatar)).setImageURI(Uri.parse(dbcVarchar5));
								((ImageView)findViewById(R.id.ai_avatar)).setBackground(null);
							}
							else{
								pictureMap.put(R.id.ai_avatar,null);
								((ImageView)findViewById(R.id.ai_avatar)).setBackgroundResource(R.drawable.ic_form_add);
							}
						}else{
							pictureMap.put(R.id.ai_avatar,null);
							((ImageView)findViewById(R.id.ai_avatar)).setBackgroundResource(R.drawable.ic_form_add);
						}

					}
					if(jsonAccount.has("dbcTextarea2")){
						if(!"".equals(jsonAccount.getString("dbcTextarea2"))){
							String db2=jsonAccount.getString("dbcTextarea2");
							JSONObject jsonDbc2=new JSONObject(db2);//身份信息
							IdentityInfo profess=(IdentityInfo) JsonUtil.jsonToEntity(jsonDbc2, IdentityInfo.class);
							user.setIdentityInformation(profess);
							if(user.getIdentityInformation()!=null){
								((CheckableEditText)findViewById(R.id.et_id)).setText(user.getIdentityInformation().getPaperscode());//身份证
								//((WheelTextView)findViewById(R.id.tv_sex)).setText(user.getIdentityInformation().getSex());//性别
								((WheelTextView)findViewById(R.id.tv_eiduation)).setText(user.getIdentityInformation().getEducation());//教育程度
								((TextView)findViewById(R.id.tv_birthday)).setText(user.getIdentityInformation().getEducation());//生日
							}
						}

					}
					if(jsonAccount.has("dbcTextarea3")){
						if(!"".equals(jsonAccount.getString("dbcTextarea3"))){
							String db3=jsonAccount.getString("dbcTextarea3");
							JSONObject jsonDbc3=new JSONObject(db3);//职业信息
							ProfessionInfo profess=(ProfessionInfo) JsonUtil.jsonToEntity(jsonDbc3, ProfessionInfo.class);
							user.setProfessionalInformation(profess);
							if(user.getProfessionalInformation()!=null){
								((CheckableEditText)findViewById(R.id.et_company_name)).setText(user.getProfessionalInformation().getCompany());//公司名
								((CheckableEditText)findViewById(R.id.et_position)).setText(user.getProfessionalInformation().getPosition());//职位
								((WheelTextView)findViewById(R.id.tv_professional)).setText(user.getProfessionalInformation().getCompanytype());//公司行业
								((WheelTextView)findViewById(R.id.tv_scale)).setText(user.getProfessionalInformation().getCompanyscale());//公司规模
							}
						}

					}
					if(!"".equals(user.getTags())&&user.getTags()!=null){
						String tag="";
						for(int i=0;i<user.getTags().size();i++){
							if(i==user.getTags().size()-1){
								tag+=user.getTags().get(i).getName();
							}else{
								tag+=user.getTags().get(i).getName()+" ";
							}
						}
						((TextView)findViewById(R.id.tv_label)).setText(tag);
					}


					((CheckableEditText)findViewById(R.id.ed_make_payment)).setText(user.getUserName());

					((RatingBar)findViewById(R.id.rb_maturity)).setRating((user.getMaturity()));//成熟度

					((RatingBar)findViewById(R.id.rb_power)).setRating(user.getPower());//实力


					((RatingBar)findViewById(R.id.rb_risk)).setRating(user.getRiskAppetite());//风险

					//((TextView)findViewById(R.id.tv_label)).setText(text);

					((CheckableEditText)findViewById(R.id.et_email)).setText(user.getEmail());//邮箱


					//					((CheckableEditText)findViewById(R.id.ed_make_payment)).setTag("accountName");//用户名
					//					checkView.addView(((CheckableEditText)findViewById(R.id.ed_make_payment)));
					//
					//					((CheckableEditText)findViewById(R.id.et_phone)).setTag("phone");//手机号
					//					checkView.addView(((CheckableEditText)findViewById(R.id.et_phone)));
					//
					//					((CheckableEditText)findViewById(R.id.et_email)).setTag("dbcVarchar3");//email
					//					checkView.addView(((CheckableEditText)findViewById(R.id.et_email)));

					//				    ((CheckableEditText)findViewById(R.id.et_id)).setTag("");//身份证
					//				    checkView.addView(((CheckableEditText)findViewById(R.id.et_id)));
					//				    
					//				    ((CheckableEditText)findViewById(R.id.et_company_name)).setTag("");//公司名称
					//				    checkView.addView(((CheckableEditText)findViewById(R.id.et_company_name)));

					//				    ((CheckableEditText)findViewById(R.id.et_position)).setTag("dbcTextarea3");//职位
					//				    checkView.addView(((CheckableEditText)findViewById(R.id.et_position)));




				}


			}else{
				printToast("返回异常");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//创建个人客户   创建机构客户
	private void postCreate(){
		String url=Url.Create;
		request(url, getJson(), Method.POST, new IRequestCallBack() {

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
	private JSONObject getJson(){
		try {
			JSONObject paramJson=new JSONObject();
			paramJson.put("token",getStringLocalData("token"));
			paramJson.put("uid",getStringLocalData("uid"));
			paramJson.put("tid",getStringLocalData("tid"));
			JSONObject accountJson=new JSONObject();
			if(currentTag==NEWORGANIZATION||currentTag==UPDATEORGANIZATION){
				accountJson.put("dbcVarchar1",((CheckableEditText)findViewById(R.id.ed_organization)).getText().toString());//机构客户

			}
			if(currentTag==UPDATEINDIVIDUAL||currentTag==UPDATEORGANIZATION){
				paramJson.put("accountId",id);	//客户ID
			}
			
			if(pictureMap.get(R.id.ai_avatar)!=null){
				String avatarUrl=Url.PictureAddress+pictureMap.get(R.id.ai_avatar);
				accountJson.put("dbcVarchar5",avatarUrl);//头像
			}else{
				accountJson.put("dbcVarchar5","");//头像
			}

			if(pictureMap.get(R.id.ai_card)!=null){
				String cardUrl=Url.PictureAddress+pictureMap.get(R.id.ai_card);
				accountJson.put("dbcVarchar2",cardUrl);//名片
			}else{
				accountJson.put("dbcVarchar2","");//名片
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
				accountJson.put("tags",lableString);//标签
			}else{
				accountJson.put("tags","");//标签
			}
			if(!TextUtils.isEmpty(((CheckableEditText)findViewById(R.id.ed_make_payment)).getText())){  //客户姓名
				accountJson.put("accountName", ((CheckableEditText)findViewById(R.id.ed_make_payment)).getText().toString());
			}else{
				accountJson.put("accountName","");//名称
			}
			accountJson.put("dbcInteger1",(int)(((RatingBar)findViewById(R.id.rb_maturity)).getRating()));//成熟度
			accountJson.put("dbcInteger2",(int)(((RatingBar)findViewById(R.id.rb_power)).getRating()));//实力
			accountJson.put("dbcInteger3",(int)(((RatingBar)findViewById(R.id.rb_risk)).getRating()));//风险
			//accountJson.put("dbcVarchar1", "");
			//accountJson.put("dbcVarchar2", "");
			if(!TextUtils.isEmpty(((CheckableEditText)findViewById(R.id.et_email)).getText())){
				accountJson.put("dbcVarchar3",((CheckableEditText)findViewById(R.id.et_email)).getText().toString());//email
			}else{
				accountJson.put("dbcVarchar3","");//email
			}

			JSONObject dbcTextarea2=new JSONObject();
			if(!TextUtils.isEmpty(birthday.getText())){
				dbcTextarea2.put("birthday",birthday.getText().toString());//生日
			}else{
				dbcTextarea2.put("birthday","");//生日
			}
			if(!TextUtils.isEmpty(((WheelTextView)findViewById(R.id.tv_sex)).getText())){
				dbcTextarea2.put("sex",((WheelTextView)findViewById(R.id.tv_sex)).getText().toString());//性别
			}else{
				dbcTextarea2.put("sex","");//性别
			}
			dbcTextarea2.put("nation","");//民族
			dbcTextarea2.put("residence","");//居住地
			//dbcTextarea2.put("paperstype","");//证件类型

			if(!TextUtils.isEmpty(((CheckableEditText)findViewById(R.id.et_id)).getText())){
				dbcTextarea2.put("paperscode",((CheckableEditText)findViewById(R.id.et_id)).getText().toString());//身份证号码
			}else{
				dbcTextarea2.put("paperscode","");//身份证号码
			}
			if(!TextUtils.isEmpty(((WheelTextView)findViewById(R.id.tv_eiduation)).getText())){
				dbcTextarea2.put("education",((WheelTextView)findViewById(R.id.tv_eiduation)).getText().toString());//教育程度
			}else{
				dbcTextarea2.put("education","");//教育程度
			}

			accountJson.put("dbcTextarea2",dbcTextarea2);//身份信息

			JSONObject dbcTextarea3=new JSONObject();
			//dbcTextarea3.put("professionstatus", "");//身份职位
			if(!TextUtils.isEmpty(((CheckableEditText)findViewById(R.id.et_company_name)).getText())){
				dbcTextarea3.put("company",((CheckableEditText)findViewById(R.id.et_company_name)).getText().toString());//公司名称
			}else{
				dbcTextarea3.put("company","");//公司名称
			}
			if(!TextUtils.isEmpty(((CheckableEditText)findViewById(R.id.et_position)).getText())){
				dbcTextarea3.put("position",((CheckableEditText)findViewById(R.id.et_position)).getText().toString());//公司职位
			}else{
				dbcTextarea3.put("position","");//公司职位
			}
			if(!TextUtils.isEmpty(((WheelTextView)findViewById(R.id.tv_professional)).getText())){
				dbcTextarea3.put("companytype",((WheelTextView)findViewById(R.id.tv_professional)).getText().toString());//公司类型
			}else{
				dbcTextarea3.put("companytype","");//公司类型
			}
			if(!TextUtils.isEmpty(((WheelTextView)findViewById(R.id.tv_scale)).getText())){
				dbcTextarea3.put("companyscale",((WheelTextView)findViewById(R.id.tv_scale)).getText().toString());//公司规模
			}else{
				dbcTextarea3.put("companyscale","");//公司规模
			}
			accountJson.put("dbcTextarea3",dbcTextarea3);//职业信息S

			//			accountJson.put("dbcTextarea4", "");//资产信息
			//			accountJson.put("dbcTextarea5", "");//负债信息
			//			accountJson.put("dbcTextarea6", "");//家庭信息
			paramJson.put("accountJson",accountJson);
			return paramJson;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}









	//修改个人资料
	private void postModify(){
			request(Url.Update, getJson(), Method.POST, new IRequestCallBack() {

				@Override
				public void onResponse(JSONObject response) {
					getModifyResult(response);

				}

				@Override
				public void onErrorResponse(String errorMessage) {
					// TODO Auto-generated method stub

				}
			});
		
	}
	private void getModifyResult(JSONObject response){
		try {
			if(response.getInt("status")==0){
				printToast("修改成功");
			}else{
				printToast("返回异常");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finish();

	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_right:
			if(currentTag==UPDATEINDIVIDUAL){
				if(((CheckableEditText)findViewById(R.id.ed_make_payment)).check()){
					postModify();
				}
			}else if(currentTag==NEWINDIVIDUAL){
				if(((CheckableEditText)findViewById(R.id.ed_make_payment)).check()){
					postCreate();
				}
			}else if(currentTag==NEWORGANIZATION){
				if(((CheckableEditText)findViewById(R.id.ed_organization)).check()
						&&((CheckableEditText)findViewById(R.id.ed_make_payment)).check()){
					postCreate();
				}

			}
			else if(currentTag==UPDATEORGANIZATION){
				if(((CheckableEditText)findViewById(R.id.ed_organization)).check()
						&&((CheckableEditText)findViewById(R.id.ed_make_payment)).check()){
					postModify();
				}

			}
			break;
		case R.id.tv_birthday://(生日)选择日期
			DisplayMetrics dm = new DisplayMetrics();
			this.getWindowManager().getDefaultDisplay().getMetrics(dm);
			int width = dm.widthPixels;
			int height = dm.heightPixels;
			String[] dates = dateTime.split(" ");
			int[] date = getYMDArray(dates[0]+"", "-");

			DateTimeDialog  dateTimeDialog= new DateTimeDialog(this,
					new PriorityListener() {
				@Override
				public void refreshPriorityUI(String year, String month,
						String day, String hours, String mins) {
					dateTime=year + "-" + month + "-" + day;
					birthday.setText(dateTime);
				}
			}, date[0], date[1], date[2], 0, 0, width,
					height, "选择时间");
			Window window = dateTimeDialog.getWindow();
			window.setGravity(Gravity.BOTTOM); //
			window.setWindowAnimations(R.style.dialogstyle); //
			dateTimeDialog.setCancelable(true);
			dateTimeDialog.setShow(false, false);
			dateTimeDialog.show();
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
				case R.id.ai_card:
					loadAndShowPiture(uri,R.id.ai_card);
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
					case R.id.ai_card:
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
				switch (id) {
				case R.id.ai_avatar:
					mHandler.sendEmptyMessage(DELETEAVATAR);
					break;
				case R.id.ai_card:
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
		switch (viewId) {
		case R.id.ai_avatar:
			String objectKey1=pictureMap.get(R.id.ai_avatar);
			asyncDelete(objectKey1,R.id.ai_avatar);
			break;
		case R.id.ai_card:
			String objectKey2=pictureMap.get(R.id.ai_card);
			asyncDelete(objectKey2,R.id.ai_card);
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
		case CARD:
			((ImageView) findViewById(R.id.ai_card)).setBackground(null);
			String cardUrl=Url.PictureAddress+pictureMap.get(R.id.ai_card);
			((ImageView) findViewById(R.id.ai_card)).setImageURI(Uri.parse(cardUrl));
			break;
		case DELETECARD:
			pictureMap.put(R.id.ai_card, null);
			((ImageView) findViewById(R.id.ai_card)).setImageURI(null);
			((ImageView) findViewById(R.id.ai_card)).setBackgroundResource(R.drawable.ic_form_add);
			break;



		default:
			break;
		}
		return false;
	}

}
