package com.collectionplus.bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**  
* @ClassName: ActiveCode  
* @Description: ��֤��ʵ��
* @author ����  
* @date 2018��4��10��    
*/
public class ActiveCode {
	private String email;
	private String activecode;
	private String genetime;
	
	
	public ActiveCode(String email, String activecode, String genetime) {
		super();
		this.email = email;
		this.activecode = activecode;
		this.genetime = genetime;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getActivecode() {
		return activecode;
	}
	public void setActivecode(String activecode) {
		this.activecode = activecode;
	}
	public String getGenetime() {
		return genetime;
	}
	public void setGenetime(String genetime) {
		this.genetime = genetime;
	}
	
	
}
