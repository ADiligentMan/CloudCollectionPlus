package com.collectionplus.handler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collectionplus.bean.RecomLink;
import com.collectionplus.bean.RecomUser;
import com.collectionplus.bean.ReturnModel;
import com.collectionplus.service.UserService;
import com.collectionplus.utils.Aspect;

@Controller
@RequestMapping("/user")
public class UserController {
	UserService service = new UserService();
	
	//发送邮箱
	@RequestMapping(value="/sendemail.do",method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public ReturnModel sendEmail(HttpServletRequest req) {
		Aspect.before(req);
		ReturnModel rm =  service.sendEmail(req);	
		Aspect.afterReturning(rm);
		
		return rm;
	}
	
	//注册
	@RequestMapping(value="/signup.do",method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public ReturnModel signup(HttpServletRequest req) {
		Aspect.before(req);
		ReturnModel rm =  service.validateSignup(req);
		Aspect.afterReturning(rm);
		return rm;
	}
	
	//登陆
	@RequestMapping(value="/signin.do",method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public ReturnModel signin(HttpServletRequest req) {
		Aspect.before(req);
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		ReturnModel rm =  service.validateSignin(username, password);
		Aspect.afterReturning(rm);
		return rm;
	}
	
	//得到收藏夹名称的列表
	@RequestMapping(value="/directory.do",method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public ReturnModel directory(HttpServletRequest req) {
		Aspect.before(req);
		ReturnModel rm =  service.getAllDirs(req);
		Aspect.afterReturning(rm);
		return rm;
	}
	
	//创建一个收藏夹夹。接收三个参数{用户名，收藏夹名，类型(请三或者非清单)}
	@RequestMapping(value="/createdir.do",method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public ReturnModel createDir(HttpServletRequest req) {
		Aspect.before(req);
		ReturnModel rm = service.createDir(req);
		Aspect.afterReturning(rm);
		return rm;
		
	}
	
	//重命名收藏夹。parems:username,olddirname,newdirname
	@RequestMapping(value="/renamedir.do",method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public ReturnModel renameDir(HttpServletRequest req) {
		Aspect.before(req);
		ReturnModel rm = service.renameDir(req);
		Aspect.afterReturning(rm);
		return rm;
	}
	
	//删除一个收藏夹。params:username,dirname
	@RequestMapping(value="/deletedir.do",method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public ReturnModel deleteDir(HttpServletRequest req) {
		Aspect.before(req);
		ReturnModel rm = service.deleteDir(req);
		Aspect.afterReturning(rm);
		return rm;
	}
		
	
	
	@RequestMapping("/modifyPassword.do")
	@ResponseBody
	public ReturnModel modifyPassword(HttpServletRequest req) {
		Aspect.before(req);
		ReturnModel rm = service.modifyPassword(req);
		Aspect.afterReturning(rm);
		return rm;
	}
	
	@RequestMapping("/checkEnsureCode.do")
	@ResponseBody
	public ReturnModel checkEnsureCode(HttpServletRequest req) {
		Aspect.before(req);
		ReturnModel rm = service.checkActiveCode(req);
		Aspect.afterReturning(rm);
		return rm;
	}

	
	//返回用户信息
	@RequestMapping(value="/getInfo.do",method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public ReturnModel getInfo(HttpServletRequest req) {
		Aspect.before(req);
		ReturnModel rm = service.selectUserByUsername(req);
		Aspect.afterReturning(rm);
		return rm;
		
	}
	//设置用户信息
	@RequestMapping(value="/modifyInfo.do",method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public ReturnModel modifyInfo(HttpServletRequest req) {
		Aspect.before(req);
		ReturnModel rm = service.modifyUserInfo(req);
		Aspect.afterReturning(rm);
		return rm;
	}
}
