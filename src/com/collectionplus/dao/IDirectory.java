package com.collectionplus.dao;

import java.util.List;

import com.collectionplus.bean.Directory;

public interface IDirectory {
	public List<Directory> selectAllDirectoryByUsername(String username);
	public List<String> selectAllDirname(String username);
	public void createDir(Directory dir);
	
	/**
	 * 
	* @Title: deleteDir  
	* @Description: ɾ��һ���ղؼ�
	* @param  username �û���
	* @param  dirname �ղؼ�����
	* @return void
	* @throws
	 */
	public void deleteDir(String username,String dirname);
	/**
	 * 
	* @Title: renameDir  
	* @Description: ������һ���ղؼ�
	* @param  username
	* @param  olddirname
	* @param  newdirname
	* @return void
	* @throws
	 */
	public void renameDir(String username,String olddirname,String newdirname);

}
