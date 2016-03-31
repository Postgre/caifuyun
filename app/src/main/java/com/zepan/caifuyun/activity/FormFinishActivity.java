package com.zepan.caifuyun.activity;

import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yiciyuan.easycomponent.listview.ECListViewLayout;
import com.yiciyuan.easycomponent.listview.ECViewHolder;
import com.yiciyuan.easycomponent.listview.ECAdapter.JSONDecodeListener;
import com.yiciyuan.easycomponent.listview.ECAdapter.ValueBindCallBack;
import com.zepan.android.util.StringUtil;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;
/**
 * 报单成功
 */

public class FormFinishActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_form_finish);
		initView();
	}

	private void initView() {

		setHeaderFields(0,R.string.form_finish,0,R.drawable.ic_return,0,0);
		ECListViewLayout eCListViewLayout = (ECListViewLayout)findViewById(R.id.eclistLayout);
		eCListViewLayout.addRequestParams("token", getStringLocalData("token"));
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
			JSONArray jsonOrders = jsonBody.getJSONArray("contracts");
			JSONObject jsonProducts=jsonBody.getJSONObject("products");
			JSONArray jsonNew=new JSONArray();
			for(int i=0;i<jsonOrders.length();i++){
				JSONObject jsonPro=jsonProducts.getJSONObject(""+jsonOrders.getJSONObject(i).getInt("productId"));
				jsonOrders.getJSONObject(i).put("name", jsonPro.getString("name"));
				if(jsonPro.has("status")){
					if(jsonPro.getInt("status")==3){
						jsonNew.put(jsonOrders);
					}
				}

			}
			return jsonNew;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

}
