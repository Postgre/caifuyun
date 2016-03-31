package com.zepan.caifuyun.entity;

import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.zepan.android.sdk.JsonTransferable;

public class Tags implements JsonTransferable,Parcelable{
	private int id;//标签的id
	private String   name;//标签的名字


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public Tags(){ 

	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	protected Tags(Parcel in) {
		this.id=in.readInt();
		this.name=in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);


	}

	@Override
	public Map<String, String> keyMatch() {
		// TODO Auto-generated method stub
		return null;
	}
	
	// 实例化静态内部对象CREATOR实现接口Parcelable.Creator
		public static final Creator<Tags> CREATOR = new Creator<Tags>() {

			@Override
			public Tags[] newArray(int size) {
				return new Tags[size];
			}

			// 将Parcel对象反序列化为ParcelableDate
			@Override
			public Tags createFromParcel(Parcel source) {
				return new Tags(source);
			}
		};

}
