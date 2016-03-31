package com.zepan.caifuyun.entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.zepan.android.sdk.JsonTransferable;



/**
 * 线索实体类
 * @author duanjie
 *
 */
public class Clues implements JsonTransferable,Parcelable{
	private int  id; 
	private String name;//姓名
	private String position;//职位
	private String company;//公司 
	private String telphone;//电话号码
	private String region;//地区
	private  List<Tags> tags; //标签
	private String source;//线索来源
	private  String  mobile;//手机
	private  String email;//邮件
	private  String comment;//备注
	private int status;//状态
	private  String dbcVarchar3;//头像
	private  String dbcVarchar1;//线索所在地区
	private int  highSeaStatus;
	private long createdAt;
	private int delFlg;



	public Clues(){ 

	}




	public int getHighSeaStatus() {
		return highSeaStatus;
	}




	public void setHighSeaStatus(int highSeaStatus) {
		this.highSeaStatus = highSeaStatus;
	}




	public int getId() {
		return id;
	}




	public void setId(int id) {
		this.id = id;
	}




	public String getDbcVarchar3() {
		return dbcVarchar3;
	}




	public void setDbcVarchar3(String dbcVarchar3) {
		this.dbcVarchar3 = dbcVarchar3;
	}




	public String getDbcVarchar1() {
		return dbcVarchar1;
	}




	public void setDbcVarchar1(String dbcVarchar1) {
		this.dbcVarchar1 = dbcVarchar1;
	}




	public List<Tags> getTags() {
		return tags;
	}




	public void setTags(List<Tags> tags) {
		this.tags = tags;
	}




	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}


	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
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

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}




	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}



	@SuppressWarnings("unchecked")
	protected Clues(Parcel in) {
		this.name=in.readString();
		this.position=in.readString();
		this.company=in.readString();
		this.telphone=in.readString();
		//		this.region=in.readString();
		this.tags=in.readArrayList(Tags.class.getClassLoader());
		//this.tags=in.readParcelable(Tags.class.getClassLoader());
		//in.readStringList(tags);
		this.source=in.readString();
		this.mobile=in.readString();
		this.email=in.readString();
		this.comment=in.readString();
		this.status=in.readInt();
		this.dbcVarchar3=in.readString();
		this.dbcVarchar1=in.readString();
		this.id=in.readInt();
		this.highSeaStatus=in.readInt();





	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(position);
		dest.writeString(company);
		dest.writeString(telphone);
		//		dest.writeString(region);
		dest.writeList(tags);  //这是list里面放实体类
		//dest.writeParcelable(tags, flags);  这是实体类  
		dest.writeString(source);
		dest.writeString(mobile);
		dest.writeString(email);
		dest.writeString(comment);
		dest.writeInt(status);
		dest.writeString(dbcVarchar3);
		dest.writeString(dbcVarchar1);
		dest.writeInt(id);
		dest.writeInt(highSeaStatus);



	}

	@Override
	public Map<String, String> keyMatch() {
		Map<String, String> map=new HashMap<String, String>();
		map.put("post", "position");
		map.put("companyName", "company");
		map.put("phone", "telphone");
		map.put("dbcVarchar2", "source");

		//		map.put("interest_pay_type", "region");
		//		map.put("interest_pay_type", "name");
		return map;
	}
	// 实例化静态内部对象CREATOR实现接口Parcelable.Creator
	public static final Creator<Clues> CREATOR = new Creator<Clues>() {

		@Override
		public Clues[] newArray(int size) {
			return new Clues[size];
		}

		// 将Parcel对象反序列化为ParcelableDate
		@Override
		public Clues createFromParcel(Parcel source) {
			return new Clues(source);
		}
	};

}
