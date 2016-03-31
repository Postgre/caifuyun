package com.yiciyuan.easycomponent.listview;/*
 * Copyright (C) 15/8/12 Lei.Zhang
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

import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public interface IECListView {

//    public void setItemLayoutId(int itemLayoutId);
//
//    public void setLoadSize(int loadSize);
//
//    public void setUrl(String url);
//    /**
//     * @see ECAdapter#getView(int, android.view.View, android.view.ViewGroup)
//     * @see com.yiciyuan.easycomponent.listview.ECAdapter.ValueBindCallBack
//     */
//    public void setBindCallBack(ECAdapter.ValueBindCallBack bindCallBack);
//
//    /**
//     * 添加请求参数
//     *
//     * @param key   请求参数json中对应的key
//     * @param value 请求参数json中对应的value
//     */
//    public void addRequestParams(String key, Object value);
//
//    /**
//     * 设置item中控件资源id与json中key的对应关系。
//     */
//    public void setResourceJsonKeyMap(SparseArray<String> resourceJsonKeyMap);
//
//    /**
//     * 设置item中控件资源id与json中key的对应关系。
//     *
//     * @param resource 资源id集合
//     * @param jsonKey  json中key集合
//     */
//    public void setResourceJsonKeyMap(int[] resource, String[] jsonKey);
//
//    /**
//     * 作为Item点击事件的回调，通常在Activity或fragment中实现此方法。
//     */
    public ECAdapter getAdaper();

    public interface OnECItemClickListener {
        void OnItemClick(AdapterView<?> parent, View view, int position, JSONObject itemJson) throws JSONException;
    }
}
