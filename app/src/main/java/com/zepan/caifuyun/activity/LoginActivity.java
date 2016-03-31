package com.zepan.caifuyun.activity;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.android.volley.Request.Method;
import com.zepan.android.sdk.IRequestCallBack;
import com.zepan.android.widget.AlphaTextView;
import com.zepan.android.widget.CheckableEditText;
import com.zepan.caifuyun.R;
import com.zepan.caifuyun.base.BaseActivity;
import com.zepan.caifuyun.cache.CompanyRespository;
import com.zepan.caifuyun.constants.Url;
import com.zepan.caifuyun.entity.User;
import com.zepan.caifuyun.util.StringUtil;

/***
 * 登录界面
 * 
 * @author long
 * 
 */
public class LoginActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setHeaderFields(0, R.string.login, 0, 0, 0, 0);
		initView();
	}

	private void initView() {
		((AlphaTextView) findViewById(R.id.tv_register)).setClickAlpha(100);
		if (getStringLocalData("phone") != null) {
			((CheckableEditText) findViewById(R.id.edtTx_name))
					.setText(getStringLocalData("phone"));
		}
		if (getStringLocalData("password") != null) {
			((CheckableEditText) findViewById(R.id.edtTx_pwd))
					.setText(getStringLocalData("password"));
		}
		findViewById(R.id.tv_register).setOnClickListener(this);
		findViewById(R.id.btn_login).setOnClickListener(this);

	}

	// Login
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_register:
			Intent registerIntent = new Intent(this, RegisterOneActivity.class);
			startActivity(registerIntent);
			break;
		case R.id.btn_login:
			getLogin();
			/*
			 * Intent loginIntent=new Intent(this, HomeActivity.class);
			 * startActivity(loginIntent);
			 */
			break;
		default:
			break;
		}

	}

	private void getLogin() {
		/****
		 * json={ account="", password="", }
		 */
		JSONObject paramJson = new JSONObject();
		try {
			paramJson.put("passport",
					((CheckableEditText) findViewById(R.id.edtTx_name))
							.getText().toString());// 账号
			paramJson.put("password",
					((CheckableEditText) findViewById(R.id.edtTx_pwd))
							.getText().toString());// 密码
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String url = Url.Login;
		request(url, paramJson, Method.POST, new IRequestCallBack() {

			@Override
			public void onResponse(JSONObject response) {
				resultLogin(response);
			}

			@Override
			public void onErrorResponse(String errorMessage) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void resultLogin(JSONObject response) {
		/****
		 * 1.2.4.5响应内容 { "status":0, "message":"error message", "token":"", }
		 * status int 0代表操作成功， 1代表登陆成功，但是使用了默认密码 2 账号不存在 3 账号存在，密码错误
		 * 
		 * message string 获取验证码错误原因,操作成功返回空字符串 token string
		 * 如果返回的状态码是0或者1表示登陆成功，生成token并返回
		 */
		try {
			if (response.getInt("status") == 0) {
				saveDataToLocal("token", response.getString("token"));
				saveDataToLocal("uid", response.getString("uid"));// 个人ID
				saveDataToLocal("icon", response.getString("icon"));// 头像地址
				saveDataToLocal("tid", response.getString("tid"));
				saveDataToLocal("name", response.getString("name"));// 昵称
				saveDataToLocal("phone", response.getString("passport"));// 手机号
				saveDataToLocal("password",
						((CheckableEditText) findViewById(R.id.edtTx_pwd))
								.getText().toString());// 密码
				Intent loginIntent = new Intent(LoginActivity.this,
						HomeActivity.class);
				startActivity(loginIntent);
				// 连接到融云服务器
				requestToken(response.getString("uid"),
						response.getString("name"), response.getString("icon"));
				// 获取租户通讯录
				requireEmployersInCompany(response.getString("tid"));
			} else if (response.getInt("status") == 1) {
				String message = response.getString("message");
				printToast(message);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// ======================================⬇️融云处理=================================
	/**
	 * 请求融云服务器，获取融云token
	 * 
	 * @param userId
	 *            用户号
	 * @param name
	 *            用户名
	 * @param portraitUri
	 *            用户头像
	 * */
	private void requestToken(String userId, String name, String portraitUri) {
		JSONObject paramJson = new JSONObject();
		try {
			String url = Url.GetRongIMToken + "?uid="
					+ URLEncoder.encode(userId, "UTF-8") + "&name="
					+ URLEncoder.encode(name, "UTF-8") + "&img="
					+ URLEncoder.encode(portraitUri, "UTF-8");
			request(url, paramJson, Method.GET, new IRequestCallBack() {
				@Override
				public void onResponse(JSONObject response) {
					try {
						JSONObject resultJson = response.getJSONObject("info");
						String token = resultJson.getString("token");
						httpGetTokenSuccess(token);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				@Override
				public void onErrorResponse(String errorMessage) {
					Log.i("=========", errorMessage);
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void httpGetTokenSuccess(String token) {

		/* IMKit SDK调用第二步 建立与服务器的连接 */
		/*
		 * 集成和测试阶段，您可以直接使用从您开发者后台获取到的 token，比如 String token =
		 * “d6bCQsXiupB......”;
		 */
		// RongIMClient.connect(token, new RongIMClient.ConnectCallback() {
		// @Override
		// public void onSuccess(String userId) {
		// /* 连接成功 */
		// Log.i(TAG, "融云服务器连接成功");
		// // 点击聊天记录跳转到聊天界面
		// RongIM.getInstance().startPrivateChat(LoginActivity.this, userId,
		// "聊天");
		// }
		// @Override
		// public void onError(RongIMClient.ErrorCode e) {
		// /* 连接失败，注意并不需要您做重连 */
		// Log.i(TAG, "融云服务器连接失败" + e.getMessage());
		// }
		// @Override
		// public void onTokenIncorrect() {
		// /*
		// * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的
		// * Token
		// */
		// Log.i(TAG, "token失效");
		// }
		// });

		RongIM.connect(token, new RongIMClient.ConnectCallback() {
			@Override
			public void onSuccess(String userId) {
				/* 连接成功 */
				Log.i(TAG, "融云服务器连接成功");
			}

			@Override
			public void onError(RongIMClient.ErrorCode e) {
				/* 连接失败，注意并不需要您做重连 */
				Log.i(TAG, "融云服务器连接失败" + e.getMessage());
			}

			@Override
			public void onTokenIncorrect() {
				/*
				 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的
				 * Token
				 */
				Log.i(TAG, "token失效");
			}
		});
	}

	// ======================================⬆️️融云处理=================================
	// ======================================⬇️️获取当前用户租户下的通讯录，将头像数据保存在本地，供融云使用=================================
	/**
	 * 获取某租户下的用户
	 * 
	 * @param companyId
	 *            租户号
	 * */
	private void requireEmployersInCompany(String companyId) {
		JSONObject paramJson = new JSONObject();
		try {
			paramJson.put("tid", companyId);
			request(Url.GetGroupUsers, paramJson, Method.GET,
					new IRequestCallBack() {
						@Override
						public void onResponse(JSONObject response) {
							try {
								if (response.getInt("status") == 0) {
									JSONArray jsonArray = response
											.getJSONArray("list");
									// 清空缓存的租户数据
									CompanyRespository.userMap.clear();
									for (int i = 0; i < jsonArray.length(); i++) {
										JSONObject itemJson = jsonArray.getJSONObject(i);
										// 获取某一区域内的用户列表
										JSONArray userJsonArray = itemJson
												.getJSONArray("users");
										for(int j = 0; j < userJsonArray.length(); j++){
											JSONObject userJson = userJsonArray.getJSONObject(j);
											// 暂存租户数据
											User user = new User();
											user.setUserId(userJson.getInt("id"));
											user.setUserName(userJson.getString("name"));
											user.setAvatar(userJson.getString("image"));
											CompanyRespository.userMap.put(userJson.getString("id"), user);
										}
									}
									// 为融云提供用户信息
									RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
									    @Override
									    public UserInfo getUserInfo(String userId) {
									    	User user = CompanyRespository.userMap.get(userId);
									    	UserInfo userInfo = null;
									    	if(user == null){
									    		// TODO
										    	userInfo = new UserInfo(userId, "火星用户", null);
									    	}else{
										    	userInfo = new UserInfo(user.getUserId() + "", user.getUserName(), Uri.parse(user.getAvatar()));
									    	}
									        return userInfo;//根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
									    }
									}, true);
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						@Override
						public void onErrorResponse(String errorMessage) {

						}
					});
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// String url = Url.GetGroupUsers + "?tid=" + tid;

	}

	
}
