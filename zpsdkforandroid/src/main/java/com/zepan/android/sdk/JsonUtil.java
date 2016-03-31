package com.zepan.android.sdk;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JsonUtil {

	/**
	 * 将任意的JSON对象转换成对应的业务实体类。 如果json参数为空或未指定目标转化类，则返回null。
	 * 
	 * @param json
	 *            Json对象
	 * @param targClass
	 *            待转成的类
	 * */
	@SuppressWarnings("unchecked")
	public static JsonTransferable jsonToEntity(JSONObject json,
			Class<? extends JsonTransferable> targetClass) {
		if (json == null || targetClass == null) {
			return null;
		}
		try {
			// 目标类实例化
			JsonTransferable instance = targetClass.newInstance();
			// 获取Json键与目标类属性名的对应关系
			Map<String, String> keyRelactionMap = instance.keyMatch();
			// 迭代Json中所有的Key
			Iterator<String> jsonIterator = json.keys();
			// 如果Json键-属性名map为空，表明用户未指定所有对应关系，默认属性名与Json键相同
			Field field = null;
			// targetClass属性集
			Field[] fields = targetClass.getDeclaredFields();
			while (jsonIterator.hasNext()) {
				String jsonKey = jsonIterator.next();

				//Log.i("----------------jsonKey=", jsonKey);
				if (keyRelactionMap == null) {
					// 判断是类中是否包含jsonKey对应的属性
					for (Field innerField : fields) {
						if (innerField.getName().equals(jsonKey)) {
							// 取类中的属性
							field = innerField;
						}
					}
				} else {
					String fieldName = keyRelactionMap.get(jsonKey);
					// 如果属性名为空，也表明该用户未指定该属性对应关系，则令属性名等于Json Key
					if (fieldName != null) {
						for (Field innerField : fields) {
							if (innerField.getName().equals(fieldName)) {
								// 取类中的属性
								field = innerField;
							}
						}
					} else {
						for (Field innerField : fields) {
							if (innerField.getName().equals(jsonKey)) {
								// 取类中的属性
								field = innerField;
							}
						}
					}
				}
				if (field != null) {
					field.setAccessible(true);
					// 如果当前属性类型实现接口为JsonTransferable
					if (JsonTransferable.class
							.isAssignableFrom(field.getType())) {
						JSONObject subJson = json.getJSONObject(jsonKey);
						field.set(
								instance,
								jsonToEntity(
										subJson,
										(Class<? extends JsonTransferable>) field
												.getType()));
					} else if (List.class.isAssignableFrom(field.getType())) {// collection
						JSONArray subJsonArray = json.getJSONArray(jsonKey);

						ParameterizedType pt = (ParameterizedType) field
								.getGenericType();
						Class<Object> returnTypeClass = (Class<Object>) pt
								.getActualTypeArguments()[0];
						field.set(instance,
								jsonArrayToList(subJsonArray, returnTypeClass));
					} else {
						if(isDataValid(json.get(jsonKey).toString())){
							field.set(instance, json.get(jsonKey));
						}
					}
					field = null;
				}
			}
			return instance;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 判断给定的值是否合法
	 * @return true合法，false不合法
	 * */
	private static boolean isDataValid(String value){
		return value != null && !"null".equals(value) && !TextUtils.isBlank(value);
	}
	
	// public static List<JsonTransferable> jsonArrayToList(
	// JSONArray jsonArray, Class<? extends JsonTransferable> targetClass) {
	// if (jsonArray == null || jsonArray.length() == 0) {
	// return null;
	// }
	// List<JsonTransferable> list = new ArrayList<JsonTransferable>();
	// try {
	// for (int i = 0; i < jsonArray.length(); i++) {
	// JSONObject json = jsonArray.getJSONObject(i);
	// list.add(jsonToEntity(json, targetClass));
	// }
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return list;
	// }

	public static List<Object> jsonArrayToList(JSONArray jsonArray,
			Class<? extends Object> targetClass) {
		if (jsonArray == null || jsonArray.length() == 0) {
			return null;
		}
		List<Object> list = new ArrayList<Object>();
		try {
			if (JsonTransferable.class.isAssignableFrom(targetClass)) {
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject json = jsonArray.getJSONObject(i);
					list.add(jsonToEntity(json,
							(Class<? extends JsonTransferable>) targetClass));
				}
			} else {
				for (int i = 0; i < jsonArray.length(); i++) {
					list.add(jsonArray.get(i));
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}
