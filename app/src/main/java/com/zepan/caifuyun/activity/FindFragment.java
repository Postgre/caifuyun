package com.zepan.caifuyun.activity;


import org.json.JSONException;
import org.json.JSONObject;

import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.yiciyuan.easycomponent.listview.ECListViewLayout;
import com.yiciyuan.easycomponent.listview.IECListView.OnECItemClickListener;
/**
 * 第四个tab，即发现
 * @author duanjie
 *
 */
public class FindFragment extends BaseFragment {
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragmnet_find,container,false);
		initView();
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		setHeaderFields(0,R.string.find,0,
				0, 0,0);
		super.onActivityCreated(savedInstanceState);
	}
	private void initView(){
		ECListViewLayout ecListViewLayout = (ECListViewLayout)view.findViewById(R.id.eclistLayout);
		ecListViewLayout.setResourceJsonKeyMap(new int[]{R.id.iv_find}, new String[]{"url"});
		ecListViewLayout.setOnItemClickListener(new OnECItemClickListener() {
			
			@Override
			public void OnItemClick(AdapterView<?> parent, View view, int position,
					JSONObject itemJson) throws JSONException {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(),FindWebActivity.class);
				intent.putExtra("url",itemJson.getString("web_url"));
				startActivity(intent);
			}
		});
		
	}
}
