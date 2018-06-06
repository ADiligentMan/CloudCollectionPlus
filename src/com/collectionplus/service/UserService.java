package com.collectionplus.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.net.ssl.SSLSession;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.collectionplus.bean.ActiveCode;
import com.collectionplus.bean.Directory;
import com.collectionplus.bean.ReturnModel;
import com.collectionplus.bean.User;
import com.collectionplus.dao.IActiveCodeDao;
import com.collectionplus.dao.IDirectory;
import com.collectionplus.dao.IUserDao;
import com.collectionplus.dao.MyBatisUtils;
import com.collectionplus.utils.DateUtil;
import com.collectionplus.utils.MailUtil;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession; 
/**
 * @ClassName: UserService
 * @Description: 处理与用户相关的业务逻辑
 * @author 王鹏
 * @date 2018年4月3日
 * 
 */

public class UserService {
	private  IUserDao userDao;
	private IActiveCodeDao activeDao;
	private IDirectory dirDao;
	private SqlSession ss;
	public UserService() {
		ss = MyBatisUtils.getSqlSession();
		this.userDao = (IUserDao)ss.getMapper(IUserDao.class);
		this.activeDao = (IActiveCodeDao)ss.getMapper(IActiveCodeDao.class);
		this.dirDao = (IDirectory)ss.getMapper(IDirectory.class);
	}
	
	public void insertUser(User user) {
		userDao.insertUser(user);
		
	}

	public void updateUserInfo(User user) {
		userDao.updateUserInfo(user);
	}

	public void updateUserSharenumber(int shareNumber) {
		userDao.updateUserSharenumber(shareNumber);
	}

	public void updateUserLikenumber(int likeNumber) {
		userDao.updateUserLikenumber(likeNumber);
	}

	public void updateUserFannumber(int fanNumber) {
		userDao.updateUserFannumber(fanNumber);
	}

	public void updateUserSourcenumber(int sourceNumber) {
		userDao.updateUserSourcenumber(sourceNumber);
	}

	public void updateUserNotenumber(int noteNumber) {
		userDao.updateUserNotenumber(noteNumber);
	}

	public ReturnModel selectUserByUsername(HttpServletRequest req) {
		String username = req.getParameter("username");
		User user = userDao.selectUserByUsername(username);
		ReturnModel rm = new ReturnModel();
		if(user==null) {
			rm.setSuccess(false);
			rm.setInfo("用户不存在");
			return rm;
		}
		List<User> list = new ArrayList<>();
		list.add(user);
		rm.setData(list);
		rm.setSuccess(true);
		rm.setInfo("返回成功");
		return rm;
	}
		
	
	/**  
	* @Title: generateCode  
	* @Description: 产生验证码  
	* @param n 验证码的位数
	* @return String 验证码
	*/      
	private String generateCode(int n) {
		Random random = new Random();  
		String result="";  
		for (int i=0;i<n;i++)  
		{  
		    result+=random.nextInt(10);  
		}  		
		return result;
	}
	
	/**  
	* @Title: sendEmail  
	* @Description: 向用户发送邮箱
	* @param req 其中包含用户邮箱
	* @return ReturnModel
	*/    
	public ReturnModel sendEmail(HttpServletRequest req) {
		ReturnModel rm = new ReturnModel();
		
		String email = req.getParameter("email");
		String content = "尊敬的用户，感谢您的注册！我们是收藏+,您的验证码是";
		String subject = "验证码";
		
		//如果邮箱已被注册责不发送验证码
		User user;
		if((user=userDao.selectUserByEmail(email))!=null) {//邮箱已被注册
			rm.setSuccess(false);
			rm.setInfo("邮箱已被注册");
			return rm;
		}
		
		//验证码以及验证码产生时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(new Date());
		String activeCode = this.generateCode(6);
		
		
		//如果给同一个邮箱发两次验证码则更新前一个验证码。
		ActiveCode ac;
		if((ac=activeDao.selectByEmail(email))!=null) {//如果可以找到，则表示给同一个邮箱再一次发送了验证码
			new Thread(new Runnable() {
				@Override
				public void run() {
					activeDao.updateByEmail(email, activeCode,time);
					ss.commit();
				}}).start();
		//如果是一个没有接收到验证码的邮箱，则把验证码存储到数据库
		}else {
			new Thread(new Runnable() {
				@Override
				public void run() {
					activeDao.insertActiveCode(new ActiveCode(email,activeCode,time));
					ss.commit();
				}}).start();	
		}
		
		
		//把验证码发送到用户的邮箱
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					MailUtil.sendActiveMail(email, content, subject, activeCode);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}}
		).start();
		
		
		rm.setInfo("发送邮件成功.");
		rm.setSuccess(true);
		
		return rm;		
	}
	
	/**  
	* @Title: validateSignup  
	* @Description: 验证用户注册,前端发三个数据。
	* @param mail 用户邮箱
	* @param username 用户名
	* @param password 用户密码
	* @param activeCode 验证码
	* @return ReturnModel
	*/    
	public ReturnModel validateSignup(HttpServletRequest req) {
		ReturnModel result = new ReturnModel();
		result.setInfo("注册成功");
		result.setSuccess(true);
		
		String email = req.getParameter("email");
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String activeCode = req.getParameter("activeCode");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		//判断验证码是否正确和过期
		boolean isOverdue = true;
		boolean isCorrect = false;
		ActiveCode serac = activeDao.selectByEmail(email);	
		if(serac==null) {
			result.setInfo("您还未得到验证码，请先发送验证码");
			result.setSuccess(false);
			return result;
		}
		
		String sercode = serac.getActivecode();//验证码
		if(sercode.equals(activeCode)) {//验证码与后台存储的验证码相同。
			isCorrect = true;
		}
		if(isCorrect) {
			try {
				Date oldDate = sdf.parse(serac.getGenetime());
				Date newDate = new Date();
				isOverdue = DateUtil.TimeDifference(oldDate.getTime(), newDate.getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}
		
		//检验用户名
		User user ;
		if((user=userDao.selectUserByEmail(email))!=null) {//邮箱已被注册
			result.setSuccess(false);
			result.setInfo("邮箱已被注册");
		}else if((user=userDao.selectUserByUsername(username)) != null) {//用户名已存在
			result.setSuccess(false);
			result.setInfo("用户名已存在");
		}else if(!isCorrect){//验证码错误
			result.setSuccess(false);
			result.setInfo("验证码错误");
		}else if(isOverdue) {//验证码过期
			result.setSuccess(false);
			result.setInfo("验证码过期,请重新发送");
		}else {//用户注册成功
			Thread td = new Thread(new Runnable() {//将耗时的操作，放入另一个线程，是用户更快的得到响应
				@Override
				public void run() {
					User userTosave = new User();
					userTosave.setEmail(email);
					userTosave.setUsername(username);
					userTosave.setPassword(password);
					userDao.insertUser(userTosave);
					ss.commit();
				} },"td1") ;
			td.start();
		}
		
		return result;
	}

	
	/**
	 * @Title: validateSignin
	 * @Description: 登陆验证用户的用户名和密码
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return ReturnModel
	 */
	
	
	public ReturnModel validateSignin(String username, String password) {
		ReturnModel result = new ReturnModel();

		User user;
		
		// 用户名登陆
		if (!username.contains("@")) {
			user = userDao.selectUserByUsername(username);
			if (user == null) {
				result.setSuccess(false);
				result.setInfo("用户名错误");
			} else if (!user.getPassword().equals(password)) {
				result.setSuccess(false);
				result.setInfo("密码错误");
			} else {
				result.setSuccess(true);
				result.setInfo("登陆成功");
			}
			// 用邮箱登陆
		} else {
			user = userDao.selectUserByEmail(username);
			if (user == null) {
				result.setSuccess(false);
				result.setInfo("邮箱错误");
			} else if (user.equals(password)) {
				result.setSuccess(false);
				result.setInfo("密码错误");
			} else {
				result.setSuccess(true);
				result.setInfo("登陆成功");
				System.gc();
			}
		}
		
		ss.commit();
		return result;
	}
	
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}

	/**  
	* @Title: getAllDirs  
	* @Description: 返回用户收藏夹的名称列表
	* @param @param req
	* @param @return
	* @return ReturnModel
	* @throws  
	*/  
	 
	public ReturnModel getAllDirs(HttpServletRequest req){
		ReturnModel rm = new ReturnModel();
		String username = req.getParameter("username");
		List<Directory> list = dirDao.selectAllDirectoryByUsername(username);
		 rm.setData(list);
		 rm.setSuccess(true);
		 rm.setInfo("返回成功");
		 ss.commit();
		 return rm;
	}
	
	/**  
	* @Title: createDir  
	* @Description: 创建一个收藏夹
	* @param  req 包含用户名，收藏夹名称，收藏夹类型
	* @return void
	* @throws  
	*/  	 
	public ReturnModel createDir(HttpServletRequest req) {
		ReturnModel rm = new ReturnModel();
		String username = req.getParameter("username");
		String dirname = req.getParameter("dirname");
		String type = req.getParameter("type");
		if(username==null) {
			rm.setInfo("用户名为空");
			rm.setSuccess(false);
			return rm;
		}else if(dirname==null) {
			rm.setInfo("收藏夹名为空");
			rm.setSuccess(false);
			return rm;
		}else if(type==null) {
			rm.setInfo("没有类型");
			rm.setSuccess(false);
			return rm;
		}
		System.out.println("tag:"+username);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(new Date());
		Directory dir = new Directory(dirname,username,type,time);
		//如果捕捉到异常，说明已存在同名收藏夹
		try {
			dirDao.createDir(dir);
		}catch(Exception e) {	
			if(e instanceof PersistenceException) {
				rm.setInfo("已存在同名收藏夹");
				rm.setSuccess(false);
				return rm;
			}
		}
		rm.setInfo("创建成功");
		rm.setSuccess(true);
		ss.commit();
		return rm;
	}
	
	/**
	 * 修改用户信息
	* @Title: modifyUserInfo  
	* @Description: TODO
	* @param @param req
	* @param @return
	* @return ReturnModel
	* @throws
	 */
	public ReturnModel modifyUserInfo(HttpServletRequest req) {
		ReturnModel rm = new ReturnModel();
		String username = req.getParameter("username");
		String introduce = req.getParameter("introduce");
		String email = req.getParameter("email");
		String phone = req.getParameter("phone");
		String address = req.getParameter("address");
		String gender  = req.getParameter("gender");
		String age = req.getParameter("age");
		
		User user = new User();
		user.setUsername(username);
		user.setIntroduce(introduce);
		user.setEmail(email);
		user.setPhone(phone);
		user.setAddress(address);
		user.setGender(gender);
		user.setAge(Integer.valueOf(age));;
		
		userDao.updateUserInfo(user);
		ss.commit();
		rm.setInfo("保存成功");
		rm.setSuccess(true);
		return rm;
	}
	
	/**
	* 
	* @Title: renameDir  
	* @Description: 修改收藏夹名称
	* @param  req
	* @return ReturnModel
	* @throws
	 */
	public ReturnModel renameDir(HttpServletRequest req) {
		ReturnModel rm = new ReturnModel();
		String username = req.getParameter("username");
		String olddirname = req.getParameter("olddirname");
		String newdirname = req.getParameter("newdirname");
		if(username==null) {
			rm.setInfo("用户名为空");
			rm.setSuccess(false);
			return rm;
		}else if(olddirname==null) {
			rm.setInfo("收藏夹名为空");
			rm.setSuccess(false);
			return rm;
		}else if(newdirname==null) {
			rm.setInfo("收藏夹名为空");
			rm.setSuccess(false);
			return rm;
		}
		
		dirDao.renameDir(username, olddirname, newdirname);
		rm.setInfo("修改成功");
		rm.setSuccess(true);
		ss.commit();
		return rm;
	}
	
	/**
	* 
	* @Title: deleteDir  
	* @Description: 删除一个收藏夹
	* @param  req 
	* @return RerurnModel
	* @throws
	*/
	public ReturnModel deleteDir(HttpServletRequest req) {
		ReturnModel rm = new ReturnModel();
		String username = req.getParameter("username");
		String dirname = req.getParameter("dirname");
		if(username==null) {
			rm.setInfo("用户名为空");
			rm.setSuccess(false);
			return rm;
		}else if(dirname==null) {
			rm.setInfo("收藏夹名为空");
			rm.setSuccess(false);
			return rm;
		}
		rm.setSuccess(true);
		rm.setInfo("删除成功");
		dirDao.deleteDir(username, dirname);
		ss.commit();
		return rm;
	}

}
