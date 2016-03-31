package com.zepan.caifuyun.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.sdk.android.oss.callback.DeleteCallback;
import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.alibaba.sdk.android.oss.storage.OSSBucket;
import com.alibaba.sdk.android.oss.storage.OSSData;
import com.alibaba.sdk.android.oss.storage.OSSFile;
import com.android.volley.Request.Method;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zepan.android.sdk.IRequestCallBack;
import com.zepan.android.sdk.JsonUtil;
import com.zepan.android.util.StringUtil;
import com.zepan.android.widget.AlphaImageView;
import com.zepan.android.widget.CheckableEditText;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.adapter.RecordAdapter;
import com.zepan.caifuyun.adapter.RecordAdapter.DeleteRecord;
import com.zepan.caifuyun.application.MyApplication;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.constants.Url;
import com.zepan.caifuyun.entity.Channel;
import com.zepan.caifuyun.entity.IdentityInfo;
import com.zepan.caifuyun.entity.ProfessionInfo;
import com.zepan.caifuyun.entity.Record;
import com.zepan.caifuyun.entity.Tags;
import com.zepan.caifuyun.entity.User;
import com.zepan.caifuyun.widget.DialogBottom;
import com.zepan.caifuyun.widget.DialogBottom.OnItemClick;



import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
/**
 *  个人收单渠道   机构收单渠道
 *
 *
 */
public class ChannelCustomersActivity extends BaseActivity implements OnClickListener,Callback{

	private int currentTag;
	private int channel_id;
	private static final int INDIVIDUAL=0x2000;
	private static final int ORGANIZATION=0x2001;
	private boolean isMenu;
	private OSSBucket bucket= MyApplication.ossService.getOssBucket("canaanwealth");
	private Handler mHandler=new Handler(this);
	private List<String> pictureList=new ArrayList<String>();
	private static final int DELETEPICTURE=0x2002;
	private static final int DELETEVOICE=0x2003;
	private MediaRecorder mRecorder = null;
	private MediaPlayer mPlayer = null;
	private Timer mRecordTimer = null;
	private int mRecordCount = 0;
	private int mSumRecordSecond = 0;
	private String mVoiceFileIdInServer = null;
	private static final int MESSAGE_WHAT_START_VOICE = 0xa;
	private static final int MESSAGE_WHAT_PLAY_VOICE = 0xb;
	private static final int MESSAGE_WHAT_STOP_VOICE = 0xc;
	private static final String VOICE_FILE_NAME = "voice.3gp";
	private String voiceUrl="";
	private int pictureCounts = 0;
	private double latitude;//纬度
	private double longitude;//经度
	private String gpsAdress="";//定位地址
	private String gpsName="";//定位名称
	private View headerView;
	private RecordAdapter recordAdapter;
	private List<Record> recordList=new ArrayList<Record>();
	private ArrayList<Tags> tagsList=new ArrayList<Tags>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_channelcustomers);
		initView();
		initData();
		getInfo();
	}
	private void initData(){
		Intent intent=getIntent();
		
		if(intent.hasExtra("guestchannel_id")){//个人渠道
			setHeaderFields(0, R.string.individual_channel, 
					0,R.drawable.ic_action_back, 0,R.drawable.ic_action_more);
			currentTag=INDIVIDUAL;
			channel_id=intent.getIntExtra("guestchannel_id", -1);
			headerView.findViewById(R.id.ly_partner).setVisibility(View.VISIBLE);
			headerView.findViewById(R.id.ly_main_user).setVisibility(View.GONE);
			//((ImageView)headerView.findViewById(R.id.iv_head)).setImageResource(R.drawable.ic_default_head);//默认头像
		}
		if(intent.hasExtra("institututionschannel_id")){//机构渠道
			setHeaderFields(0, R.string.organization_channel, 
					0,R.drawable.ic_action_back, 0,R.drawable.ic_action_more);
			currentTag=ORGANIZATION;
			channel_id=intent.getIntExtra("institututionschannel_id", -1);
			headerView.findViewById(R.id.ly_partner).setVisibility(View.GONE);
			headerView.findViewById(R.id.ly_main_user).setVisibility(View.VISIBLE);
			//((ImageView)headerView.findViewById(R.id.iv_head)).setImageResource(R.drawable.ic_default_institutions);
		}

	}

	private void initView() {
		headerView=getLayoutInflater().inflate(R.layout.include_header_channel_customers_list,null);
		ListView listView=(ListView) findViewById(android.R.id.list);
		listView.addHeaderView(headerView,null,false);

		recordAdapter=new RecordAdapter(this, recordList);
		recordAdapter.setDeleteRecord(new DeleteRecord() {

			@Override
			public void deleteRecord(int id) {
				getDeleteRecord(id);
			}
		});
		listView.setAdapter(recordAdapter);
		listView.setOnTouchListener(new OnTouchListener()  
		{  

			public boolean onTouch(View arg0, MotionEvent arg1)  
			{  
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);  
				return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);  
			}  
		});
		findViewById(R.id.ly_maturity).setVisibility(View.GONE);//成熟度，实力，风险偏好
		
		((AlphaImageView)findViewById(R.id.iv_left)).setClickAlpha(150, true, false);
		((AlphaImageView)findViewById(R.id.iv_right)).setClickAlpha(150, true, false);
		findViewById(R.id.iv_right).setOnClickListener(this);
		findViewById(R.id.iv_add).setOnClickListener(this);
		findViewById(R.id.tv_voice).setOnClickListener(this);
		//		findViewById(R.id.tv_picture).setOnClickListener(this);
		findViewById(R.id.iv_gps).setOnClickListener(this);
		findViewById(R.id.iv_voice).setOnClickListener(this);
		//findViewById(R.id.iv_voice_is_deldete).setOnClickListener(this);
		findViewById(R.id.iv_voice_is_deldete).setOnClickListener(this);
		findViewById(R.id.tv_send).setOnClickListener(this);
		headerView.findViewById(R.id.rb_associates).setOnClickListener(this);
		headerView.findViewById(R.id.rl_lable).setOnClickListener(this);
		headerView.findViewById(R.id.rb_details).setOnClickListener(this);
		headerView.findViewById(R.id.rb_trading_record).setOnClickListener(this);
		headerView.findViewById(R.id.rb_remind).setOnClickListener(this);
		headerView.findViewById(R.id.iv_sms).setOnClickListener(this);
		headerView.findViewById(R.id.iv_phone).setOnClickListener(this);
		headerView.findViewById(R.id.iv_email).setOnClickListener(this);
		 
//		findViewById(R.id.btn_speak).setOnTouchListener(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				switch (event.getAction()) {
//				case MotionEvent.ACTION_DOWN:
//					// start record voice
//					mRecorder = new MediaRecorder();
//					mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//					mRecorder
//					.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//					mRecorder.setOutputFile(getCacheRootPath()
//							+ File.separator + VOICE_FILE_NAME);
//					mRecorder
//					.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//					try {
//						mRecorder.prepare();
//					} catch (IOException e) {
//						Log.e(TAG, "prepare() failed");
//					}
//					mRecorder.start();
//					//mRecordCount = 0;
//					// run a timer
//					mRecordTimer = new Timer();
//					mRecordTimer.schedule(new TimerTask() {
//						@Override
//						public void run() {
//							mRecordCount++;
//							mHandler.sendEmptyMessage(MESSAGE_WHAT_START_VOICE);
//							if(mRecordCount>=30){
//								mHandler.sendEmptyMessage(MESSAGE_WHAT_STOP_VOICE);
//								mRecordTimer.cancel();
//
//							}
//						}
//					}, 0, 1000);
//					break;
//				case MotionEvent.ACTION_UP:
//					if(mRecorder!=null){
//						mRecorder.setOnErrorListener(null);
//						mRecorder.setPreviewDisplay(null);
//						mRecorder.stop();
//						mRecorder.reset();
//						mRecorder.release();
//						mRecorder = null;
//						mSumRecordSecond = mRecordCount;
//						// cancel the timer
//						if (mRecordTimer != null) {
//							mRecordTimer.cancel();
//						}
//						File file=new File(
//								getCacheRootPath() + File.separator + VOICE_FILE_NAME);
//						asynUploadVoice(file, R.id.btn_speak);
//					}
//					break;
//
//				default:
//					break;
//				}
//				return false;
//			}
//		});



	}
	public String getCacheRootPath() {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable()) {
			return this.getExternalCacheDir().getPath();
		} else {
			return this.getCacheDir().getPath();
		}
	}

	@Override
	protected void onStart() {
		getInfo();
		//getRecodeList();
		super.onStart();
	}
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		// TODO Auto-generated method stub
//		if (event.getAction() == MotionEvent.ACTION_DOWN) {
//			System.out.println("down");
//			if (ChannelCustomersActivity.this.getCurrentFocus() != null) {
//				if (ChannelCustomersActivity.this.getCurrentFocus().getWindowToken() != null) {
//					//调用系统自带的隐藏软键盘
//					((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).
//					hideSoftInputFromWindow(ChannelCustomersActivity.this.getCurrentFocus().
//							getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//				}
//			}
//		}
//		return super.onTouchEvent(event);  
//	}
	private void getDeleteRecord(int id){
		try {
			String token=URLEncoder.encode(getStringLocalData("token"), "UTF-8");
			String uid=getStringLocalData("uid");
			String tid=getStringLocalData("tid");
			String url=Url.RecodeDelete+"?token="+token+"&id="+id+"&uid="+uid+"&tid="+tid;
			request(url, null, Method.GET, new IRequestCallBack() {

				@Override
				public void onResponse(JSONObject response) {
					try {
						if(response.getInt("status")==0){
							printToast("删除成功");
							getRecodeList();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				@Override
				public void onErrorResponse(String errorMessage) {


				}
			});
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
	private void getInfo(){
		try {

			String token=URLEncoder.encode(getStringLocalData("token"), "UTF-8");
			
			String url=Url.GetChannelInfo+"?token="+token+"&channel_id="+channel_id;
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
				JSONObject json=response.getJSONObject("info");
				JSONObject bodyJson=json.getJSONObject("body");
				JSONObject jsonChannel=bodyJson.getJSONObject("channel");

				Channel channel=(Channel) JsonUtil.jsonToEntity(jsonChannel, Channel.class);
				if(channel!=null){

					if(currentTag==INDIVIDUAL){//个人
						
						((TextView)headerView.findViewById(R.id.tv_name)).setText(channel.getName());
						((TextView)headerView.findViewById(R.id.tv_company)).setText(channel.getCompany());//公司
						((TextView)headerView.findViewById(R.id.tv_position)).setText(channel.getPost());//职位
					}
					else if(currentTag==ORGANIZATION){//机构
						
						((TextView)headerView.findViewById(R.id.tv_name)).setText(channel.getCompany());
						((TextView)headerView.findViewById(R.id.tv_mian_user)).setText(channel.getContact());
					}
					((TextView)headerView.findViewById(R.id.tv_phone)).setText(channel.getMobile());//手机
					((TextView)headerView.findViewById(R.id.tv_emai)).setText(channel.getEmail());//邮箱
					


				}





			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.iv_right:
//			final DialogBottom infoDailog=new DialogBottom(this);
//			//,"共享客户"
//			String[] nameArray={"升级","编辑客户资料","删除客户","取消"};
//			infoDailog.setArray(nameArray);
//			infoDailog.setOnItemClick(new OnItemClick() {
//
//				@Override
//				public void itemClick(AdapterView<?> parent, View view, int position, long id) {
//					switch (position) {
//					case 0:
//						infoDailog.dismiss();
//						getUpdateLevel();
//						break;
//					case 1:
//						infoDailog.dismiss();
//						Intent intent=new Intent(ChannelCustomersActivity.this, NewIndividualCustomersActivity.class);
//						if(currentTag==INDIVIDUAL){
//							intent.putExtra("updateIndividual","updateIndividual");
//							intent.putExtra("updateIndividualId", channel_id);
//
//						}else if(currentTag==ORGANIZATION){
//							intent.putExtra("updateOrganization","updateOrganization");
//							intent.putExtra("updateOrganizationId", channel_id);
//						}
//						startActivity(intent);
//						break;
//					case 2:
//						infoDailog.dismiss();
//						getDelete();
//						break;
//					case 3:
//						infoDailog.dismiss();
//						break;
//					case 4:
//
//						break;
//
//					default:
//						break;
//					}
//
//				}
//			});
//			infoDailog.show();
//
//			break;
//
//		case R.id.iv_add:
//			if(isMenu){
//				findViewById(R.id.ly_small_menu).setVisibility(View.GONE);
//				isMenu=false;
//			}else{
//				findViewById(R.id.ly_small_menu).setVisibility(View.VISIBLE);
//				isMenu=true;
//			}
//
//			break;
//		case R.id.iv_gps:
//			Intent intent=new Intent(this, LocationActivity.class);
//			startActivityForResult(intent,0);
//			break;
//		case R.id.iv_voice:
//			findViewById(R.id.iv_add).setEnabled(false);
//			findViewById(R.id.tv_send).setEnabled(false);
//			findViewById(R.id.btn_speak).setVisibility(View.VISIBLE); 
//			findViewById(R.id.ly_small_menu).setVisibility(View.GONE);
//			break;
//		case R.id.iv_voice_is_deldete:
//			String objectKey=voiceUrl.substring(voiceUrl.lastIndexOf("/")+1, voiceUrl.length());
//			asyncDeleteVoice(objectKey);
//			break;
//		case R.id.tv_voice:
//			if(voiceUrl!=null&&!"".equals(voiceUrl)){
//				//播放声音
//				try {
//					MediaPlayer mPlayer = new MediaPlayer();
//					findViewById(R.id.iv_voice_animation).setVisibility(View.VISIBLE);
//					final AnimationDrawable animationDrawable = (AnimationDrawable)
//							(findViewById(R.id.iv_voice_animation).getBackground());
//					findViewById(R.id.iv_voice_hide).setVisibility(View.GONE);
//					mPlayer.setDataSource(voiceUrl);
//					mPlayer.prepare();
//					mPlayer.start();
//					animationDrawable.start();
//					mPlayer.setOnCompletionListener(new OnCompletionListener() {
//
//						@Override
//						public void onCompletion(MediaPlayer mp) {
//							animationDrawable.stop();
//							findViewById(R.id.iv_voice_animation).setVisibility(View.GONE);
//							findViewById(R.id.iv_voice_hide).setVisibility(View.VISIBLE);
//						}
//					});
//				} catch (IllegalArgumentException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (SecurityException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IllegalStateException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//
//
//			break;
//		case R.id.tv_send:
//			if(((CheckableEditText)findViewById(R.id.et_input_recode)).check()){
//				postCreateRecord();
//			}
//
//			break;
//		case R.id.rb_associates:
//			Intent intentAssociates=new Intent(this, AffiliatedPersonActivity.class);
//			intentAssociates.putExtra("accountId", channel_id);
//			startActivity(intentAssociates);
//			break;
//		case R.id.rl_lable:
//			Intent intentLable=new Intent(this, LabelActivity.class);
//			intentLable.putExtra("accountId",channel_id);
//			intentLable.putParcelableArrayListExtra("tagsList",tagsList);
//			startActivityForResult(intentLable, 0);
//			break;
//		case R.id.rb_details:
//			Intent intentDetail =new Intent(this, DetailActivity.class);
//			intentDetail.putExtra("detailId",channel_id);
//			startActivity(intentDetail);
//			break;
//		case R.id.rb_trading_record://交易历史
//			Intent intentTransactionRecord =new Intent(this, TransactionRecordActivity.class);
//			intentTransactionRecord.putExtra("accountId", channel_id);
//			startActivity(intentTransactionRecord);
//			break;
//		case R.id.rb_remind:
//			Intent intentRemind =new Intent(this, RemindActivity.class);
//			startActivity(intentRemind);
//			break;
//		case R.id.iv_sms://短信
//			Intent intentSms = new Intent();
//			//系统默认的action，用来打开默认的短信界面
//			intentSms.setAction(Intent.ACTION_SENDTO);
//			//需要发短息的号码
//			intentSms.setData(Uri.parse("smsto:"+((TextView)findViewById(R.id.tv_phone)).getText().toString()));
//			startActivity(intentSms);
//			break;
//		case R.id.iv_phone://电话
//			Uri uri = Uri.parse("tel:"+((TextView)findViewById(R.id.tv_phone)).getText().toString()); 
//			Intent it = new Intent(Intent.ACTION_DIAL, uri);   
//			startActivity(it); 
//			break;
//		case R.id.iv_email://邮件
//			//邮件
//			Intent intentEmail=new Intent(Intent.ACTION_SENDTO);  
//			//到时候该成动态的
//			intentEmail.setData(Uri.parse("mailto:"+((TextView)findViewById(R.id.tv_email)).getText().toString()));  
//			intentEmail.putExtra(Intent.EXTRA_SUBJECT, "这是标题");  
//			intentEmail.putExtra(Intent.EXTRA_TEXT, "这是内容");  
//			startActivity(intentEmail);  
//
//
//
//
//		default:
//			break;
		}

	}
	private void postCreateRecord(){
		try {

			String url=Url.RecodeCreate;
			JSONObject json=new JSONObject();
			json.put("token", getStringLocalData("token"));
			json.put("uid", getStringLocalData("uid"));
			json.put("tid", getStringLocalData("tid"));
			json.put("accountId",channel_id);
			json.put("content",((CheckableEditText)findViewById(R.id.et_input_recode)).getText().toString());
			JSONArray jsonArray=new JSONArray();
			for(String pictureUrl:pictureList){
				JSONObject jsonChild=new JSONObject();
				jsonChild.put("fileLength","");
				jsonChild.put("fileName", "");
				jsonChild.put("fileUrl",pictureUrl);
				jsonArray.put(jsonChild);
			}
			json.put("imageFiles",jsonArray);
			json.put("voiceUrl",voiceUrl);
			json.put("duration",mRecordCount);//声音时间长度
			json.put("voiceSize","");
			json.put("longitude",longitude);//经度
			json.put("latitude",latitude);//纬度
			json.put("location",gpsName);
			json.put("locationDetail",gpsAdress);
			request(url, json, Method.POST, new IRequestCallBack() {

				@Override
				public void onResponse(JSONObject response) {
					postCreateRecordResult(response);
				}

				@Override
				public void onErrorResponse(String errorMessage) {
					// TODO Auto-generated method stub

				}
			});
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	private void postCreateRecordResult(JSONObject response){
		try {
			if(response.getInt("status")==0){
				printToast("创建成功");
				if(((LinearLayout)findViewById(R.id.ly_picture)).getChildCount()>0){
					((LinearLayout)findViewById(R.id.ly_picture)).removeAllViews();
				}
				if(pictureList.size()>0){
					pictureList.clear();
				}
				findViewById(R.id.ly_voice).setVisibility(View.GONE);
				findViewById(R.id.tv_gps_adress).setVisibility(View.GONE);
				((TextView)findViewById(R.id.tv_gps_adress)).setText("");
				((CheckableEditText)findViewById(R.id.et_input_recode)).setText("");
				findViewById(R.id.ly_small_menu).setVisibility(View.GONE);
				isMenu=false;
				getRecodeList();

			}else{
				printToast(response.getString("message"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//获取客户所有跟进记录
	private void getRecodeList(){
		try {
			String token=URLEncoder.encode(getStringLocalData("token"), "UTF-8");
			String uid=getStringLocalData("uid");
			String tid=getStringLocalData("tid");
			String url=Url.RecodeGetAll+"?token="+token+"&accountId="+channel_id+"&uid="+uid+"&tid="+tid;
			request(url, null, Method.GET, new IRequestCallBack() {

				@Override
				public void onResponse(JSONObject response) {

					try {
						if(response.getInt("status")==0){
							getRecodeListResult(response);
						}else{
							printToast(response.getString("message"));
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

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
	private void getRecodeListResult(JSONObject response){
		try {
			if(recordList.size()>0){
				recordList.clear();
			}
			if(response.has("ativityrecords")){
				JSONArray jsonArray = response.getJSONArray("ativityrecords");
				for(int i=0;i<jsonArray.length();i++){
					Record record=(Record) JsonUtil.jsonToEntity((JSONObject)jsonArray.get(i), Record.class);
					JSONObject jsonRecord=jsonArray.getJSONObject(i);
					List<String> fileList=new ArrayList<String>();
					if(jsonRecord.has("activityRecord")){
						JSONObject jsonActivityRecord=jsonRecord.getJSONObject("activityRecord");
						if(jsonActivityRecord.has("imgFiles")){
							JSONArray jsonArrayFiles=jsonActivityRecord.getJSONArray("imgFiles");
							for(int j=0;j<jsonArrayFiles.length();j++){
								String file=jsonArrayFiles.getJSONObject(j).getString("fileUrl");
								fileList.add(file);
							}
						}
						if(jsonActivityRecord.has("content")){
							record.setContent(jsonActivityRecord.getString("content"));
						}
						if(jsonActivityRecord.has("id")){
							record.setId(jsonActivityRecord.getInt("id"));
						}
						if(jsonActivityRecord.has("createdAt")){
							record.setCreatedAt(jsonActivityRecord.getLong("createdAt"));
						}
					}
					record.setImgFilesList(fileList);
					recordList.add(record);
				}
				recordAdapter.notifyDataSetChanged();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void getUpdateLevel(){
		try {
			String token=URLEncoder.encode(getStringLocalData("token"), "UTF-8");
			String uid =getStringLocalData("uid");
			String tid =getStringLocalData("tid");
			String url=Url.UpdateLevel+"?token="+token+"&accountId="+channel_id+"&level="+2+"&uid="+uid+"&tid="+tid;
			request(url, null, Method.GET, new IRequestCallBack() {

				@Override
				public void onResponse(JSONObject response) {

					try {
						if(response.getInt("status")==0){
							printToast(response.getString("message"));
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

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
	private void getDelete(){
		try {
			String token=URLEncoder.encode(getStringLocalData("token"), "UTF-8");
			String uid=getStringLocalData("uid");
			String tid=getStringLocalData("tid");
			String url=Url.Delete+"?token="+token+"&accountIds="+channel_id+"&uid="+uid+"&tid="+tid;
			request(url, null, Method.GET, new IRequestCallBack() {

				@Override
				public void onResponse(JSONObject response) {

					try {
						if(response.getInt("status")==0){
                            finish();
							printToast(response.getString("message"));
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

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
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data!=null){
			if(data.hasExtra("uri")){
				Uri uri = data.getParcelableExtra("uri");
				int viewId = data.getIntExtra("viewId",0);
				switch (viewId) {
				case R.id.iv_picture:
					if(pictureCounts>=4){
						printToast("只能添加4张图片");
						return;
					}
					loadAndShowPiture(uri,viewId);

					break;


				default:
					break;
				}
			}else if(data.hasExtra("gpsName")){
				((TextView)findViewById(R.id.tv_gps_adress)).setVisibility(View.VISIBLE);
				if(data.hasExtra("gpsAdress")){
					((TextView)findViewById(R.id.tv_gps_adress)).setText(
							data.getStringExtra("gpsName")+"("+data.getStringExtra("gpsAdress")+")");
					gpsAdress=data.getStringExtra("gpsAdress");//地址
				}else{
					((TextView)findViewById(R.id.tv_gps_adress)).setText(
							data.getStringExtra("gpsName"));
				}
				latitude=data.getDoubleExtra("latitude", 0);//纬度
				longitude=data.getDoubleExtra("longitude", 0);//经度
				gpsName=data.getStringExtra("gpsName");//名称
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
	// 断点上传图片
	public void asynUpload(File file,final int id) {
		try {
			OSSFile bigfFile = MyApplication.ossService.getOssFile(bucket, id+"_"+System.currentTimeMillis()+"_cropped.jpg");
			bigfFile.enableUploadCheckMd5sum();
			bigfFile.setUploadFilePath(file.getPath(), "raw/binary");
			bigfFile.ResumableUploadInBackground(new SaveCallback() {

				@Override
				public void onSuccess(String objectKey) {
					switch (id) {
					case R.id.iv_picture:
						pictureList.add(Url.PictureAddress+objectKey);
						mHandler.sendEmptyMessage(R.id.iv_picture);
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
	// 断点上传语音
	public void asynUploadVoice(File file,final int id) {
		try {
			OSSFile bigfFile = MyApplication.ossService.getOssFile(bucket, id+"_"+System.currentTimeMillis()+"_voice.3gp");
			bigfFile.enableUploadCheckMd5sum();
			bigfFile.setUploadFilePath(file.getPath(), "raw/binary");
			bigfFile.ResumableUploadInBackground(new SaveCallback() {

				@Override
				public void onSuccess(String objectKey) {
					voiceUrl=Url.PictureAddress+objectKey;
					mHandler.sendEmptyMessage(id);

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
	// 异步删除照片
	public void asyncDelete(String objectKey) {
		OSSData data = MyApplication.ossService.getOssData(bucket,objectKey);
		data.deleteInBackground(new DeleteCallback() {

			@Override
			public void onSuccess(String objectKey) {
				Message msg=new Message();
				msg.what=DELETEPICTURE;
				msg.obj=objectKey;
				mHandler.sendMessage(msg);


			}

			@Override
			public void onFailure(String objectKey, OSSException ossException) {
				Log.e(TAG, "[onFailure] - delete " + objectKey + " failed!\n" + ossException.toString());
				// HandleException.handleException(ossException);
			}
		});
	}
	// 异步删除声音
	public void asyncDeleteVoice(String objectKey) {
		OSSData data = MyApplication.ossService.getOssData(bucket,objectKey);
		data.deleteInBackground(new DeleteCallback() {

			@Override
			public void onSuccess(String objectKey) {
				mHandler.sendEmptyMessage(DELETEVOICE);
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
		case R.id.iv_picture:
			View view= getLayoutInflater().inflate(R.layout.include_image_is_delete,null);//添加图片语音定位布局
			SimpleDraweeView imageView=(SimpleDraweeView) view.findViewById(R.id.iv_picture);
			imageView.setBackground(null);
			imageView.setImageURI(Uri.parse(pictureList.get(msg.arg1)));
			ImageView deleteView=(ImageView) view.findViewById(R.id.iv_delete);
			deleteView.setTag(pictureList.get(msg.arg1));
			deleteView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int index=pictureList.indexOf(v.getTag());
					String objectKey=pictureList.get(index).substring(pictureList.get(index).lastIndexOf("/")+1,
							pictureList.get(index).length());
					asyncDelete(objectKey);
				}


			});
			((LinearLayout)findViewById(R.id.ly_picture)).addView(view);
			break;
		case DELETEPICTURE:
			String url=Url.PictureAddress+msg.obj;
			int index=pictureList.indexOf(url);
			((LinearLayout)findViewById(R.id.ly_picture)).removeViewAt(index);
			pictureList.remove(index);
			break;
		case DELETEVOICE:
			voiceUrl="";
			findViewById(R.id.ly_voice).setVisibility(View.GONE);
			break;

		case R.id.btn_speak:
			findViewById(R.id.iv_add).setEnabled(true);
			findViewById(R.id.tv_send).setEnabled(true);
			findViewById(R.id.btn_speak).setVisibility(View.GONE); 
			findViewById(R.id.ly_small_menu).setVisibility(View.VISIBLE);
			findViewById(R.id.ly_voice).setVisibility(View.VISIBLE);
			((TextView)findViewById(R.id.tv_voice_time)).setText(mRecordCount+"");
			break;

		default:
			break;
		}
		return false;
	}
}
