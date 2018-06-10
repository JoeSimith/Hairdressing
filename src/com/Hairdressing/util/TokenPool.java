package com.Hairdressing.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class TokenPool extends HashMap<String,Token>{
	
	private static final long serialVersionUID = 1L;
	private static TokenPool tokenPool;
	private TokenPool() {}
	public static synchronized TokenPool getInstance() {
		if(tokenPool==null) {
			tokenPool=new TokenPool();
			cleanPoolTask();
		}
		return tokenPool;
	}
	public boolean addToken(Token token) {
		tokenPool.remove(token.getToken());
		tokenPool.put(token.getToken(), token);
		return true;
	}
	public Token getToken(String tokenStr) {
		return tokenPool.get(tokenStr);
	}
	public boolean flushToken(String tokenStr) {
		Token token = tokenPool.getToken(tokenStr);
		token.flushExpiryTime();
		tokenPool.remove(tokenStr);
		tokenPool.addToken(token);
		return true;
	}
	
	private static boolean  cleanPoolTask() {
		Timer timer = new Timer(true);
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				cleanPool();
			}
		};
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date stratTime=null;
		try {
			stratTime=simpleDateFormat.parse("2017-12-01 00:00:00");
			timer.schedule(task,stratTime , 24*60*60*1000); 
		}catch (Exception e) {
			System.out.println("TokenPool定时清理任务设置出现异常");
			return false;
		}
		return true;
	}
	
	public static synchronized boolean cleanPool() {
		if(tokenPool!=null) {
			System.out.println("TokenPool Clean Start");
			Set<String> keySet = tokenPool.keySet();
			for (String tokenStr : keySet) {
				Token token = tokenPool.get(tokenStr);
				if(token.isExpiry()) {
					tokenPool.remove(tokenStr);
				}
			}
		}
		return true;
	}
}
