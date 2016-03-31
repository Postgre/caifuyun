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
package com.zepan.android.util;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

/**
 * 字符串校验工具类，如非空验证等。
 * @author aic
 * @version 1.0
 * @date 2014-10-11
 */
public class StringUtil {

	/**
	 * 判断给定的字符串是否为空。
	 * @param text 待校验的字符串
	 * @return 如果为null或字符串长度为0，返回true，否则返回false
	 * */
	public static boolean isNullOrEmpty(String text){
		return text == null || text.trim().isEmpty();
	}
	/**
	 * 根据属性名取方法名(set)
	 * @param proName 属性名
	 * @return 方法名
	 * */
	public static String getMethodOfSet(String proName){
		return "set" + toFirstCharUpper(proName);
	}

	/**
	 * 首字母大写
	 * @param srcStr 源字符串
	 * @return 首字母大写后的字符串
	 * */
	public static String toFirstCharUpper(String srcStr){
		if(srcStr == null || srcStr.trim().length() == 0){
			return null;
		}
		srcStr = srcStr.substring(0, 1).toUpperCase(Locale.ENGLISH) + srcStr.substring(1);
		char[] cs = srcStr.toCharArray();
		cs[0] -= 32;
		return String.valueOf(cs);
	}

	/**
	 * Judging is a image url format string.
	 * */
	public static boolean isImageUrl(String url){
		if(url == null){
			return false;
		}
		//		return url.matches("\\w+\\.(jpg|jpeg|gif|bmp|png)");
		String enableFormat[] = {"jpg", "jpeg","gif", "bmp", "png"};
		for(String format : enableFormat){
			if(url.endsWith(format)){
				return true;
			}
		}
		return false;
	}

	public final static String md5(String s) {
		char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	/** 时间戳转化为时间字符串 */
	public static String longToFormatTimeStr(String longStr, String format) {
		long time = Long.parseLong(longStr);
		SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.CHINA);
		//SimpleDateFormat formatter = new SimpleDateFormat(format);
		formatter.setTimeZone(TimeZone.getTimeZone("GMT-0"));
		//formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		return formatter.format(new Date(time+8*3600000));
	}
	/** utc时间戳转化为时间字符串 */
	public static String longToFormatTimeUtcStr(String longStr, String format) {
		long time = Long.parseLong(longStr);
		//SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.CHINA);
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		//formatter.setTimeZone(TimeZone.getTimeZone("GMT-0"));
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		return formatter.format(new Date((time+8*3600)* 1000L));
	}
	/** 
	 * 将utc时间戳转为代表"距现在多久之前"的字符串 
	 * @param timeStr   时间戳 
	 * @return 
	 */  
	public static String getStandardDate(String timeStr, String format) {  
	  
	    StringBuffer sb = new StringBuffer();  
	  
	    long t = Long.parseLong(timeStr);  
	    long time = System.currentTimeMillis() - (((t+8*3600)* 1000L));  
	    long mill = (long) Math.ceil(time /1000);//秒前  
	  
	    long minute = (long) Math.ceil(time/60/1000.0f);// 分钟前  
	  
	    long hour = (long) Math.ceil(time/60/60/1000.0f);// 小时  
	  
	    long day = (long) Math.ceil(time/24/60/60/1000.0f);// 天前  
	  
	    if (day - 1 > 0) {  
	        sb.append(day + "天");
	        if(day>30){
	        	SimpleDateFormat formatter = new SimpleDateFormat(format);
	    		//formatter.setTimeZone(TimeZone.getTimeZone("GMT-0"));
	    		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
	    		return formatter.format(new Date((t+8*3600)* 1000L));
	        }
	    } else if (hour - 1 > 0) {  
	        if (hour >= 24) {  
	            sb.append("1天");  
	        } else {  
	            sb.append(hour + "小时");  
	        }  
	    } else if (minute - 1 > 0) {  
	        if (minute == 60) {  
	            sb.append("1小时");  
	        } else {  
	            sb.append(minute + "分钟");  
	        }  
	    } else if (mill - 1 > 0) {  
	        if (mill == 60) {  
	            sb.append("1分钟");  
	        } else {  
	            sb.append(mill + "秒");  
	        }  
	    } else {  
	        sb.append("刚刚");  
	    }  
	    if (!sb.toString().equals("刚刚")) {  
	        sb.append("前");  
	    }  
	    return sb.toString();  
	}  
	//uri转成file 
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] {
						split[1]
				};

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}
	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}
	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @param selection (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
			String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = {
				column
		};

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

}
