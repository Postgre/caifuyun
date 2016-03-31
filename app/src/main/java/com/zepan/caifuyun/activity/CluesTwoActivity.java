package com.zepan.caifuyun.activity;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.zepan.android.sdk.IRequestCallBack;
import com.zepan.android.sdk.JsonUtil;
import com.zepan.android.widget.AlphaImageView;
import com.zepan.android.widget.AlphaTextView;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.constants.Url;
import com.zepan.caifuyun.entity.Clues;
import com.zepan.caifuyun.entity.Tags;
import com.zepan.caifuyun.widget.DialogBottom;
import com.zepan.caifuyun.widget.DialogBottom.OnItemClick;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 线索的第二个界面
 * @author duanjie
 *
 */
public class CluesTwoActivity extends BaseActivity implements OnClickListener{
	private Clues clues;
	private TextView  tv_company,
	tv_source,tv_other_name,tv_mobile,tv_email,tv_tags;
	private DialogBottom dialogBottom;
	private  CheckBox iv_star;
	private  RadioGroup  mRg;
	private   RadioButton   rabtn_order ;//丢弃客户
	private   RadioButton   rabtn_form ;//升级为客户
	private ImageView iv_head;
	int cluesId;
	private ArrayList<Tags> tagsList=new ArrayList<Tags>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clues_two);
		setHeaderFields(0, R.string.clues, 
				0,R.drawable.ic_action_back, 0,R.drawable.ic_action_more);
		initView();
		initData();
	}
	@Override
	protected void onStart() {
		getCluesInfo();
		super.onStart();
	}

	private void initData() {
		Intent intent=getIntent();
		cluesId=intent.getIntExtra("cluesId",-1);
		
	}

	private void initView() {
		iv_head=(ImageView)findViewById(R.id.iv_head);
		iv_star=(CheckBox)findViewById(R.id.iv_star);
		((CheckBox)findViewById(R.id.iv_star)).setOnClickListener(this);
		((AlphaImageView)findViewById(R.id.iv_right)).setOnClickListener(this);
		tv_mobile=(TextView)findViewById(R.id.tv_mobile);
		((ImageView)findViewById(R.id.iv_sms)).setOnClickListener(this);
		((ImageView)findViewById(R.id.iv_phone)).setOnClickListener(this);
		((ImageView)findViewById(R.id.iv_email)).setOnClickListener(this);
		findViewById(R.id.rl_label).setOnClickListener(this);
		mRg=(RadioGroup) findViewById(R.id.RadioGroup_bottom);//RadioGroup 
		rabtn_order=(RadioButton) findViewById(R.id.rabtn_order);//丢弃客户
		rabtn_form=(RadioButton) findViewById(R.id.rabtn_form);//升级为客户
		mRg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int SelectedId) {
				rabtn_order.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(clues.getHighSeaStatus()==1){//个人线索   
							getLeadDrop();
						}else{
							//丢弃客户    公司的才有丢弃 ，个人的话只有删除
							getLeadRelease();	
						}
					}
				});

				rabtn_form.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						//升级为客户
						getLeadConvert();
					}
				});

			}
		});
	}

	private void getCluesInfo(){
		String url = Url.LeadInfo;
		JSONObject paramJson=new JSONObject();
		try {
			String token = URLEncoder.encode(getStringLocalData("token"), "UTF-8");
			paramJson.put("token",token);
			paramJson.put("uid",getStringLocalData("uid"));
			paramJson.put("tid",getStringLocalData("tid"));
			paramJson.put("id",cluesId);
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request(url, paramJson, Method.GET, new IRequestCallBack() {

			@Override
			public void onResponse(JSONObject response) {
				cluesInfoResult(response);
			}

			@Override
			public void onErrorResponse(String errorMessage) {
				// TODO Auto-generated method stub

			}
		});
	}
	private void cluesInfoResult(JSONObject response){
		try {
			if(response.getInt("status")==0){
				JSONObject jsonLead =response.getJSONObject("lead");
				clues=(Clues) JsonUtil.jsonToEntity(jsonLead, Clues.class);
				if(clues.getStatus()==2){   //Status   1  是普通线索   ic_star_small
					iv_star.setBackgroundResource(R.drawable.ic_star_small_marked);//亮
					iv_star.setSelected(true);
					//线索状态更新接口
				}else {
					iv_star.setBackgroundResource(R.drawable.ic_star_small);//不亮
					iv_star.setSelected(false);
				}
				((TextView)findViewById(R.id.tv_name)).setText(clues.getName());//名字
				((TextView)findViewById(R.id.tv_relation)).setText(clues.getPosition());//合伙人
				((TextView)findViewById(R.id.tv_company)).setText(clues.getCompany());//公司
				((TextView)findViewById(R.id.tv_source)).setText(clues.getSource());//线索来源
				tv_mobile.setText(clues.getMobile());	//手机号码
				((TextView)findViewById(R.id.tv_email)).setText(clues.getEmail());   //邮箱
				//标签
				String lableString="";
				if(clues.getTags()!=null){
					for (int i=0;i<clues.getTags().size();i++) { 
						if(i==clues.getTags().size()-1){
							lableString+=clues.getTags().get(i).getName();
						}else{
							lableString+=clues.getTags().get(i).getName()+" ";
						}
						tagsList.add(clues.getTags().get(i));
					} 
					((TextView)findViewById(R.id.tv_tags)).setText(lableString);  
				}
				((TextView)findViewById(R.id.ed_note)).setText(clues.getComment());  //备注
				if(clues.getDbcVarchar3()!=null&&!"".equals(clues.getDbcVarchar3().trim())){
					if(clues.getDbcVarchar3().substring(0, 4).equals("http")
							||clues.getDbcVarchar3().substring(0, 4).equals("HTTP")){
						Uri uri=Uri.parse(clues.getDbcVarchar3());
						iv_head.setImageURI(uri);
					}else{
						iv_head.setImageResource(R.drawable.ic_default_head);
					}
				}else{
					iv_head.setImageResource(R.drawable.ic_default_head);
				}

				//clues.getHighSeaStatus()==1  发布者
				if(clues.getHighSeaStatus()==1){//1   个人线索   2公司
					((TextView)findViewById(R.id.tv_other_name)).setText("个人线索");
				}else{
					((TextView)findViewById(R.id.tv_other_name)).setText("公司");
				}

				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}


	/**
	 * 获取升级成客户接口的请求
	 */
	private void  getLeadConvert(){
		String token;
		try {
			token = URLEncoder.encode(getStringLocalData("token"), "UTF-8");
			String uid=getStringLocalData("uid");
			String tid=getStringLocalData("tid");
			String url=Url.LeadConvert+"?token="+token+"&leadId="+cluesId+"&uid="+uid+"&tid="+tid;
			request(url,null, Method.GET,new IRequestCallBack() {//    +"&customData="+json+""  leadId   线索ID
				@Override
				public void onResponse(JSONObject response) {
					// TODO Auto-generated method stub
					getLeadReleaseResult(response);
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



	/**
	 * 丢弃客户的请求
	 */
	private void  getLeadRelease(){
		String token;
		try {
			token = URLEncoder.encode(getStringLocalData("token"), "UTF-8");
			String tid=getStringLocalData("tid");
			String uid=getStringLocalData("uid");
			String url=Url.LeadRelease+"?token="+token+"&leadIds="+cluesId+"&tid="+tid+"&uid="+uid;
			request(url,null, Method.GET,new IRequestCallBack() {//    +"&customData="+json+""  leadId   线索ID
				@Override
				public void onResponse(JSONObject response) {
					// TODO Auto-generated method stub
					getLeadDropResult(response);
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






	private void getLeadUpdateStatus(JSONObject response){
		try {
			if(response.getInt("status")==0){
				iv_star.setSelected(!iv_star.isSelected());
				if(iv_star.isSelected()){
					iv_star.setBackgroundResource(R.drawable.ic_star_small_marked);
					Toast.makeText(this, "已标记为重点线索", Toast.LENGTH_LONG).show();
					//iv_star.setBackgroundResource(R.drawable.ic_star_small_marked);
				}else{
					//iv_star.setBackgroundResource(R.drawable.ic_star_small);
					iv_star.setBackgroundResource(R.drawable.ic_star_small);
				}


			}else if(response.getInt("status")==1){
				String message=response.getString("message");
				printToast(message);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}



	private void getLeadDropResult(JSONObject response){
		try {
			if(response.getInt("status")==0){
				this.finish();
			}else if(response.getInt("status")==1){
				String message=response.getString("message");
				printToast(message);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void getLeadReleaseResult(JSONObject response){
		try {
			if(response.getInt("status")==0){
			}else if(response.getInt("status")==1){
				String message=response.getString("message");
				printToast(message);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 线索状态更新接口的请求
	 */
	private void  getLeadUpdateStatus(){
		String token;
		int star=iv_star.isSelected()? 1:2;
		try {
			token = URLEncoder.encode(getStringLocalData("token"), "UTF-8");
			String tid=getStringLocalData("tid");
			String uid=getStringLocalData("uid");
			String url=Url.LeadUpdateStatus+"?token="+token
					+"&leadId="+cluesId+""+"&status="+star+"&tid="+tid+"&uid="+uid;
			request(url,null, Method.GET,new IRequestCallBack() {//    +"&customData="+json+""  leadId   线索ID
				@Override
				public void onResponse(JSONObject response) {
					// TODO Auto-generated method stub
					getLeadUpdateStatus(response);
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_right:
			final DialogBottom dialog =new DialogBottom(this);
			if(clues.getHighSeaStatus()==1){
				//1 是个人线索
				String[] array={"编辑","取消"};
				dialog.setArray(array);
			}else{
				//公司分配的线索
				String[] array={"编辑","报错","取消"};
				dialog.setArray(array);
			}
			dialog.setOnItemClick(new OnItemClick() {

				@Override
				public void itemClick(AdapterView<?> parent, View view, int position, long id) {
					switch (position) {
					//编辑
					case 0:
						dialog.dismiss();
						Intent  intentEdit=new  Intent(CluesTwoActivity.this,NewCluesActivity.class);
						intentEdit.putExtra("tag", "change");
						intentEdit.putExtra("clues", clues);
						startActivity(intentEdit);
						break;
						//删除
						//					case 1:
						//						dialog.dismiss();
						//						getLeadDrop();
						//						break;
						//取消或者报错
					case 1:
						dialog.dismiss();
						if(clues.getHighSeaStatus()!=1){
							Intent   intentError=new Intent(CluesTwoActivity.this,AnErrorActivity.class);
							intentError.putExtra("clues", clues);
							startActivity(intentError);
						}

						break;
						//取消
					case 2:
						dialog.dismiss();
						break;

					default:
						break;
					}

				}
			});
			dialog.show();

			break;
		case R.id.iv_sms://短信
			Intent intent = new Intent();
			//系统默认的action，用来打开默认的短信界面
			intent.setAction(Intent.ACTION_SENDTO);
			//需要发短息的号码
			intent.setData(Uri.parse("smsto:"+tv_mobile.getText().toString()));
			startActivity(intent);
			break;
		case R.id.iv_phone://电话
			Uri uri = Uri.parse("tel:"+tv_mobile.getText().toString()); 
			Intent it = new Intent(Intent.ACTION_DIAL, uri);   
			startActivity(it); 
			break;
		case R.id.iv_email://邮件
			//邮件
			Intent data=new Intent(Intent.ACTION_SENDTO);  
			//到时候该成动态的
			data.setData(Uri.parse("mailto:"+((TextView)findViewById(R.id.tv_email)).getText().toString()));  
			data.putExtra(Intent.EXTRA_SUBJECT, "这是标题");  
			data.putExtra(Intent.EXTRA_TEXT, "这是内容");  
			startActivity(data);  
			break;
		case R.id.iv_star://星星  判断是否为重点线索     iv_star为1的话，为重点线索
			getLeadUpdateStatus();
			break;
		case R.id.rl_label://标签
			Intent intentLabel = new Intent(this, LabelActivity.class);
			intentLabel.putExtra("accountId",cluesId);
			intentLabel.putParcelableArrayListExtra("tagsList",tagsList);
			startActivity(intentLabel);
			break;
			
		default:
			break;
		}

	}




	/**
	 *删除个人线索（删除公海池线索）接口
	 */
	private void  getLeadDrop(){
		String token;
		try {
			token = URLEncoder.encode(getStringLocalData("token"), "UTF-8");
			String uid=getStringLocalData("uid");
			String tid=getStringLocalData("tid");
			String url=Url.LeadDrop+"?token="+token+"&leadIds="+cluesId+"&uid="+uid+"&tid="+tid;
			request(url,null, Method.GET,new IRequestCallBack() {//    +"&customData="+json+""  leadId   线索ID
				@Override
				public void onResponse(JSONObject response) {
					// TODO Auto-generated method stub
					getLeadDropResult(response);
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


}
