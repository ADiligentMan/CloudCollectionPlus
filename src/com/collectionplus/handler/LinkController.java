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
	
	
	
	//���������б��������û������ղؼ�����
	@RequestMapping(value="/collectionlist.do",method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public ReturnModel getLinks(HttpServletRequest req) {
		Aspect.before(req);
		ReturnModel rm =  service.getLinkByDirname(req);
		Aspect.afterReturning(rm);
		return rm;
	}
	
	
	//��ĳЩ�ղؼв���һ�����ӡ� ������username ,dirname and so on;
	@RequestMapping(value="/saveLink.do",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ReturnModel saveLinks(HttpServletRequest req) {
		Aspect.before(req);
		ReturnModel rm =  service.saveLink(req);
		Aspect.afterReturning(rm);
		return rm;
	}
}
