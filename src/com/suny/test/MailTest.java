package com.suny.test;

import com.suny.utils.MailUtils;

public class MailTest {
	public static void main(String[] args) {
		try {
//			MailUtils.sendSimpleEmail("327310547@qq.com", "标题", "1234567");
			
//			MailUtils.sendImageEmail("yzy_zhaoyang@163.com", "327310547@qq.com", "标题", "C:/Users/Suny/Desktop/1.jpg");
			
			MailUtils.sendFileEmail("yzy_zhaoyang@163.com", "327310547@qq.com", "标题",null, "C:/Users/Suny/Desktop/1.jpg", "123.jpg");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
