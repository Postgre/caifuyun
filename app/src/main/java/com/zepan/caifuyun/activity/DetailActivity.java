package com.zepan.caifuyun.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.zepan.android.sdk.IRequestCallBack;
import com.zepan.android.sdk.JsonUtil;
import com.zepan.android.widget.AlphaImageView;
import com.zepan.android.widget.AlphaTextView;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.adapter.InfoAdapter;
import com.zepan.caifuyun.adapter.SheetsAdapter;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.constants.Url;
import com.zepan.caifuyun.entity.IdentityInfo;
import com.zepan.caifuyun.entity.ProfessionInfo;
import com.zepan.caifuyun.entity.User;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 详情界面
 * @author long
 *
 */
public class DetailActivity extends BaseActivity {
	private String[] selfArray={"生日","性别","民族","居住地","身份证","行业","公司规模"};
	private InfoAdapter selfInfoAdapter;
	private List<String> selfList=new ArrayList<String>();
	private String[] sheetsArray={"年收入","可用资产","现金资产","股票资产","基金资产","信托资产","住房","房产投资","海外房产"};
	private SheetsAdapter sheetsAdapter;
	private List<String> sheetsList=new ArrayList<String>();
	private String[] liabilitiesArray={"房产负债","事业负债","其他负债"};
	private SheetsAdapter liabilitiesAdapter;
	private List<String> liabilitiesList=new ArrayList<String>();
	private String[] familyInfoArray={"家庭住址","婚姻状况","家庭成员"};
	private InfoAdapter familyInfoAdapter;
	private List<String> familyInfoList=new ArrayList<String>();
	private String[] familyMemberInfoArray={"关系","姓名","电话","关系","生日","关系","生日"};
	private InfoAdapter familyMemberInfoAdapter;
	private List<String> familyMemberInfoList=new ArrayList<String>();
	private int currentTag;
	private static final int INDIVIDUAL=0x2000;
	private static final int ORGANIZATION=0x2001;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		setHeaderFields(0, R.string.detail, R.string.edite,R.drawable.ic_action_back, 0,0);
		initView();
		initData();
	}
	private void initData(){
		Intent intent=getIntent();
		if(intent.hasExtra("detailId")){
			getInfo(intent.getIntExtra("detailId", -1));
		}
	}
	private void initView(){

		((AlphaImageView)findViewById(R.id.iv_left)).setClickAlpha(150, true, false);
		((AlphaTextView)findViewById(R.id.tv_right)).setClickAlpha(100);
		//个人信息
		ListView selfInfoListView=(ListView) findViewById(R.id.list_self_info);
//		selfList.add("1974-3-21");
//		selfList.add("男");
//		selfList.add("汉");
//		selfList.add("上海市");
//		selfList.add("411325198911061371");
//		selfList.add("金融业");
//		selfList.add("中型企业");
		selfInfoAdapter=new InfoAdapter(this, selfArray,selfList);
		selfInfoListView.setAdapter(selfInfoAdapter);
		selfInfoListView.setDivider(null);
		//资产状况
		ListView sheetsListView=(ListView) findViewById(R.id.list_balance_sheets);
		sheetsList.add("120");
		sheetsList.add("500");
		sheetsList.add("60");
		sheetsList.add("80");
		sheetsList.add("200");
		sheetsList.add("200");
		sheetsList.add("400");
		sheetsList.add("300");
		sheetsList.add("300");
		sheetsAdapter=new SheetsAdapter(this, sheetsArray,sheetsList);
		sheetsAdapter.setColor(R.drawable.seekbar_style_green);
		sheetsListView.setAdapter(sheetsAdapter);
		sheetsListView.setDivider(null);

		//负债状况
		ListView liabilitiesListView=(ListView) findViewById(R.id.list_liabilities);
		liabilitiesList.add("120");
		liabilitiesList.add("200");
		liabilitiesList.add("50");
		liabilitiesAdapter=new SheetsAdapter(this, liabilitiesArray,liabilitiesList);
		liabilitiesAdapter.setColor(R.drawable.seekbar_style_red);
		liabilitiesListView.setAdapter(liabilitiesAdapter);
		liabilitiesListView.setDivider(null);

		//		家庭信息
		ListView familyInfoListView=(ListView) findViewById(R.id.list_family_info);
		familyInfoList.add("120");
		familyInfoList.add("200");
		familyInfoList.add("50");
		familyInfoAdapter=new InfoAdapter(this, familyInfoArray,familyInfoList);
		familyInfoListView.setAdapter(familyInfoAdapter);
		familyInfoListView.setDivider(null);
		//		家庭成员信息		
		ListView familyMemberInfoListView=(ListView) findViewById(R.id.list_family_member_info);
		familyMemberInfoList.add("120");
		familyMemberInfoList.add("200");
		familyMemberInfoList.add("50");
		familyMemberInfoList.add("120");
		familyMemberInfoList.add("200");
		familyMemberInfoList.add("50");
		familyMemberInfoList.add("120");
		familyMemberInfoAdapter=new InfoAdapter(this, familyMemberInfoArray,familyMemberInfoList);
		familyMemberInfoListView.setAdapter(familyMemberInfoAdapter);
		familyMemberInfoListView.setDivider(null);

	}
	private void getInfo(int normalId){
		try {

			String token=URLEncoder.encode(getStringLocalData("token"), "UTF-8");
			String uid=getStringLocalData("uid");
			String tid=getStringLocalData("tid");
			String url=Url.GetInfo+"?token="+token+"&id="+normalId+"&uid="+uid+"&tid="+tid;
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
				JSONObject jsonAccount=bodyJson.getJSONObject("account");

				User user=(User) JsonUtil.jsonToEntity(jsonAccount, User.class);
				if(user!=null){
					((TextView)findViewById(R.id.tv_email)).setText(user.getEmail());//邮箱
//					((TextView)findViewById(R.id.tv_work_number)).setText(user.get);//工作电话
//					((TextView)findViewById(R.id.tv_phone_number)).setText(user.getEmail());//手机号
					
					if(jsonAccount.has("dbcTextarea2")){
						if(!"".equals(jsonAccount.getString("dbcTextarea2"))){
							String db2=jsonAccount.getString("dbcTextarea2");
							JSONObject jsonDbc2=new JSONObject(db2);//身份信息
							IdentityInfo profess=(IdentityInfo) JsonUtil.jsonToEntity(jsonDbc2, IdentityInfo.class);
							user.setIdentityInformation(profess);

							
							selfList.add(user.getIdentityInformation().getBirthday());
							//selfList.add(user.getIdentityInformation().getSex());
							selfList.add(user.getIdentityInformation().getNation());
							selfList.add(user.getIdentityInformation().getResidence());
							selfList.add(user.getIdentityInformation().getPaperscode());

						}

					}
					if(jsonAccount.has("dbcTextarea3")){
						if(!"".equals(jsonAccount.getString("dbcTextarea3"))){
							String db3=jsonAccount.getString("dbcTextarea3");
							JSONObject jsonDbc3=new JSONObject(db3);//职业信息
							ProfessionInfo profess=(ProfessionInfo) JsonUtil.jsonToEntity(jsonDbc3, ProfessionInfo.class);
							user.setProfessionalInformation(profess);
							selfList.add(user.getProfessionalInformation().getCompanytype());
							selfList.add(user.getProfessionalInformation().getCompanyscale());
							
						}

					}
					selfInfoAdapter.notifyDataSetChanged();

				}





			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}
