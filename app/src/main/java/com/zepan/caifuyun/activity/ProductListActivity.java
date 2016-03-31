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
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Request.Method;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.umeng.socialize.utils.Log;
import com.zepan.android.sdk.IRequestCallBack;
import com.zepan.android.sdk.JsonUtil;
import com.zepan.android.widget.AlphaImageView;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.adapter.ProductAdapter;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.constants.Code;
import com.zepan.caifuyun.constants.Url;
import com.zepan.caifuyun.entity.Product;
/**
 *  信托产品
 * @author duanjie
 *
 */
public class ProductListActivity extends BaseActivity implements OnItemClickListener {
	private ListView mShoplist_toplist;
	private ImageView mSearch_city_img;
	private boolean toplistview = false;
	private ProductAdapter productAdapter;
	private List<Product> productList=new ArrayList<Product>();
	private int mPage = Code.FIRST_PAGE;
	private int mSize = Code.PAGE_SIZE;
	private int productType=0;
	private PullToRefreshListView productRefreshListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_product);
		initView();
		initData();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initView() {
		setHeaderFields(0,0, 0,
				R.drawable.ic_action_back, 0,0);
		((AlphaImageView)findViewById(R.id.iv_left)).setClickAlpha(150, true, false);

		//产品列表
		productRefreshListView=(PullToRefreshListView) findViewById(R.id.product_refreshListView);
		ListView productListView=productRefreshListView.getRefreshableView();
		
		productAdapter=new ProductAdapter(this, productList);
		productListView.setAdapter(productAdapter);
		productListView.setOnItemClickListener(this);
		productListView.setDivider(null);
		productRefreshListView.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				mPage=1;
				getProductList();

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				mPage++;
				getProductList();
			}
		});
	}
	private void initData(){
		Intent intent=getIntent();
		String tag=intent.getStringExtra("tag");
		if(tag!=null){
			int name=0;
			if(tag.equals("trustProduct")){
				name=R.string.trusttroduct;//信托产品
				productType=1;
			}else if(tag.equals("infoManagementProduct")){
				name=R.string.infoManagementProduct;//资管产品
				productType=2;
			}else if(tag.equals("privateProduct")){
				name=R.string.privateProduct;//私募产品
				productType=3;
			}else if(tag.equals("publicProduct")){
				name=R.string.publicProduct;//所有产品
				productType=4;
			}else if(tag.equals("otherProduct")){
				name=R.string.otherProduct;//其他产品
				productType=5;
			}
			setHeaderFields(0,name, 
					0,R.drawable.ic_action_back, 0,0);
			getProductList();
		}
	}

	private void getProductList(){
		try {
			String 	token = URLEncoder.encode(getStringLocalData("token"), "UTF-8");

			String uid = getStringLocalData("uid");
			String tid = getStringLocalData("tid");

			String url = Url.GetCategoryProductList+"?productType="
					+productType+"&sortorder="+"asc"+"&token="+token+"&page="+mPage
					+"&size="+mSize+"&uid="+uid+"&tid="+tid;
			request(url, null,Method.GET, new IRequestCallBack() {


				@Override
				public void onResponse(JSONObject response) {
					// TODO Auto-generated method stub
					productListResult(response);
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

	private void productListResult(JSONObject response){
		try {
			if(mPage==1){
				productList.clear();

			}
			if(response.getInt("status")==0){
				JSONObject json = response.getJSONObject("info");
				JSONObject jsonBody=json.getJSONObject("body");
				JSONArray jsonArray=jsonBody.getJSONArray("products");
				for(int i=0;i<jsonArray.length();i++){
					Product product=(Product) JsonUtil.jsonToEntity((JSONObject)jsonArray.get(i), Product.class);
					productList.add(product);
				}
				productAdapter.notifyDataSetChanged();

			}
			productRefreshListView.onRefreshComplete();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (toplistview == true) {
				mSearch_city_img.setImageResource(R.drawable.search_city);
				mShoplist_toplist.setVisibility(View.GONE);
				toplistview = false;
			} else {
				ProductListActivity.this.finish();
			}
		}
		return false;
	}



	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Product product=productList.get(position-1);
		
		Intent intent=new Intent(this, ProductIntroductionActivity.class);
		intent.putExtra("productId", product.getId());
		startActivity(intent);
	}
}