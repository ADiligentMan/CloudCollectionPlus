package com.collectionplus.dao;

import com.collectionplus.bean.ActiveCode;

/**  
* @ClassName: IActiveCode  
* @Description: IActiveCode CRUD(create retrieve update delete)
* @author acer  
* @date 2018��4��10��
*/
public interface IActiveCodeDao {
	public void insertActiveCode(ActiveCode ac);
	public void updateByEmail(String email,String activecode,String time);
	public ActiveCode selectByEmail(String email);
}
