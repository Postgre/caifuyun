package com.zepan.caifuyun.entity;
import java.util.HashMap;
import java.util.Map;

import com.zepan.android.sdk.JsonTransferable;

import android.os.Parcel;
import android.os.Parcelable;



/**
 * 负债信息实体类
 * @author duanjie
 *
 */
public class WealthInfo implements JsonTransferable,Parcelable{
	
	private String source;//工资收入
	private String useableassets;//可用的资产
	private String cash;//现金
	




	public String getSource() {
		return source;
	}





	public void setSource(String source) {
		this.source = source;
	}





	public String getUseableassets() {
		return useableassets;
	}





	public void setUseableassets(String useableassets) {
		this.useableassets = useableassets;
	}





	public String getCash() {
		return cash;
	}





	public void setCash(String cash) {
		this.cash = cash;
	}





	public WealthInfo(){ 

	}





	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}



	@SuppressWarnings("unchecked")
	protected WealthInfo(Parcel in) {
		




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
	public static final Creator<WealthInfo> CREATOR = new Creator<WealthInfo>() {

		@Override
		public WealthInfo[] newArray(int size) {
			return new WealthInfo[size];
		}

		// 将Parcel对象反序列化为ParcelableDate
		@Override
		public WealthInfo createFromParcel(Parcel source) {
			return new WealthInfo(source);
		}
	};

}
