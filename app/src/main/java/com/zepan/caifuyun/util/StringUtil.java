/*
 * Copyright (C) 2014-10-11 The Android Open Source Project
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
package com.zepan.caifuyun.util;

import com.zepan.caifuyun.R;

import android.net.Uri;

/**
 * 
 */
public class StringUtil {

	/**
	 * 判断给定的字符串是否为空，是否为是http格式的字符串。
	 * @param url 待校验的字符串
	 * @return 如果为null或不是http格式的字符串，返回false，否则返回true
	 * */
	public static boolean isNullOrHttp(String url){
		if(url!=null&&!"".equals(url.trim())){
			if(url.length()>4){
				if(url.substring(0, 4).equals("http")
						||url.substring(0, 4).equals("HTTP")){
					return true;
				}else{ 
					return false;
				}
			}else{
				return false;			
			}

		}else{
			return false;	
		}
	}



}
