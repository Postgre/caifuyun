package com.zepan.caifuyun.application;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.StrictMode;

import com.alibaba.sdk.android.oss.OSSService;
import com.alibaba.sdk.android.oss.OSSServiceProvider;
import com.alibaba.sdk.android.oss.model.AccessControlList;
import com.alibaba.sdk.android.oss.model.AuthenticationType;
import com.alibaba.sdk.android.oss.model.ClientConfiguration;
import com.alibaba.sdk.android.oss.model.OSSFederationToken;
import com.alibaba.sdk.android.oss.model.StsTokenGetter;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.zepan.android.sdk.BaseApplication;
import com.zepan.caifuyun.constants.Url;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import io.rong.imkit.RongIM;


public class MyApplication extends BaseApplication {
	private static MyApplication mInstance =new MyApplication();
	public static OSSService ossService = OSSServiceProvider.getService();
	private List<Activity> mContextStack = new LinkedList<Activity>();


	@Override
	public void onCreate() {
		super.onCreate();
		Fresco.initialize(this);
		RongIM.init(this);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		// 初始化设置
		ossService.setApplicationContext(this.getApplicationContext());
		ossService.setGlobalDefaultHostId("oss-cn-hangzhou.aliyuncs.com"); // 设置region host 即 endpoint
		ossService.setGlobalDefaultACL(AccessControlList.PRIVATE); // 默认为private
		ossService.setAuthenticationType(AuthenticationType.FEDERATION_TOKEN); // 设置加签类型为原始AK/SK加签
		ossService.setGlobalDefaultStsTokenGetter(new StsTokenGetter() {
			// 在这里编写您的代码，实现获取一个新的STS Federation Token
			// 一般情况下，这个Token应该是通过网络请求去到您的业务服务器获取
			// 注意，返回的OSSFederationToken必须包含有效的四个字段：tempAK/tempSK/securityToken/expiration
			// expiration值为FederationToken的失效时间，格式为UNIX Epoch时间，即从协调世界时1970年1月1日0时0分0秒起到现在的总秒数
			@SuppressWarnings({ "deprecation", "unused" })
			@Override
			public OSSFederationToken getFederationToken() {
				OSSFederationToken token=null;
				try{
					SharedPreferences sp = getSharedPreferences("user",MODE_PRIVATE);
					String uriAPI =Url.GetFederationToken+"?name="+sp.getString("uid", "");  
					//建立HTTP get连线  
					HttpGet httpRequest =new HttpGet(uriAPI); 
					httpRequest.addHeader("connection", "Keep-Alive");
					httpRequest.addHeader("Content-Type", "application/json;charset=UTF-8");
					httpRequest.addHeader("Accept", "application/json");
					//发出HTTP request  
					//httpRequest.setEntity(new StringEntity(content,HTTP.UTF_8));  
					//取得HTTP response  
					HttpResponse httpResponse=new DefaultHttpClient().execute(httpRequest);  

					//若状态码为200 ok   
					if(httpResponse.getStatusLine().getStatusCode()==200){  
						//取出回应字串  
						//{"status": 0, "AccessKeySecret": "qu3N2hK58RbJZMVFnjxLirV6r9jWmF1IPTg7uq5M",
						//"SecurityToken": "CAESkgIIARKAATPUowtaHKfcYu3P/C8w1a64Y4GAIjwNmBEqVvSGBSJlQep4/tyGmmtmxkTuAUnAC5Ak7jfTEQ5QK/x1zSzl3nP8UvqrKRhIUy9GtQB1czfybaPRzUuFwm6lGDBZ+dYlzFAagZvL1nHowo+o++Ba0OPVDnP8H8OEtCP/kZ/Fp5INGhhTVFMuVlZDck9ud3NIVU9zWk5nUDI4bzUiEDE4NjQ2MzQwMDY1NDk1MTMqBGxvbmcwpYGW7OQpOgZSc2FNRDVCSgoBMRpFCgVBbGxvdxIbCgxBY3Rpb25FcXVhbHMSBkFjdGlvbhoDCgEqEh8KDlJlc291cmNlRXF1YWxzEghSZXNvdXJjZRoDCgEq",
						//"Expiration": "2015-07-02T06:47:51.717Z", "AccessKeyId": "STS.VVCrOnwsHUOsZNgP28o5"}
						String strResult=EntityUtils.toString(httpResponse.getEntity());
						String tempAK=null;
						String tempSK=null;
						String securityToken=null;
						long expiration=0;
						//Access Key ID和Access Key Secret，称为ID对，本文中简称为AK/SK
						JSONObject json=new JSONObject(strResult);
						if(json.has("AccessKeySecret")){
							tempSK=json.getString("AccessKeySecret");
						}
						if(json.has("AccessKeyId")){
							tempAK=json.getString("AccessKeyId");
						}
						if(json.has("SecurityToken")){
							securityToken=json.getString("SecurityToken");
						}
						if(json.has("Expiration")){
							//							String expir=json.getString("Expiration");
							//							Date epoch = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(expir);
							//							expiration=epoch.getTime();
						}
						token=new OSSFederationToken(tempAK, tempSK, securityToken, (System.currentTimeMillis()/1000+8*3600));


					}else{ 
						return null; 
					}  
				}catch(Exception e){
					e.printStackTrace();  
				}

				return token;
			}

		});
		ossService.setCustomStandardTimeWithEpochSec(System.currentTimeMillis() / 1000);
		ClientConfiguration conf = new ClientConfiguration();
		conf.setConnectTimeout(15 * 1000); // 设置全局网络连接超时时间，默认30s
		conf.setSocketTimeout(15 * 1000); // 设置全局socket超时时间，默认30s
		conf.setMaxConnections(50); // 设置全局最大并发网络链接数, 默认50
		ossService.setClientConfiguration(conf);


		// 百度地图在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		//SDKInitializer.initialize(this);


	}











	public static MyApplication getInstance() {

		return mInstance;
	}
	public boolean removeContext(Activity ctx){
		return mContextStack.remove(ctx);
	}
	public void pushActivity(Activity ctx){
		mContextStack.add(ctx);
	}
	public void finishAllContext(){
		for(Activity ctx : mContextStack){
			ctx.finish();
		}
	}
}
