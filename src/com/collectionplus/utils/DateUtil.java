package com.collectionplus.utils;

public class DateUtil {
	 public static boolean TimeDifference(long start, long end) {
		  boolean isOverdue = false;
		  long between = end - start;
		  long interval = 5*60*60*1000; //设置过期时间为5小时。
		  if(between>interval ) {
			  isOverdue =true;
		  }
		  
		 return isOverdue;
	 }

}
