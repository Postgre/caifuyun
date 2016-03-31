package com.zepan.caifuyun.entity;

import java.util.HashMap;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.zepan.android.sdk.JsonTransferable;


public class Picture implements JsonTransferable,Parcelable {

	private int id;//产品id
	private String productName;//产品名称
	private String productPicture;//产品图片地址
	
	public Picture(){}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	protected  Picture(Parcel in) {
		this.id=in.readInt();
		this.productName=in.readString();
		this.productPicture=in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(productName);
		dest.writeString(productPicture);
	}

	@Override
	public Map<String, String> keyMatch() {
		Map<String, String> map=new HashMap<String, String>();
		return map;
	}
	// 实例化静态内部对象CREATOR实现接口Parcelable.Creator
	public static final Creator<Picture> CREATOR = new Creator<Picture>() {

		@Override
		public Picture[] newArray(int size) {
			return new Picture[size];
		}

		// 将Parcel对象反序列化为ParcelableDate
		@Override
		public Picture createFromParcel(Parcel source) {
			return new Picture(source);
		}
	};

	public String getProductPicture() {
		return productPicture;
	}

	public void setProductPicture(String productPicture) {
		this.productPicture = productPicture;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

}
