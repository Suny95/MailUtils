package com.suny.utils;


import java.io.File;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * 发送邮件工具类
 * 可以发送简单文本、带图片邮件、带附件邮件
 * 
 * @author Suny
 * @version 1.0
 */
public class MailUtils {
	
	private MailUtils() {}
	
	
	/**
	 * @param from 发送者邮箱
	 * @param toAddress 接收者邮箱
	 * @param Subject 标题
	 * @param text 内容,不需要内容: null
	 * @param filePath 文件路径
	 * @param fileName 文件名
	 * @throws Exception
	 */
	public static void sendFileEmail(String from, String toAddress,String Subject,String text,String filePath,String fileName) throws Exception {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		JavaMailSender mailSender = (JavaMailSender) ctx.getBean("mailSender");
		
		//创建邮件
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		
		//创建工具类
		/*
		 * 参数一: 邮件
		 * 参数二: 是否是复杂格式邮件
		 */
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
		
		//设置发送者
		helper.setFrom(from);
		
		//设置接收者
		helper.setTo(toAddress);
		
		//设置标题
		helper.setSubject(Subject);
		
		//设置内容
		helper.setText(text == null ? "" : text,true);
		
		//读取图片路径
		FileSystemResource fsr = new FileSystemResource(new File(filePath));
		
		//添加附件
		helper.addAttachment(fileName, fsr);
		
		//发送
		mailSender.send(mimeMessage);
	}
	
	
	/**
	 * 复杂类型邮件 可发送图片,需提供applicationContext.xml配置  发送邮箱的id为 mailSender  
	 * @param from 发送者邮箱
	 * @param toAddress 接收者邮箱
	 * @param Subject 标题
	 * @param filePath 图片路径
	 * @param text 可选内容,可写文本,也可以不写
	 * @throws Exception
	 */
	public static void sendImageEmail(String from, String toAddress,String Subject,String filePath) throws Exception {
		//发送带图片的邮件
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		JavaMailSender mailSender = (JavaMailSender) ctx.getBean("mailSender");
		
		//设置信息
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		
		//创建一个封装的帮助工具
		/*
		 * 参数一: 邮件
		 * 参数二: 当前发送的是否是复杂邮件, true表示是复杂邮件,可以带图片和附件
		 */
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		
		//发送者
		helper.setFrom(from);
		helper.setTo(toAddress);
		helper.setSubject(Subject);
		
		//设置内容,html格式  内容中带有图片   后面的true跟之前的意思一样,都代表复杂邮件
		helper.setText("<html><head><title>标题</title></head><body><img src=cid:img/></body></html>",true);
		
		//读取图片,放入text文本中
		FileSystemResource fsr = new FileSystemResource(new File(filePath));
		
		//这里的img与上方文本中的  img 一致
		helper.addInline("img", fsr);
		
		//发送
		mailSender.send(mimeMessage);
	}

	/**
	 * 发送简单的邮箱,纯文本
	 * @param toAddress 接收邮件的邮箱地址
	 * @param Subject 标题
	 * @param text 内容
	 * @throws Exception
	 */
	public static void sendSimpleEmail(String toAddress,String Subject,String text) throws Exception {
		//设置邮件发送相关属性
		Properties props = new Properties();
		
		//设置邮箱主机地址
		props.put("mail.smtp.host", "smtp.163.com");
		
		//设置验证是否打开
		props.put("mail.smtp.auth", "true");
		
		//获取与服务器的链接
		Session session = Session.getDefaultInstance(props);
		
		//创建邮件
		MimeMessage mimeMessage = new MimeMessage(session);
		
		//设置发送地址
		InternetAddress address = new InternetAddress("yzy_zhaoyang@163.com");
		//发送者
		mimeMessage.setFrom(address);

		//设置接受地址
		InternetAddress MyToAddress = new InternetAddress(toAddress);
		
		/**
		 * RecipientType.TO: 发送
		 * RecipientType.cc: 抄送
		 * RecipientType.bcc: 暗送
		 */
		mimeMessage.setRecipient(RecipientType.TO, MyToAddress);
		
		//设置邮件标题
		mimeMessage.setSubject(Subject);
		
		//设置邮件内容
		mimeMessage.setText(text == null ? "" : text);
		
		//发送
		Transport transport = session.getTransport("smtp");
		
		//获取链接
		transport.connect("yzy_zhaoyang@163.com","az5625418");
		
		//发送
		transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
		
		//关闭链接
		transport.close();
	}
}
