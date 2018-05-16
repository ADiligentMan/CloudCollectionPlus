package com.collectionplus.bean;

public class Directory {
	private Integer ID;
	private String dirname;
	private String username;
	private String time;
	private String type;
	
	
	
	public Directory(Integer iD, String dirname, String username, String time, String type) {
		super();
		ID = iD;
		this.dirname = dirname;
		this.username = username;
		this.time = time;
		this.type = type;
	}
	
	public Directory(String dirname, String username, String time, String type) {
		super();
		this.dirname = dirname;
		this.username = username;
		this.time = time;
		this.type = type;
	}

	public Integer getID() {
		return ID;
	}
	public void setID(Integer iD) {
		ID = iD;
	}
	public String getDirname() {
		return dirname;
	}
	public void setDirname(String dirname) {
		this.dirname = dirname;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
