package com.collectionplus.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;  
import java.util.HashSet;  
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;  
import java.util.Map.Entry;

import com.collectionplus.bean.User;

import java.util.Scanner;  
import java.util.Set;  

public class recCollection {
	
    
    public Map<String , Double> recCollectionUsers(int N,String[] all_item,String recommendUser){ 
    	Map<String, Integer> userItemLength = new HashMap<>();//存储每一个用户对应的不同物品总数  eg: A 3  
        Map<String, Set<String>> itemUserCollection = new HashMap<>();//建立物品到用户的倒排表 eg: a A B  
        Set<String> items = new HashSet<>();//辅助存储物品集合  
        Map<String, Integer> userID = new HashMap<>();//辅助存储每一个用户的用户ID映射  
        Map<Integer, String> idUser = new HashMap<>();//辅助存储每一个ID对应的用户映射
    	int[][] sparseMatrix = new int[N][N];//建立用户稀疏矩阵，用于用户相似度计算【相似度矩阵】  
    	Map<String , Double> recommUsers = new HashMap<String, Double>(); 
        for(int i = 0; i < N ; i++){//依次处理N个用户 输入数据  以空格间隔  
            String[] user_item = all_item[i].split(" ");  
            int length = user_item.length;  
            userItemLength.put(user_item[0], length-1);//eg: A 3  
            userID.put(user_item[0], i);//用户ID与稀疏矩阵建立对应关系  
            idUser.put(i, user_item[0]);  
            //建立物品--用户倒排表  
            for(int j = 1; j < length; j ++){  
                if(items.contains(user_item[j])){//如果已经包含对应的物品--用户映射，直接添加对应的用户  
                    itemUserCollection.get(user_item[j]).add(user_item[0]);  
                }else{//否则创建对应物品--用户集合映射  
                    items.add(user_item[j]);  
                    itemUserCollection.put(user_item[j], new HashSet<String>());//创建物品--用户倒排关系  
                    itemUserCollection.get(user_item[j]).add(user_item[0]);  
                }  
            }  
        }  
        //System.out.println(itemUserCollection.toString());  
        //计算相似度矩阵【稀疏】  
        Set<Entry<String, Set<String>>> entrySet = itemUserCollection.entrySet();  
        Iterator<Entry<String, Set<String>>> iterator = entrySet.iterator();  
        while(iterator.hasNext()){  
            Set<String> commonUsers = iterator.next().getValue();  
            for (String user_u : commonUsers) {  
                for (String user_v : commonUsers) {  
                    if(user_u.equals(user_v)){  
                        continue;  
                    }  
                    sparseMatrix[userID.get(user_u)][userID.get(user_v)] += 1;//计算用户u与用户v都有正反馈的物品总数  
                }  
            }  
        }  
        //System.out.println(userItemLength.toString());   
        //System.out.println(userID.get(recommendUser));  
        //计算用户之间的相似度【余弦相似性】  
        int recommendUserId = userID.get(recommendUser);  
        for (int j = 0;j < sparseMatrix.length; j++) {  
                if(j != recommendUserId){  
                	recommUsers.put(idUser.get(j),sparseMatrix[recommendUserId][j]/Math.sqrt(userItemLength.get(idUser.get(recommendUserId))*userItemLength.get(idUser.get(j))));
                    //System.out.println(idUser.get(recommendUserId)+"--"+idUser.get(j)+"相似度:"+sparseMatrix[recommendUserId][j]/Math.sqrt(userItemLength.get(idUser.get(recommendUserId))*userItemLength.get(idUser.get(j))));  
                }  
        }  
        Map<String, Double> result = new LinkedHashMap<>();
        recommUsers.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEachOrdered(x -> result.put(x.getKey(), x.getValue()));
		return result;   
    }
    
    
    public Map<String , Double> recCollectionLinks(int N,String[] all_item,String recommendUser){ 
    	Map<String, Integer> userItemLength = new HashMap<>();//存储每一个用户对应的不同物品总数  eg: A 3  
        Map<String, Set<String>> itemUserCollection = new HashMap<>();//建立物品到用户的倒排表 eg: a A B  
        Set<String> items = new HashSet<>();//辅助存储物品集合  
        Map<String, Integer> userID = new HashMap<>();//辅助存储每一个用户的用户ID映射  
        Map<Integer, String> idUser = new HashMap<>();//辅助存储每一个ID对应的用户映射
    	Map<String , Double> recommLinks = new HashMap<String, Double>();
    	int[][] sparseMatrix = new int[N][N];//建立用户稀疏矩阵，用于用户相似度计算【相似度矩阵】  
        for(int i = 0; i < N ; i++){//依次处理N个用户 输入数据  以空格间隔  
            String[] user_item = all_item[i].split(" ");  
            int length = user_item.length;  
            userItemLength.put(user_item[0], length-1);//eg: A 3  
            userID.put(user_item[0], i);//用户ID与稀疏矩阵建立对应关系  
            idUser.put(i, user_item[0]);  
            //建立物品--用户倒排表  
            for(int j = 1; j < length; j ++){  
                if(items.contains(user_item[j])){//如果已经包含对应的物品--用户映射，直接添加对应的用户  
                    itemUserCollection.get(user_item[j]).add(user_item[0]);  
                }else{//否则创建对应物品--用户集合映射  
                    items.add(user_item[j]);  
                    itemUserCollection.put(user_item[j], new HashSet<String>());//创建物品--用户倒排关系  
                    itemUserCollection.get(user_item[j]).add(user_item[0]);  
                }  
            }  
        }  
        
        //计算相似度矩阵【稀疏】  
        Set<Entry<String, Set<String>>> entrySet = itemUserCollection.entrySet();  
        Iterator<Entry<String, Set<String>>> iterator = entrySet.iterator();  
        while(iterator.hasNext()){  
            Set<String> commonUsers = iterator.next().getValue();  
            for (String user_u : commonUsers) {  
                for (String user_v : commonUsers) {  
                    if(user_u.equals(user_v)){  
                        continue;  
                    }  
                    sparseMatrix[userID.get(user_u)][userID.get(user_v)] += 1;//计算用户u与用户v都有正反馈的物品总数  
                }  
            }  
        }  
       
        //计算用户之间的相似度【余弦相似性】  
        for(String item: items){//遍历每一件物品  
    		Set<String> users = itemUserCollection.get(item);//得到购买当前物品的所有用户集合  
    		if(!users.contains(recommendUser)){//如果被推荐用户没有购买当前物品，则进行推荐度计算  
    			double itemRecommendDegree = 0.0;  
                for(String user: users){  
                    itemRecommendDegree += sparseMatrix[userID.get(recommendUser)][userID.get(user)]/Math.sqrt(userItemLength.get(recommendUser)*userItemLength.get(user));//推荐度计算  
                }  
              
                recommLinks.put(item, itemRecommendDegree);
                  
            }  
        }
        Map<String, Double> result = new LinkedHashMap<>();
        recommLinks.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEachOrdered(x -> result.put(x.getKey(), x.getValue()));
		return result;   
    }
}
