package com.collectionplus.bean;

public class Link {
	private String dirname;
    private String picPath;					//ͼƬ�ڿͻ��˵�·����
    private String value;			//���ӵ�URL��ַ
    private Boolean read;			//�����Ƿ��Ѷ�
	private String title;					//���ӵı���
    private String source;					//���ӵ���Դ��Ӧ�����ƣ�
    private String time;				//�ղ�����ʱ��
	private String type;					//�����ĸ����
	
	
	public Link(String dirname,String picPath, String value, Boolean read, String title, String source, String time, String type) {
		super();
		this.dirname = dirname;
		this.picPath = picPath;
		this.value = value;
		this.read = read;
		this.title = title;
		this.source = source;
		this.time = time;
		this.type = type;
	}
	
	public String getDirname() {
		return this.dirname;
	}
	
	public void setDirname(String dirname) {
		this.dirname = dirname;
	}
	
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public boolean isRead() {
		return read;
	}
	public void setRead(boolean read) {
		this.read = read;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
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
