package com.zepan.caifuyun.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.zepan.android.sdk.JsonTransferable;

public class PersonalContacts implements JsonTransferable,Parcelable{
	
	private String first_letter;
	private List<UserInfo> users;
	
	public PersonalContacts(){}
	public PersonalContacts(Parcel in) {
		// TODO Auto-generated constructor stub
		this.first_letter=in.readString();
		
	}
	
	public String getFirst_letter() {
		return first_letter;
	}
	public void setFirst_letter(String first_letter) {
		this.first_letter = first_letter;
	}
	public List<UserInfo> getUsers() {
		return users;
	}
	public void setUsers(List<UserInfo> users) {
		this.users = users;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(first_letter);
		
	}
	@Override
	public Map<String, String> keyMatch() {
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("allUsers", "users");
//		return map;
		return null;
	}
	
	public static final Creator<PersonalContacts> CREATOR = new Creator<PersonalContacts>() {

		@Override
		public PersonalContacts[] newArray(int size) {
			return new PersonalContacts[size];
		}

		// 将Parcel对象反序列化为ParcelableDate
		@Override
		public PersonalContacts createFromParcel(Parcel source) {
			return new PersonalContacts(source);
		}
	};

}
