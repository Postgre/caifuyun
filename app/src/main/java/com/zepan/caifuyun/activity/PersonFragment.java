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
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zepan.android.sdk.IRequestCallBack;
import com.zepan.android.sdk.JsonUtil;
import com.zepan.android.widget.SelectMenuAttachView;
import com.zepan.android.widget.SelectMenuAttachView.OnItemSelectedListener;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.adapter.PersonAdapter;
import com.zepan.caifuyun.base.BaseFragment;
import com.zepan.caifuyun.constants.Code;
import com.zepan.caifuyun.constants.Url;
import com.zepan.caifuyun.entity.IdentityInfo;
import com.zepan.caifuyun.entity.ProfessionInfo;
import com.zepan.caifuyun.entity.User;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
/**
 * 个人客户fragment
 * @author long
 *
 */
public class PersonFragment extends BaseFragment implements OnClickListener,OnItemClickListener,Callback{
	private View view;
	private PersonAdapter personAdapter;
	private List<User> userList=new ArrayList<User>();
	private int accountType=1;
	private int mPage = Code.FIRST_PAGE;
	private int mSize = Code.PAGE_SIZE;
	private int mSort=-1;//排序
	private int mScreen=-1;//成熟度
	private String[] sortArray={"dbcInteger1","dbcDate1","accountName"};
	public Handler mHandler=new Handler(this);
	private boolean isSelectPerson;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_person,container,false);
		initView();
		return view;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initView() {
		SelectMenuAttachView sortView = (SelectMenuAttachView)view.findViewById(R.id.ly_sort);
		sortView.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void itemSelected(int index) {
				mSort=index;
				mPage=Code.FIRST_PAGE;
				getPerson();
			}
		});
		SelectMenuAttachView screenView = (SelectMenuAttachView)view.findViewById(R.id.ly_screen);
		screenView.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void itemSelected(int index) {
				mScreen=index;
				mPage=Code.FIRST_PAGE;
				getPerson();
			}
		});

		PullToRefreshListView pullToRefreshListView=(PullToRefreshListView) view.findViewById(android.R.id.list);
		ListView listView=pullToRefreshListView.getRefreshableView();
		personAdapter=new PersonAdapter(getActivity(), userList);
		listView.setAdapter(personAdapter);
		listView.setDivider(null);
		listView.setOnItemClickListener(this);
		pullToRefreshListView.setMode(Mode.PULL_FROM_END);
		pullToRefreshListView.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				mPage++;
				getPerson();

			}
		});
		

	}
	@Override
	public void onStart() {
		mPage = Code.FIRST_PAGE;
		getPerson();
		super.onStart();
	}
	private void getPerson(){
		try {
			String token=URLEncoder.encode(getStringLocalData("token"), "UTF-8");
			String asc=URLEncoder.encode("asc", "UTF-8");
			String uid=getStringLocalData("uid");
			String tid=getStringLocalData("tid");
			String url=Url.GetAll+
					"?token="+token+"&accountType="+accountType+"&size="
					+mSize+"&page="+mPage+"&sortorder="+asc+"&uid="+uid+"&tid="+tid;
			
			if(mScreen!=0){
				if(mSort!=-1){
					String sort=URLEncoder.encode(sortArray[mSort], "UTF-8");//排序字段名
					url+="&sortdatafield="+sort;
				}
				if(mScreen!=-1){
					//customData
					//{ "conditions": { "accountName": "客户测试" }}
					JSONObject json=new JSONObject();
					JSONObject jsonChild=new JSONObject();
					try {
						jsonChild.put("dbcInteger1", 4-mScreen);
						json.put("conditions", jsonChild);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String sort=URLEncoder.encode(json.toString(), "UTF-8");//搜索条件三星二星一星
					url+="&customData="+sort;
				}
			}
			request(url, null, Method.GET, new IRequestCallBack() {

				@Override
				public void onResponse(JSONObject response) {
					getPersonResult(response);

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

	private void getPersonResult(JSONObject response){
		try {
			if(mPage==Code.FIRST_PAGE){
				if(userList.size()>0){
					userList.clear();
				}
			}
			if(response.getInt("status")==0){
				JSONObject json=response.getJSONObject("info");
				JSONObject bodyJson=json.getJSONObject("body");
				JSONArray jsonArray=bodyJson.getJSONArray("accounts");
				for(int i=0;i<jsonArray.length();i++){
					User user=(User) JsonUtil.jsonToEntity(
							jsonArray.getJSONObject(i), User.class);
					if(jsonArray.getJSONObject(i).has("dbcTextarea2")){
						if(!"".equals(jsonArray.getJSONObject(i).getString("dbcTextarea2"))){
							String db2=jsonArray.getJSONObject(i).getString("dbcTextarea2");
							JSONObject jsonDbc2=new JSONObject(db2);//身份信息
							IdentityInfo identityInfo=(IdentityInfo) JsonUtil.jsonToEntity(jsonDbc2, IdentityInfo.class);
							user.setIdentityInformation(identityInfo);
						}

					}
					if(jsonArray.getJSONObject(i).has("dbcTextarea3")){
						if(!"".equals(jsonArray.getJSONObject(i).getString("dbcTextarea3"))){
							String db3=jsonArray.getJSONObject(i).getString("dbcTextarea3");
							JSONObject jsonDbc3=new JSONObject(db3);//职业信息
							ProfessionInfo profess=(ProfessionInfo) JsonUtil.jsonToEntity(jsonDbc3, ProfessionInfo.class);
							user.setProfessionalInformation(profess);
						}

					}



					userList.add(user);
				}
				personAdapter.notifyDataSetChanged(); 
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		default:
			break;
		}

	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		User user=userList.get(position-1);
		if(isSelectPerson){
			Intent data=new Intent();
			data.putExtra("customId",user.getUserId());
			data.putExtra("customName",user.getUserName());
			getActivity().setResult(getActivity().RESULT_OK, data);
			getActivity().finish();
			
		}else{
			Intent intent=new Intent(getActivity(),IndividualCustomersActivity.class);
			intent.putExtra("userId", user.getUserId());
			startActivity(intent);
		}
	}
	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case SelectCustomerTwoActivity.SELECTCUSTOM:
			isSelectPerson=true;
			break;


		default:
			break;
		}
		return false;
	}

}
