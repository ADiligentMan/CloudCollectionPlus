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
	* @Description: 删除一个收藏夹
	* @param  username 用户名
	* @param  dirname 收藏夹名称
	* @return void
	* @throws
	 */
	public void deleteDir(String username,String dirname);
	/**
	 * 
	* @Title: renameDir  
	* @Description: 重命名一个收藏夹
	* @param  username
	* @param  olddirname
	* @param  newdirname
	* @return void
	* @throws
	 */
	public void renameDir(String username,String olddirname,String newdirname);

}
