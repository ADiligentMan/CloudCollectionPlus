package com.collectionplus.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collectionplus.bean.ReturnModel;
import com.collectionplus.service.LinkService;
import com.collectionplus.utils.Aspect;

@Controller
@RequestMapping("/user")
public class LinkController {
	LinkService service = new LinkService();
	
	
	
	//返回链接列表。参数：用户名，收藏夹名称
	@RequestMapping(value="/collectionlist.do",method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public ReturnModel getLinks(HttpServletRequest req) {
		Aspect.before(req);
		ReturnModel rm =  service.getLinkByDirname(req);
		Aspect.afterReturning(rm);
		return rm;
	}
	
	
	//向某些收藏夹插入一个链接。 参数：username ,dirname and so on;
	@RequestMapping(value="/saveLink.do",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ReturnModel saveLinks(HttpServletRequest req) {
		Aspect.before(req);
		ReturnModel rm =  service.saveLink(req);
		Aspect.afterReturning(rm);
		return rm;
	}
}
