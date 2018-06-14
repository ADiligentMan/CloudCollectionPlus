package com.collectionplus.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collectionplus.bean.ReturnModel;
import com.collectionplus.service.UserService;
import com.collectionplus.service.recCollectionService;
import com.collectionplus.utils.Aspect;

@Controller
@RequestMapping("/user")
public class recomController {
		recCollectionService service = new recCollectionService();
		
		//�Ƽ��û�
		@RequestMapping(value="/recom_user.do",method = {RequestMethod.GET,RequestMethod.POST})
		@ResponseBody
		public ReturnModel getRecomUser(HttpServletRequest req) {	
			Aspect.before(req);
			ReturnModel rm =  service.getRecomUsers(req);
			Aspect.afterReturning(rm);
			System.out.println("����Controller");
			return rm;
		}
		//�Ƽ��û�
		@RequestMapping(value="/recom_link.do",method = {RequestMethod.GET,RequestMethod.POST})
		@ResponseBody
		public ReturnModel getRecomLink(HttpServletRequest req) {
			Aspect.before(req);
			ReturnModel rm =  service.getRecomLinks(req);
			Aspect.afterReturning(rm);
			return rm;
		}
}
