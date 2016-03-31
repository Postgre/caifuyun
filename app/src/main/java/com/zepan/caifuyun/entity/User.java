package com.zepan.caifuyun.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.zepan.android.sdk.JsonTransferable;


public class User implements JsonTransferable,Parcelable {

	private int userId;//用户id
	private String userName;//用户名称
	private String avatar;//用户头像
	private String card;//名片
	private int maturity;//成熟度
	private int power;//实力
	private int riskAppetite;//风险偏好
	private String relationUser;
	//private List<E>  //联系信息
	private String email;//邮件 dbcVarchar3
	private IdentityInfo identityInformation;//身份信息 dbcTextarea2
	private ProfessionInfo professionalInformation;//职业信息 dbcTextarea3
	//private WealthInfo assetInformation;//资产信息 dbcTextarea4
	//private WealthInfo debtInformation;//负债信息 dbcTextarea5
	//private String familyInfomation;//家庭信息 dbcTextarea6
	//private String familyMemberInfomation;//家庭成员信息 dbcTextarea7
	private long accessedTime;//被访问时间 dbcDate1
	private int level;//客户级别 Level
	private long createdAt;
	private boolean canFollow;
	private int delFlg;
	private List<Tags> tags;
	
	private float amount;   //交易金额
	private String beginTime; //理财开始时间
	private String endTime;  //理财结束时间
	private float expectProfit;//预期收益
	private String productType;//产品类型
	private String productName;//产品名称
	
	private int accountCount;//客户数量
	private int channelCount;//渠道数量
	private int leadCount;//线索数量
	private int orderCount;//预约单数量
	
	
	public int getAccountCount() {
		return accountCount;
	}

	public void setAccountCount(int accountCount) {
		this.accountCount = accountCount;
	}

	public int getChannelCount() {
		return channelCount;
	}

	public void setChannelCount(int channelCount) {
		this.channelCount = channelCount;
	}

	public int getLeadCount() {
		return leadCount;
	}

	public void setLeadCount(int leadCount) {
		this.leadCount = leadCount;
	}

	public int getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public float getExpectProfit() {
		return expectProfit;
	}

	public void setExpectProfit(float expectProfit) {
		this.expectProfit = expectProfit;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}


	public int getMaturity() {
		return maturity;
	}

	public void setMaturity(int maturity) {
		this.maturity = maturity;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public int getRiskAppetite() {
		return riskAppetite;
	}

	public void setRiskAppetite(int riskAppetite) {
		this.riskAppetite = riskAppetite;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public IdentityInfo getIdentityInformation() {
		return identityInformation;
	}

	public void setIdentityInformation(IdentityInfo identityInformation) {
		this.identityInformation = identityInformation;
	}

	public ProfessionInfo getProfessionalInformation() {
		return professionalInformation;
	}

	public void setProfessionalInformation(ProfessionInfo professionalInformation) {
		this.professionalInformation = professionalInformation;
	}


//	public String getFamilyInfomation() {
//		return familyInfomation;
//	}
//
//	public void setFamilyInfomation(String familyInfomation) {
//		this.familyInfomation = familyInfomation;
//	}


	public long getAccessedTime() {
		return accessedTime;
	}

	public void setAccessedTime(long accessedTime) {
		this.accessedTime = accessedTime;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	public boolean isCanFollow() {
		return canFollow;
	}

	public void setCanFollow(boolean canFollow) {
		this.canFollow = canFollow;
	}

	public int getDelFlg() {
		return delFlg;
	}

	public void setDelFlg(int delFlg) {
		this.delFlg = delFlg;
	}

	//	private String  
	public User(){}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	protected  User(Parcel in) {
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
	}
	@Override
	public Map<String, String> keyMatch() {
		Map<String, String> map=new HashMap<String, String>();
		map.put("name", "userName");
		map.put("id", "userId");
		map.put("dbcVarchar1", "relationUser");//机构联系人
		map.put("dbcVarchar2", "card");//名片
		map.put("dbcVarchar5", "avatar");//头像
		map.put("dbcInteger1", "maturity");//成熟度
		map.put("dbcInteger2", "power");//实力
		map.put("dbcInteger3", "riskAppetite");//风险偏好
		map.put("dbcVarchar3", "email");//邮件 dbcVarchar3
//		map.put("dbcTextarea2", "identityInformation");//身份信息 dbcTextarea2
//		map.put("dbcTextarea3", "professionalInformation");//职业信息 dbcTextarea3
//		map.put("dbcTextarea4", "assetInformation");//资产信息 dbcTextarea4
		//map.put("dbcTextarea5", "debtInformation");//负债信息 dbcTextarea5
//		／／map.put("dbcTextarea6", "familyInfomation");//家庭信息 dbcTextarea6
		///map.put("dbcTextarea7", "familyMemberInfomation");//家庭成员信息 dbcTextarea7
		//map.put("dbcDate1", "accessedTime");//被访问时间 dbcDate1
		map.put("Level", "level");//客户级别 Level
		return map;
	}
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getRelationUser() {
		return relationUser;
	}

	public void setRelationUser(String relationUser) {
		this.relationUser = relationUser;
	}
	public List<Tags> getTags() {
		return tags;
	}

	public void setTags(List<Tags> tags) {
		this.tags = tags;
	}
	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}
	// 实例化静态内部对象CREATOR实现接口Parcelable.Creator
	public static final Creator<User> CREATOR = new Creator<User>() {

		@Override
		public User[] newArray(int size) {
			return new User[size];
		}

		// 将Parcel对象反序列化为ParcelableDate
		@Override
		public User createFromParcel(Parcel source) {
			return new User(source);
		}
	};



}
