package com.zepan.caifuyun.entity;

import java.util.HashMap;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.zepan.android.sdk.JsonTransferable;


public class History implements JsonTransferable,Parcelable {
	public static final String TAG = "History";
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	

	private String id;
	private String text;
	private String  time;
	//	private String  
	public History(){}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	protected  History(Parcel in) {
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
	}

	@Override
	public Map<String, String> keyMatch() {
		Map<String, String> map=new HashMap<String, String>();
		return map;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	// 实例化静态内部对象CREATOR实现接口Parcelable.Creator
	public static final Creator<History> CREATOR = new Creator<History>() {

		@Override
		public History[] newArray(int size) {
			return new History[size];
		}

		// 将Parcel对象反序列化为ParcelableDate
		@Override
		public History createFromParcel(Parcel source) {
			return new History(source);
		}
	};



}
