/*
 * Copyright (C) 15/8/2 Lei.Zhang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yiciyuan.easycomponent.listview;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.zepan.android.sdk.TestDataFactory;

/**
 * ___________ ___________ / __________/ / __________/ / /_________ / / /
 * /__________/ / / / /__________ / /_________ /____________/ /____________/
 * 自定义适配器
 * 
 * @see com.yiciyuan.easycomponent.listview.ECPullToRefreshListView
 * @see com.yiciyuan.easycomponent.listview.ECViewHolder
 */
public class ECAdapter extends BaseAdapter {

	public static final String TAG = "ECAdapter";
	protected Context context = null;
	protected List<JSONObject> dataList = null;
	/** @see ValueBindCallBack */
	private ValueBindCallBack bindCallBack = null;
	/** @see OnDataLoadComplete */
	private OnDataLoadComplete dataLoadComplete = null;
	// ==============================⬇来自xml中的配置参数=========================
	protected int itemLayoutId = 0;
	private String url = "";
	private int requestMethod = 1;
	private String listKeyInJson = "";
	private int page = 1;
	private int loadSize = 10;
	private boolean isLatestData = true;
	// ==============================⬆来自xml中的配置参数=========================
	/** post请求时参数 */
	private JSONObject requestParams = new JSONObject();
	/** Volley中的请求队列 */
	private RequestQueue requestQueue = null;
	/**
	 * item中控件资源id与json中key的对应关系
	 * 
	 * @see com.yiciyuan.easycomponent.listview.ECPullToRefreshListView#setResourceJsonKeyMap(int[],
	 *      String[])
	 * */
	private SparseArray<String> resourceJsonKeyMap = null;
	/** 自定义json解析，提供此值后url也可为空，不需要时可为空 */
	private JSONDecodeListener jsonDecodeListener = null;

	/** constructor */
	public ECAdapter(Context context) {
		this.context = context;
		requestQueue = Volley.newRequestQueue(context);
		dataList = new ArrayList<JSONObject>();
	}

	@Override
	public int getCount() {
		return dataList == null ? 0 : dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList == null ? null : dataList.get(position - 1);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ECViewHolder holder = ECViewHolder.get(context, convertView,
				parent, itemLayoutId, position);
		try {
			/** 文本与图片绑定 */
			if (resourceJsonKeyMap != null) {
				JSONObject itemJson = dataList.get(position);
				for (int i = 0; i < resourceJsonKeyMap.size(); i++) {
					int resourceId = resourceJsonKeyMap.keyAt(i);
					String jsonKey = resourceJsonKeyMap.get(resourceId);
					String jsonValue = itemJson.getString(jsonKey);
					View view = holder.getView(resourceId);

					if (view instanceof TextView) {
						TextView tv = (TextView) view;
						tv.setText(jsonValue);
					} else if (view instanceof ImageView) {
						ImageView iv = (ImageView) view;
						iv.setImageURI(Uri.parse(jsonValue));
					}

				}
			}
			/** 自定义绑定 */
			if (bindCallBack != null) {
				bindCallBack.bind(holder, dataList.get(position));
			}
			return holder.getConvertView();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 请求方法 */
	private void request() {
		// 非自定义json解析方式
		if (Request.Method.GET == requestMethod || url.endsWith("&")) {
			url = url.substring(0, url.length() - 1);
			Log.i("请求参数：", "url=" + url);
		} else {
			try {
				this.requestParams.put("page", this.page);
				this.requestParams.put("size", loadSize);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Log.i("请求参数：", this.requestParams.toString() + "url=" + url);
		}
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				requestMethod, url, this.requestParams,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject resultJson) {
						if (dataLoadComplete != null) {
							dataLoadComplete.onComplete();
						}
						// 如果是自定义json解析方式
						if (jsonDecodeListener != null) {
							JSONArray jsonArray = jsonDecodeListener.decode(resultJson);
							refreshList(jsonArray);
							return;
						}
						Log.i("返回值：", resultJson.toString());
						refreshList(parseJsonData(resultJson, listKeyInJson));
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "返回数据：error="
								+ (error == null ? null : error.getMessage()));
						Log.e(TAG, "可能原因如下：1､请求URL不存在；\n2､服务器返回未返回任何数据");
						// 从url中获得action
						String action = url.substring(url.lastIndexOf("/") + 1, url.contains("?") ? url.indexOf("?") : url.length());
						// 调用测试数据
						if (action != null) {
							String debugResultStr = TestDataFactory
									.getValue(action);
							if (debugResultStr != null) {
								try {
									Log.e(TAG, "当前正使用测试数据:" + debugResultStr);
									JSONObject debugResultJson = new JSONObject(
											debugResultStr);
									// 如果是自定义json解析方式
									if (jsonDecodeListener != null) {
										JSONArray jsonArray = jsonDecodeListener.decode(debugResultJson);
										refreshList(jsonArray);
										return;
									}
									// 解析list在json中的key，解决嵌套问题
									refreshList(parseJsonData(debugResultJson, listKeyInJson));
								} catch (JSONException e) {
									Log.i(TAG,
											"返回数据(测试数据)解析成json报错，请检查文件testdata.properties中测试json格式是否正确。");
								}
							} else {
								Log.i(TAG, "testdata.properties中不存在方法" + action
										+ "对应的测试数据。");
							}
						}
					}
				});
		if (requestQueue != null) {
			requestQueue.add(jsonObjectRequest);
		}
	}

	/**
	 * 从返回json中解析出json数组
	 * 
	 * @param json
	 *            服务器返回的json
	 * @param jsonKey
	 *            返回json中json数组的key，有层次关系，如 { "body": { "list":[ ] } }
	 *            则jsonKey为body.list
	 * @return 取出后的json数组
	 */
	public JSONArray parseJsonData(JSONObject json, String jsonKey) {
		String[] jsonKeyArray = listKeyInJson.split(".");
		try {
			if (jsonKeyArray.length == 0) {
				JSONArray jsonArray = json.getJSONArray(listKeyInJson);
				return jsonArray;
			}
			for (int i = 0; i < jsonKeyArray.length; i++) {
				JSONObject tempJson = json;
				// 如果是最后一层，则认为是json数组
				if (i == jsonKeyArray.length - 1) {
					JSONArray jsonArray = tempJson
							.getJSONArray(jsonKeyArray[i]);
					return jsonArray;
				} else {
					tempJson = tempJson.getJSONObject(jsonKeyArray[i]);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 显示解析后的数据，刷新界面
	 * @param jsonArray 从服务器得到的数据
	 * */
	public void refreshList(JSONArray jsonArray) {
		try {
			if (page == 1) {
				dataList.clear();
			}
			for (int i = 0; i < jsonArray.length(); i++) {
				dataList.add(jsonArray.getJSONObject(i));
			}
			notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public void setListKeyInJson(String listKeyInJson) {
		this.listKeyInJson = listKeyInJson;
	}

	/** 加载最新 */
	public void requestLastest() {
		this.page = 1;
		if (this.requestParams != null) {
			try {
				this.requestParams.put("page", this.page);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		request();
		isLatestData = true;
	}

	/** 加载下一页 */
	public void requestNext() {
		this.page++;
		if (this.requestParams != null) {
			try {
				this.requestParams.put("page", this.page);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		request();
		isLatestData = false;
	}

	public void setOnDataLoadComplete(OnDataLoadComplete dataLoadComplete) {
		this.dataLoadComplete = dataLoadComplete;
	}

	public void setRequestMethod(int requestMethod) {
		this.requestMethod = requestMethod;
	}

	public void setResourceJsonKeyMap(SparseArray<String> resourceJsonKeyMap) {
		this.resourceJsonKeyMap = resourceJsonKeyMap;
	}

	private boolean isAddQuestionMark = true;

	/**
	 * @see com.yiciyuan.easycomponent.listview.ECPullToRefreshListView#addRequestParams(String,
	 *      Object)
	 * */
	public void addRequestParams(String key, Object value) {
		// 根据请求方式构建请求参数
		if (Request.Method.GET == requestMethod) {
			if (isAddQuestionMark) {
				url += "?";
			}
			url += (key + "=" + value + "&");
			isAddQuestionMark = false;
		} else {
			// 如果是post请求
			try {
				if (this.requestParams == null) {
					this.requestParams = new JSONObject();
				}
				this.requestParams.put(key, value);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public void setItemLayoutId(int itemLayoutId) {
		this.itemLayoutId = itemLayoutId;
	}

	public void setValueBindCallBack(ValueBindCallBack bindCallBack) {
		this.bindCallBack = bindCallBack;
	}

	public void setLoadSize(int loadSize) {
		this.loadSize = loadSize;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<JSONObject> getDataList() {
		return dataList;
	}

	public void setJsonDecodeListener(JSONDecodeListener jsonDecodeListener) {
		this.jsonDecodeListener = jsonDecodeListener;
	}

	/**
	 * 加载完成回调
	 * */
	public interface OnDataLoadComplete {
		public void onComplete();
	}

	/**
	 * @return 如果是下拉取最新返回true，否则返回false
	 * */
	public boolean isLatestData() {
		return this.isLatestData;
	}

	/**
	 * 完成adapter中getView里面，控件与值的绑定
	 * */
	public interface ValueBindCallBack {
		/**
		 * @param holder
		 *            is
		 *            {@link com.yiciyuan.easycomponent.listview.ECViewHolder}
		 * @param json
		 *            与item对应的业务对象json
		 * */
		public void bind(ECViewHolder holder, JSONObject json)
				throws JSONException;
	}

	/**
	 * 自定义json解析方式
	 * */
	public interface JSONDecodeListener {
		public JSONArray decode(JSONObject json);
	}
}
