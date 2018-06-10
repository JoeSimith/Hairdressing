package com.Hairdressing.controller.system.login;

import javax.annotation.Resource;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Hairdressing.controller.base.BaseController;
import com.Hairdressing.entity.system.User;
import com.Hairdressing.service.system.user.UserService;
import com.Hairdressing.util.License;
import com.Hairdressing.util.PageData;
import com.Hairdressing.util.Token;
import com.Hairdressing.util.TokenPool;

/*
 * 总入口
 */
@Controller
public class LoginController extends BaseController {

	@Resource(name="userService")
	private UserService userService;

	/**
	 * 用户登录
	 * @param username String,用户名
	 * @param password String,密码
	 * @return token:令牌，user:用户信息，state:状态码
	 */
	@RequestMapping(value="/login" ,produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object login()throws Exception{
		if(License.licenseCheck()==false){
			return	this.jsonContent("error","license error");
		}
		PageData pd = this.getPageData();
		String password = new SimpleHash("SHA-1", pd.getString("loginid"), pd.getString("password")).toString();	//密码加密
		PageData searchPd= new PageData();
		searchPd.put("loginId",pd.getString("loginid"));
		searchPd.put("password",password);
		User user = this.userService.loginUser(searchPd);
		if(user==null){
			return this.jsonContent("error", "该用户不存在或密码错误");
		}
		PageData resultPD=new PageData();
		TokenPool tokenPool=TokenPool.getInstance();
		Token token = new Token(user);
		tokenPool.addToken(token);
		resultPD.put("token", token.getToken());
		System.out.println("addToken:"+token.getToken());
		resultPD.put("user", user);
		resultPD.put("state", "success");
		return this.jsonContent(resultPD);

	}

	/**
	 * 注销
	 * @param token String,需要注销的token
	 * @return state:注销状态
	 */
	@ResponseBody
	@RequestMapping(value="/logout" ,produces="application/json;charset=UTF-8")
	public String logout()throws Exception{
		PageData pd = this.getPageData();
		String token = pd.getString("token");
		PageData resultPD=new PageData();
		TokenPool tokenPool=TokenPool.getInstance();
		tokenPool.remove(token);
		System.out.println("removeToken:"+token);
		resultPD.put("state", "success");
		return this.jsonContent(resultPD);
	}



}
	

