package com.collectionplus.dao;

import java.util.List;

import com.collectionplus.bean.User;

public interface IUserDao {
	public void insertUser(User user);
	public void deleteByUsername(String string);
	public void updateUserInfo(User user);
	public void updateUserSharenumber(int shareNumber);
	public void updateUserLikenumber(int likeNumber);
	public void updateUserFannumber(int fanNumber);
	public void updateUserSourcenumber(int sourceNumber);
	public void updateUserNotenumber(int noteNumber);
	public List<User> selectAllUser();
	public List<User> selectUserByUsername(String username);
	public User selectUserByEmail(String mail);
}
