package com.zepan.caifuyun.entity;
import java.util.HashMap;
import java.util.Map;

import com.zepan.android.sdk.JsonTransferable;

import android.os.Parcel;
import android.os.Parcelable;



/**
 * 关联人实体类
 * @author duanjie
 *
 */
public class Contact implements JsonTransferable,Parcelable{
	private int  id; 
	private String comment;//备注
	private String mobile;//手机
	private String name;//姓名
	private String dbcVarchar1;//关联人 




	public int getId() {
		return id;
	}






	public void setId(int id) {
		this.id = id;
	}






	public String getComment() {
		return comment;
	}






	public void setComment(String comment) {
		this.comment = comment;
	}






	public String getMobile() {
		return mobile;
	}






	public void setMobile(String mobile) {
		this.mobile = mobile;
	}






	public String getName() {
		return name;
	}






	public void setName(String name) {
		this.name = name;
	}






	public String getDbcVarchar1() {
		return dbcVarchar1;
	}






	public void setDbcVarchar1(String dbcVarchar1) {
		this.dbcVarchar1 = dbcVarchar1;
	}






	public Contact(){ 

	}






	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}



	protected Contact(Parcel in) {
		this.id=in.readInt();
		this.name=in.readString();
		this.comment=in.readString();
		this.mobile=in.readString();
		this.dbcVarchar1=in.readString();



	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
       dest.writeInt(id);
       dest.writeString(name);
       dest.writeString(comment);
       dest.writeString(mobile);
       dest.writeString(dbcVarchar1);



	}

	@Override
	public Map<String, String> keyMatch() {
		Map<String, String> map=new HashMap<String, String>();

		return map;
	}
	// 实例化静态内部对象CREATOR实现接口Parcelable.Creator
	public static final Creator<Contact> CREATOR = new Creator<Contact>() {

		@Override
		public Contact[] newArray(int size) {
			return new Contact[size];
		}

		// 将Parcel对象反序列化为ParcelableDate
		@Override
		public Contact createFromParcel(Parcel source) {
			return new Contact(source);
		}
	};

}
