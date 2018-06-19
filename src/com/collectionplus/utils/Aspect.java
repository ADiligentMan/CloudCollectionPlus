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
		
		//	去掉最后一个&
		String s = sb.substring(0,sb.length()-1);
		
		System.out.println(time + " " + "请求URL:" + s);
	}
	
	public static void afterReturning(Object result) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
		String time = sdf.format(new Date());
		ReturnModel rm = (ReturnModel)result;
		String output = time+" "+"返回结果："+rm.toString();
		
		System.out.println(output);
	}
	
	public boolean isJwtValid(String jwt) {
		  try {
		  // 解析JWT字符串中的数据，并进行最基础的验证
		  Claims claims = Jwts.parser()
		  .setSigningKey("")  //SECRET_KEY是加密算法对应的密钥，jjwt可以自动判断机密算法；
		  .parseClaimsJws(jwt)   //jwt是客户端生成的JWT字符串；
		  .getBody();
		  String vaule = claims.get("key", String.class);   //获取自定义字段key；

		  // 判断自定义字段是否正确
		  if ("vaule".equals(vaule)) {
		  return true;
		  } else {
		  return false;
		  }
		  }
		  //在解析JWT字符串时，如果密钥不正确，将会解析失败，抛出SignatureException异常，说明该JWT字符串是伪造的；
		  //在解析JWT字符串时，如果‘过期时间字段’已经早于当前时间，将会抛出ExpiredJwtException异常，说明本次请求已经失效；
		  catch (SignatureException|ExpiredJwtException e) {
		  return false;
		  }
		}

}
