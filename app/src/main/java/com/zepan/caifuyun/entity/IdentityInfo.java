package com.zepan.caifuyun.entity;
import java.util.HashMap;
import java.util.Map;

import com.zepan.android.sdk.JsonTransferable;

import android.os.Parcel;
import android.os.Parcelable;



/**
 * 身份信息 实体类
 * @author long
 *
 */
public class IdentityInfo implements JsonTransferable,Parcelable{
	private String birthday;//生日
	//private String sex;//性别
	private String nation;//民族
	private String residence;//住宅
	private String paperstype;//证件类型
	private String paperscode;//证件号
	private String education;//教育程度
	
	

	public String getBirthday() {
		return birthday;
	}




	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}




//	public String getSex() {
//		return sex;
//	}
//
//
//
//
//	public void setSex(String sex) {
//		this.sex = sex;
//	}




	public String getNation() {
		return nation;
	}




	public void setNation(String nation) {
		this.nation = nation;
	}




	public String getResidence() {
		return residence;
	}




	public void setResidence(String residence) {
		this.residence = residence;
	}




	public String getPaperstype() {
		return paperstype;
	}




	public void setPaperstype(String paperstype) {
		this.paperstype = paperstype;
	}




	public String getPaperscode() {
		return paperscode;
	}




	public void setPaperscode(String paperscode) {
		this.paperscode = paperscode;
	}




	public String getEducation() {
		return education;
	}




	public void setEducation(String education) {
		this.education = education;
	}




	public IdentityInfo(){ 

	}




	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}



	@SuppressWarnings("unchecked")
	protected IdentityInfo(Parcel in) {





	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {



	}

	@Override
	public Map<String, String> keyMatch() {
		Map<String, String> map=new HashMap<String, String>();
		return map;
	}
	// 实例化静态内部对象CREATOR实现接口Parcelable.Creator
	public static final Creator<IdentityInfo> CREATOR = new Creator<IdentityInfo>() {

		@Override
		public IdentityInfo[] newArray(int size) {
			return new IdentityInfo[size];
		}

		// 将Parcel对象反序列化为ParcelableDate
		@Override
		public IdentityInfo createFromParcel(Parcel source) {
			return new IdentityInfo(source);
		}
	};

}
