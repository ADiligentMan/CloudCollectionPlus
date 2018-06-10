package com.collectionplus.dao;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MyBatisUtils {
	private static SqlSessionFactory sqlSessionFactory;
	private static SqlSession sqlSession;
	public static SqlSession getSqlSession() {
		try {
			InputStream is = Resources.getResourceAsStream("mybatis.xml");
			if (sqlSessionFactory == null) {
				sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
			}
			if(sqlSession==null) {
				sqlSession = sqlSessionFactory.openSession();
			}
			
			return sqlSession;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
