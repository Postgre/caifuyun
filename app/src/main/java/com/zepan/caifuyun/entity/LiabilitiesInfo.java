package com.zepan.caifuyun.entity;
import java.util.HashMap;
import java.util.Map;

import com.zepan.android.sdk.JsonTransferable;

import android.os.Parcel;
import android.os.Parcelable;



/**
 * 实体资产
 * @author long
 *
 */
public class LiabilitiesInfo implements JsonTransferable,Parcelable{
	private int house;//房屋数量
	private int cause;//
    private int other;//其他
	public int getHouse() {
		return house;
	}





	public void setHouse(int house) {
		this.house = house;
	}





	public int getCause() {
		return cause;
	}





	public void setCause(int cause) {
		this.cause = cause;
	}





	public int getOther() {
		return other;
	}





	public void setOther(int other) {
		this.other = other;
	}





	public LiabilitiesInfo(){ 

	}




	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}



	@SuppressWarnings("unchecked")
	protected LiabilitiesInfo(Parcel in) {
		




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
	public static final Creator<LiabilitiesInfo> CREATOR = new Creator<LiabilitiesInfo>() {

		@Override
		public LiabilitiesInfo[] newArray(int size) {
			return new LiabilitiesInfo[size];
		}

		// 将Parcel对象反序列化为ParcelableDate
		@Override
		public LiabilitiesInfo createFromParcel(Parcel source) {
			return new LiabilitiesInfo(source);
		}
	};

}
