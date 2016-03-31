package com.zepan.caifuyun.entity;

import java.util.HashMap;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.zepan.android.sdk.JsonTransferable;


public class Company implements JsonTransferable,Parcelable{
	private int companyId;//公司id
	private String companyName;//公司名称


	public int getCompanyId() {
		return companyId;
	}
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public Company(){

	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}



	protected Company(Parcel in) {
		this.companyId=in.readInt();
		this.companyName=in.readString();

	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(companyId);
		dest.writeString(companyName);

	}

	@Override
	public Map<String, String> keyMatch() {
		Map<String, String> map=new HashMap<String, String>();
		map.put("id", "companyId");
		map.put("name", "companyName");
		return map;
	}
	// 实例化静态内部对象CREATOR实现接口Parcelable.Creator
	public static final Creator<Company> CREATOR = new Creator<Company>() {

		@Override
		public Company[] newArray(int size) {
			return new Company[size];
		}

		// 将Parcel对象反序列化为ParcelableDate
		@Override
		public Company createFromParcel(Parcel source) {
			return new Company(source);
		}
	};

}
