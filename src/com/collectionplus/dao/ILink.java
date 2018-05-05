package com.collectionplus.dao;

import java.util.List;
import java.util.Map;

import com.collectionplus.bean.Link;

public interface ILink {
	List<Link> selectLinkByDirname(String dirname, String username);
	String findDirID(String dirname,String username);//根据用户名和收藏夹名称找到收藏夹ID；
	void insertLink(Map<String,String> map);
}
