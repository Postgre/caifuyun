package com.zepan.caifuyun.entity;

import java.util.List;
import java.util.Map;

import com.zepan.android.sdk.JsonTransferable;

import android.os.Parcel;
import android.os.Parcelable;

public class Record implements JsonTransferable,Parcelable{

	//客户id
	private int accountId;
	//名字
	private String   accountName;
	//内容
	private String   content;
	
	private long createdAt;
	//纪录id
	private int id;
	private List<String> imgFilesList;
	//纬度
	private double latitude;
	//经度
	private double longitude;
	//声音长度
	private int soundDuration;
	//声音URL
	private String soundUrl;
	//我的ID
	private int uid;

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<String> getImgFilesList() {
		return imgFilesList;
	}

	public void setImgFilesList(List<String> imgFilesList) {
		this.imgFilesList = imgFilesList;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public int getSoundDuration() {
		return soundDuration;
	}

	public void setSoundDuration(int soundDuration) {
		this.soundDuration = soundDuration;
	}

	public String getSoundUrl() {
		return soundUrl;
	}

	public void setSoundUrl(String soundUrl) {
		this.soundUrl = soundUrl;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}




	public Record(){ 

	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	protected Record(Parcel in) {


	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {


	}

	@Override
	public Map<String, String> keyMatch() {
		// TODO Auto-generated method stub
		return null;
	}

	// 实例化静态内部对象CREATOR实现接口Parcelable.Creator
	public static final Creator<Record> CREATOR = new Creator<Record>() {

		@Override
		public Record[] newArray(int size) {
			return new Record[size];
		}

		// 将Parcel对象反序列化为ParcelableDate
		@Override
		public Record createFromParcel(Parcel source) {
			return new Record(source);
		}
	};

}
