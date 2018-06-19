package com.collectionplus.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import com.collectionplus.bean.ReturnModel;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

public class Aspect {

	public static void before(Object req) {
		HttpServletRequest futherReq = (HttpServletRequest) req;
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
		String time = sdf.format(new Date());
		Enumeration<String> namesOfParams = futherReq.getParameterNames();

		StringBuilder sb = new StringBuilder("");
		sb.append(futherReq.getRequestURI());
		if (namesOfParams != null) {
			sb.append('?');
			while (namesOfParams.hasMoreElements()) {
				String key = namesOfParams.nextElement();
				String value = futherReq.getParameter(key);
				sb.append(key+"="+value+"&");
			}
		}
		
		//	ȥ�����һ��&
		String s = sb.substring(0,sb.length()-1);
		
		System.out.println(time + " " + "����URL:" + s);
	}
	
	public static void afterReturning(Object result) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
		String time = sdf.format(new Date());
		ReturnModel rm = (ReturnModel)result;
		String output = time+" "+"���ؽ����"+rm.toString();
		
		System.out.println(output);
	}
	
	public boolean isJwtValid(String jwt) {
		  try {
		  // ����JWT�ַ����е����ݣ����������������֤
		  Claims claims = Jwts.parser()
		  .setSigningKey("")  //SECRET_KEY�Ǽ����㷨��Ӧ����Կ��jjwt�����Զ��жϻ����㷨��
		  .parseClaimsJws(jwt)   //jwt�ǿͻ������ɵ�JWT�ַ�����
		  .getBody();
		  String vaule = claims.get("key", String.class);   //��ȡ�Զ����ֶ�key��

		  // �ж��Զ����ֶ��Ƿ���ȷ
		  if ("vaule".equals(vaule)) {
		  return true;
		  } else {
		  return false;
		  }
		  }
		  //�ڽ���JWT�ַ���ʱ�������Կ����ȷ���������ʧ�ܣ��׳�SignatureException�쳣��˵����JWT�ַ�����α��ģ�
		  //�ڽ���JWT�ַ���ʱ�����������ʱ���ֶΡ��Ѿ����ڵ�ǰʱ�䣬�����׳�ExpiredJwtException�쳣��˵�����������Ѿ�ʧЧ��
		  catch (SignatureException|ExpiredJwtException e) {
		  return false;
		  }
		}

}
