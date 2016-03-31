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
import com.zepan.caifuyun.adapter.OrganizationAdapter;
import com.zepan.caifuyun.base.BaseFragment;
import com.zepan.caifuyun.constants.Code;
import com.zepan.caifuyun.constants.Url;
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
 * 机构客户fragment
 * @author long
 *
 */
public class OrganizationFragment extends BaseFragment implements OnClickListener,OnItemClickListener,Callback{
	private View view;
	private OrganizationAdapter organizationAdapter;
	private List<User> organizationList=new ArrayList<User>();
	private int accountType=2;
	private int mPage = Code.FIRST_PAGE;
	private int mSize = Code.PAGE_SIZE;
	private int mSort=-1;//排序
	private int mScreen=-1;//成熟度
	private String[] sortArray={"dbcInteger1","dbcDate1","accountName"};
	private PullToRefreshListView pullToRefreshListView;
	public Handler mHandler=new Handler(this);
	private boolean isSelectOrganization;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_organization,container,false);
		initView();
		return view;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initView() {
		SelectMenuAttachView sortView = (SelectMenuAttachView)view.findViewById(R.id.ly_sort);
		sortView.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void itemSelected(int index) {
				mSort=index;
				mPage=Code.FIRST_PAGE;
				getOrganization();
			}
		});
		SelectMenuAttachView screenView = (SelectMenuAttachView)view.findViewById(R.id.ly_screen);
		screenView.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void itemSelected(int index) {
				mScreen=index;
				mPage=Code.FIRST_PAGE;
				getOrganization();
			}
		});
		pullToRefreshListView=(PullToRefreshListView) view.findViewById(android.R.id.list);
		//pullToRefreshListView.setmo
		ListView listView=pullToRefreshListView.getRefreshableView();
		organizationAdapter=new OrganizationAdapter(getActivity(), organizationList);
		listView.setAdapter(organizationAdapter);
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
				getOrganization();
			}
		});
		getOrganization();
	}
	private void getOrganization(){
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
					getOrganizationResult(response);

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
	private void getOrganizationResult(JSONObject response){
		try {
			if(mPage==Code.FIRST_PAGE){
				if(organizationList.size()>0){
					organizationList.clear();
				}
			}
			if(response.getInt("status")==0){
				JSONObject json=response.getJSONObject("info");
				JSONObject bodyJson=json.getJSONObject("body");
				JSONArray jsonArray=bodyJson.getJSONArray("accounts");
				for(int i=0;i<jsonArray.length();i++){
					User user=(User) JsonUtil.jsonToEntity(
							jsonArray.getJSONObject(i), User.class);
					organizationList.add(user);
				}
				organizationAdapter.notifyDataSetChanged(); 
			}
			pullToRefreshListView.onRefreshComplete();
			
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
		User user=organizationList.get(position-1);
		if(isSelectOrganization){
			Intent data=new Intent();
			data.putExtra("customId",user.getUserId());
			data.putExtra("customName",user.getUserName());
			getActivity().setResult(getActivity().RESULT_OK, data);
			getActivity().finish();
		}else{
			Intent intent=new Intent(getActivity(), IndividualCustomersActivity.class);
			intent.putExtra("organizationId", user.getUserId());
			startActivity(intent);
		}
		

	}
	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case SelectCustomerTwoActivity.SELECTCUSTOM:
			isSelectOrganization=true;
			break;


		default:
			break;
		}
		return false;
	}

}
