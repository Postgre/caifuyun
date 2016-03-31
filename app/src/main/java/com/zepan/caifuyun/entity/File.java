package com.zepan.caifuyun.entity;

import java.util.HashMap;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.zepan.android.sdk.JsonTransferable;


public class File implements JsonTransferable,Parcelable {

	private String fileId;//产品id
	private String fileName;//产品名称
	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean isChcek() {
		return isChcek;
	}

	public void setChcek(boolean isChcek) {
		this.isChcek = isChcek;
	}
	private boolean isChcek;
	public File(){}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	protected  File(Parcel in) {
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
	}

	@Override
	public Map<String, String> keyMatch() {
		Map<String, String> map=new HashMap<String, String>();
		map.put("product_id", "productId");
		map.put("name", "fileName");
		map.put("product_picture", "productPicture");
		return map;
	}
	// 实例化静态内部对象CREATOR实现接口Parcelable.Creator
	public static final Creator<File> CREATOR = new Creator<File>() {

		@Override
		public File[] newArray(int size) {
			return new File[size];
		}

		// 将Parcel对象反序列化为ParcelableDate
		@Override
		public File createFromParcel(Parcel source) {
			return new File(source);
		}
	};



}
