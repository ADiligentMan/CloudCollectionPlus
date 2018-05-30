package com.collectionplus.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.apache.ibatis.session.SqlSession; 
/**
 * @ClassName: UserService
 * @Description: �������û���ص�ҵ���߼�
 * @author ����
 * @date 2018��4��3��
 * 
 */

public class UserService {
	private  IUserDao dao;
	private IActiveCodeDao dao2;
	private IDirectory dao3;
	private SqlSession ss;
	public UserService() {
		ss = MyBatisUtils.getSqlSession();
		this.dao = (IUserDao)ss.getMapper(IUserDao.class);
		this.dao2 = (IActiveCodeDao)ss.getMapper(IActiveCodeDao.class);
		this.dao3 = (IDirectory)ss.getMapper(IDirectory.class);
	}
	
	public void insertUser(User user) {
		dao.insertUser(user);
		
	}

	public void updateUserInfo(User user) {
		dao.updateUserInfo(user);
	}

	public void updateUserSharenumber(int shareNumber) {
		dao.updateUserSharenumber(shareNumber);
	}

	public void updateUserLikenumber(int likeNumber) {
		dao.updateUserLikenumber(likeNumber);
	}

	public void updateUserFannumber(int fanNumber) {
		dao.updateUserFannumber(fanNumber);
	}

	public void updateUserSourcenumber(int sourceNumber) {
		dao.updateUserSourcenumber(sourceNumber);
	}

	public void updateUserNotenumber(int noteNumber) {
		dao.updateUserNotenumber(noteNumber);
	}

	public ReturnModel selectUserByUsername(HttpServletRequest req) {
		String username = req.getParameter("username");
		User user = dao.selectUserByUsername(username);
		ReturnModel rm = new ReturnModel();
		if(user==null) {
			rm.setSuccess(false);
			rm.setInfo("�û�������");
			return rm;
		}
		
		rm.setData(user);
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
		if((user=dao.selectUserByEmail(email))!=null) {//�����ѱ�ע��
			rm.setSuccess(false);
			rm.setInfo("�����ѱ�ע��");
			return rm;
		}
		
		//�õ�������ַ����֤���Լ���֤�����ʱ��
		String host =req.getRemoteHost();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(new Date());
		String activeCode = this.generateCode(6);
		
		
		//�����ͬһ�����䷢������֤�������ǰһ����֤�롣
		ActiveCode ac;
		if((ac=dao2.selectByEmail(email))!=null) {//��������ҵ������ʾ��ͬһ��������һ�η�������֤��
			new Thread(new Runnable() {
				@Override
				public void run() {
					dao2.updateByEmail(email, activeCode,time);
					ss.commit();
				}}).start();
		//�����һ��û�н��յ���֤������䣬�����֤��洢�����ݿ�
		}else {
			new Thread(new Runnable() {
				@Override
				public void run() {
					dao2.insertActiveCode(new ActiveCode(email,activeCode,time));
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
		ActiveCode serac = dao2.selectByEmail(email);	
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
		if((user=dao.selectUserByEmail(email))!=null) {//�����ѱ�ע��
			result.setSuccess(false);
			result.setInfo("�����ѱ�ע��");
		}else if((user=dao.selectUserByUsername(username)) != null) {//�û����Ѵ���
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
					dao.insertUser(userTosave);
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
			user = dao.selectUserByUsername(username);
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
			user = dao.selectUserByEmail(username);
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
		List<Directory> list = dao3.selectAllDirectoryByUsername(username);
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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(new Date());
		Directory dir = new Directory(username,dirname,type,time);
		dao3.createDir(dir);
		rm.setInfo("�ɹ�����һ���ղؼ�");
		rm.setSuccess(true);
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
		
		dao.updateUserInfo(user);
		ss.commit();
		rm.setInfo("����ɹ�");
		rm.setSuccess(true);
		return rm;
	}

}
