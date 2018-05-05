package com.collectionplus.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @ClassName: MailUtil
 * @Description: 工具类，向用户发送邮件
 * @author 王鹏
 * @date 2018年4月4日
 * 
 */ 
public class MailUtil {
	
	private static Properties props;
	static {
	InputStream in = MailUtil.class.getClassLoader()    
                .getResourceAsStream("mail.properties");    
	props = new Properties();
	try {
		props.load(in);
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
	}

	
	 	    
	/**  
	* @Title: sendActiveMail  
	* @Description: 给用户发送包含验证码的邮件
	* @param toMail
	* 			收件人的邮箱地址		
	* @param content
	* 			邮件正文内容
	* @param subject
	* 			邮件主题
	* @param activeCode
	* 			邮件中的验证码
	* @throws Exception  
	* @return void
	*/  	    
	public static void sendActiveMail(String toMail, String content, String subject, String activeCode)
			throws Exception {
	
		// 根据配置创建会话对象, 用于和邮件服务器交互
		Session session = Session.getDefaultInstance(props);
		session.setDebug(true); // 设置为debug模式, 可以查看详细的发送 log
		//得到发送人邮箱地址
		String fromMail = props.getProperty("fromMail");
		// 创建一封邮件
		MimeMessage message = createMimeMessage(session,fromMail , toMail, activeCode, content, subject);

		// 根据 Session 获取邮件传输对象
		Transport transport = session.getTransport();

		// 建立连接
		transport.connect(props.getProperty("fromMail"),props.getProperty("password"));

		// 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
		transport.sendMessage(message, message.getAllRecipients());

		// 关闭连接
		transport.close();
	}


	/**  
	* @Title: createMimeMessage  
	* @Description: 创建一封只包含文本的简单邮件  
	* @param session
	* 			和服务器交互的会话
	* @param sendMail
	* 			发件人邮箱
	* @param receiveMail
	* 			收件人邮箱
	* @param mailActiveCode
	* 			验证码
	* @return MimeMessage
	* @throws Exception  
	* @return MimeMessage
	*/  	    
	public static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail,
			String mailActiveCode,String content,String subject) throws Exception {
		// 1. 创建一封邮件
		MimeMessage message = new MimeMessage(session);

		// 2. From: 发件人
		message.setFrom(new InternetAddress(sendMail,"UTF-8"));

		// 3. To: 收件人（可以增加多个收件人、抄送、密送）
		message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail,"UTF-8"));

		// 4. Subject: 邮件主题
		message.setSubject(subject, "UTF-8");
		
		// 5. Content: 邮件正文（可以使用html标签）
		message.setContent(content+" "+mailActiveCode+"。",
				"text/plain;charset=UTF-8");

		// 6. 设置发件时间
		message.setSentDate(new Date());

		// 7. 保存设置
		message.saveChanges();

		return message;
	}

	public static void main(String[] args) {
		String content = "尊敬的用户，感谢您的注册！我们是收藏+,您的验证码是";
		String subject = "验证码";
		try {
			sendActiveMail("15301100@bjtu.edu.cn",content,subject,"1234");
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
}
