package com.collectionplus.utils;

public class DateUtil {
	 public static boolean TimeDifference(long start, long end) {
		  boolean isOverdue = false;
		  long between = end - start;
		  long interval = 5*60*60*1000; //���ù���ʱ��Ϊ5Сʱ��
		  if(between>interval ) {
			  isOverdue =true;
		  }
		  
		 return isOverdue;
	 }

}
