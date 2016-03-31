/*
 * 文件名:Checkable.java
 * 包含类名列表:Checkable.java
 * 版本号:1.0
 * 创建日期:2014/12/1
 * Copyright (c) 2014 ZePan.Co.Ltd. All rights reserved.
 */
package com.zepan.android.widget;

/**
 * 此接口供需要做输入检查的输入框实现，如非空检查、邮箱检查等。
 * 具体的检查规则在方法check中定义。
 * @author zhanglei <br/>
 * @date 2014/12/1 
 */
public interface Checkable {
	/**
	 * 判断当前文本框的输入是否合法。判断规则由实现类决定。
	 * @return 输入合法返回true,否则返回false。
	 * */
	boolean check();
	
}
