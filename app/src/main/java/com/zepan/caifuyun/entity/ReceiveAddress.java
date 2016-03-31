package com.zepan.caifuyun.entity;

import java.util.HashMap;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.zepan.android.sdk.JsonTransferable;


public class ReceiveAddress implements JsonTransferable,Parcelable {

	private String region;
	private String address;
	private String name;
	private int id;
	private String zipcode;
	private String mobile;
	private int default_address;
	
	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public ReceiveAddress(){}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	public ReceiveAddress(Parcel in){
		this.region=in.readString();
		this.address=in.readString();
		this.name=in.readString();
		this.id=in.readInt();
		this.zipcode=in.readString();
		this.mobile=in.readString();
		this.default_address=in.readInt();
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(region);
		dest.writeString(address);
		dest.writeString(name);
		dest.writeInt(id);
		dest.writeString(zipcode);
		dest.writeString(mobile);
		dest.writeInt(default_address);
	}

	@Override
	public Map<String, String> keyMatch() {
		Map<String, String> map=new HashMap<String, String>();
		return map;
	}
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
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
	public int getDefault_address() {
		return default_address;
	}

	public void setDefault_address(int default_address) {
		this.default_address = default_address;
	}
	// 实例化静态内部对象CREATOR实现接口Parcelable.Creator
	public static final Creator<ReceiveAddress> CREATOR = new Creator<ReceiveAddress>() {

		@Override
		public ReceiveAddress[] newArray(int size) {
			return new ReceiveAddress[size];
		}

		// 将Parcel对象反序列化为ParcelableDate
		@Override
		public ReceiveAddress createFromParcel(Parcel source) {
			return new ReceiveAddress(source);
		}
	};

}
