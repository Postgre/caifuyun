package com.zepan.caifuyun.entity;

import java.util.HashMap;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.zepan.android.sdk.JsonTransferable;


public class Product implements JsonTransferable,Parcelable {

	private int id;               // 产品id
	private String productName;                  // 名称
	private String shortName;                    // 简称
	private int benefitType;                      // 收益类型 1、固定收益  2、浮动收益
	private int productType;                      // 产品类型  1、信托     2､  资管     3、私募     4、所有
	private int investType;                      // 投向  1、房地产 2、征信 3、工商企业 4、PE/VC 5、新三板 6、打新 7、定增 8、二级市场－》股票型 9、其他

	private String financingParty;                // 资金使用方
	private String investmentManager;            // 投资管理人
	private String investmentAdviser;             // 投资顾问
	private String productDistribution;           // 产品发行方
	private String raiseBank;                     // 募集行
	private String raiseAccount;                  // 募集账号
	private String raiseAccountName;             // 募集账户名称
	private String trusteeBank;                 // 托管行
	private String entrustBank;                  // 委贷行
	
	//private String raiseTotalRemark;             // 募集规模备注
	private int investmentDeadline;               // 投资期限 单位 月
	private String investmentDeadlineRemark;    // 投资期限备注
	private String productPicture;               // 产品图片
	//private long createTime;                    // 产品创建日期
//	private String beginForecastTime;            // 预告开始日期
//	private String lastForecastTime;           // 预告结束日期
//	private String beginWarmupTime;               // 预热开始日期
	//private String lastWarmupTime;                // 预热结束日期
//	private String beginPayTime;                  // 打款开始日期
	private long lastPayTime;                  // 打款截止日期
//	private String distributionTime;             // 发行日期
	private String createUserId;                  // 创建产品用户号
	private int fundType;                       // 基金类别 1、股票型 2、债券型 3、期货基金 4、量化对冲 5、FOF 6、定向增发 7、打新基金 8、PE基金 9、指数基金

	private double rebateRatio;                  // 返佣比例
//	private int isPromote;                       // 是否是宣传产品
//	private int isVerify;                        // 是否审核通过

	private String contract;                      // 合同文件
	private String feature;                       // 产品亮点
	private String repaymentSource;               // 还款来源
	private String trustMeasure;                  // 增信措施
	private String tranchePoint;                  // 起购金额
	private String annualBenefitRate;            // 年化收益率
	private String distribution_plan;             // 发行方案
	private double netFund; 					//基金净值
    
	private String commissionPoint;             // 佣金起点
    private String commissionRatio;             //佣金比率
    private int  amountSum;   //已认购金额
    private int paySum;//已到款金额
    private long remainTime;  //剩余时间
    private double raiseTotal;   // 募集规模 单位 万
   
    private long openDate;//开放日
    private int theRaiseTotal; //本期募集总额
    private int warningLimit; //募集预警通知
    private int holdTotal;//成立规模
    private int largeAmount;//大单金额
    private int isCustomerUpload;//是否上传客户资料
    
    
    
	public int getInvestmentDeadline() {
		return investmentDeadline;
	}

	public void setInvestmentDeadline(int investmentDeadline) {
		this.investmentDeadline = investmentDeadline;
	}

	public long getOpenDate() {
		return openDate;
	}

	public void setOpenDate(long openDate) {
		this.openDate = openDate;
	}

	public int getTheRaiseTotal() {
		return theRaiseTotal;
	}

	public void setTheRaiseTotal(int theRaiseTotal) {
		this.theRaiseTotal = theRaiseTotal;
	}

	

	public int getWarningLimit() {
		return warningLimit;
	}

	public void setWarningLimit(int warningLimit) {
		this.warningLimit = warningLimit;
	}

	public int getHoldTotal() {
		return holdTotal;
	}

	public void setHoldTotal(int holdTotal) {
		this.holdTotal = holdTotal;
	}

	public int getLargeAmount() {
		return largeAmount;
	}

	public void setLargeAmount(int largeAmount) {
		this.largeAmount = largeAmount;
	}

	

	public int getIsCustomerUpload() {
		return isCustomerUpload;
	}

	public void setIsCustomerUpload(int isCustomerUpload) {
		this.isCustomerUpload = isCustomerUpload;
	}

	public long getLastPayTime() {
		return lastPayTime;
	}

	public void setLastPayTime(long lastPayTime) {
		this.lastPayTime = lastPayTime;
	}

	public double getNetFund() {
		return netFund;
	}

	public void setNetFund(double netFund) {
		this.netFund = netFund;
	}

	public double getRebateRatio() {
		return rebateRatio;
	}

	public void setRebateRatio(double rebateRatio) {
		this.rebateRatio = rebateRatio;
	}

	
	public int getAmountSum() {
		return amountSum;
	}


	public void setAmountSum(int amountSum) {
		this.amountSum = amountSum;
	}


	public int getPaySum() {
		return paySum;
	}


	public void setPaySum(int paySum) {
		this.paySum = paySum;
	}





	public long getRemainTime() {
		return remainTime;
	}


	public void setRemainTime(long remainTime) {
		this.remainTime = remainTime;
	}


	public double getRaiseTotal() {
		return raiseTotal;
	}


	public void setRaiseTotal(double raiseTotal) {
		this.raiseTotal = raiseTotal;
	}


	public String getCommissionPoint() {
		return commissionPoint;
	}


	public void setCommissionPoint(String commissionPoint) {
		this.commissionPoint = commissionPoint;
	}


	public String getCommissionRatio() {
		return commissionRatio;
	}


	public void setCommissionRatio(String commissionRatio) {
		this.commissionRatio = commissionRatio;
	}


	public int getId() {
		return id;
	}

	
	public void setId(int id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public int getBenefitType() {
		return benefitType;
	}

	public void setBenefitType(int benefitType) {
		this.benefitType = benefitType;
	}

	public int getProductType() {
		return productType;
	}

	public void setProductType(int productType) {
		this.productType = productType;
	}

	public int getInvestType() {
		return investType;
	}

	public void setInvestType(int investType) {
		this.investType = investType;
	}

	public String getFinancingParty() {
		return financingParty;
	}

	public void setFinancingParty(String financingParty) {
		this.financingParty = financingParty;
	}

	public String getInvestmentManager() {
		return investmentManager;
	}

	public void setInvestmentManager(String investmentManager) {
		this.investmentManager = investmentManager;
	}

	public String getInvestmentAdviser() {
		return investmentAdviser;
	}

	public void setInvestmentAdviser(String investmentAdviser) {
		this.investmentAdviser = investmentAdviser;
	}

	public String getProductDistribution() {
		return productDistribution;
	}

	public void setProductDistribution(String productDistribution) {
		this.productDistribution = productDistribution;
	}

	public String getRaiseBank() {
		return raiseBank;
	}

	public void setRaiseBank(String raiseBank) {
		this.raiseBank = raiseBank;
	}

	public String getRaiseAccount() {
		return raiseAccount;
	}

	public void setRaiseAccount(String raiseAccount) {
		this.raiseAccount = raiseAccount;
	}

	public String getRaiseAccountName() {
		return raiseAccountName;
	}

	public void setRaiseAccountName(String raiseAccountName) {
		this.raiseAccountName = raiseAccountName;
	}

	public String getTrusteeBank() {
		return trusteeBank;
	}

	public void setTrusteeBank(String trusteeBank) {
		this.trusteeBank = trusteeBank;
	}

	public String getEntrustBank() {
		return entrustBank;
	}

	public void setEntrustBank(String entrustBank) {
		this.entrustBank = entrustBank;
	}



//	public double getRaiseTotal() {
//		return raiseTotal;
//	}
//
//	public void setRaiseTotal(double raiseTotal) {
//		this.raiseTotal = raiseTotal;
//	}


//	public void setRaiseTotal(double raiseTotal) {
//		this.raiseTotal = raiseTotal;
//	}
//	
//
//	public String getRaiseTotalRemark() {
//		return raiseTotalRemark;
//	}


//	public void setRaiseTotalRemark(String raiseTotalRemark) {
//		this.raiseTotalRemark = raiseTotalRemark;
//	}

//	public int getInvestmentDeadline() {
//		return investmentDeadline;
//	}
//
//	public void setInvestmentDeadline(int investmentDeadline) {
//		this.investmentDeadline = investmentDeadline;
//	}

	public String getInvestmentDeadlineRemark() {
		return investmentDeadlineRemark;
	}

	public void setInvestmentDeadlineRemark(String investmentDeadlineRemark) {
		this.investmentDeadlineRemark = investmentDeadlineRemark;
	}

	public String getProductPicture() {
		return productPicture;
	}

	public void setProductPicture(String productPicture) {
		this.productPicture = productPicture;
	}

//	public long getCreateTime() {
//		return createTime;
//	}
//
//	public void setCreateTime(long createTime) {
//		this.createTime = createTime;
//	}

//	public String getBeginForecastTime() {
//		return beginForecastTime;
//	}
//
//	public void setBeginForecastTime(String beginForecastTime) {
//		this.beginForecastTime = beginForecastTime;
//	}
//
//	public String getLastForecastTime() {
//		return lastForecastTime;
//	}
//
//	public void setLastForecastTime(String lastForecastTime) {
//		this.lastForecastTime = lastForecastTime;
//	}
//
//	public String getBeginWarmupTime() {
//		return beginWarmupTime;
//	}
//
//	public void setBeginWarmupTime(String beginWarmupTime) {
//		this.beginWarmupTime = beginWarmupTime;
//	}

//	public String getLastWarmupTime() {
//		return lastWarmupTime;
//	}
//
//	public void setLastWarmupTime(String lastWarmupTime) {
//		this.lastWarmupTime = lastWarmupTime;
//	}

//	public String getBeginPayTime() {
//		return beginPayTime;
//	}
//
//	public void setBeginPayTime(String beginPayTime) {
//		this.beginPayTime = beginPayTime;
//	}
//
//	public String getLastPayTime() {
//		return lastPayTime;
//	}
//
//	public void setLastPayTime(String lastPayTime) {
//		this.lastPayTime = lastPayTime;
//	}
//
//	public String getDistributionTime() {
//		return distributionTime;
//	}
//
//	public void setDistributionTime(String distributionTime) {
//		this.distributionTime = distributionTime;
//	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public int getFundType() {
		return fundType;
	}

	public void setFundType(int fundType) {
		this.fundType = fundType;
	}



//	public boolean isPromote() {
//		return isPromote;
//	}
//
//	public void setPromote(boolean isPromote) {
//		this.isPromote = isPromote;
//	}
//
//	public boolean isVerify() {
//		return isVerify;
//	}
//
//	public void setVerify(boolean isVerify) {
//		this.isVerify = isVerify;
//	}

	public String getContract() {
		return contract;
	}

	public void setContract(String contract) {
		this.contract = contract;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public String getRepaymentSource() {
		return repaymentSource;
	}

	public void setRepaymentSource(String repaymentSource) {
		this.repaymentSource = repaymentSource;
	}

	public String getTrustMeasure() {
		return trustMeasure;
	}

	public void setTrustMeasure(String trustMeasure) {
		this.trustMeasure = trustMeasure;
	}

	public String getTranchePoint() {
		return tranchePoint;
	}

	public void setTranchePoint(String tranchePoint) {
		this.tranchePoint = tranchePoint;
	}

	public String getAnnualBenefitRate() {
		return annualBenefitRate;
	}

	public void setAnnualBenefitRate(String annualBenefitRate) {
		this.annualBenefitRate = annualBenefitRate;
	}

	public String getDistribution_plan() {
		return distribution_plan;
	}

	public void setDistribution_plan(String distribution_plan) {
		this.distribution_plan = distribution_plan;
	}

	public static Creator<Product> getCreator() {
		return CREATOR;
	}

	public Product(String productName,String productId){
		this.productName = productName;

	}

	public Product(){}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	protected Product(Parcel in) {
		this.id=in.readInt();
		this.productName=in.readString();
		this.investType=in.readInt();
		//this.investmentDeadline=in.readInt();
		this.tranchePoint=in.readString();
		//this.rebateRatio=in.readDouble();

	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeInt(id);
		dest.writeString(productName);
		dest.writeInt(investType);
		//dest.writeInt(investmentDeadline);
		dest.writeString(tranchePoint);
		//dest.writeDouble(rebateRatio);

	}

	@Override
	public Map<String, String> keyMatch() {
		Map<String, String> map=new HashMap<String, String>();

		return map;
	}
	// 实例化静态内部对象CREATOR实现接口Parcelable.Creator
	public static final Creator<Product> CREATOR = new Creator<Product>() {

		@Override
		public Product[] newArray(int size) {
			return new Product[size];
		}

		// 将Parcel对象反序列化为ParcelableDate
		@Override
		public Product createFromParcel(Parcel source) {
			return new Product(source);
		}
	};


}
