package com.zepan.caifuyun.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request.Method;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.zepan.android.sdk.IRequestCallBack;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.adapter.CommitJournalAdapter;
import com.zepan.caifuyun.adapter.InstitutionsAcceptAdapter;
import com.zepan.caifuyun.base.BaseFragment;
import com.zepan.caifuyun.constants.Code;

/*
 * 我提交的日报
 */
public class CommitJournalFragment extends BaseFragment {
 
	private View view;
	private int mPage = Code.FIRST_PAGE;
	private int mSize = Code.PAGE_SIZE;
	private CommitJournalAdapter commitjournaladapter;
	private List<String> list=new ArrayList<String>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view=inflater.inflate(R.layout.fragment_all_clue,null);
		initView();
		return view;
	}
	
	public void initView(){
		PullToRefreshListView pullToRefreshListView=(PullToRefreshListView) view.findViewById(android.R.id.list);
		ListView listView=pullToRefreshListView.getRefreshableView();
		for(int i=0;i<10;i++){
			list.add("123");
		}
		commitjournaladapter=new CommitJournalAdapter(getActivity(), null, list);
		listView.setAdapter(commitjournaladapter);
		listView.setDivider(null);
	
		pullToRefreshListView.setMode(Mode.PULL_FROM_END);
		pullToRefreshListView.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				mPage++;
				//getData();
				
			}
		});
		//getData();
	}
	
//	public void getData(){
//		String url="";
//	    request(url, null, Method.GET, new IRequestCallBack() {
//			
//			@Override
//			public void onResponse(JSONObject response) {
//				// TODO Auto-generated method stub
//				getJson(response);
//			}
//			
//			@Override
//			public void onErrorResponse(String errorMessage) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//	}
//	
//	public void getJson(JSONObject response){
//		
//	}
	
	
}
