package com.collectionplus.dao;

import java.util.List;
import java.util.Map;

import com.collectionplus.bean.Link;

public interface ILink {
	List<Link> selectLinkByDirname(String dirname, String username);
	String findDirID(String dirname,String username);//�����û������ղؼ������ҵ��ղؼ�ID��
	void insertLink(Map<String,String> map);
}
