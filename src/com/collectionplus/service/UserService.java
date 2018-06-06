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
 * @Description: �������û���ص�ҵ���߼�
 * @author ����
 * @date 2018��4��3��
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
			rm.setInfo("�û�������");
			return rm;
		}
		List<User> list = new ArrayList<>();
		list.add(user);
		rm.setData(list);
		rm.setSuccess(true);
		rm.setInfo("���سɹ�");
		return rm;
	}
		
	
	/**  
	* @Title: generateCode  
	* @Description: ������֤��  
	* @param n ��֤���λ��
	* @return String ��֤��
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
	* @Description: ���û���������
	* @param req ���а����û�����
	* @return ReturnModel
	*/    
	public ReturnModel sendEmail(HttpServletRequest req) {
		ReturnModel rm = new ReturnModel();
		
		String email = req.getParameter("email");
		String content = "�𾴵��û�����л����ע�ᣡ�������ղ�+,������֤����";
		String subject = "��֤��";
		
		//��������ѱ�ע���𲻷�����֤��
		User user;
		if((user=userDao.selectUserByEmail(email))!=null) {//�����ѱ�ע��
			rm.setSuccess(false);
			rm.setInfo("�����ѱ�ע��");
			return rm;
		}
		
		//��֤���Լ���֤�����ʱ��
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(new Date());
		String activeCode = this.generateCode(6);
		
		
		//�����ͬһ�����䷢������֤�������ǰһ����֤�롣
		ActiveCode ac;
		if((ac=activeDao.selectByEmail(email))!=null) {//��������ҵ������ʾ��ͬһ��������һ�η�������֤��
			new Thread(new Runnable() {
				@Override
				public void run() {
					activeDao.updateByEmail(email, activeCode,time);
					ss.commit();
				}}).start();
		//�����һ��û�н��յ���֤������䣬�����֤��洢�����ݿ�
		}else {
			new Thread(new Runnable() {
				@Override
				public void run() {
					activeDao.insertActiveCode(new ActiveCode(email,activeCode,time));
					ss.commit();
				}}).start();	
		}
		
		
		//����֤�뷢�͵��û�������
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
		
		
		rm.setInfo("�����ʼ��ɹ�.");
		rm.setSuccess(true);
		
		return rm;		
	}
	
	/**  
	* @Title: validateSignup  
	* @Description: ��֤�û�ע��,ǰ�˷��������ݡ�
	* @param mail �û�����
	* @param username �û���
	* @param password �û�����
	* @param activeCode ��֤��
	* @return ReturnModel
	*/    
	public ReturnModel validateSignup(HttpServletRequest req) {
		ReturnModel result = new ReturnModel();
		result.setInfo("ע��ɹ�");
		result.setSuccess(true);
		
		String email = req.getParameter("email");
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String activeCode = req.getParameter("activeCode");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		//�ж���֤���Ƿ���ȷ�͹���
		boolean isOverdue = true;
		boolean isCorrect = false;
		ActiveCode serac = activeDao.selectByEmail(email);	
		if(serac==null) {
			result.setInfo("����δ�õ���֤�룬���ȷ�����֤��");
			result.setSuccess(false);
			return result;
		}
		
		String sercode = serac.getActivecode();//��֤��
		if(sercode.equals(activeCode)) {//��֤�����̨�洢����֤����ͬ��
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
		
		//�����û���
		User user ;
		if((user=userDao.selectUserByEmail(email))!=null) {//�����ѱ�ע��
			result.setSuccess(false);
			result.setInfo("�����ѱ�ע��");
		}else if((user=userDao.selectUserByUsername(username)) != null) {//�û����Ѵ���
			result.setSuccess(false);
			result.setInfo("�û����Ѵ���");
		}else if(!isCorrect){//��֤�����
			result.setSuccess(false);
			result.setInfo("��֤�����");
		}else if(isOverdue) {//��֤�����
			result.setSuccess(false);
			result.setInfo("��֤�����,�����·���");
		}else {//�û�ע��ɹ�
			Thread td = new Thread(new Runnable() {//����ʱ�Ĳ�����������һ���̣߳����û�����ĵõ���Ӧ
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
	 * @Description: ��½��֤�û����û���������
	 * @param username
	 *            �û���
	 * @param password
	 *            ����
	 * @return ReturnModel
	 */
	
	
	public ReturnModel validateSignin(String username, String password) {
		ReturnModel result = new ReturnModel();

		User user;
		
		// �û�����½
		if (!username.contains("@")) {
			user = userDao.selectUserByUsername(username);
			if (user == null) {
				result.setSuccess(false);
				result.setInfo("�û�������");
			} else if (!user.getPassword().equals(password)) {
				result.setSuccess(false);
				result.setInfo("�������");
			} else {
				result.setSuccess(true);
				result.setInfo("��½�ɹ�");
			}
			// �������½
		} else {
			user = userDao.selectUserByEmail(username);
			if (user == null) {
				result.setSuccess(false);
				result.setInfo("�������");
			} else if (user.equals(password)) {
				result.setSuccess(false);
				result.setInfo("�������");
			} else {
				result.setSuccess(true);
				result.setInfo("��½�ɹ�");
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
	* @Description: �����û��ղؼе������б�
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
		 rm.setInfo("���سɹ�");
		 ss.commit();
		 return rm;
	}
	
	/**  
	* @Title: createDir  
	* @Description: ����һ���ղؼ�
	* @param  req �����û������ղؼ����ƣ��ղؼ�����
	* @return void
	* @throws  
	*/  	 
	public ReturnModel createDir(HttpServletRequest req) {
		ReturnModel rm = new ReturnModel();
		String username = req.getParameter("username");
		String dirname = req.getParameter("dirname");
		String type = req.getParameter("type");
		if(username==null) {
			rm.setInfo("�û���Ϊ��");
			rm.setSuccess(false);
			return rm;
		}else if(dirname==null) {
			rm.setInfo("�ղؼ���Ϊ��");
			rm.setSuccess(false);
			return rm;
		}else if(type==null) {
			rm.setInfo("û������");
			rm.setSuccess(false);
			return rm;
		}
		System.out.println("tag:"+username);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(new Date());
		Directory dir = new Directory(dirname,username,type,time);
		//�����׽���쳣��˵���Ѵ���ͬ���ղؼ�
		try {
			dirDao.createDir(dir);
		}catch(Exception e) {	
			if(e instanceof PersistenceException) {
				rm.setInfo("�Ѵ���ͬ���ղؼ�");
				rm.setSuccess(false);
				return rm;
			}
		}
		rm.setInfo("�����ɹ�");
		rm.setSuccess(true);
		ss.commit();
		return rm;
	}
	
	/**
	 * �޸��û���Ϣ
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
		rm.setInfo("����ɹ�");
		rm.setSuccess(true);
		return rm;
	}
	
	/**
	* 
	* @Title: renameDir  
	* @Description: �޸��ղؼ�����
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
			rm.setInfo("�û���Ϊ��");
			rm.setSuccess(false);
			return rm;
		}else if(olddirname==null) {
			rm.setInfo("�ղؼ���Ϊ��");
			rm.setSuccess(false);
			return rm;
		}else if(newdirname==null) {
			rm.setInfo("�ղؼ���Ϊ��");
			rm.setSuccess(false);
			return rm;
		}
		
		dirDao.renameDir(username, olddirname, newdirname);
		rm.setInfo("�޸ĳɹ�");
		rm.setSuccess(true);
		ss.commit();
		return rm;
	}
	
	/**
	* 
	* @Title: deleteDir  
	* @Description: ɾ��һ���ղؼ�
	* @param  req 
	* @return RerurnModel
	* @throws
	*/
	public ReturnModel deleteDir(HttpServletRequest req) {
		ReturnModel rm = new ReturnModel();
		String username = req.getParameter("username");
		String dirname = req.getParameter("dirname");
		if(username==null) {
			rm.setInfo("�û���Ϊ��");
			rm.setSuccess(false);
			return rm;
		}else if(dirname==null) {
			rm.setInfo("�ղؼ���Ϊ��");
			rm.setSuccess(false);
			return rm;
		}
		rm.setSuccess(true);
		rm.setInfo("ɾ���ɹ�");
		dirDao.deleteDir(username, dirname);
		ss.commit();
		return rm;
	}

}
