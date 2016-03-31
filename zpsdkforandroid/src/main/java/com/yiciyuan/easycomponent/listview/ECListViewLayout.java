package com.yiciyuan.easycomponent.listview;/*
 * Copyright (C) 15/8/11 Lei.Zhang
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

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.yiciyuan.easycomponent.listview.ECAdapter.JSONDecodeListener;
import com.zepan.android.sdk.R;

/**
 *           ___________     ___________
 *         / __________/   / __________/
 *       / /_________    / /
 *     / /__________/  / /
 *   / /__________   / /_________
 * /____________/  /____________/
 * <p/>
 * 自定义view基于{@link RelativeLayout}，具体描述如下：<br>
 * <strong>功能点:</strong><br>
 * <ul>
 * <li>不需再建Adapter。</li>
 * <li>2､不用再建ListView中item的实体类。</li>
 * <li>3､不用再写请求方法。</li>
 * <li>4､支持xml中配置url，请求方法，加载条数。</li>
 * <li>5､对于item中的文本和图片，只需简单代码即可；对于文本和图片之外的布局，请参考{@link ECAdapter.ValueBindCallBack}。</li>
 * <li>6､支持切换不同的刷新控件，目前仅限PullToRefreshListView和SwipeListView</li>
 * <li>7､支持假数据测试，但分页功能无效</li>
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
 * <li>listKeyInJson:jsonArray在返回json中对应的key，不指定为list;如果层次太深，可通过点来表示，如body.list</li>
 * <li>itemLayout:item布局资源</li>
 * <li>requestMethod: 请求方法</li>
 * <li>refreshComponent:刷新控件</li>
 * <li>refreshMode: 刷新方式</li>
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
public class ECListViewLayout extends RelativeLayout implements ECAdapter.OnDataLoadComplete {

    /**
     * 控件－PullToRefreshListView
     */
    public static final int REFRESH_COMPONENT_PULLTOREFRESH = 0;
    /**
     * 控件－SwipeListView
     */
    public static final int REFRESH_COMPONENT_SWIPE = 1;
    /**
     * 控件类型
     */
    private int refreshComponent = REFRESH_COMPONENT_PULLTOREFRESH;
    /**
     * 上拉
     */
    public static final int REFRESH_MODE_TOP = 0;
    /**
     * 下拉
     */
    public static final int REFRESH_MODE_BOTTOM = 1;
    /**
     * 上拉 ＋ 下拉
     */
    public static final int REFRESH_MODE_BOTH = 2;
    /**
     * adatper
     */
    private ECAdapter adapter = null;
    /**
     * Item点击事件，原始OnItemClickListener依然能用。
     *
     * @see IECListView.OnECItemClickListener
     */
    private IECListView.OnECItemClickListener itemClickListener = null;
    public ECListViewLayout(final Context context, AttributeSet attrs) {
        super(context, attrs);
        //==============================获取XML中配置的参数==================
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ECListViewLayout);
        // 获取请求信息
        int itemResourceId = a.getResourceId(R.styleable.ECListViewLayout_itemLayout, 0);
        String url = a.getString(R.styleable.ECListViewLayout_url);
        int loadSize = a.getInt(R.styleable.ECListViewLayout_loadSize, 0);
        String listKeyInJson = a.getString(R.styleable.ECListViewLayout_listKeyInJson);
        int requestMethod = a.getInt(
                R.styleable.ECListViewLayout_requestMethod, 0);
        int refreshMode = a.getInt(
                R.styleable.ECListViewLayout_refreshMode, 0);
        // 获取控件类型
        refreshComponent = a.getInt(
                R.styleable.ECListViewLayout_refreshComponent, REFRESH_COMPONENT_PULLTOREFRESH);
        if (refreshComponent == REFRESH_COMPONENT_PULLTOREFRESH) {
            View.inflate(context, R.layout.inflate_eclistlayout_pulltorefresh, this);
        } else if (refreshComponent == REFRESH_COMPONENT_SWIPE) {
            View.inflate(context, R.layout.inflate_eclistlayout_swipe, this);
        }
        a.recycle();
        // 设置adapter属性
        IECListView listView = (IECListView) findViewById(android.R.id.list);
        adapter = listView.getAdaper();
        adapter.setItemLayoutId(itemResourceId);
        adapter.setUrl(url);
        adapter.setLoadSize(loadSize);
        adapter.setListKeyInJson(listKeyInJson);
        adapter.setRequestMethod(requestMethod);
        setRefreshMode(refreshMode);
        setOnRefreshListener();
        adapter.setOnDataLoadComplete(this);
        this.post(new Runnable() {
            @Override
            public void run() {
                setRefreshing(true);
            }
        });
        //==============================Item点击事件==========================
        ListView contentListView = getListView();
        if(contentListView != null){
            contentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        }
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
     * 设置刷新模式
     *
     * @param mode 刷新模式
     * @see #REFRESH_MODE_TOP
     * @see #REFRESH_MODE_BOTTOM
     * @see #REFRESH_MODE_BOTH
     */
    public void setRefreshMode(int mode) {
        if (refreshComponent == REFRESH_COMPONENT_PULLTOREFRESH) {
            ECPullToRefreshListView ecListView = (ECPullToRefreshListView) findViewById(android.R.id.list);
            switch (mode) {
                case REFRESH_MODE_TOP:
                    ecListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                    break;
                case REFRESH_MODE_BOTTOM:
                    ecListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    break;
                case REFRESH_MODE_BOTH:
                    ecListView.setMode(PullToRefreshBase.Mode.BOTH);
                    break;
            }
        } else if (refreshComponent == REFRESH_COMPONENT_SWIPE) {
            SwipyRefreshLayout swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipe);
            switch (mode) {
                case REFRESH_MODE_TOP:
                    swipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.TOP);
                    break;
                case REFRESH_MODE_BOTTOM:
                    swipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTTOM);
                    break;
                case REFRESH_MODE_BOTH:
                    swipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
                    break;
            }
        }

    }

    /**
     * 设置刷新事件
     */
    public void setOnRefreshListener() {
        if (refreshComponent == REFRESH_COMPONENT_SWIPE) {
            final SwipyRefreshLayout swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipe);
            swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh(SwipyRefreshLayoutDirection swipyRefreshLayoutDirection) {
                    if (SwipyRefreshLayoutDirection.TOP == swipyRefreshLayoutDirection) {
                        adapter.requestLastest();
                    } else {
                        adapter.requestNext();
                    }
                }
            });
        }
    }

    /**
     * 设置是否界面初始加载
     */
    private void setRefreshing(boolean isRefreshing) {
        if (!isRefreshing) {
            return;
        }
        if (refreshComponent == REFRESH_COMPONENT_SWIPE) {
            final SwipyRefreshLayout swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipe);
            // 界面显示时加载数据，并显示刷新图标。
            swipyRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipyRefreshLayout.setRefreshing(true);
                    adapter.requestLastest();
                }
            });
        }
    }

    /**请求完成后调用*/
    @Override
    public void onComplete() {
        if ((refreshComponent == REFRESH_COMPONENT_PULLTOREFRESH)) {
            ECPullToRefreshListView ecListView = (ECPullToRefreshListView) findViewById(android.R.id.list);
            ecListView.onRefreshComplete();
        } else if (refreshComponent == REFRESH_COMPONENT_SWIPE) {
            final SwipyRefreshLayout swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipe);
            swipyRefreshLayout.setRefreshing(false);
        }
        // List滑到底部
        getListView().post(new Runnable() {
            @Override
            public void run() {
                getListView().smoothScrollToPosition(adapter.isLatestData() ? 0 : getListView().getBottom());
//                getListView().setSelection(adapter.isLatestData() ? 0 : getListView().getBottom());
            }
        });
    }

    /**
     *  返回列表控件ListView
     * */
    public ListView getListView(){
        if ((refreshComponent == REFRESH_COMPONENT_PULLTOREFRESH)) {
            ECPullToRefreshListView ecListView = (ECPullToRefreshListView) findViewById(android.R.id.list);
            return ecListView.getRefreshableView();
        } else if (refreshComponent == REFRESH_COMPONENT_SWIPE) {
            return (ListView) findViewById(android.R.id.list);
        }
        return null;
    }

    /**
     * 设置item点击回调
     *
     * @see ECAdapter
     * @see IECListView.OnECItemClickListener
     */
    public void setOnItemClickListener(IECListView.OnECItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    /**
     * 自定义json解析方式
     * @param 
     * */
	public void setJsonDecodeListener(JSONDecodeListener jsonDecodeListener) {
		adapter.setJsonDecodeListener(jsonDecodeListener);
	}
}
