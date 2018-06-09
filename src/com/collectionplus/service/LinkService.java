package com.collectionplus.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;

import com.collectionplus.bean.Link;
import com.collectionplus.bean.Note;
import com.collectionplus.bean.ReturnModel;
import com.collectionplus.dao.ILink;
import com.collectionplus.dao.INote;
import com.collectionplus.dao.MyBatisUtils;

/**  
* @ClassName: LinkService  
* @Description: �����й����ӵ�����
* @author ����  
* @date 2018��4��18��    
*/
public class LinkService {
	private ILink dao;
	private INote noteDao; 
	private SqlSession ss;
	
	public LinkService() {
		ss = MyBatisUtils.getSqlSession();
		dao = (ILink)ss.getMapper(ILink.class);
		noteDao = (INote)ss.getMapper(INote.class);
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
		int page = Integer.parseInt(req.getParameter("page"));
		
		List<Link> list = null;
		//�ղؼ����Ƶ���all��᷵�����е����е��ղ�
		if(dirname.equals("all")) {
			int offset = (page-1)*6;
			list = dao.selectAllLink(username, offset);
		}else {
			int offset =(page-1)*6;
		  list= dao.selectLinkByDirname(username, dirname,offset);
		}
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
	
	/**
	 * ����һ���ʼ�
	 * 
	 * @param req
	 * @return
	 */
	public ReturnModel createNote(HttpServletRequest req) {
		ReturnModel rm = new ReturnModel();
		//���������б�
		Map<String,String[]> originMap = req.getParameterMap();
		Map<String,String>  targetMap = new HashMap();
		for(Map.Entry<String, String[]> entry:originMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue()[0];
			targetMap.put(key, value);
		}
		//�����ʼ�
		try {
			noteDao.createNote(targetMap);
		}catch(Exception e) {
			rm.setInfo("����ʧ��");
			rm.setSuccess(false);
			return rm;
		}
		
		rm.setInfo("�����ɹ�");
		rm.setSuccess(true);
		ss.commit();
		return rm;
	}
	
	/**
	 * ��ȡһ�����ӵ����бʼ�
	 * @param req
	 * @return
	 */
	public ReturnModel getNotes(HttpServletRequest req) {
		ReturnModel rm = new ReturnModel();
		String linkID = req.getParameter("linkID");
		if(linkID==null) {
			rm.setSuccess(false);
			rm.setInfo("����Ϊ��");
			return rm;
		}
		Integer int_linkID ;
		
		try {
			int_linkID = Integer.parseInt(linkID);
		}catch(Exception e){
			rm.setSuccess(false);
			rm.setInfo("��������");
			return rm;
		}
		
		List<Note> data = noteDao.findNotesByLinkId(int_linkID);
		rm.setSuccess(true);
		rm.setInfo("�ɹ�");
		rm.setData(data);
		return rm;
	}
	
	
	/**
	 * ɾ��һ���ʼ�
	 * 
	 * @param req
	 * @return
	 */
	public ReturnModel deleteNotes(HttpServletRequest req) {
		ReturnModel rm = new ReturnModel();
		String ID = req.getParameter("ID");
		if(ID==null) {
			rm.setSuccess(false);
			rm.setInfo("����Ϊ��");
			return rm;
		}
		
		Integer int_ID ;
		
		try {
			int_ID = Integer.parseInt(ID);
		}catch(Exception e){
			rm.setSuccess(false);
			rm.setInfo("��������");
			return rm;
		}
		noteDao.deleteNote(int_ID);
		ss.commit();
		rm.setSuccess(true);
		rm.setInfo("�ɹ�ɾ��");
		return rm;
	}
	/**
	 * �޸�һ���ʼ�
	 * @param req
	 * @return
	 */
	public ReturnModel modifyNote(HttpServletRequest req) {
		ReturnModel rm = new ReturnModel();
		//���������б�
		Map<String,String[]> originMap = req.getParameterMap();
		Map<String,String>  targetMap = new HashMap();
		for(Map.Entry<String, String[]> entry:originMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue()[0];
			targetMap.put(key, value);
		}
		//�����ʼ�
		try {
			noteDao.modifyNote(targetMap);
		}catch(Exception e) {
			rm.setInfo("�޸�ʧ��");
			rm.setSuccess(false);
			return rm;
		}
		
		rm.setInfo("�޸ĳɹ�");
		rm.setSuccess(true);
		ss.commit();
		return rm;
	}
}
