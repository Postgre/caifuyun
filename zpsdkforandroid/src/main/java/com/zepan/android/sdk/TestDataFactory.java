/**
 * Copyright (c) 2014 ZePan.Co.Ltd. All rights reserved. 
 *
 */
package com.zepan.android.sdk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import android.annotation.SuppressLint;

/**
 * 测试数据工厂，使用时需配置文件路径
 * @author zhanglei
 */
@SuppressLint("NewApi")
public class TestDataFactory {
	static Properties props = new Properties();
 
    public static void load(BufferedReader reader) throws IOException {
        String line = null;
        String key = null;
        StringBuilder values = new StringBuilder(64);
 
        while ((line = reader.readLine()) != null) {
            String str = line.trim();
            if (str.startsWith("#")) {
                continue;
            }
            if (str.startsWith("[") && str.endsWith("]")) {
                if (key != null) { // save last key/value
                    props.put(key, values.toString());
                }
                key = str.substring(1, str.length() - 1).trim();
                values.setLength(0);
            } else {
                values.append(line).append("\n");
            }
        }
 
        if (key != null) { // save last key/value
            props.put(key, values.toString());
        }
    }
 
    public static String getValue(String key) {
        return props.getProperty(key);
    }
     
//    public static void main(String[] args) {
//        System.out.println(TestDataFactory.getValue("update_user_password"));
//    }
	
}
