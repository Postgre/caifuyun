package com.zepan.caifuyun.entity;

import java.util.HashMap;
import java.util.Map;

import com.zepan.android.sdk.JsonTransferable;

import android.os.Parcel;
import android.os.Parcelable;

public class UserInfo implements JsonTransferable,Parcelable{

	private String user_id;
	private String user_name;
	private String user_img;
	private String label;
	private boolean isChecked;
	private boolean isDelete;
	private boolean isClick=true;
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_img() {
		return user_img;
	}
	public void setUser_img(String user_img) {
		this.user_img = user_img;
	}
	public boolean isDelete() {
		return isDelete;
	}
	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	public UserInfo(){}
	public UserInfo(Parcel in) {
		this.user_name=in.readString();
		this.user_id=in.readString();
		this.user_img=in.readString();
		this.label=in.readString();
		this.isChecked=in.readInt()==0?false:true;
		this.isDelete=in.readInt()==0?false:true;
		this.isClick=in.readInt()==0?false:true;
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(user_name);
		dest.writeString(user_id);
		dest.writeString(user_img);
		dest.writeString(label);
		dest.writeInt(isChecked?1:0);
		dest.writeInt(isDelete?1:0);
		dest.writeInt(isClick?1:0);
	}
	@Override
	public Map<String, String> keyMatch() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", "user_id");
		map.put("image", "user_img");
		map.put("name", "user_name");
		return map;
	}
	
	public boolean isClick() {
		return isClick;
	}
	public void setClick(boolean isClick) {
		this.isClick = isClick;
	}

		// 实例化静态内部对象CREATOR实现接口Parcelable.Creator
		public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {

			@Override
			public UserInfo[] newArray(int size) {
				return new UserInfo[size];
			}

			// 将Parcel对象反序列化为ParcelableDate
			@Override
			public UserInfo createFromParcel(Parcel source) {
				return new UserInfo(source);
			}
		};
	
}
