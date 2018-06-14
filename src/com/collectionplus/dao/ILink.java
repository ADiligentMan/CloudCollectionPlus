package com.collectionplus.dao;

import java.util.List;
import java.util.Map;

import com.collectionplus.bean.Link;

public interface ILink {
	List<Link> selectLinkByDirname(String username, String dirnam,int offset);
	List<Link> selectAllLink(String username,int offset);
	String findDirID(String dirname,String username);//根据用户名和收藏夹名称找到收藏夹ID；
	void insertLink(Map<String,String> map);
	List<Link> getAllLink(String username);
	List<Link> getWholeLink();
}
