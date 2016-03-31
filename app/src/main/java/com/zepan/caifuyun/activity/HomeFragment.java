package com.zepan.caifuyun.activity;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.zepan.android.sdk.IRequestCallBack;
import com.zepan.android.sdk.JsonUtil;
import com.zepan.android.widget.AlphaImageView;
import com.zepan.android.widget.AlphaTextView;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.adapter.ProductAdapter;
import com.zepan.caifuyun.adapter.SlidePictureAdapter;
import com.zepan.caifuyun.base.BaseFragment;
import com.zepan.caifuyun.constants.Code;
import com.zepan.caifuyun.constants.Url;
import com.zepan.caifuyun.entity.Picture;
import com.zepan.caifuyun.entity.Product;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
/**
 * 第一个tab，即首页
 * @author duanjie
 *
 */
public class HomeFragment extends BaseFragment implements OnClickListener{
	private View view;
	private ViewPager viewPager; // android-support-v4中的滑动组件
	/*	private List<ImageView> imageViews; // 滑动的图片集合
	 */
	private List<View> dots; // 图片标题正文的那些点
	private int currentItem = 0; // 当前图片的索引号   
	private ListView HotProductListView;//热销产品列表
	// 切换当前显示的图片
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			viewPager.setCurrentItem(currentItem);// 切换当前显示的图片
		};
	};
	private List<Picture> urlList;
	private ScheduledExecutorService scheduledExecutorService;
	private SlidePictureAdapter horizontalScrollAdapter;

	private int mPage = Code.FIRST_PAGE;
	private int mSize = Code.PAGE_SIZE;
	private ProductAdapter hotProductAdapter;
	private List<Product> hotProductList;
	private int hotPage = Code.FIRST_PAGE;
	private int hotSize = Code.PAGE_SIZE;
	private View  headerView;



	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_home,container,false);
		initView();
		initData();
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		setHeaderFields(0, R.string.product,0,
				0, 0, R.drawable.ic_action_search);
		super.onActivityCreated(savedInstanceState);

	}
	@Override
	public void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);
	}
	private void initView(){
		headerView=getActivity().getLayoutInflater().inflate(R.layout.include_header_home_list,null);
		((AlphaTextView)headerView.findViewById(R.id.tv_product_tr)).setClickAlpha(100);
		((AlphaTextView)headerView.findViewById(R.id.tv_product_am)).setClickAlpha(100);
		((AlphaTextView)headerView.findViewById(R.id.tv_product_pe)).setClickAlpha(100);
		((AlphaTextView)headerView.findViewById(R.id.tv_product_po)).setClickAlpha(100);
		((AlphaImageView)view.findViewById(R.id.iv_right)).setClickAlpha(150, true, false);

		((AlphaImageView)headerView.findViewById(R.id.iv_product_tr)).setClickAlpha(150, false, true);
		((AlphaImageView)headerView.findViewById(R.id.iv_product_am)).setClickAlpha(150, false, true);
		((AlphaImageView)headerView.findViewById(R.id.iv_product_pe)).setClickAlpha(150, false, true);
		((AlphaImageView)headerView.findViewById(R.id.iv_product_po)).setClickAlpha(150, false, true);
//		Picture pic1=new Picture();
//		pic1.setProductPicture("http://img3.imgtn.bdimg.com/it/u=3491381031,3037658446&fm=21&gp=0.jpg");
//		Picture pic2=new Picture();
//		pic2.setProductPicture("http://img3.imgtn.bdimg.com/it/u=3491381031,3037658446&fm=21&gp=0.jpg");
//		Picture pic3=new Picture();
//		pic3.setProductPicture("http://img3.imgtn.bdimg.com/it/u=3491381031,3037658446&fm=21&gp=0.jpg");
//		urlList.add(pic1);
//		urlList.add(pic2);
//		urlList.add(pic3);

		dots = new ArrayList<View>();
		urlList=new ArrayList<Picture>();
		viewPager = (ViewPager)headerView.findViewById(R.id.vp);
		horizontalScrollAdapter=new SlidePictureAdapter(getActivity(), urlList);
		viewPager.setAdapter(horizontalScrollAdapter);// 设置填充ViewPager页面的适配器
		// 设置一个监听器，当ViewPager中的页面改变时调用
		viewPager.setOnPageChangeListener(new SlidePageChangeListener());
//		viewPager.seton
		//信托产品
		headerView.findViewById(R.id.ly_trustproduct).setOnClickListener(this);
		//资管产品
		headerView.findViewById(R.id.ly_product_am).setOnClickListener(this);		
		//私募产品
		headerView.findViewById(R.id.ly_product_pe).setOnClickListener(this);		
		//公募产品
		headerView.findViewById(R.id.ly_product_po).setOnClickListener(this);		
		//其他产品
		headerView.findViewById(R.id.ly_product_others).setOnClickListener(this);	
		//搜索
		view.findViewById(R.id.iv_right).setOnClickListener(this);	
		((PullToRefreshListView)view.findViewById(android.R.id.list)).setMode(Mode.PULL_FROM_END);
		HotProductListView=((PullToRefreshListView)view.findViewById(android.R.id.list)).getRefreshableView();
		HotProductListView.addHeaderView(headerView);
		hotProductList=new ArrayList<Product>();
		hotProductAdapter=new ProductAdapter(getActivity(), hotProductList);
		HotProductListView.setAdapter(hotProductAdapter);
		HotProductListView.setDivider(null);
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 当Activity显示出来后，每4秒钟切换一次图片显示
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 6, TimeUnit.SECONDS);
		HotProductListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent=new Intent(getActivity(), ProductIntroductionActivity.class);
				intent.putExtra("productId", hotProductList.get(position-2).getId());
				startActivity(intent);
				
			}
		});


	}
	@Override
	public void onStart() {
		getHotProductList();
		super.onStart();
	}
	//获取热销产品
	private void getHotProductList(){
		try {
			String token =URLEncoder.encode(getStringLocalData("token"), "UTF-8") ;
			String url = Url.GetHotProductList+"?token="+token+"&size="+hotSize+"&page="+hotPage;
			request(url, null,Method.GET, new IRequestCallBack() {
				@Override
				public void onResponse(JSONObject response) {
					getHotProductListResultJson(response);
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


	private void getHotProductListResultJson(JSONObject response){
		try {

			if(response.getInt("status")==0){
				if(hotPage==1){
					hotProductList.clear();
				}
				JSONObject json =response.getJSONObject("info");
				JSONObject jsonBody = json.getJSONObject("body");
				JSONArray jsonArray=jsonBody.getJSONArray("products");
				for(int i=0;i<jsonArray.length();i++){
					Product product=(Product) JsonUtil.jsonToEntity((JSONObject)jsonArray.get(i), Product.class);
					hotProductList.add(product);
				}
				hotProductAdapter.notifyDataSetChanged();

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void initData(){
		try {
			String token =URLEncoder.encode(getStringLocalData("token"), "UTF-8");
			String uid = getStringLocalData("uid");
			String tid = getStringLocalData("tid");
			String url =Url.GetPromoteProductList+"?token="+token+"&page="+mPage
					+"&size="+mSize+"&uid="+uid+"&tid="+tid;
			request(url, null,Method.GET, new IRequestCallBack() {
				@Override
				public void onResponse(JSONObject response) {
					getPromotePicturesResultJson(response);
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
		//GetHotProductList();
	}
	private void getPromotePicturesResultJson(JSONObject response){
		try {
			if(response.getInt("status")==0){
				JSONObject json =response.getJSONObject("info");
				JSONObject jsonBody = json.getJSONObject("body");
				JSONArray jsonArray=jsonBody.getJSONArray("products");
				for(int i=0;i<jsonArray.length();i++){
					Picture picture=(Picture) JsonUtil.jsonToEntity((JSONObject)jsonArray.get(i), Picture.class);
					urlList.add(picture);
					View dotView=getActivity().getLayoutInflater().inflate(R.layout.view_dot,null);
					LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(20, 20);
					params.setMargins(10, 0, 10, 0);
					dotView.setLayoutParams(params);
					dots.add(dotView);
					((LinearLayout)headerView.findViewById(R.id.ly_dots)).addView(dotView);
				}

				horizontalScrollAdapter.notifyDataSetChanged();
			}else{
				return;
			}

		}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 换行切换任务
	 * 
	 * @author long
	 * 
	 */
	private class ScrollTask implements Runnable {

		public void run() {
			synchronized (viewPager) {
				currentItem = (currentItem + 1) % urlList.size();
				handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
			}
		}

	}

	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 * 
	 * @author long
	 * 
	 */
	private class SlidePageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {
			currentItem = position;
			dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
			dots.get(position).setBackgroundResource(R.drawable.dot_focused);
			oldPosition = position;
		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}

	@Override
	public void onClick(View v) {
		String tag="";
		switch (v.getId()) {
		case R.id.ly_trustproduct:
			tag="trustProduct";
			Intent trustIntent=new Intent(getActivity(), ProductListActivity.class);
			trustIntent.putExtra("tag", tag);
			startActivity(trustIntent);
			break;
		case R.id.ly_product_am:
			tag="infoManagementProduct";
			Intent infoManagementIntent=new Intent(getActivity(), ProductListActivity.class);
			infoManagementIntent.putExtra("tag", tag);
			startActivity(infoManagementIntent);
			break;
		case R.id.ly_product_pe:
			tag="privateProduct";
			Intent privateIntent=new Intent(getActivity(), ProductListActivity.class);
			privateIntent.putExtra("tag", tag);
			startActivity(privateIntent);
			break;
		case R.id.ly_product_po:
			tag="publicProduct";
			Intent publicIntent=new Intent(getActivity(), ProductListActivity.class);
			publicIntent.putExtra("tag", tag);
			startActivity(publicIntent);
			break;
		case R.id.ly_product_others:
			tag="otherProduct";
			Intent otherIntent=new Intent(getActivity(), ProductListActivity.class);
			otherIntent.putExtra("tag", tag);
			startActivity(otherIntent);
			break;
		case R.id.iv_right:
			Intent searchIntent=new Intent(getActivity(), SearchOneActivity.class);
			startActivity(searchIntent);
			break;
		default:
			break;
		}


	}
}
