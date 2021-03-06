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
* @Description: 处理有关链接的请求
* @author 王鹏  
* @date 2018年4月18日    
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
	* @Description: 向用户返回某个收藏夹下的所有链接
	* @param req 包含用户名和用户收藏夹
	* @return ReturnModel
	* @throws  
	*/   
	public ReturnModel getLinkByDirname(HttpServletRequest req) {
		String dirname = req.getParameter("dirname");
		String username = req.getParameter("username");
		int page = Integer.parseInt(req.getParameter("page"));
		
		List<Link> list = null;
		//收藏夹名称等于all则会返回所有的所有的收藏
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
			rm.setInfo("该收藏夹暂时没有收藏哦~");
		}
		rm.setSuccess(true);
		return rm;
	}
	

	 
	/**  
	* @Title: saveLink  
	* @Description: 保存一个链接
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
			rm.setInfo("保存失败,找不到对应的收藏夹！");
			
			rm.setSuccess(false);
			return rm;
		}
		map2.put("dirID", dirID);
		dao.insertLink(map2);
		rm.setInfo("保存成功");
		rm.setSuccess(true);
		ss.commit();
		return rm;
	}
	
	/**
	 * 创建一个笔记
	 * 
	 * @param req
	 * @return
	 */
	public ReturnModel createNote(HttpServletRequest req) {
		ReturnModel rm = new ReturnModel();
		//构建参数列表
		Map<String,String[]> originMap = req.getParameterMap();
		Map<String,String>  targetMap = new HashMap();
		for(Map.Entry<String, String[]> entry:originMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue()[0];
			targetMap.put(key, value);
		}
		//创建笔记
		try {
			noteDao.createNote(targetMap);
		}catch(Exception e) {
			rm.setInfo("创建失败");
			rm.setSuccess(false);
			return rm;
		}
		
		rm.setInfo("创建成功");
		rm.setSuccess(true);
		ss.commit();
		return rm;
	}
	
	/**
	 * 获取一个链接的所有笔记
	 * @param req
	 * @return
	 */
	public ReturnModel getNotes(HttpServletRequest req) {
		ReturnModel rm = new ReturnModel();
		String linkID = req.getParameter("linkID");
		if(linkID==null) {
			rm.setSuccess(false);
			rm.setInfo("参数为空");
			return rm;
		}
		Integer int_linkID ;
		
		try {
			int_linkID = Integer.parseInt(linkID);
		}catch(Exception e){
			rm.setSuccess(false);
			rm.setInfo("参数有误");
			return rm;
		}
		
		List<Note> data = noteDao.findNotesByLinkId(int_linkID);
		rm.setSuccess(true);
		rm.setInfo("成功");
		rm.setData(data);
		return rm;
	}
	
	
	/**
	 * 删除一个笔记
	 * 
	 * @param req
	 * @return
	 */
	public ReturnModel deleteNotes(HttpServletRequest req) {
		ReturnModel rm = new ReturnModel();
		String ID = req.getParameter("ID");
		if(ID==null) {
			rm.setSuccess(false);
			rm.setInfo("参数为空");
			return rm;
		}
		
		Integer int_ID ;
		
		try {
			int_ID = Integer.parseInt(ID);
		}catch(Exception e){
			rm.setSuccess(false);
			rm.setInfo("参数有误");
			return rm;
		}
		noteDao.deleteNote(int_ID);
		ss.commit();
		rm.setSuccess(true);
		rm.setInfo("成功删除");
		return rm;
	}
	/**
	 * 修改一个笔记
	 * @param req
	 * @return
	 */
	public ReturnModel modifyNote(HttpServletRequest req) {
		ReturnModel rm = new ReturnModel();
		//构建参数列表
		Map<String,String[]> originMap = req.getParameterMap();
		Map<String,String>  targetMap = new HashMap();
		for(Map.Entry<String, String[]> entry:originMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue()[0];
			targetMap.put(key, value);
		}
		//创建笔记
		try {
			noteDao.modifyNote(targetMap);
		}catch(Exception e) {
			rm.setInfo("修改失败");
			rm.setSuccess(false);
			return rm;
		}
		
		rm.setInfo("修改成功");
		rm.setSuccess(true);
		ss.commit();
		return rm;
	}
	public ReturnModel getLinks(HttpServletRequest req) {
		String username = req.getParameter("username");
		
		List<Link> list = null;
		//收藏夹名称等于all则会返回所有的所有的收藏
		
		list= dao.getAllLink(username);
		ReturnModel rm = new ReturnModel();
		rm.setData(list);
		if(list==null) {
			rm.setInfo("该用户暂时没有收藏哦~");
		}
		rm.setSuccess(true);
		return rm;
	}
}
