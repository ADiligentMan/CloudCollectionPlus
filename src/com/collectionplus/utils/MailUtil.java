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
 * @Description: �����࣬���û������ʼ�
 * @author ����
 * @date 2018��4��4��
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
	* @Description: ���û����Ͱ�����֤����ʼ�
	* @param toMail
	* 			�ռ��˵������ַ		
	* @param content
	* 			�ʼ���������
	* @param subject
	* 			�ʼ�����
	* @param activeCode
	* 			�ʼ��е���֤��
	* @throws Exception  
	* @return void
	*/  	    
	public static void sendActiveMail(String toMail, String content, String subject, String activeCode)
			throws Exception {
	
		// �������ô����Ự����, ���ں��ʼ�����������
		Session session = Session.getDefaultInstance(props);
		session.setDebug(true); // ����Ϊdebugģʽ, ���Բ鿴��ϸ�ķ��� log
		//�õ������������ַ
		String fromMail = props.getProperty("fromMail");
		// ����һ���ʼ�
		MimeMessage message = createMimeMessage(session,fromMail , toMail, activeCode, content, subject);

		// ���� Session ��ȡ�ʼ��������
		Transport transport = session.getTransport();

		// ��������
		transport.connect(props.getProperty("fromMail"),props.getProperty("password"));

		// �����ʼ�, �������е��ռ���ַ, message.getAllRecipients() ��ȡ�������ڴ����ʼ�����ʱ��ӵ������ռ���, ������, ������
		transport.sendMessage(message, message.getAllRecipients());

		// �ر�����
		transport.close();
	}


	/**  
	* @Title: createMimeMessage  
	* @Description: ����һ��ֻ�����ı��ļ��ʼ�  
	* @param session
	* 			�ͷ����������ĻỰ
	* @param sendMail
	* 			����������
	* @param receiveMail
	* 			�ռ�������
	* @param mailActiveCode
	* 			��֤��
	* @return MimeMessage
	* @throws Exception  
	* @return MimeMessage
	*/  	    
	public static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail,
			String mailActiveCode,String content,String subject) throws Exception {
		// 1. ����һ���ʼ�
		MimeMessage message = new MimeMessage(session);

		// 2. From: ������
		message.setFrom(new InternetAddress(sendMail,"UTF-8"));

		// 3. To: �ռ��ˣ��������Ӷ���ռ��ˡ����͡����ͣ�
		message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail,"UTF-8"));

		// 4. Subject: �ʼ�����
		message.setSubject(subject, "UTF-8");
		
		// 5. Content: �ʼ����ģ�����ʹ��html��ǩ��
		message.setContent(content+" "+mailActiveCode+"��",
				"text/plain;charset=UTF-8");

		// 6. ���÷���ʱ��
		message.setSentDate(new Date());

		// 7. ��������
		message.saveChanges();

		return message;
	}

	public static void main(String[] args) {
		String content = "�𾴵��û�����л����ע�ᣡ�������ղ�+,������֤����";
		String subject = "��֤��";
		try {
			sendActiveMail("15301100@bjtu.edu.cn",content,subject,"1234");
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
}
