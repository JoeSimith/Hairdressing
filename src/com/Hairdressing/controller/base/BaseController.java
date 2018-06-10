package com.Hairdressing.controller.base;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.Hairdressing.entity.Page;
import com.Hairdressing.entity.system.User;
import com.Hairdressing.util.Logger;
import com.Hairdressing.util.PageData;
import com.Hairdressing.util.Token;
import com.Hairdressing.util.TokenPool;
import com.Hairdressing.util.UuidUtil;
import com.alibaba.fastjson.JSON;

public class BaseController {
	
	protected Logger logger = Logger.getLogger(this.getClass());

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 6357869213649815390L;
	
	/**
	 * 得到PageData
	 */
	public PageData getPageData(){
		return new PageData(this.getRequest());
	}
	
	/**
	 * 得到ModelAndView
	 */
	public ModelAndView getModelAndView(){
		return new ModelAndView();
	}
	
	/**
	 * 得到request对象
	 */
	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		
		return request;
	}
	
	public User getCurrentUser() {
		PageData pd = this.getPageData();
		String tokenStr = pd.getString("token");
		TokenPool tokenPool=TokenPool.getInstance();
		Token token = tokenPool.getToken(tokenStr);
		return token.getUser();
	}
	
	/**
	 * 得到32位的uuid
	 * @return
	 */
	public String get32UUID(){
		
		return UuidUtil.get32UUID();
	}
	
	/**
	 * 得到分页列表的信息 
	 */
	public Page getPage(){
		
		return new Page();
	}
	
	public static void logBefore(Logger logger, String interfaceName){
		logger.info("start");
		logger.info(interfaceName);
	}
	
	public static void logAfter(Logger logger){
		logger.info("end");
	}
	/**
	 * 返回常规json字符串
	 * @param state 状态码
	 * @param message 返回消息
	 * @return
	 */
	public String jsonContent(String state,String message) {
		Map<String,Object> res= new HashMap<String, Object>();
		res.put("state", state);
		res.put("message", message);
		return JSON.toJSONString(res);
	}
	
	public String jsonContent(Map pageData) {
		return JSON.toJSONString(pageData);
	}

	/**
	 * 返回分页json字符串
	 * @param dataList 结果集
	 * @param page 分页
	 * @return
	 */
	public String jsonContent(List dataList,Page page) {
		String json = JSON.toJSONString(dataList);
		json="{\"rows\":"+json+",\"total\":"+page.getTotalPage()+",\"page\":"+page.getCurrentPage()+",\"records\":"+page.getTotalResult()+"}";
		return json;
	}
	
	public String jsonContent(List dataList) {
		String json = JSON.toJSONString(dataList);
		return "{\"rows\":"+json+"}";
	}
	
}
