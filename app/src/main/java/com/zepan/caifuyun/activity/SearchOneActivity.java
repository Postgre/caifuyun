package com.zepan.caifuyun.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.zepan.android.sdk.IRequestCallBack;
import com.zepan.android.sdk.JsonUtil;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.adapter.HistoryAdapter;
import com.zepan.caifuyun.adapter.HistoryAdapter.onDeleteListener;
import com.zepan.caifuyun.adapter.ProductAdapter;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.cache.HistoryDBManager;
import com.zepan.caifuyun.constants.Url;
import com.zepan.caifuyun.entity.History;
import com.zepan.caifuyun.entity.Product;
import com.zepan.caifuyun.widget.ClearEditText;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 搜索的第一个界面，
 * @author duanjie
 *
 */
@SuppressLint("CutPasteId")
public class SearchOneActivity extends BaseActivity implements OnClickListener{
	/** 取消 */
	private TextView cancel;
	/**历史记录*/
	private ArrayList<History> historyList=new ArrayList<History>();
	private HistoryAdapter historyAdapter;
	private HistoryDBManager historyDBManager;
	/**产品*/
	private ArrayList<Product> productList=new ArrayList<Product>();
	private ProductAdapter productAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_one);
		initView();
	}
	private void initView(){
		cancel = (TextView) findViewById(R.id.tv_right);
		cancel.setText(R.string.head_title_register_right);
		cancel.setOnClickListener(this);
		//历史记录
		historyDBManager=new HistoryDBManager(this);
		ListView listView=(ListView) findViewById(R.id.list_history);
		historyAdapter=new HistoryAdapter(this, historyList);
		listView.setAdapter(historyAdapter);
		listView.setDivider(null);
		historyAdapter.setOnDeleteListener(new onDeleteListener() {
			
			@Override
			public void refreshHotWordDBManager(String id) {
				historyDBManager.deleteFromTable(id);
				sortList();
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				((ClearEditText)findViewById(R.id.ed_search)).setText(historyList.get(position).getText());
				
			}
		});
		//产品
		ListView productListView=(ListView) findViewById(R.id.list_hint_product);
		productAdapter=new ProductAdapter(this, productList);
		productListView.setAdapter(productAdapter);
		productListView.setDivider(null);
		productListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				History history=new History();
				history.setId(UUID.randomUUID().toString());
				history.setText(((ClearEditText)findViewById(R.id.ed_search)).getText().toString());
				historyDBManager.insert(history);
				
			}
		});
		
		if(TextUtils.isEmpty(((ClearEditText)findViewById(R.id.ed_search)).getText())){
			sortList();
		}
		((ClearEditText)findViewById(R.id.ed_search)).addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.length()>0){
					findViewById(R.id.ly_first).setVisibility(View.GONE);
					findViewById(R.id.ly_enter_text).setVisibility(View.VISIBLE);
					getProduct();
				}else{
					findViewById(R.id.ly_first).setVisibility(View.VISIBLE);
					findViewById(R.id.ly_enter_text).setVisibility(View.GONE);
					sortList();
				}
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	/*
	 * 对list排序
	 */
	private void sortList(){
		if(historyList.size()>0){
			historyList.clear();
		}
		Comparator<History> comparator = new Comparator<History>(){  
			public int compare(History s1, History s2) {  
				//时间
				long t1=Long.parseLong(s1.getTime());
				long t2=Long.parseLong(s2.getTime());
				if(t1!=t2){  
					return (int) (t2-t1);  
				}
				return 0;  

			}  
		}; 
		for(History hotWord:historyDBManager.getHistoryList()){
			historyList.add(hotWord);
			Log.i("------------", hotWord.getTime());
		}
		Collections.sort(historyList,comparator);
		historyAdapter.notifyDataSetChanged();
	}
	private void getProduct(){
		String url="https://192.168.1/get_productsk_list";
		JSONObject paramJson=new JSONObject();
		request(url, paramJson, Method.POST, new IRequestCallBack() {
			
			@Override
			public void onResponse(JSONObject response) {
				getProductResult(response);
				
			}
			
			@Override
			public void onErrorResponse(String errorMessage) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	private void getProductResult(JSONObject response){
		try {
			if(productList.size()>0){
				productList.clear();
			}
			if(response.getInt("status")==0){
				JSONArray jsonArray=response.getJSONArray("list");
				for(int i=0;i<jsonArray.length();i++){
					Product product=(Product) JsonUtil.jsonToEntity(jsonArray.getJSONObject(i), Product.class);
					productList.add(product);
				}
				productAdapter.notifyDataSetChanged();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_right:
			finish();
			break;

		default:
			break;
		}
		
	}

}
