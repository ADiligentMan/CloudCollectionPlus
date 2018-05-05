package com.collectionplus.bean;

public class ReturnModel {
	@Override
	public String toString() {
		return "ReturnModel [data=" + data + ", Info=" + Info + ", success=" + success + "]";
	}
	private Object data; 
	private String Info ="";
	private boolean success;
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getInfo() {
		return Info;
	}
	public void setInfo(String info) {
		Info = info;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	
	
}
