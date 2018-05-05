package com.collectionplus.dao;

import java.util.List;

import com.collectionplus.bean.Directory;

public interface IDirectory {
	public List<Directory> selectAllDirectoryByUsername(String username);
	public List<String> selectAllDirname(String username);
	public void createDir(Directory dir);
}
