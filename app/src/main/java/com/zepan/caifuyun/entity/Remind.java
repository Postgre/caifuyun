package com.zepan.caifuyun.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.zepan.android.sdk.JsonTransferable;


public class Remind implements JsonTransferable,Parcelable{
	private int remindType;//提醒类型
	private String remindName;//提醒名称
    private List<File> childFile;
    private boolean isChcek;//是否选中
    private boolean isOpen;//是否打开
	public Remind(){

	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}



	public List<File> getChildFile() {
		return childFile;
	}
	public void setChildFile(List<File> childFile) {
		this.childFile = childFile;
	}
	public boolean isChcek() {
		return isChcek;
	}
	public void setChcek(boolean isChcek) {
		this.isChcek = isChcek;
	}
	protected Remind(Parcel in) {
		this.remindType=in.readInt();
		this.remindName=in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(remindType);
		dest.writeString(remindName);
	}

	@Override
	public Map<String, String> keyMatch() {
		Map<String, String> map=new HashMap<String, String>();
		map.put("name", "remindName");
		map.put("child", "childFile");
		map.put("type", "remindType");
		return map;
	}
	public int getRemindType() {
		return remindType;
	}
	public void setRemindType(int remindType) {
		this.remindType = remindType;
	}
	public String getRemindName() {
		return remindName;
	}
	public void setRemindName(String remindName) {
		this.remindName = remindName;
	}
	public boolean isOpen() {
		return isOpen;
	}
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
	// 实例化静态内部对象CREATOR实现接口Parcelable.Creator
	public static final Creator<Remind> CREATOR = new Creator<Remind>() {

		@Override
		public Remind[] newArray(int size) {
			return new Remind[size];
		}

		// 将Parcel对象反序列化为ParcelableDate
		@Override
		public Remind createFromParcel(Parcel source) {
			return new Remind(source);
		}
	};

}
