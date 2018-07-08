package com.Hairdressing.controller.business;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.text.StrBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.Hairdressing.controller.base.BaseController;
import com.Hairdressing.entity.Page;
import com.Hairdressing.entity.system.User;
import com.Hairdressing.service.business.RebackService;
import com.Hairdressing.util.AppUtil;
import com.Hairdressing.util.Const;
import com.Hairdressing.util.DateUtil;
import com.Hairdressing.util.FileUpload;
import com.Hairdressing.util.ObjectExcelView;
import com.Hairdressing.util.PageData;
import com.Hairdressing.util.Tools;

/** 
 * 类名称：RebackController
 * 创建时间：2018-06-09
 */
@Controller
@RequestMapping(value="/reback")
public class RebackController extends BaseController {
	
	String menuUrl = "reback/list.do"; //菜单地址(权限用)
	@Resource(name="rebackService")
	private RebackService rebackService;
	
	/**
	 * 新增或编辑
	 */
	@ResponseBody
	@RequestMapping(value = "/saveOrUpdate", produces = "application/json;charset=UTF-8")
	public String saveOrUpdate() throws Exception{
		logBefore(logger, "新增或编辑Reback");
		PageData pd = this.getPageData();
		if(pd.get("ID")==null||pd.get("ID").equals("")) {
			pd.put("ID", this.get32UUID()); // 主键
			this.rebackService.save(pd);
		}else {
			this.rebackService.edit(pd);
		}
		return this.jsonContent("success", "保存成功");
	}
	
	/**
	 * 新增
	 */
	@ResponseBody
	@RequestMapping(value = "/apisave")
	public String save(@RequestParam(value="file", required=false) MultipartFile[] file
			,@RequestParam(value="token", required=false) String token
			,@RequestParam(value="rebackType", required=false) String rebackType
			,@RequestParam(value="rebackContent", required=false) String rebackContent) throws Exception{
		logBefore(logger, "新增Reback");
		PageData pd = new PageData();
		if(token == null)
		{
			pd.put("rebackName", "匿名用户反馈！");
		}else{
			User  user = this.getCurrentUser(token);
			pd.put("rebackName", user.getUserName());
		}
		//文件上传处理部分start
		StrBuilder fileNames = new StrBuilder();
	    String saveFileName = DateUtil.sdfTimeString() + Tools.getRandomNum();
	    String filePath = Const.REBACK_SAVE_PATH;
	    for (MultipartFile multipartFile : file) {
			String fileName = FileUpload.fileUp(multipartFile, filePath, saveFileName + "-"+multipartFile.getOriginalFilename());
			System.out.println("multipartFile.getOriginalFilename():"+multipartFile.getOriginalFilename());
			fileNames.append(fileName);
			fileNames.append("@#");
	    }
	    //文件上传处理部分end
	    pd.put("rebackPath", fileNames.substring(0,fileNames.length()-2));
		pd.put("rebackType", rebackType);
		pd.put("rebackContent", rebackContent);
		pd.put("rebackDate", DateUtil.sdfTimeString());
		pd.put("ID", this.get32UUID()); // 主键
		this.rebackService.save(pd);
		return this.jsonContent("success", "保存成功");
	}
	
	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping(value = "/delete", produces = "application/json;charset=UTF-8")
	public String delete() throws Exception{
		logBefore(logger, "删除Reback");
		PageData pd = this.getPageData();
		rebackService.delete(pd);
		return this.jsonContent("success", "删除成功");
	}
	
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping(value="/edit", produces = "application/json;charset=UTF-8")
	public String edit() throws Exception{
		logBefore(logger, "修改Reback");
		PageData pd = this.getPageData();
		this.rebackService.edit(pd);
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
		logBefore(logger, "获取Reback列表Json");
		PageData pd = this.getPageData();
		Page page = new Page();
		page.setCurrentPage(pd.getInt("page"));
		page.setShowCount(pd.getInt("rows"));
		page.setPd(pd);
		List<PageData> resultList = this.rebackService.listPage(page);// 分页查询列表
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
		PageData resultPD = this.rebackService.findById(pd);
		return this.jsonContent(resultPD);
	}
	
	
	/**
	 * 列表
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page){
		logBefore(logger, "列表Reback");
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("business/reback/list");
		return mv;
	}
	
	/**
	 * 去新增页面
	 */
	@RequestMapping(value="/toAdd")
	public ModelAndView toAdd(){
		logBefore(logger, "去新增Reback页面");
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("business/reback/form");
		return mv;
	}	
	
	/**
	 * 去修改页面
	 */
	@RequestMapping(value="/toEdit")
	public ModelAndView toEdit(){
		logBefore(logger, "去修改Reback页面");
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("business/reback/form");		
		return mv;
	}	
	
	/**
	 * 去查看页面
	 */
	@RequestMapping(value="/toDetail")
	public ModelAndView toDetail(){
		logBefore(logger, "去查看Reback页面");
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("business/reback/detail");		
		return mv;
	}
	
	/**
	 * 批量删除
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() {
		logBefore(logger, "批量删除Reback");
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			pd = this.getPageData();
			List<PageData> pdList = new ArrayList<PageData>();
			String DATA_IDS = pd.getString("DATA_IDS");
			if(null != DATA_IDS && !"".equals(DATA_IDS)){
				String ArrayDATA_IDS[] = DATA_IDS.split(",");
				rebackService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, "导出Reback到excel");
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			Map<String,Object> dataMap = new HashMap<String,Object>();
			List<String> titles = new ArrayList<String>();
			titles.add("反馈唯一ID");	//1
			titles.add("反馈人用户名");	//2
			titles.add("反馈时间");	//3
			titles.add("反馈内容");	//4
			titles.add("反馈类型");	//5
			titles.add("反馈图片");	//6
			dataMap.put("titles", titles);
			List<PageData> varOList = rebackService.listAll(pd);
			List<PageData> varList = new ArrayList<PageData>();
			for(int i=0;i<varOList.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", varOList.get(i).getString("ID"));	//1
				vpd.put("var2", varOList.get(i).getString("rebackName"));	//2
				vpd.put("var3", varOList.get(i).getString("rebackDate"));	//3
				vpd.put("var4", varOList.get(i).getString("rebackContent"));	//4
				vpd.put("var5", varOList.get(i).getString("rebackType"));	//5
				vpd.put("var6", varOList.get(i).getString("rebackPath"));	//6
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
