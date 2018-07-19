package com.Hairdressing.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.Hairdressing.service.system.user.UserService;
import com.Hairdressing.util.Const;
import com.Hairdressing.util.Token;
import com.Hairdressing.util.TokenPool;
import com.alibaba.fastjson.JSON;
/**
 * 
* 类名称：LoginHandlerInterceptor.java
* 类描述：
* Spring MVC中使用拦截器的方法，继承HandlerInterceptorAdapter类，
* 并根据需求实现其中的preHandle方法（预处理）、
* postHandle方法（返回处理）、afterCompletion方法（后处理） 
 */
public class LoginHandlerInterceptor extends HandlerInterceptorAdapter{

	@Resource(name="userService")
	private UserService userService;
	//1.当preHandle方法返回false时，从当前拦截器往回执行所有拦截器的afterCompletion方法，再退出拦截器链。也就是说，请求不继续往下传了，直接沿着来的链往回跑。
	//2.当preHandle方法全为true时，执行下一个拦截器,直到所有拦截器执行完。
	//  再运行被拦截的Controller。然后进入拦截器链，运行所有拦截器的postHandle方法,完后从最后一个拦截器往回执行所有拦截器的afterCompletion方法.
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String path = request.getServletPath();
		System.out.println("path:"+path); 
		//return true;
		//判断是否是需要拦截的访问地址
		if(path.matches(Const.NO_INTERCEPTOR_PATH)){
			return true;
		}else{
			String tokenStr = request.getParameter("token");
			System.out.println("tokenStr:" + tokenStr);
			TokenPool tokenPool = TokenPool.getInstance();
			Token token = tokenPool.getToken(tokenStr);
			if(token==null) {
				Map<String,Object> res= new HashMap<String, Object>();
				res.put("state", "errorToken");
				res.put("message", "Error Token");
				String jsonString = JSON.toJSONString(res);
				this.writeJson(response, jsonString);
				return false;
			}else if(token.isExpiry()) {
				Map<String,Object> res= new HashMap<String, Object>();
				res.put("state", "errorToken");
				res.put("message", "Expiry Token");
				String jsonString = JSON.toJSONString(res);
				this.writeJson(response, jsonString);
				return false;
			}else {
				tokenPool.flushToken(tokenStr);
				return true;
			}
 		}
	}
	
	private void writeJson(HttpServletResponse response,String JsonStr) {
		response.setCharacterEncoding("UTF-8");  
	    response.setContentType("application/json; charset=utf-8");  
	    PrintWriter out = null;  
	    try {  
	        out = response.getWriter();  
	        out.append(JsonStr);  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    } finally {  
	        if (out != null) {  
	            out.close();  
	        }  
	    } 
	}
}
