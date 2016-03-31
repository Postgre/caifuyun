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
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 *           ___________     ___________
 *         / __________/   / __________/
 *       / /_________    / /
 *     / /__________/  / /
 *   / /__________   / /_________
 * /____________/  /____________/
 * <p/>
 * 自定义ListView基于{@link com.handmark.pulltorefresh.library.PullToRefreshListView}，具体描述如下：<br>
 * <strong>功能点:</strong><br>
 * <ul>
 * <li>不需再建Adapter。</li>
 * <li>2､不用再建ListView中item的实体类。</li>
 * <li>3､不用再写请求方法。</li>
 * <li>4､支持xml中配置url，请求方法，加载条数。</li>
 * <li>5､对于item中的文本和图片，只需简单代码即可；对于文本和图片之外的布局，请参考{@link ECAdapter.ValueBindCallBack}。</li>
 * </ul>
 * <strong>注意点:</strong><br>
 * <ul>
 * <li>
 * 1､post请求后，请求参数中page和size不需要指定，如果参数名不同，则可以另外通过方法{@link #addRequestParams(String, Object)}指定
 * </li>
 * </ul>
 * <strong>xml中自定义属性:</strong><br>
 * <ul>
 * <li>url:请求的Url</li>
 * <li>loadSize:一次性加载的条数</li>
 * <li>listKeyInJson:jsonArray在返回json中对应的key，不指定为list</li>
 * <li>itemLayout:item布局资源</li>
 * <li>requestMethod: 请求方法</li>
 * </ul>
 * <strong>准备:</strong><br>
 * <ul>gradle</ul>
 * compile 'com.mcxiaoke.volley:library:1.0.18';
 * compile 'com.facebook.fresco:fresco:0.6.0+';
 * compile project(":PullToRefresh");
 * <ul>eclise</ul>
 * 下载以下库：volley,fresco,PullToRefresh<br>
 * <strong>使用方法:</strong><br>
 * <ul>
 * <li>在Fragment或Activity的布局文件xml中使用此类</li>
 * <li>写item的布局文件</li>
 * <li>配置对应的属性，如url,size等，并指定item布局</li>
 * <li>配置请求参数</li>
 * <li>绑定item中文本与图片资源id与Json中key</li>
 * <li>item中非文本与图片控件的处理，如事件等</li>
 * </ul>
 *
 * @see ECAdapter
 * @see ECViewHolder
 */
public class ECPullToRefreshListView extends PullToRefreshListView implements IECListView{

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
    public ECPullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //==============================属性初始化=========================
        adapter = new ECAdapter(getContext());
        setAdapter(adapter);
        //==============================事件========================
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
        this.setMode(Mode.BOTH);
//        this.setOnRefreshListener(new OnRefreshListener<ListView>() {
//            @Override
//            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//                if (getHeaderLayout().isShown()) {
//                    adapter.requestLastest();
//                } else {
//                    adapter.requestNext();
//                }
//            }
//        });

        this.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                adapter.requestLastest();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                adapter.requestNext();
            }
        });

        this.post(new Runnable() {
            @Override
            public void run() {
                setRefreshing(true);
                adapter.requestLastest();
            }
        });
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
