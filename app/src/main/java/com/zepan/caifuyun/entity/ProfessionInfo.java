package com.zepan.caifuyun.entity;
import java.util.HashMap;
import java.util.Map;

import com.zepan.android.sdk.JsonTransferable;

import android.os.Parcel;
import android.os.Parcelable;



/**
 * 职业信息实体类
 * @author long
 *
 */
public class ProfessionInfo implements JsonTransferable,Parcelable{
	private String professionstatus;//身份职位
	private String position;//职位
	private String company;//公司 
	private String companytype;//公司类型
	private String companyscale;//公司规模
	public String getProfessionstatus() {
		return professionstatus;
	}

	public void setProfessionstatus(String professionstatus) {
		this.professionstatus = professionstatus;
	}

	public String getCompanytype() {
		return companytype;
	}

	public void setCompanytype(String companytype) {
		this.companytype = companytype;
	}

	public String getCompanyscale() {
		return companyscale;
	}

	public void setCompanyscale(String companyscale) {
		this.companyscale = companyscale;
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

	
	public ProfessionInfo(){}
	
	
	
	
	
	
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}



	@SuppressWarnings("unchecked")
	protected ProfessionInfo(Parcel in) {
		





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
	public static final Creator<ProfessionInfo> CREATOR = new Creator<ProfessionInfo>() {

		@Override
		public ProfessionInfo[] newArray(int size) {
			return new ProfessionInfo[size];
		}

		// 将Parcel对象反序列化为ParcelableDate
		@Override
		public ProfessionInfo createFromParcel(Parcel source) {
			return new ProfessionInfo(source);
		}
	};

}
