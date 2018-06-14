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
    	Map<String, Integer> userItemLength = new HashMap<>();//�洢ÿһ���û���Ӧ�Ĳ�ͬ��Ʒ����  eg: A 3  
        Map<String, Set<String>> itemUserCollection = new HashMap<>();//������Ʒ���û��ĵ��ű� eg: a A B  
        Set<String> items = new HashSet<>();//�����洢��Ʒ����  
        Map<String, Integer> userID = new HashMap<>();//�����洢ÿһ���û����û�IDӳ��  
        Map<Integer, String> idUser = new HashMap<>();//�����洢ÿһ��ID��Ӧ���û�ӳ��
    	int[][] sparseMatrix = new int[N][N];//�����û�ϡ����������û����ƶȼ��㡾���ƶȾ���  
    	Map<String , Double> recommUsers = new HashMap<String, Double>(); 
        for(int i = 0; i < N ; i++){//���δ���N���û� ��������  �Կո���  
            String[] user_item = all_item[i].split(" ");  
            int length = user_item.length;  
            userItemLength.put(user_item[0], length-1);//eg: A 3  
            userID.put(user_item[0], i);//�û�ID��ϡ���������Ӧ��ϵ  
            idUser.put(i, user_item[0]);  
            //������Ʒ--�û����ű�  
            for(int j = 1; j < length; j ++){  
                if(items.contains(user_item[j])){//����Ѿ�������Ӧ����Ʒ--�û�ӳ�䣬ֱ����Ӷ�Ӧ���û�  
                    itemUserCollection.get(user_item[j]).add(user_item[0]);  
                }else{//���򴴽���Ӧ��Ʒ--�û�����ӳ��  
                    items.add(user_item[j]);  
                    itemUserCollection.put(user_item[j], new HashSet<String>());//������Ʒ--�û����Ź�ϵ  
                    itemUserCollection.get(user_item[j]).add(user_item[0]);  
                }  
            }  
        }  
        //System.out.println(itemUserCollection.toString());  
        //�������ƶȾ���ϡ�衿  
        Set<Entry<String, Set<String>>> entrySet = itemUserCollection.entrySet();  
        Iterator<Entry<String, Set<String>>> iterator = entrySet.iterator();  
        while(iterator.hasNext()){  
            Set<String> commonUsers = iterator.next().getValue();  
            for (String user_u : commonUsers) {  
                for (String user_v : commonUsers) {  
                    if(user_u.equals(user_v)){  
                        continue;  
                    }  
                    sparseMatrix[userID.get(user_u)][userID.get(user_v)] += 1;//�����û�u���û�v��������������Ʒ����  
                }  
            }  
        }  
        //System.out.println(userItemLength.toString());   
        //System.out.println(userID.get(recommendUser));  
        //�����û�֮������ƶȡ����������ԡ�  
        int recommendUserId = userID.get(recommendUser);  
        for (int j = 0;j < sparseMatrix.length; j++) {  
                if(j != recommendUserId){  
                	recommUsers.put(idUser.get(j),sparseMatrix[recommendUserId][j]/Math.sqrt(userItemLength.get(idUser.get(recommendUserId))*userItemLength.get(idUser.get(j))));
                    //System.out.println(idUser.get(recommendUserId)+"--"+idUser.get(j)+"���ƶ�:"+sparseMatrix[recommendUserId][j]/Math.sqrt(userItemLength.get(idUser.get(recommendUserId))*userItemLength.get(idUser.get(j))));  
                }  
        }  
        Map<String, Double> result = new LinkedHashMap<>();
        recommUsers.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEachOrdered(x -> result.put(x.getKey(), x.getValue()));
		return result;   
    }
    
    
    public Map<String , Double> recCollectionLinks(int N,String[] all_item,String recommendUser){ 
    	Map<String, Integer> userItemLength = new HashMap<>();//�洢ÿһ���û���Ӧ�Ĳ�ͬ��Ʒ����  eg: A 3  
        Map<String, Set<String>> itemUserCollection = new HashMap<>();//������Ʒ���û��ĵ��ű� eg: a A B  
        Set<String> items = new HashSet<>();//�����洢��Ʒ����  
        Map<String, Integer> userID = new HashMap<>();//�����洢ÿһ���û����û�IDӳ��  
        Map<Integer, String> idUser = new HashMap<>();//�����洢ÿһ��ID��Ӧ���û�ӳ��
    	Map<String , Double> recommLinks = new HashMap<String, Double>();
    	int[][] sparseMatrix = new int[N][N];//�����û�ϡ����������û����ƶȼ��㡾���ƶȾ���  
        for(int i = 0; i < N ; i++){//���δ���N���û� ��������  �Կո���  
            String[] user_item = all_item[i].split(" ");  
            int length = user_item.length;  
            userItemLength.put(user_item[0], length-1);//eg: A 3  
            userID.put(user_item[0], i);//�û�ID��ϡ���������Ӧ��ϵ  
            idUser.put(i, user_item[0]);  
            //������Ʒ--�û����ű�  
            for(int j = 1; j < length; j ++){  
                if(items.contains(user_item[j])){//����Ѿ�������Ӧ����Ʒ--�û�ӳ�䣬ֱ����Ӷ�Ӧ���û�  
                    itemUserCollection.get(user_item[j]).add(user_item[0]);  
                }else{//���򴴽���Ӧ��Ʒ--�û�����ӳ��  
                    items.add(user_item[j]);  
                    itemUserCollection.put(user_item[j], new HashSet<String>());//������Ʒ--�û����Ź�ϵ  
                    itemUserCollection.get(user_item[j]).add(user_item[0]);  
                }  
            }  
        }  
        
        //�������ƶȾ���ϡ�衿  
        Set<Entry<String, Set<String>>> entrySet = itemUserCollection.entrySet();  
        Iterator<Entry<String, Set<String>>> iterator = entrySet.iterator();  
        while(iterator.hasNext()){  
            Set<String> commonUsers = iterator.next().getValue();  
            for (String user_u : commonUsers) {  
                for (String user_v : commonUsers) {  
                    if(user_u.equals(user_v)){  
                        continue;  
                    }  
                    sparseMatrix[userID.get(user_u)][userID.get(user_v)] += 1;//�����û�u���û�v��������������Ʒ����  
                }  
            }  
        }  
       
        //�����û�֮������ƶȡ����������ԡ�  
        for(String item: items){//����ÿһ����Ʒ  
    		Set<String> users = itemUserCollection.get(item);//�õ�����ǰ��Ʒ�������û�����  
    		if(!users.contains(recommendUser)){//������Ƽ��û�û�й���ǰ��Ʒ��������Ƽ��ȼ���  
    			double itemRecommendDegree = 0.0;  
                for(String user: users){  
                    itemRecommendDegree += sparseMatrix[userID.get(recommendUser)][userID.get(user)]/Math.sqrt(userItemLength.get(recommendUser)*userItemLength.get(user));//�Ƽ��ȼ���  
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
