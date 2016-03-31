/*
 * Copyright (C) 2015 Lei.Zhang
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

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;

import com.fortysevendeg.swipelistview.SwipeListView;


public class ECSwipeListView extends SwipeListView implements IECListView{

    /**
     * 适配器
     */
    private ECAdapter adapter = null;
    /**
     * Item点击事件，原始OnItemClickListener依然能用。
     *
     * @see OnECItemClickListener
     */
    private OnECItemClickListener itemClickListener = null;

    /**
     * constructor
     */
    public ECSwipeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //==============================属性初始化=========================
        adapter = new ECAdapter(getContext());
        setAdapter(adapter);
        //==============================Item点击事件==========================
        this.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject itemJson = adapter.getDataList().get(position - 1);
                if (itemClickListener != null) {
                    try {
                        itemClickListener.OnItemClick(parent, view, position, itemJson);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //==============================刷新方式==========================
    }

    /**
     * @see ECAdapter#getView(int, View, android.view.ViewGroup)
     * @see ECAdapter.ValueBindCallBack
     */
    public void setBindCallBack(ECAdapter.ValueBindCallBack bindCallBack) {
        adapter.setValueBindCallBack(bindCallBack);
    }

    /**
     * 添加请求参数
     *
     * @param key   请求参数json中对应的key
     * @param value 请求参数json中对应的value
     */
    public void addRequestParams(String key, Object value) {
        adapter.addRequestParams(key, value);
    }

    /**
     * 设置item中控件资源id与json中key的对应关系。
     */
    public void setResourceJsonKeyMap(SparseArray<String> resourceJsonKeyMap) {
        adapter.setResourceJsonKeyMap(resourceJsonKeyMap);
    }

    /**
     * 设置item中控件资源id与json中key的对应关系。
     *
     * @param resource 资源id集合
     * @param jsonKey  json中key集合
     */
    public void setResourceJsonKeyMap(int[] resource, String[] jsonKey) {
        SparseArray<String> resourceJsonKeyMap = new SparseArray<>();
        for (int i = 0; i < resource.length; i++) {
            resourceJsonKeyMap.put(resource[i], jsonKey[i]);
        }
        adapter.setResourceJsonKeyMap(resourceJsonKeyMap);
    }

    /**
     * 设置item点击回调
     *
     * @see ECAdapter
     * @see OnECItemClickListener
     */
    public void setOnItemClickListener(OnECItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ECAdapter getAdaper() {
        return adapter;
    }
}
