package com.zepan.caifuyun.entity;

import java.util.List;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.zepan.android.sdk.JsonTransferable;

public class Channel implements JsonTransferable,Parcelable {
	
	private String post;//职位
	private int id;
   // private int isCertification;//是否加V
    //private String transaction; //累计交易额
    private String avatar;
    private String email;
    private String name;
    private String contact;
    private String company;
    private String mobile;
    private String address;
    
    List<Tags> tags;
    
	public Channel(Parcel source) {
		// TODO Auto-generated constructor stub
	}
    public Channel(){}
	public List<Tags> getTags() {
		return tags;
	}

	public void setTags(List<Tags> tags) {
		this.tags = tags;
	}

	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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
		return null;
	}
	// 实例化静态内部对象CREATOR实现接口Parcelable.Creator
		public static final Creator<Channel> CREATOR = new Creator<Channel>() {

			@Override
			public Channel[] newArray(int size) {
				return new Channel[size];
			}

			// 将Parcel对象反序列化为ParcelableDate
			@Override
			public Channel createFromParcel(Parcel source) {
				return new Channel(source);
			}
		};

}
