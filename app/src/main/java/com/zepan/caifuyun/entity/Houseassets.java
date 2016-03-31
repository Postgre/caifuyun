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
 * 资产
 * @author long
 *
 */
public class Houseassets implements JsonTransferable,Parcelable{
	
	private String selfUse;//可用资金
	private String invest;//投资
	private String overseas;//海外资产
	




	public String getSelfUse() {
		return selfUse;
	}





	public void setSelfUse(String selfUse) {
		this.selfUse = selfUse;
	}





	public String getInvest() {
		return invest;
	}





	public void setInvest(String invest) {
		this.invest = invest;
	}





	public String getOverseas() {
		return overseas;
	}





	public void setOverseas(String overseas) {
		this.overseas = overseas;
	}





	public Houseassets(){ 

	}





	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}



	@SuppressWarnings("unchecked")
	protected Houseassets(Parcel in) {
		




	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		

	}

	@Override
	public Map<String, String> keyMatch() {
		Map<String, String> map=new HashMap<String, String>();
		map.put("self-use", "selfUse");
		return map;
	}
	// 实例化静态内部对象CREATOR实现接口Parcelable.Creator
	public static final Creator<Houseassets> CREATOR = new Creator<Houseassets>() {

		@Override
		public Houseassets[] newArray(int size) {
			return new Houseassets[size];
		}

		// 将Parcel对象反序列化为ParcelableDate
		@Override
		public Houseassets createFromParcel(Parcel source) {
			return new Houseassets(source);
		}
	};

}
