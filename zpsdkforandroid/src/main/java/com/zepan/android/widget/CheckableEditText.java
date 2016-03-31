/*
 * 文件名:CheckEditText.java
 * 包含类名列表:CheckEditText.java
 * 版本号:1.0
 * 创建日期:2014/12/1
 * Copyright (c) 2014 ZePan.Co.Ltd. All rights reserved.
 */
package com.zepan.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.zepan.android.sdk.R;
import com.zepan.android.widget.Checkable;


/**
 * 可检查内容的输入框，根据XML中的配置的输入类型和自定义的配置，应用不同检查规则。可用于以下应用场景：
 * <ol>
 * <li>非空/包含空格检查</li>
 * <li>邮箱格式检查</li>
 * <li>手机格式检查</li>
 * <li>纯数字检查</li>
 * <li>数字、字母和下划线检查</li>
 * <li>位数检查</li>
 * <li>自定义检查和自定义提示信息</li>
 * </ol>
 * 使用方法如下： <li>xml中引入自定义属性标签，如
 * <code>xmlns:app="http://schemas.android.com/apk/res/com.zepan.library.app"</code>
 * </li> <li>将此类以标签的形式插入xml中，与TextView类似</li> <li>
 * 如果做邮箱或手机号检查，请配置inputType="textEmailAddress"或inputType="phone"</li> <li>
 * 如果规定长度，请配置maxSize和minSize</li> <li>如果规定纯数字，请配置enableCharacter="number"</li>
 * <li>如果规定中英文或下划线，请配置enableCharacter="number_en_underline"</li>
 * 
 * @see Checkable
 * @author zhanglei <br/>
 * @date 2014-10-12
 */

public class CheckableEditText extends EditText implements Checkable {

	private static final String TAG = "CheckableEditText";
	/** 不包含空格正则表达式 */
	public static final String REG_NO_EMPTY = "\\S+";
	/** 邮箱正则表达式 */
	public static final String REG_EMAIL = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
	/** 手机正则表达式 */
	public static final String REG_PHONE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
	/** 纯数字正则表达式 */
	public static final String REG_NUMBER = "^[0-9]*$";
	/** 数字、字母和下划线正则表达式 */
	public static final String REG_NUMBER_EN_UNDERLINE = "^\\w+$";
	/** 用户自定义的检查规则 */
	private String mRegular = null;
	/** 输入最小位数 */
	private int mMinSize = 0;
	/** 输入最大位数 */
	private int mMaxSize = 0;
	/** 允许字符，如纯数字、英字母和下划线 */
	private int mEnableCharacter = 0;
	/** 当前输入框的名称 */
	private String mName = null;
	/**
	 * 字符类型：纯数字
	 * */
	public static final int TYPE_NUMBER = 1;
	/**
	 * 字符类型：英数字和下划线
	 * */
	public static final int TYPE_NUMBER_EN_UNDERLINE = 2;

	/**
	 * @param context
	 * @param attrs
	 */
	public CheckableEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 从资源文件attrs.xml中读取配置信息
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.CheckableEditText);
		// 取输入框名称
		mName = a.getString(R.styleable.CheckableEditText_name);
		if (mName == null) {
			mName = "";
		}
		// 取最大位数
		mMinSize = a.getInt(R.styleable.CheckableEditText_minSize, 0);
		// 取最小位数
		mMaxSize = a.getInt(R.styleable.CheckableEditText_maxSize, 0);
		// 取允许字符形式
		mEnableCharacter = a.getInt(
				R.styleable.CheckableEditText_enableCharacter, 0);
		a.recycle();
	}

	/**
	 * <ol>
	 * 根据{@link #getInputType()}的值选择正确的正则表达式。
	 * <li>如当前输入类型为“邮箱”，则启用邮件的输入检查规则；</li>
	 * <li>如当前输入类型为“手机”，则启用手机号的输入规则；</li>
	 * <li>如当前输入类型为“文字”，且XML中配置了minSize和maxSize（长度范围），则启动长度的输入检查规则；</li>
	 * <li>如当前输入类型为“文字”，且XML中配置了enableCharacter（允许字符），则启用字符输入检查规则</li>
	 * </ol>
	 * <hr>
	 * <strong>注意：</strong><br>
	 * <ol>
	 * <li>文本框一定会做非空验证，如果不需要非空验证，推荐直接使用{@link EditText}；</li>
	 * <li>在上面的列表中，1、2的验证不受其他属性（如minSize、maxSize和enableCharacter的影响）</li>
	 * </ol>
	 * 
	 * @return 检测成功返回true，失败返回false，并弹出提示信息。
	 * */
	@Override
	public boolean check() {
		// 获取输入框内容
		String text = getText().toString();
		// 如果输入为空，直接弹出错误信息，返回false
		if (text == null || text.trim().length() == 0) {
			errorProcess("请输入" + mName);
			return false;
		}
		// 如果输入包含空格，直接弹出错误信息，返回false
		if (!text.matches(REG_NO_EMPTY)) {
			errorProcess(mName + "不能包含空格");
			return false;
		}
		// 以下关于输入类型的判断请参考http://www.eoeandroid.com/thread-293754-1-1.html
		// 如果输入类型为“邮箱”
		if ((InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS) == getInputType()) {
			// 如果输入非邮箱格式，弹出错误信息，返回false
			if (!text.matches(REG_EMAIL)) {
				errorProcess("邮箱格式不正确");
				return false;
			} else {
				return true;
			}
		} else if (InputType.TYPE_CLASS_PHONE == getInputType()) {// 如果输入类型为“手机”
			// 如果输入非数字格式，弹出错误信息，返回false
			if (!text.matches(REG_PHONE)) {
				errorProcess("手机格式不正确");
				return false;
			} else {
				return true;
			}
		} else {// 如果输入为普通文本类型
				// 判断长度范围合法性，如果最小范围大于或等于最大范围，检查无效
			if (mMinSize != 0 || mMaxSize != 0) {
				if (mMinSize >= mMaxSize) {
					Log.w(TAG, "最大范围不大于最小范围。");
				} else {
					// 判断长度的合法性
					if (text.length() < mMinSize || text.length() > mMaxSize) {
						errorProcess(mName + "长度必须为" + mMinSize + "-"
								+ mMaxSize + "位");
						return false;
					}
				}
			}
			// 判断字符类型，如果是纯数字类型，启动纯数字检查规则
			if (TYPE_NUMBER == mEnableCharacter) {
				if (!text.matches(REG_NUMBER)) {
					errorProcess(mName + "只能为纯数字");
					return false;
				}
			} else if (TYPE_NUMBER_EN_UNDERLINE == mEnableCharacter) {// 英数字类型
				if (!text.matches(REG_NUMBER_EN_UNDERLINE)) {
					errorProcess(mName + "只能为英数字或下划线");
					return false;
				}
			}
		}
		// 如果用户自定义匹配
		if (mRegular != null && "".equals(mRegular.trim())) {
			return text.matches(mRegular);
		}
		return true;
	};

	/**
	 * 如果用户不在XML中配置，可自行指定匹配规则。
	 * 
	 * @param regularExpression
	 *            用户指定的正则表达式。
	 * */
	public void setRegular(String regularExpression) {
		mRegular = regularExpression;
	}

	/**
	 * 内容不匹配时的处理。
	 * 
	 * @see InfoToast
	 * @param message
	 *            错误内容
	 * */
	public void errorProcess(String message) {
//		Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
		Animation shake = AnimationUtils.loadAnimation(getContext(),
				R.anim.shake);
		this.startAnimation(shake);
	}
	
	
}
