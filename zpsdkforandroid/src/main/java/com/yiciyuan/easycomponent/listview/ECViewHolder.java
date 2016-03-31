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

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
/**
 *           ___________     ___________
 *         / __________/   / __________/
 *       / /_________    / /
 *     / /__________/  / /
 *   / /__________   / /_________
 * /____________/  /____________/
 * @see  ECAdapter
 */
public class ECViewHolder {

    private SparseArray<View> childViews;
    private View convertView;

    private ECViewHolder(Context context, ViewGroup container, int layoutId, int position) {
        this.childViews = new SparseArray<>();
        convertView = View.inflate(context, layoutId, null);
        convertView.setTag(this);
    }

    public static ECViewHolder get(Context context, View convertView, ViewGroup container, int layoutId, int position) {
        if (convertView == null) {
            return new ECViewHolder(context, container, layoutId, position);
        }else{
            return (ECViewHolder)convertView.getTag();
        }
    }

    public View getView(int viewId){
        View view = childViews.get(viewId);
        if(view == null){
            view = convertView.findViewById(viewId);
            childViews.put(viewId, view);
        }
        return view;
    }

    public View getConvertView(){
        return convertView;
    }
}





