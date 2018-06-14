package com.collectionplus.service;

import com.collectionplus.bean.Link;
import com.collectionplus.bean.ReturnModel;
import com.collectionplus.bean.User;
import com.collectionplus.dao.ILink;
import com.collectionplus.dao.IUserDao;
import com.collectionplus.dao.MyBatisUtils;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class recCollectionService {
	IUserDao dao;
	ILink linkDao;

	public recCollectionService() {
		super();
		this.dao = MyBatisUtils.getSqlSession().getMapper(IUserDao.class);
		this.linkDao = MyBatisUtils.getSqlSession().getMapper(ILink.class);
	}

	public ReturnModel getRecomUsers(HttpServletRequest req) {
		ReturnModel rm = new ReturnModel();
		String username = req.getParameter("username");
		List<User> users = dao.selectAllUser();
		int size = users.size();
		List<Link> links;
		String[] all_item = new String[users.size()];
		for (int i = 0; i < users.size(); i++) {
			links = linkDao.getAllLink(users.get(i).getUsername());
			all_item[i] = users.get(i).getUsername();
			for (int j = 0; j < links.size(); j++) {
				all_item[i] += " " + links.get(j).getValue();
			}
		}
		recCollection rec = new recCollection();
		Map<String, Double> recommUsers = rec.recCollectionUsers(users.size(), all_item, username);
		if (recommUsers.size() == 0) {
			rm.setSuccess(false);
			rm.setInfo("推荐用户失败");
			return rm;
		}
		
		//生成目标data
		List<User> targetList = new ArrayList<>();
		for (String key : recommUsers.keySet()) {
			for (User user : users) {
				if (user.getUsername().equals(key)) {
					targetList.add(user);
				}
			}
		}

		rm.setSuccess(true);
		rm.setInfo("推荐用户成功");
		rm.setData(targetList);
		return rm;
	}

	
	
	public ReturnModel getRecomLinks(HttpServletRequest req) {
		ReturnModel rm = new ReturnModel();
		String username = req.getParameter("username");
		List<User> users = dao.selectAllUser();
		List<Link> linkList = linkDao.getWholeLink();
		List<Link> links;
		String[] all_item = new String[users.size()];
		for (int i = 0; i < users.size(); i++) {
			links = linkDao.getAllLink(users.get(i).getUsername());
			all_item[i] = users.get(i).getUsername();
			for (int j = 0; j < links.size(); j++) {
				all_item[i] += " " + links.get(j).getValue();
			}
		}
		
		recCollection rec = new recCollection();
		Map<String, Double> recommLinks = rec.recCollectionLinks(users.size(), all_item, username);
		if (recommLinks.size() == 0) {
			rm.setSuccess(false);
			rm.setInfo("推荐链接失败");
			return rm;
		}
		List<Link> targetList = new ArrayList<>();
		for (String key : recommLinks.keySet()) {
			for (Link link : linkList) {
				if (link.getValue().equals(key)) {
					targetList.add(link);
					break;
				}
			}
		}
		rm.setSuccess(true);
		rm.setInfo("推荐链接成功");
		rm.setData(targetList);

		return rm;
	}
}
