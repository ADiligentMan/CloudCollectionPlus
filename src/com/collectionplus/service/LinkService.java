package com.collectionplus.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;

import com.collectionplus.bean.Link;
import com.collectionplus.bean.ReturnModel;
import com.collectionplus.dao.ILink;
import com.collectionplus.dao.MyBatisUtils;

/**  
* @ClassName: LinkService  
* @Description: �����й����ӵ�����
* @author ����  
* @date 2018��4��18��    
*/
public class LinkService {
	private ILink dao;
	private SqlSession ss;
	
	public LinkService() {
		ss = MyBatisUtils.getSqlSession();
		dao = (ILink)ss.getMapper(ILink.class);
	}
	
	/**  
	* @Title: LinkService  
	* @Description: ���û�����ĳ���ղؼ��µ���������
	* @param req �����û������û��ղؼ�
	* @return ReturnModel
	* @throws  
	*/   
	public ReturnModel getLinkByDirname(HttpServletRequest req) {
		String dirname = req.getParameter("dirname");
		String username = req.getParameter("username");
		List<Link> list = dao.selectLinkByDirname(dirname, username);
		ReturnModel rm = new ReturnModel();
		rm.setData(list);
		if(list==null) {
			rm.setInfo("���ղؼ���ʱû���ղ�Ŷ~");
		}
		rm.setSuccess(true);
		return rm;
	}
	

	 
	/**  
	* @Title: saveLink  
	* @Description: ����һ������
	* @param req 
	* @return ReturnModel
	* @throws  
	*/   
	public ReturnModel saveLink(HttpServletRequest req) {
		try {
			req.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		ReturnModel rm = new ReturnModel();
		Map<String,String[]> map1 = req.getParameterMap();
		
		Map<String,String> map2 = new HashMap();
		for(Map.Entry<String, String[]> entry:map1.entrySet()) {
			String key = entry.getKey();
			String value = (entry.getValue())[0];
			map2.put(key, value);
		}
		String username = req.getParameter("username");
		String dirname = req.getParameter("dirname");
//		String picPath = req.getParameter("picPath");
//		String value = req.getParameter("value");
//		String read = req.getParameter("read");
//		String title = req.getParameter("title");
//		String source = req.getParameter("source");
//		String time = req.getParameter("time");
//		String  type = req.getParameter("type");
		
		String dirID =  dao.findDirID(dirname, username);
		if(dirID==null) {
			System.out.println("wangpeng:"+dirID);
			rm.setInfo("����ʧ��,�Ҳ�����Ӧ���ղؼУ�");
			
			rm.setSuccess(false);
			return rm;
		}
		map2.put("dirID", dirID);
		dao.insertLink(map2);
		rm.setInfo("����ɹ�");
		rm.setSuccess(true);
		ss.commit();
		return rm;
	}

}
