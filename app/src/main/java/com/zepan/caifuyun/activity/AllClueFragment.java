package com.zepan.caifuyun.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zepan.android.sdk.IRequestCallBack;
import com.zepan.android.sdk.JsonUtil;
import com.zepan.android.util.StringUtil;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.adapter.CluesAdapter;
import com.zepan.caifuyun.base.BaseFragment;
import com.zepan.caifuyun.constants.Code;
import com.zepan.caifuyun.constants.Url;
import com.zepan.caifuyun.entity.Clues;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
/**
 * 全部线索
 * @author long
 *
 */
public class AllClueFragment extends BaseFragment{
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
	int currentTag=allTag;
	private final static int allTag=0x200;
	private final static int keycluesTag=0x201;
	private final static int generalcluesTag=0x202;
	private View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_all_clue,null);
		initView();
		return view;
	}
	@Override
	public void onStart() {
		getLeadGetUserLeads();
		super.onStart();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initView() {
		/*((AlphaImageView)view.findViewById(R.id.iv_right)).setClickAlpha(150, true, false);
		((AlphaImageView)view.findViewById(R.id.iv_left)).setClickAlpha(150, true, false);
		((AlphaImageView)view.findViewById(R.id.iv_right)).setOnClickListener(this);*/
		mPullListView = (PullToRefreshListView)view.findViewById(android.R.id.list);
		listDebate = mPullListView.getRefreshableView();
		listDebate.setDivider(null);
		mDataList = new ArrayList<Clues>();
		mCluesAdapter=new CluesAdapter(getActivity(),mDataList);
		listDebate.setAdapter(mCluesAdapter);
		listDebate.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Clues  clues=(Clues)mDataList.get(position);
				Intent  intent=new Intent(getActivity(),CluesTwoActivity.class);
				intent.putExtra("clues",clues);
				getActivity().startActivity(intent);
			}
		}); 
		mPullListView.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				
			}
		});
		mRadioGroup=(RadioGroup)view.findViewById(R.id.RadioGroup_bottom);
		mAll=(RadioButton)view.findViewById(R.id.rb_all_clues);//全部线索
		//mAll.setChecked(true);
		/*mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				//获取变更后的选中项的ID
				//mAll=(RadioButton)view.findViewById(R.id.rbtn_all);
				mAll.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						currentTag=allTag;
						mPage=1;
						getLeadGetUserLeads();
					}
				});*/
		/*mKeyClues=(RadioButton)view.findViewById(R.id.rb_key_clues);//重点线索
				mKeyClues.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						currentTag=keycluesTag;
						mPage=1;
						getLeadGetUserLeads();
					}
				});

				mGeneralClues=(RadioButton)view.findViewById(R.id.rb_general_clue);//一般线索
				mGeneralClues.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						currentTag=generalcluesTag;
						mPage=1;
						getLeadGetUserLeads();
					}
				});
			}
		});*/
	}
	/**
	 * 请求   
	 * @param response
	 */
	private void  getLeadGetUserLeads(){
		String token;
		try {
			token = URLEncoder.encode("uc0oj3R6hGpsCHVks8Ddt3QVcaRW9vwIhm2L4vw3Cb2JLbLgozKWhw==", "UTF-8");
			JSONObject json = new JSONObject();
			try {
				/*	json.put("user_id","");//getUser().getId()
				json.put("page",mPage);
				json.put("size",mSize);*/
				JSONObject changeInfoJson = new JSONObject();
				json.put("conditions",changeInfoJson);
				changeInfoJson.put("status", 0);
				/*if(currentTag==allTag){
					changeInfoJson.put("status", 0);
				}else if(currentTag==keycluesTag){
					changeInfoJson.put("status", 1);
				}else if(currentTag==generalcluesTag){
					changeInfoJson.put("status",2);
				}*/
				String tid=getStringLocalData("tid");
				String uid=getStringLocalData("uid");
				String url=Url.LeadGetUserLeads+"?token="+"uc0oj3R6hGpsCHVks8Ddt3QVcaRW9vwIhm2L4vw3Cb2JLbLgozKWhw=="
				+"&size="+mSize+"&page="+mPage +"&customData="+json.toString()+"&uid="+uid+"&tid="+tid;
				request(url,null, Method.GET,new IRequestCallBack() {//    +"&customData="+json+""
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


			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}




		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}


	private void getLeadGetUserLeadsResult(JSONObject response){
		try {
			List<Clues> list = new ArrayList<Clues>();
			JSONObject json = response;
			JSONObject jsons = json.getJSONObject("leads");
			JSONObject jsonOne=jsons.getJSONObject("body");
			JSONArray jsonArray=jsonOne.getJSONArray("leads");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonChildParam = jsonArray.getJSONObject(i);
				Clues c=(Clues)JsonUtil.jsonToEntity(jsonChildParam, Clues.class);
				list.add(c);// 添加在存储信息列表
			}
			refreshListView(list);
			changePullListStatus(list);
			//mProductListAdapter.notifyDataSetChanged();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_right://CluesTwoActivity.class  线索的第二个界面
			//NewCluesActivity  新建线索
			Intent intentCluesTwo=new Intent(getActivity(),NewCluesActivity.class);
			startActivity(intentCluesTwo);
			break;
		default:
			break;
		}

	}*/
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
	private void changePullListStatus(List<Clues> list){
		mPullListView.onRefreshComplete();
		setLastUpdateTime();

	}
	@SuppressWarnings("deprecation")
	private void setLastUpdateTime() {
		mPullListView.setLastUpdatedLabel(StringUtil.longToFormatTimeStr(
				System.currentTimeMillis() + "", "MM-dd HH:mm:ss"));
	}



}
