package com.collectionplus.bean;

public class Note {
	private Integer ID; 
	private Integer linkID;
	private String title;
	private String username;
	private String content;
	private String time;
	
	  
	  
	public Note(Integer iD, Integer linkID, String title, String username, String content, String time) {
		super();
		ID = iD;
		this.linkID = linkID;
		this.title = title;
		this.username = username;
		this.content = content;
		this.time = time;
	}

	
	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public Integer getLinkID() {
		return linkID;
	}

	public void setLinkID(Integer linkID) {
		this.linkID = linkID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
