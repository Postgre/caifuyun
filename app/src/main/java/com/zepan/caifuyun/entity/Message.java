package com.zepan.caifuyun.entity;

import java.util.HashMap;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.zepan.android.sdk.JsonTransferable;

public class Message implements JsonTransferable,Parcelable{

	private String name;
	private String content;
	private String img_url;
	private String time;
	private String id;
    
	public Message(){}
	public Message(Parcel in) {
		// TODO Auto-generated constructor stub
		this.name=in.readString();
		this.content=in.readString();
		this.img_url=in.readString();
		this.time=in.readString();
		this.id=in.readString();
	}

	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}
	
	

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
		dest.writeString(name);
		dest.writeString(content);
		dest.writeString(img_url);
	}

	@Override
	public Map<String, String> keyMatch() {
		// TODO Auto-generated method stub
		Map<String, String> map=new HashMap<String, String>();
//		map.put("name", "name");
//		map.put("content", "content");
//		map.put("img_url", "img_url");
		return map;
	}

	// 实例化静态内部对象CREATOR实现接口Parcelable.Creator
		public static final Creator<Message> CREATOR = new Creator<Message>() {

			@Override
			public Message[] newArray(int size) {
				return new Message[size];
			}

			// 将Parcel对象反序列化为ParcelableDate
			@Override
			public Message createFromParcel(Parcel source) {
				return new Message(source);
			}
		};

}
