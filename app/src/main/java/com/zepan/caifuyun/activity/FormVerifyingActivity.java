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
 *报单审核中 
 */

public class FormVerifyingActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_form_verifying);
		
		initView();
		

	}

	private void initView() {
		setHeaderFields(0,R.string.form_verifying,0,R.drawable.ic_return,0,0);
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
				//0 1 未通过   2 审核中   3通过
				if(json.getInt("status")==0||json.getInt("status")==1){
					holder.getView(R.id.image).setBackgroundResource(R.drawable.not_by);
				}else if(json.getInt("status")==2){
					holder.getView(R.id.image).setBackgroundResource(R.drawable.audit);
				}
				
			}
		});
	}
	private JSONArray praseResult(JSONObject response){
		try {
			JSONObject jsonInfo=response.getJSONObject("info");
			JSONObject jsonBody = jsonInfo.getJSONObject("body");
			JSONArray jsonOrders = jsonBody.getJSONArray("contracts");
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
