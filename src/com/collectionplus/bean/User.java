package com.collectionplus.bean;

public class User {
	private String username;
	private String password;
	private String phone;
	private String email;
	private String gender;
	private String intruduce;
	private int age;
	private int sharenumber;
	private int likenumber;
	private int fannumber;
	private int sourcenumber;
	private int notenumber;
	
	
	public User() {
		
	}
	public User(String username, String password, String phone, String email, String gender, String intruduce, int age,
			int sharenumber, int likenumber, int fannumber, int sourcenumber, int notenumber) {
		super();
		this.username = username;
		this.password = password;
		this.phone = phone;
		this.email = email;
		this.gender = gender;
		this.intruduce = intruduce;
		this.age = age;
		this.sharenumber = sharenumber;
		this.likenumber = likenumber;
		this.fannumber = fannumber;
		this.sourcenumber = sourcenumber;
		this.notenumber = notenumber;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getIntruduce() {
		return intruduce;
	}
	public void setIntruduce(String intruduce) {
		this.intruduce = intruduce;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getSharenumber() {
		return sharenumber;
	}
	public void setSharenumber(int sharenumber) {
		this.sharenumber = sharenumber;
	}
	public int getLikenumber() {
		return likenumber;
	}
	public void setLikenumber(int likenumber) {
		this.likenumber = likenumber;
	}
	public int getFannumber() {
		return fannumber;
	}
	public void setFannumber(int fannumber) {
		this.fannumber = fannumber;
	}
	public int getSourcenumber() {
		return sourcenumber;
	}
	public void setSourcenumber(int sourcenumber) {
		this.sourcenumber = sourcenumber;
	}
	public int getNotenumber() {
		return notenumber;
	}
	public void setNotenumber(int notenumber) {
		this.notenumber = notenumber;
	}
	
	
}
