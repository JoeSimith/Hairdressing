package com.Hairdressing.controller.system.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.Hairdressing.controller.base.BaseController;
import com.Hairdressing.entity.Page;
import com.Hairdressing.util.AppUtil;
import com.Hairdressing.util.ObjectExcelView;
import com.Hairdressing.util.PageData;
import com.Hairdressing.service.system.user.UserService;

/** 
 * 类名称：UserController
 * 创建时间：2018-06-09
 */
@Controller
@RequestMapping(value="/user")
public class UserController extends BaseController {
	
	String menuUrl = "user/list.do"; //菜单地址(权限用)
	@Resource(name="userService")
	private UserService userService;
	
	/**
	 * 新增或编辑
	 */
	@ResponseBody
	@RequestMapping(value = "/saveOrUpdate", produces = "application/json;charset=UTF-8")
	public String saveOrUpdate() throws Exception{
		logBefore(logger, "新增或编辑User");
		PageData pd = this.getPageData();
		if(pd.get("userId")==null||pd.get("userId").equals("")) {
			pd.put("userId", this.get32UUID()); // 主键
			String password = new SimpleHash("SHA-1", pd.getString("loginid"), pd.getString("password")).toString();	//密码加密
			pd.put("password",password);
			this.userService.save(pd);
		}else {
			this.userService.edit(pd);
		}
		return this.jsonContent("success", "保存成功");
	}
	
	/**
	 * 新增
	 */
	@ResponseBody
	@RequestMapping(value = "/save", produces = "application/json;charset=UTF-8")
	public String save() throws Exception{
		logBefore(logger, "新增User");
		PageData pd = this.getPageData();
		pd.put("userId", this.get32UUID()); // 主键
		this.userService.save(pd);
		return this.jsonContent("success", "保存成功");
	}
	
	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping(value = "/delete", produces = "application/json;charset=UTF-8")
	public String delete() throws Exception{
		logBefore(logger, "删除User");
		PageData pd = this.getPageData();
		userService.delete(pd);
		return this.jsonContent("success", "删除成功");
	}
	
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping(value="/edit", produces = "application/json;charset=UTF-8")
	public String edit() throws Exception{
		logBefore(logger, "修改User");
		PageData pd = this.getPageData();
		this.userService.edit(pd);
		return this.jsonContent("success", "保存成功");
	}
	
	/**
	 * 返回列表JSON
	 * 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getGridListJson", produces = "application/json;charset=UTF-8")
	public Object getGridListJson() throws Exception {
		logBefore(logger, "获取User列表Json");
		PageData pd = this.getPageData();
		Page page = new Page();
		page.setCurrentPage(pd.getInt("page"));
		page.setShowCount(pd.getInt("rows"));
		page.setPd(pd);
		List<PageData> resultList = this.userService.listPage(page);// 分页查询列表
		return this.jsonContent(resultList, page);
	}
	
	/**
	 * 获取表单页面JSON
	 */
	@RequestMapping(value = "/getFormJson", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getFormJson() throws Exception {
		logBefore(logger, "获取新建页面数据");
		PageData pd = this.getPageData();
		PageData resultPD = this.userService.findById(pd);
		return this.jsonContent(resultPD);
	}
	
	
	/**
	 * 列表
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page){
		logBefore(logger, "列表User");
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("system/user/list");
		return mv;
	}
	
	/**
	 * 去新增页面
	 */
	@RequestMapping(value="/toAdd")
	public ModelAndView toAdd(){
		logBefore(logger, "去新增User页面");
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("system/user/form");
		return mv;
	}	
	
	/**
	 * 去修改页面
	 */
	@RequestMapping(value="/toEdit")
	public ModelAndView toEdit(){
		logBefore(logger, "去修改User页面");
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("system/user/form");		
		return mv;
	}	
	
	/**
	 * 去查看页面
	 */
	@RequestMapping(value="/toDetail")
	public ModelAndView toDetail(){
		logBefore(logger, "去查看User页面");
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("system/user/detail");		
		return mv;
	}
	
	/**
	 * 批量删除
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() {
		logBefore(logger, "批量删除User");
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			pd = this.getPageData();
			List<PageData> pdList = new ArrayList<PageData>();
			String DATA_IDS = pd.getString("DATA_IDS");
			if(null != DATA_IDS && !"".equals(DATA_IDS)){
				String ArrayDATA_IDS[] = DATA_IDS.split(",");
				userService.deleteAll(ArrayDATA_IDS);
				pd.put("msg", "ok");
			}else{
				pd.put("msg", "no");
			}
			pdList.add(pd);
			map.put("list", pdList);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		} finally {
			logAfter(logger);
		}
		return AppUtil.returnObject(pd, map);
	}
	
	/*
	 * 导出到excel
	 * @return
	 */
	@RequestMapping(value="/excel")
	public ModelAndView exportExcel(){
		logBefore(logger, "导出User到excel");
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			Map<String,Object> dataMap = new HashMap<String,Object>();
			List<String> titles = new ArrayList<String>();
			titles.add("用户唯一ID");	//1
			titles.add("用户名");	//2
			titles.add("登录密码");	//3
			titles.add("店铺唯一ID");	//4
			titles.add("发型师联系电话");	//5
			titles.add("发型师QQORVX");	//6
			titles.add("发型师别名");	//7
			titles.add("用户登录名");	//8
			titles.add("区分不同的人");	//9
			titles.add("图象路径");	//10
			titles.add("创建日期");	//11
			titles.add("删除标识");	//12
			dataMap.put("titles", titles);
			List<PageData> varOList = userService.listAll(pd);
			List<PageData> varList = new ArrayList<PageData>();
			for(int i=0;i<varOList.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", varOList.get(i).getString("userId"));	//1
				vpd.put("var2", varOList.get(i).getString("userName"));	//2
				vpd.put("var3", varOList.get(i).getString("password"));	//3
				vpd.put("var4", varOList.get(i).getString("businessId"));	//4
				vpd.put("var5", varOList.get(i).getString("fxsPhone"));	//5
				vpd.put("var6", varOList.get(i).getString("fxsQQorVX"));	//6
				vpd.put("var7", varOList.get(i).getString("fxsUser"));	//7
				vpd.put("var8", varOList.get(i).getString("loginId"));	//8
				vpd.put("var9", varOList.get(i).getString("diffFlag"));	//9
				vpd.put("var10", varOList.get(i).getString("headSculpturePath"));	//10
				vpd.put("var11", varOList.get(i).getString("createDate"));	//11
				vpd.put("var12", varOList.get(i).getString("delFlag"));	//12
				varList.add(vpd);
			}
			dataMap.put("varList", varList);
			ObjectExcelView erv = new ObjectExcelView();
			mv = new ModelAndView(erv,dataMap);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
}
