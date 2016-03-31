package com.zepan.caifuyun.constants;

public class Url {

	// http://www.mifengyun.com/
	public static final String UserRootUrl = "http://120.26.209.49/";
	// 获取注册验证码 GET
	public static final String GetCaptcha = UserRootUrl + "reg/get-captcha";
	// 检测验证码 GET
	public static final String CheckCaptcha = UserRootUrl + "reg/check-captcha";
	// 用户注册接口 POST reg/register-user
	public static final String RegisterUser = UserRootUrl + "reg/register-user";
	// 用户登录接口 POST
	public static final String Login = UserRootUrl + "reg/login";
	// 公司创建接口 POST
	public static final String CreateTenant = UserRootUrl
			+ "tenant/create-tenant";
	// 搜索公司接口 GET
	public static final String SearchTenant = UserRootUrl
			+ "tenant/search-tenant";
//	// 加入公司接口 GET
//	public static final String JoinTenant = UserRootUrl + "tenant/join-tenant";
	// 申请加入公司接口 POST
	public static final String ApplyJoinTenant = UserRootUrl
			+ "user/apply-join-tenant";

	// 认证公司信息接口 申请认证公司接口 POST
	public static final String ApplycertificateTenant = UserRootUrl
			+ "tenant/apply-certificate-tenant";
	// 获得阿里云token接口 GET
	public static final String GetFederationToken = "http://120.26.67.31/oss/get-federationtoken";
	// 阿里云图片地址
	public static final String PictureAddress = "http://canaanwealth.oss-cn-hangzhou.aliyuncs.com/";
	// 创建个人线索接口 POST
	public static final String CreatePrivateLead = UserRootUrl
			+ "lead/create-private-lead";

	// 线索更新接口 POST
	public static final String LeadUpdate = UserRootUrl + "lead/update";

	// 获取线索接口 get
	public static final String LeadGetUserLeads = UserRootUrl
			+ "lead/get-user-leads";

	// 线索丢弃接口 get
	public static final String LeadRelease = UserRootUrl + "lead/release";
	// 获取升级成客户接口 get
	public static final String LeadConvert = UserRootUrl + "lead/convert";
	// 线索状态更新接口 get http://192.168.1.17:5000/
	public static final String LeadUpdateStatus = UserRootUrl
			+ "lead/update-status";
	// public static final String
	// LeadUpdateStatus="http://192.168.1.17:5000/lead/update-status";

	// 删除个人线索（删除公海池线索）接口 GET
	public static final String LeadDrop = UserRootUrl + "lead/drop";

	// 5.线索报错接口 GET
	public static final String LeadMarkError = UserRootUrl + "lead/mark-error";

	// 5.获取线索详情 GET
	public static final String LeadInfo = UserRootUrl + "lead/get-lead-info";

	// 创建渠道
	public static final String CreateChannels = UserRootUrl + "channel/create";
	// 获取有客渠道列表或者机构渠道列表 
	public static final String GetChannelList = UserRootUrl + "channel/my-channel-list";
	//获取渠道详情
	public static final String GetChannelInfo=UserRootUrl+"channel/info";
	
	// 获取客户列表或者机构列表 GET
	public static final String GetAll = UserRootUrl + "account/get-all";

	// 获取客户详情或者机构详情 GET
	public static final String GetInfo = UserRootUrl + "account/get-info";
	//获取当前用户相关业务数据数量
	public static final String GetIndexCounts = UserRootUrl + "user/index-counts";

	// 设置客户等级 GET
	public static final String UpdateLevel = UserRootUrl
			+ "account/update-level";
 
	// 删除客户 GET
	public static final String Delete = UserRootUrl + "account/delete";

	// 修改客户 post
	public static final String Update = UserRootUrl + "account/update";

	// 创建客户 post
	public static final String Create = UserRootUrl + "account/create";

	// 创建客户跟进记录 post
	public static final String RecodeCreate = UserRootUrl
			+ "account/activityrecord/create";

	// 获得所有跟进记录 get
	public static final String RecodeGetAll = UserRootUrl
			+ "account/activityrecord/get-all";

	// 删除客户跟进记录
	public static final String RecodeDelete = UserRootUrl
			+ "account/activityrecord/delete";

	// 创建客户关联人
	public static final String ContactCreate = UserRootUrl
			+ "account/contact/create";

	// 更新客户关联人
	public static final String ContactUpdate = UserRootUrl
			+ "account/contact/update";

	// 删除客户关联人
	public static final String ContactDelete = UserRootUrl
			+ "account/contact/delete";

	// 获取客户关联人
	public static final String ContactGetAll = UserRootUrl
			+ "account/contact/get-all";

	// 客户最近使用标签
	public static final String GetRecentlyTags = UserRootUrl
			+ "account/tags/get-recently-tags";

	// 创建标签
	public static final String CreateTags = UserRootUrl
			+ "account/tags/create-tags";

	// 客户标签删除
	public static final String DeleteTags = UserRootUrl
			+ "account/tags/delete-tags";

	//public static final String ProductRootUrl = "http://119.254.101.12:8606/";
	// 获取宣传产品图片
	public static final String GetPromoteProductList = UserRootUrl
			+ "product/get-promote-product-list";
	// 获取推荐产品列表，当前取最新发布的产品
	public static final String GetHotProductList = UserRootUrl
			+ "product/get-hot-product-list";
	// 按产品分类和产品投向获取产品列表
	public static final String GetCategoryProductList = UserRootUrl
			+ "product/get-category-product-list";
	// 获取产品详情
	public static final String GetProductInfo = UserRootUrl
			+ "product/get-info";
	
	//---- 获取产品认购信息
	public static final String GetProductSubscription = UserRootUrl
				+ "product/get-product-subscription";
	//---- 获取客户交易记录
	public static final String GetTransactionRecords = UserRootUrl
					+ "financial/transaction-records";
	//---- 新增理财历史
	public static final String CreateFinancialHistory = UserRootUrl
						+ "financial/financial-history-create";
	// 新增地址
	public static final String GreateAddress = UserRootUrl
			+ "product/address-create";

	// 获得默认地址
	public static final String GetDefaultAddress = UserRootUrl
			+ "product/get-default-address";
	// 收件地址列表
	public static final String GetAddressList = UserRootUrl
			+ "product/address-list";
	// 修改地址
	public static final String ModifyAddress = UserRootUrl
			+ "product/address-update";
	// 设置默认地址
	public static final String SetDefaultAddress = UserRootUrl
			+ "product/address-default";
	// 删除地址
	public static final String DeleteAddress = UserRootUrl
			+ "product/address-delete";
	// 申领合同(post)
	public static final String CreateContractApplyRecord = UserRootUrl
			+ "product/contract-create";

	// 创建订单
	public static final String createOrder = UserRootUrl
			+ "product/create-order";
	// 查询订单列表(POST)
	public static final String MyOrderList = UserRootUrl + "my-order-list";

	// 创建报单(post)
	public static final String CreateReport = UserRootUrl + "report/create";
	
	// 签到(get)
	public static final String Sign = UserRootUrl + "user/sign";

	// 获取融云token
	public static final String GetRongIMToken = UserRootUrl
			+ "message/get-token";

	// 获取某租户下的通讯录
	public static final String GetGroupUsers = UserRootUrl
			+ "user/get-group-users";
	// 创建讨论组
	public static final String CreateGroup = UserRootUrl
			+ "message/group-create";
	// 加入讨论组
	public static final String JoinGroup = UserRootUrl + "message/group-join";
	// 退出讨论组
	public static final String QuitGroup = UserRootUrl + "message/group-quit";
	//讨论组成员列表
    public static final String GroupInfo = UserRootUrl + "message/group-info";
    //讨论组成员列表
    public static final String GroupUpdate = UserRootUrl + "message/group-update";
    
}
