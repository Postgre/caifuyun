package com.zepan.caifuyun.entity;
import java.util.HashMap;
import java.util.Map;

import com.zepan.android.sdk.JsonTransferable;

import android.os.Parcel;
import android.os.Parcelable;



/**
 * 家庭信息 实体类
 * @author long
 *
 */
public class FamilyInfo implements JsonTransferable,Parcelable{
	private String homeaddress;//家庭地址
	private boolean marriaged;//是否已婚
	private String familymember;//家庭成员
	
	
	
	public String getHomeaddress() {
		return homeaddress;
	}




	public void setHomeaddress(String homeaddress) {
		this.homeaddress = homeaddress;
	}




	public boolean isMarriaged() {
		return marriaged;
	}




	public void setMarriaged(boolean marriaged) {
		this.marriaged = marriaged;
	}




	public String getFamilymember() {
		return familymember;
	}




	public void setFamilymember(String familymember) {
		this.familymember = familymember;
	}




	public FamilyInfo(){ 

	}




	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}



	@SuppressWarnings("unchecked")
	protected FamilyInfo(Parcel in) {





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
	public static final Creator<FamilyInfo> CREATOR = new Creator<FamilyInfo>() {

		@Override
		public FamilyInfo[] newArray(int size) {
			return new FamilyInfo[size];
		}

		// 将Parcel对象反序列化为ParcelableDate
		@Override
		public FamilyInfo createFromParcel(Parcel source) {
			return new FamilyInfo(source);
		}
	};

}
