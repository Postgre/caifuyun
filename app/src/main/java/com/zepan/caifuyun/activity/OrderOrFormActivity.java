package com.zepan.caifuyun.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yiciyuan.easycomponent.listview.ECAdapter.JSONDecodeListener;
import com.yiciyuan.easycomponent.listview.ECAdapter.ValueBindCallBack;
import com.yiciyuan.easycomponent.listview.ECListViewLayout;
import com.yiciyuan.easycomponent.listview.ECViewHolder;
import com.zepan.android.util.StringUtil;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.constants.Code;

import android.os.Bundle;
import android.widget.TextView;
/**
 * 预约待报单页面
 * @author duanjie 
 *
 */
public class OrderOrFormActivity extends BaseActivity {
	private int mPage=Code.FIRST_PAGE;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_or_form);
		initView();
	}
	//app:url="http://120.26.209.49/product/my-order-list"
	private void initView() {
		setHeaderFields(0,R.string.order_or_form,0,R.drawable.ic_return,0,0);
		ECListViewLayout eCListViewLayout=(ECListViewLayout) findViewById(R.id.eclistLayout);
		eCListViewLayout.addRequestParams("token", getStringLocalData("token"));
		eCListViewLayout.addRequestParams("isReport",0);
		eCListViewLayout.setResourceJsonKeyMap(
				new int[]{R.id.order_title,R.id.order_money_num},
				new String[]{"name","amount"});
		eCListViewLayout.setJsonDecodeListener(new JSONDecodeListener() {

			@Override
			public JSONArray decode(JSONObject json) {

				return praseResult(json);
			}
		});
		eCListViewLayout.setBindCallBack(new ValueBindCallBack() {
			
			@Override
			public void bind(ECViewHolder holder, JSONObject json) throws JSONException {
				String time=StringUtil.longToFormatTimeStr(json.getLong("createdAt")+"", "yyyy-MM-dd");
				((TextView)holder.getView(R.id.order_time)).setText(time);
				
			}
		});

	}
	private JSONArray praseResult(JSONObject response){
		try {
			JSONObject jsonInfo=response.getJSONObject("info");
			JSONObject jsonBody = jsonInfo.getJSONObject("body");
			JSONArray jsonOrders = jsonBody.getJSONArray("orders");
			JSONObject jsonProducts=jsonBody.getJSONObject("products");
			for(int i=0;i<jsonOrders.length();i++){
				JSONObject jsonPro=jsonProducts.getJSONObject(""+jsonOrders.getJSONObject(i).getInt("productId"));
				jsonOrders.getJSONObject(i).put("name", jsonPro.getString("name"));
			}
			return jsonOrders;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}


}
