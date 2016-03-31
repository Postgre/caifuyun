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
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.adapter.InstitutionsAcceptAdapter;
import com.zepan.caifuyun.base.BaseFragment;
import com.zepan.caifuyun.constants.Code;
import com.zepan.caifuyun.constants.Url;
import com.zepan.caifuyun.entity.Channel;
import com.zepan.caifuyun.entity.IdentityInfo;
import com.zepan.caifuyun.entity.ProfessionInfo;
import com.zepan.caifuyun.entity.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
/**
 * 机构渠道
 * @author long
 *
 */
public class InstitutionsAcceptFragment extends BaseFragment implements OnItemClickListener{
	private View view;
	private int channelType=2;
	private int mPage = Code.FIRST_PAGE;
	private int mSize = Code.PAGE_SIZE;
	
	private InstitutionsAcceptAdapter institutionsAcceptAdapter;
	private List<Channel> list=new ArrayList<Channel>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_all_clue,null);
		initView();
		return view;
	}
     @Override
      public void onStart() {//刷新数据
	      // TODO Auto-generated method stub
    	   
	       super.onStart();
	       mPage = Code.FIRST_PAGE;
	       getChannelList();
       }
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initView() {
		PullToRefreshListView pullToRefreshListView=(PullToRefreshListView) view.findViewById(android.R.id.list);
		ListView listView=pullToRefreshListView.getRefreshableView();

		institutionsAcceptAdapter=new InstitutionsAcceptAdapter(getActivity(), null, list);
		listView.setAdapter(institutionsAcceptAdapter);
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
				getChannelList();
				
			}
		});
		getChannelList();
	}
	private void getChannelList(){
		try {
			String token=URLEncoder.encode(getStringLocalData("token"), "UTF-8");
			String asc=URLEncoder.encode("asc", "UTF-8");
			
			String url=Url.GetChannelList+"?token="
			+token+"&channelType="+channelType+"&page="
					+mPage+"&size="+mSize;
			
			request(url, null, Method.GET, new IRequestCallBack() {

				@Override
				public void onResponse(JSONObject response) {
					getChannelListResult(response);

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

	private void getChannelListResult(JSONObject response){
		try {
			if(mPage==Code.FIRST_PAGE){
				if(list.size()>0){
					list.clear();
				}
			}
			if(response.getInt("status")==0){
				
				JSONObject json=response.getJSONObject("info");
				JSONObject bodyJson=json.getJSONObject("body");
				JSONArray jsonArray=bodyJson.getJSONArray("channels");
				for(int i=0;i<jsonArray.length();i++){
					
					Channel channels=(Channel) JsonUtil.jsonToEntity(
							jsonArray.getJSONObject(i), Channel.class);
					list.add(channels);
				}
				institutionsAcceptAdapter.notifyDataSetChanged();
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent=new Intent(getActivity(),ChannelCustomersActivity.class);
		
		Channel channel=list.get(position-1);
		intent.putExtra("institututionschannel_id",channel.getId());
		startActivity(intent);

	}

}
