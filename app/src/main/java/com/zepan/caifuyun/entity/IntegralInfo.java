package com.zepan.caifuyun.entity;

import java.util.HashMap;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;


import com.zepan.android.sdk.JsonTransferable;

//积分商城
public class IntegralInfo implements JsonTransferable,Parcelable{

	private String url;
	private String discount;
	private int num;
	private String content;
	
	
	 public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public IntegralInfo() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, String> keyMatch() {
		// TODO Auto-generated method stub
		Map<String, String> map=new HashMap<String, String>();
		return map;
	}
	// 实例化静态内部对象CREATOR实现接口Parcelable.Creator
		public static final Creator<Clues> CREATOR = new Creator<Clues>() {

			@Override
			public Clues[] newArray(int size) {
				return new Clues[size];
			}

			// 将Parcel对象反序列化为ParcelableDate
			@Override
			public Clues createFromParcel(Parcel source) {
				return new Clues(source);
			}
		};

}
