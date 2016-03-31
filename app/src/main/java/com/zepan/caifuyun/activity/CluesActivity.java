package com.zepan.caifuyun.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.android.volley.Request.Method;
import com.zepan.android.sdk.IRequestCallBack;
import com.zepan.android.sdk.JsonUtil;
import com.zepan.android.util.StringUtil;
import com.zepan.android.widget.AlphaImageView;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.adapter.CluesAdapter;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.constants.Code;
import com.zepan.caifuyun.constants.Url;
import com.zepan.caifuyun.entity.Clues;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;


/**
 * 线索
 * @author duanjie
 *
 */
public class CluesActivity extends BaseActivity implements OnClickListener{
	private  CluesAdapter   mCluesAdapter;
	private List<Clues> mDataList;
	private ListView listDebate;
	private PullToRefreshListView mPullListView = null;
	private int mPage = Code.FIRST_PAGE;
	private int mSize = Code.PAGE_SIZE;
	private RadioGroup mRadioGroup;
	private  RadioButton mAll;//全部线索   rb_all_clues
	private  RadioButton mKeyClues;//重点线索   rb_key_clues
	private  RadioButton mGeneralClues;//一般线索   rb_general_clue
	private final static int allTag=0x200;
	private final static int keycluesTag=0x201;
	private final static int generalcluesTag=0x202;
	int currentTag=allTag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clues);
		setHeaderFields(0, R.string.clues, 
				0,R.drawable.ic_action_back,0,R.drawable.ic_action_add);
		initView();
	}

	@Override
	protected void onStart() {
		getLeadGetUserLeads();
		super.onStart();
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initView() {
		((AlphaImageView)findViewById(R.id.iv_right)).setClickAlpha(150, true, false);
		((AlphaImageView)findViewById(R.id.iv_left)).setClickAlpha(150, true, false);
		((AlphaImageView)findViewById(R.id.iv_right)).setOnClickListener(this);
		mPullListView = (PullToRefreshListView)findViewById(android.R.id.list);
		listDebate = mPullListView.getRefreshableView();
		listDebate.setDivider(null);
		mDataList = new ArrayList<Clues>();
		mCluesAdapter=new CluesAdapter(CluesActivity.this,mDataList);
		listDebate.setAdapter(mCluesAdapter);
		listDebate.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Clues  clues=(Clues)mDataList.get(position-1);
				Intent  intent=new Intent(CluesActivity.this,CluesTwoActivity.class);
				intent.putExtra("cluesId",clues.getId());
				CluesActivity.this.startActivity(intent);
			}
		});
		mPullListView.setMode(Mode.PULL_FROM_END);
		mPullListView.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				mPage++;
				getLeadGetUserLeads();
			}
		});

		mRadioGroup=(RadioGroup)findViewById(R.id.RadioGroup_bottom);
		mAll=(RadioButton)findViewById(R.id.rb_all_clues);
		mAll.setChecked(true);
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				//获取变更后的选中项的ID
				//mAll=(RadioButton)view.findViewById(R.id.rbtn_all);
				if(arg1==R.id.rb_all_clues){
					currentTag=allTag;//全部线索
				}else if(arg1==R.id.rb_key_clues){
					currentTag=keycluesTag;//重点线索
				}
//				else if(arg1==R.id.rb_general_clue){
//					currentTag=generalcluesTag;//一般线索
//				}
				mPage=1;
				getLeadGetUserLeads();
			}
		});

	}

	/**
	 * 请求  LeadGetUserLeads
	 */
	private void  getLeadGetUserLeads(){
		String token;
		try {
			token=URLEncoder.encode(getStringLocalData("token"), "UTF-8");
			//			token = URLEncoder.encode("uc0oj3R6hGpsCHVks8Ddt3QVcaRW9vwIhm2L4vw3Cb2JLbLgozKWhw==", "UTF-8");
			JSONObject json = new JSONObject();
			try {

				/*json.put("page",mPage);
				json.put("size",mSize); */
				JSONObject changeInfoJson = new JSONObject();
				json.put("conditions",changeInfoJson);
				if(currentTag==keycluesTag){
					changeInfoJson.put("status",2);//  2是重点 
				}
//				else if(currentTag==generalcluesTag){
//					changeInfoJson.put("status",1);// 1  是普通
//				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String tid=getStringLocalData("tid");
			String uid=getStringLocalData("uid");
			String url=Url.LeadGetUserLeads+"?token="+token+
					"&customData="+json+""+"&size="+mSize+"&page="+mPage+"&uid="+uid+"&tid="+tid;
			request(url,null, Method.GET,new IRequestCallBack() {
				@Override
				public void onResponse(JSONObject response) {
					// TODO Auto-generated method stub
					getLeadGetUserLeadsResult(response);
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








	private void getLeadGetUserLeadsResult(JSONObject response){
		try {
			if(response.getInt("status")==0){
				List<Clues> list = new ArrayList<Clues>();
				JSONObject json = response;
				JSONObject jsons = json.getJSONObject("leads");
				//JSONObject jsonOne=jsons.getJSONObject("body");
				JSONArray jsonArray=jsons.getJSONArray("leads");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonChildParam = jsonArray.getJSONObject(i);
					Clues c=(Clues)JsonUtil.jsonToEntity(jsonChildParam, Clues.class);
					list.add(c);// 添加在存储信息列表
				}
				refreshListView(list);
				mPullListView.onRefreshComplete();
				setLastUpdateTime();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_right://CluesTwoActivity.class  线索的第二个界面
			//NewCluesActivity  新建线索
			Intent intentCluesTwo=new Intent(CluesActivity.this,NewCluesActivity.class);
			startActivity(intentCluesTwo);
			break;
		default:
			break;
		}

	}
	private void refreshListView(List<Clues> list) {
		if(mPage == 1){
			mDataList.clear();
		}
		if (list == null) {
			return;
		}
		for (Clues t : list) {
			if (!mDataList.contains(t)) {
				mDataList.add(t);
			}
		}
		mCluesAdapter.notifyDataSetChanged();

	}
	@SuppressWarnings("deprecation")
	private void setLastUpdateTime() {
		mPullListView.setLastUpdatedLabel(StringUtil.longToFormatTimeStr(
				System.currentTimeMillis() + "", "MM-dd HH:mm:ss"));
	}

}
