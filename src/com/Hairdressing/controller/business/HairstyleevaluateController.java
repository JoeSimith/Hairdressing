package com.Hairdressing.controller.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.Hairdressing.controller.base.BaseController;
import com.Hairdressing.entity.Page;
import com.Hairdressing.util.AppUtil;
import com.Hairdressing.util.ObjectExcelView;
import com.Hairdressing.util.PageData;
import com.Hairdressing.service.business.HairstyleevaluateService;

/** 
 * 类名称：HairstyleevaluateController
 * 创建时间：2018-06-09
 */
@Controller
@RequestMapping(value="/hairstyleevaluate")
public class HairstyleevaluateController extends BaseController {
	
	String menuUrl = "hairstyleevaluate/list.do"; //菜单地址(权限用)
	@Resource(name="hairstyleevaluateService")
	private HairstyleevaluateService hairstyleevaluateService;
	
	/**
	 * 新增或编辑
	 */
	@ResponseBody
	@RequestMapping(value = "/saveOrUpdate", produces = "application/json;charset=UTF-8")
	public String saveOrUpdate() throws Exception{
		logBefore(logger, "新增或编辑Hairstyleevaluate");
		PageData pd = this.getPageData();
		if(pd.get("id")==null||pd.get("id").equals("")) {
			pd.put("id", this.get32UUID()); // 主键
			this.hairstyleevaluateService.save(pd);
		}else {
			this.hairstyleevaluateService.edit(pd);
		}
		return this.jsonContent("success", "保存成功");
	}
	
	/**
	 * 新增
	 */
	@ResponseBody
	@RequestMapping(value = "/save", produces = "application/json;charset=UTF-8")
	public String save() throws Exception{
		logBefore(logger, "新增Hairstyleevaluate");
		PageData pd = this.getPageData();
		pd.put("id", this.get32UUID()); // 主键
		this.hairstyleevaluateService.save(pd);
		return this.jsonContent("success", "保存成功");
	}
	
	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping(value = "/delete", produces = "application/json;charset=UTF-8")
	public String delete() throws Exception{
		logBefore(logger, "删除Hairstyleevaluate");
		PageData pd = this.getPageData();
		hairstyleevaluateService.delete(pd);
		return this.jsonContent("success", "删除成功");
	}
	
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping(value="/edit", produces = "application/json;charset=UTF-8")
	public String edit() throws Exception{
		logBefore(logger, "修改Hairstyleevaluate");
		PageData pd = this.getPageData();
		this.hairstyleevaluateService.edit(pd);
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
		logBefore(logger, "获取Hairstyleevaluate列表Json");
		PageData pd = this.getPageData();
		Page page = new Page();
		page.setCurrentPage(pd.getInt("page"));
		page.setShowCount(pd.getInt("rows"));
		page.setPd(pd);
		List<PageData> resultList = this.hairstyleevaluateService.listPage(page);// 分页查询列表
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
		PageData resultPD = this.hairstyleevaluateService.findById(pd);
		return this.jsonContent(resultPD);
	}
	
	
	/**
	 * 列表
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page){
		logBefore(logger, "列表Hairstyleevaluate");
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("business/hairstyleevaluate/list");
		return mv;
	}
	
	/**
	 * 去新增页面
	 */
	@RequestMapping(value="/toAdd")
	public ModelAndView toAdd(){
		logBefore(logger, "去新增Hairstyleevaluate页面");
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("business/hairstyleevaluate/form");
		return mv;
	}	
	
	/**
	 * 去修改页面
	 */
	@RequestMapping(value="/toEdit")
	public ModelAndView toEdit(){
		logBefore(logger, "去修改Hairstyleevaluate页面");
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("business/hairstyleevaluate/form");		
		return mv;
	}	
	
	/**
	 * 去查看页面
	 */
	@RequestMapping(value="/toDetail")
	public ModelAndView toDetail(){
		logBefore(logger, "去查看Hairstyleevaluate页面");
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("business/hairstyleevaluate/detail");		
		return mv;
	}
	
	/**
	 * 批量删除
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() {
		logBefore(logger, "批量删除Hairstyleevaluate");
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			pd = this.getPageData();
			List<PageData> pdList = new ArrayList<PageData>();
			String DATA_IDS = pd.getString("DATA_IDS");
			if(null != DATA_IDS && !"".equals(DATA_IDS)){
				String ArrayDATA_IDS[] = DATA_IDS.split(",");
				hairstyleevaluateService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, "导出Hairstyleevaluate到excel");
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			Map<String,Object> dataMap = new HashMap<String,Object>();
			List<String> titles = new ArrayList<String>();
			titles.add("发型评价唯一ID");	//1
			titles.add("发型唯一ID");	//2
			titles.add("发型评价的内容");	//3
			titles.add("评价用户名ID");	//4
			dataMap.put("titles", titles);
			List<PageData> varOList = hairstyleevaluateService.listAll(pd);
			List<PageData> varList = new ArrayList<PageData>();
			for(int i=0;i<varOList.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", varOList.get(i).getString("id"));	//1
				vpd.put("var2", varOList.get(i).getString("hairstyleid"));	//2
				vpd.put("var3", varOList.get(i).getString("hairstyleidcontent"));	//3
				vpd.put("var4", varOList.get(i).getString("userid"));	//4
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
