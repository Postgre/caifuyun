package com.zepan.caifuyun.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.adapter.CommitJournalAdapter;
import com.zepan.caifuyun.adapter.ReadJournalAdapter;
import com.zepan.caifuyun.base.BaseFragment;
import com.zepan.caifuyun.constants.Code;

//我批阅的日报
public class ReadJournalFragment extends BaseFragment {
	
	private View view;
	private int mPage = Code.FIRST_PAGE;
	private int mSize = Code.PAGE_SIZE;
	private ReadJournalAdapter readjournalAdapter;
	private List<String> list=new ArrayList<String>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view=inflater.inflate(R.layout.fragment_all_clue, null);
		initView();
		return view;
	}
	
	public void initView(){
		PullToRefreshListView pullToRefreshListView=(PullToRefreshListView) view.findViewById(android.R.id.list);
		ListView listView=pullToRefreshListView.getRefreshableView();
		
		for(int i=0;i<5;i++){
			list.add("123");
		}
		readjournalAdapter=new ReadJournalAdapter(getActivity(), null, list);
		listView.setAdapter(readjournalAdapter);
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
				//getPerson();
				//
			}
		});
		//getPerson();
	}

}
