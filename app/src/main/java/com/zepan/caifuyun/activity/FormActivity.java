package com.zepan.caifuyun.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
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
import com.zepan.android.util.StringUtil;
import com.zepan.android.widget.AchievePhotoImageView;
import com.zepan.android.widget.AchievePhotoImageView.OnPhotoDeleteCallBack;
import com.zepan.android.widget.AlphaImageView;
import com.zepan.android.widget.CheckableEditText;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.application.MyApplication;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.constants.Url;
import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
/**
 * 报单
 * @author duanjie
 *
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class FormActivity extends BaseActivity implements OnClickListener,OnPhotoDeleteCallBack,Callback{
	private String contractSignatureUrl;
//	private String identityCardUrl;
//	private String receiptUrl;
//	private String bankUrl;
	private TextView productName;
	private TextView customerName;
	private CheckableEditText makePayment;
	private RadioButton directBtn,channelBtn;
	private int defaultChannel=1;
	private OSSBucket bucket= MyApplication.ossService.getOssBucket("canaanwealth");

	private LinearLayout contractSignaturePage;
	private List<String> urlList=new ArrayList<String>();
	private Handler mHandler=new Handler(this);
	private int contractIndex;
	private int productId;
	private int customId;
	private Map<Integer, String> pictureMap=new ArrayMap<Integer, String>();
	private final static int CARD=0x100;
	private final static int DELETECARD=0x101;
	private final static int RECEIPT=0x102;
	private final static int DELETERECEIPT=0x103;
	private final static int BANK=0x104;
	private final static int DELETEBANK=0x105;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_form);
		setHeaderFields(0, R.string.form, 
				0,R.drawable.ic_action_back, 0,0);
		((AlphaImageView)findViewById(R.id.iv_left)).setClickAlpha(150, true, false);
		initView();
		initData();
	}
	private void initData(){
		Intent intent=getIntent();
		if(intent.hasExtra("productId")){
			productId=intent.getIntExtra("productId",-1);
		}
		if(intent.hasExtra("productName")){
			((TextView)findViewById(R.id.tv_product_name)).setText(intent.getStringExtra("productName"));

		}
	}
	private void initView(){
		/***
		 * 提交报单
		 */
		findViewById(R.id.submit_report).setOnClickListener(this);
		/***
		 * 选择客户
		 */
		findViewById(R.id.select_customername).setOnClickListener(this);
		productName=(TextView) findViewById(R.id.et_product_name);
		customerName=(TextView) findViewById(R.id.select_customername);
		makePayment=(CheckableEditText) findViewById(R.id.ed_make_payment);
		directBtn=(RadioButton) findViewById(R.id.rb_direct_entry);
		channelBtn=(RadioButton) findViewById(R.id.rb_channel_entry);
		contractSignaturePage=(LinearLayout) findViewById(R.id.ly_contract_signature_page);
		findViewById(R.id.rl_choice_channel).setVisibility(View.GONE); 
		findViewById(R.id.fl_line).setVisibility(View.GONE);
		((RadioGroup)findViewById(R.id.rg_channel_order)).setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_direct_entry:
					defaultChannel=1;
					findViewById(R.id.rl_choice_channel).setVisibility(View.GONE); 
					findViewById(R.id.fl_line).setVisibility(View.GONE); 
					break;
				case R.id.rb_channel_entry:
					defaultChannel=2;
					findViewById(R.id.rl_choice_channel).setVisibility(View.VISIBLE); 
					findViewById(R.id.fl_line).setVisibility(View.VISIBLE); 
					break;

				default:
					break;
				}

			}
		});
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.select_customername:
			Intent  customerIntent=new Intent(FormActivity.this,SelectCustomerTwoActivity.class);
			customerIntent.putExtra("selectCustom", "selectCustom");
			startActivityForResult(customerIntent, 0);
			break;
		case R.id.submit_report:
			if(!((CheckableEditText)findViewById(R.id.ed_make_payment)).check()){
				return;
			}
			if(contractSignatureUrl==null){
				printToast("合同签字页没有选择");
				return;
			}
			if(pictureMap.get(R.id.iv_identity_card)==null){
				printToast("身份证没有选择");
				return;
			}
			if(pictureMap.get(R.id.iv_receipt)==null){
				printToast("打款凭据没有选择");
				return;
			}
			if(pictureMap.get(R.id.iv_bank_card)==null){
				printToast("银行卡没有选择");
				return;
			}
			if(makePayment.check()){
				getCreateReport();
			}
			break;
		default:
			break;
		}
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub<br>
		if(data != null){
			if(data.hasExtra("uri")){
				Uri uri = data.getParcelableExtra("uri");
				int viewId = data.getIntExtra("viewId",0);
				switch (viewId) {
				case R.id.iv_contract_signature_page:
					loadAndShowContractSignaturePiture(uri,R.id.iv_contract_signature_page);
					break;
				case R.id.iv_identity_card:
					loadAndShowPiture(uri,R.id.iv_identity_card);
					break;
				case R.id.iv_receipt:
					loadAndShowPiture(uri,R.id.iv_receipt);
					break;
				case R.id.iv_bank_card:
					loadAndShowPiture(uri,R.id.iv_bank_card);
					break;
				default:
					break;
				}
			}else if(data.hasExtra("customId")){
				customId=data.getIntExtra("customId", -1);
				((TextView)findViewById(R.id.select_customername)).setText(data.getStringExtra("customName"));
				((TextView)findViewById(R.id.select_customername)).setBackground(null);
			}



		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	private void loadAndShowPiture(Uri uri,int id){
		if (uri != null) {
			///data/data/com.zepan.caifuyun/cache/cropped
			File file = new File(StringUtil.getPath(this,uri));
			if (file != null && file.exists()) {
				asynUpload(file,id,uri);
			}
		}
	}
	//Drawable drwable = AchievePhotoImageView.this.getDrawable();
	private void loadAndShowContractSignaturePiture(Uri uri,int id){
		if (uri != null) {
			///data/data/com.zepan.caifuyun/cache/cropped
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
					switch (id) {
					case R.id.iv_contract_signature_page:
						contractSignatureUrl=Url.PictureAddress+objectKey;
						urlList.add(contractSignatureUrl);
						mHandler.sendEmptyMessage(0xabc);
						break;
					case R.id.iv_identity_card:
						//identityCardUrl=Url.PictureAddress+objectKey;
						pictureMap.put(id, objectKey);
						mHandler.sendEmptyMessage(CARD);
						break;
					case R.id.iv_receipt:
						//receiptUrl=Url.PictureAddress+objectKey;
						pictureMap.put(id, objectKey);
						mHandler.sendEmptyMessage(RECEIPT);
						break;
					case R.id.iv_bank_card:
						//bankUrl=Url.PictureAddress+objectKey;
						pictureMap.put(id, objectKey);
						mHandler.sendEmptyMessage(BANK);
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
	public void asyncDelete(final int id,String objectKey) {

		OSSData data =MyApplication.ossService.getOssData(bucket,objectKey);
		data.deleteInBackground(new DeleteCallback() {

			@Override
			public void onSuccess(String objectKey) {
				switch (id) {
				case R.id.iv_contract_signature_page:
					mHandler.sendEmptyMessage(0xaba);
					break;
				case R.id.iv_identity_card:
					mHandler.sendEmptyMessage(DELETECARD);
					break;
				case R.id.iv_receipt:
					mHandler.sendEmptyMessage(DELETERECEIPT);
					break;
				case R.id.iv_bank_card:
					mHandler.sendEmptyMessage(DELETEBANK);
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

	private void getCreateReport(){
		request(Url.CreateReport, getCreateReportParamJson(),Method.POST, new IRequestCallBack() {

			@Override
			public void onResponse(JSONObject response) {
				try {
					if(response.getInt("status")==0){

						printToast("申请报单成功");
					}else{
						printToast(response.getString("message"));
					}
					finish();
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
	}
	private JSONObject getCreateReportParamJson(){
		if(directBtn.isChecked()){
			defaultChannel=1;
		}
		if(channelBtn.isChecked()){
			defaultChannel=2;
		}
		JSONObject json=new JSONObject();
		try {
			json.put("token",getStringLocalData("token"));
			json.put("uid",getStringLocalData("uid"));//创建用户号
			json.put("tid",getStringLocalData("tid"));//创建用户号
			json.put("productId",productId);//产品ID
			json.put("accountId",customId);//客户号
			json.put("fromType",defaultChannel);//报单来源类型。1:直客，2:渠道
			json.put("channelId","");//渠道号
			json.put("orderId","");//订单号，可为空
			json.put("amount", makePayment.getText().toString());//打款金额
			json.put("contractPicUrl",contractSignatureUrl);//合同拍照图片url，多个以逗号分开
			json.put("accountIdPicUrl",pictureMap.get(R.id.iv_identity_card));//身份证拍照图片url，多个以逗号分开
			json.put("paySlipPicUrl",pictureMap.get(R.id.iv_receipt));//付款凭证拍照图片ur
			json.put("bankCardPicUrl",pictureMap.get(R.id.iv_bank_card));//银行卡拍照图片url
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	@Override
	public void deleteItemClick(int viewId, String imageUrl) {
		switch (viewId) {
		case R.id.iv_contract_signature_page:
			if(urlList.contains(imageUrl)){
				contractIndex=urlList.indexOf(imageUrl);
				String a[] = imageUrl.split("/"); 
				asyncDelete(R.id.iv_contract_signature_page,a[3]);
			}
			break;
		case R.id.iv_identity_card:
			asyncDelete(R.id.iv_identity_card,pictureMap.get(R.id.iv_identity_card));
			break;
		case R.id.iv_receipt:
			asyncDelete(R.id.iv_receipt,pictureMap.get(R.id.iv_receipt));
			break;
		case R.id.iv_bank_card:
			asyncDelete(R.id.iv_bank_card,pictureMap.get(R.id.iv_bank_card));
			break;

		default:
			break;
		}




	}
	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case 0xabc:
			AchievePhotoImageView imageView=(AchievePhotoImageView) View.inflate(FormActivity.this, R.layout.linearlayout_achievephotoimage, null);
			imageView.setOnPhotoDeleteCallBack(FormActivity.this);
			imageView.setImageURI(Uri.parse(contractSignatureUrl));
			LinearLayout.LayoutParams  lp=new LinearLayout.LayoutParams (150,150);
			lp.setMargins(20, 0, 20, 0);
			imageView.setLayoutParams(lp); 
			contractSignaturePage.addView(imageView,urlList.size()-1);
			break;
		case 0xaba:
			contractSignaturePage.removeViewAt(contractIndex);
			urlList.remove(contractIndex);
			break;
		case CARD:
			ImageView imageCard = (ImageView) findViewById(R.id.iv_identity_card);
			String identityCardUrl=Url.PictureAddress+pictureMap.get(R.id.iv_identity_card);
			imageCard.setImageURI(Uri.parse(identityCardUrl));
			break;
		case RECEIPT:
			ImageView imageReceipt = (ImageView) findViewById(R.id.iv_receipt);
			String receiptUrl=Url.PictureAddress+pictureMap.get(R.id.iv_receipt);
			imageReceipt.setImageURI(Uri.parse(receiptUrl));
			break;
		case BANK:
			ImageView imageBank = (ImageView) findViewById(R.id.iv_bank_card);
			String bankUrl=Url.PictureAddress+pictureMap.get(R.id.iv_bank_card);
			imageBank.setImageURI(Uri.parse(bankUrl));
			break;
		case DELETECARD:
			pictureMap.put(R.id.iv_identity_card, null);
			break;
		case DELETERECEIPT:
			pictureMap.put(R.id.iv_receipt, null);
			break;
		case DELETEBANK:
			pictureMap.put(R.id.iv_bank_card, null);
			break;
		default:
			break;
		}
		return false;
	}
}
