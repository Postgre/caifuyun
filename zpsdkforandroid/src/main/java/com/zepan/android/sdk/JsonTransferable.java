/*
 * 文件名:JsonTransferable.java
 * 包含类名列表:JsonTransferable
 * 版本号:1.0
 * 创建日期:2014/12/9
 * Copyright (c) 2014 ZePan.Co.Ltd. All rights reserved.
 */
package com.zepan.android.sdk;

import java.util.Map;

/**
 * 如果对象需要从Json转化成具体的业务对象，需实现此接口
 * @author zhanglei <br/>
 * @date 2014/12/9 
 */
public interface JsonTransferable {
	
	/**
	 * 指定Json中的key与业务对象中的属性名的对应关系。
	 * 如果返回null，则属性名与Key相同。
	 * 
	 * @return Json Key与当前类的实现类的属性名的对应关系
	 * */
	Map<String, String> keyMatch();
}
