package com.collectionplus.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import com.collectionplus.bean.ReturnModel;

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
		
		System.out.println(time + " " + "Request:" + s);
	}
	
	public static void afterReturning(Object result) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
		String time = sdf.format(new Date());
		ReturnModel rm = (ReturnModel)result;
		String output = time+" "+"返回结果："+rm.toString();
		
		System.out.println(output);
	}

}
