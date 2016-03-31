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
public class Financeassets implements JsonTransferable,Parcelable{
	
	private String source;//工资收入
	private String useableassets;//可用的资产
	private String cash;//现金
	




	public Financeassets(){ 

	}





	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}



	@SuppressWarnings("unchecked")
	protected Financeassets(Parcel in) {
		




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
	public static final Creator<Financeassets> CREATOR = new Creator<Financeassets>() {

		@Override
		public Financeassets[] newArray(int size) {
			return new Financeassets[size];
		}

		// 将Parcel对象反序列化为ParcelableDate
		@Override
		public Financeassets createFromParcel(Parcel source) {
			return new Financeassets(source);
		}
	};

}
