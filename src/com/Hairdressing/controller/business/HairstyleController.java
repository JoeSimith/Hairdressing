package com.Hairdressing.controller.business;

import java.io.File;
import java.util.ArrayList;
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
import com.Hairdressing.util.AppUtil;
import com.Hairdressing.util.Const;
import com.Hairdressing.util.DateUtil;
import com.Hairdressing.util.FileUpload;
import com.Hairdressing.util.ObjectExcelView;
import com.Hairdressing.util.PageData;
import com.Hairdressing.util.Tools;

import net.coobird.thumbnailator.Thumbnails;

import com.Hairdressing.service.business.HairstyleService;
import com.Hairdressing.service.business.HairstyleevaluateService;

/** 
 * 类名称：HairstyleController
 * 创建时间：2018-06-09
 */
@Controller
@RequestMapping(value="/hairstyle")
public class HairstyleController extends BaseController {
	
	String menuUrl = "hairstyle/list.do"; //菜单地址(权限用)
	@Resource(name="hairstyleService")
	private HairstyleService hairstyleService;
	
	@Resource(name="hairstyleevaluateService")
	private HairstyleevaluateService hairstyleevaluateService;
	
	/**
	 * 新增或编辑
	 */
	@ResponseBody
	@RequestMapping(value = "/saveOrUpdate", produces = "application/json;charset=UTF-8")
	public String saveOrUpdate() throws Exception{
		logBefore(logger, "新增或编辑Hairstyle");
		PageData pd = this.getPageData();
		if(pd.get("id")==null||pd.get("id").equals("")) {
			pd.put("id", this.get32UUID()); // 主键
			this.hairstyleService.save(pd);
		}else {
			this.hairstyleService.edit(pd);
		}
		return this.jsonContent("success", "保存成功");
	}
	
	/**
	 * 新增
	 */
	@ResponseBody
	@RequestMapping(value = "/save", produces = "application/json;charset=UTF-8")
	public String save(@RequestParam(value="file", required=false) MultipartFile[] file
			,@RequestParam(value="token", required=false) String token
			,@RequestParam(value="styletitle", required=false) String styletitle
			,@RequestParam(value="content", required=false) String content) throws Exception{
		logBefore(logger, "新增Hairstyle");
		PageData pd = new PageData();
		pd.put("id", this.get32UUID()); // 主键
		User  user = this.getCurrentUser(token);
		pd.put("userid", user.getUserId());
		pd.put("styletitle", styletitle);
		pd.put("content", content);
		pd.put("publishtime", DateUtil.getDays());
		pd.put("viewcount", 0);
		//文件上传处理部分start
		StrBuilder fileNames = new StrBuilder();
	    String saveFileName = DateUtil.sdfTimeString() + Tools.getRandomNum();
	    String filePath = Const.HAIRSTYLE_SAVE_PATH;
	    for (MultipartFile multipartFile : file) {
			String fileName = FileUpload.fileUp(multipartFile, filePath, saveFileName + "-"+multipartFile.getOriginalFilename());
			Thumbnails.of(filePath+File.separator+fileName).scale(0.3f).outputQuality(0.8f).toFile(Const.HAIRSTYLE_SAVE_COMPRESS_PATH+File.separator+fileName);
			System.out.println("multipartFile.getOriginalFilename():"+multipartFile.getOriginalFilename());
			fileNames.append(fileName);
			fileNames.append("@#");
	    }
	    //文件上传处理部分end
		pd.put("srcImagePath", fileNames.substring(0,fileNames.length()-2));
		pd.put("publishdateteime", DateUtil.sdfTimeString());
		PageData hairstyleidsearch = new PageData();
		hairstyleidsearch.put("hairstyleid", pd.get("id"));
		int count = hairstyleevaluateService.findCountById(hairstyleidsearch);
		pd.put("evaluatecount", count);
		this.hairstyleService.save(pd);
		return this.jsonContent("success", "保存成功");
	}
	
	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping(value = "/delete", produces = "application/json;charset=UTF-8")
	public String delete() throws Exception{
		logBefore(logger, "删除Hairstyle");
		PageData pd = this.getPageData();
		hairstyleService.delete(pd);
		return this.jsonContent("success", "删除成功");
	}
	
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping(value="/edit", produces = "application/json;charset=UTF-8")
	public String edit() throws Exception{
		logBefore(logger, "修改Hairstyle");
		PageData pd = this.getPageData();
		this.hairstyleService.edit(pd);
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
		logBefore(logger, "获取Hairstyle列表Json");
		PageData pd = this.getPageData();
		Page page = new Page();
		page.setCurrentPage(pd.getInt("page"));
		page.setShowCount(pd.getInt("rows"));
		page.setPd(pd);
		List<PageData> resultList = this.hairstyleService.listPage(page);// 分页查询列表
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
		PageData resultPD = this.hairstyleService.findById(pd);
		return this.jsonContent(resultPD);
	}
	
	
	/**
	 * 列表
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page){
		logBefore(logger, "列表Hairstyle");
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("business/hairstyle/list");
		return mv;
	}
	
	/**
	 * 去新增页面
	 */
	@RequestMapping(value="/toAdd")
	public ModelAndView toAdd(){
		logBefore(logger, "去新增Hairstyle页面");
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("business/hairstyle/form");
		return mv;
	}	
	
	/**
	 * 去修改页面
	 */
	@RequestMapping(value="/toEdit")
	public ModelAndView toEdit(){
		logBefore(logger, "去修改Hairstyle页面");
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("business/hairstyle/form");		
		return mv;
	}	
	
	/**
	 * 去查看页面
	 */
	@RequestMapping(value="/toDetail")
	public ModelAndView toDetail(){
		logBefore(logger, "去查看Hairstyle页面");
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("business/hairstyle/detail");		
		return mv;
	}
	
	/**
	 * 批量删除
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() {
		logBefore(logger, "批量删除Hairstyle");
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			pd = this.getPageData();
			List<PageData> pdList = new ArrayList<PageData>();
			String DATA_IDS = pd.getString("DATA_IDS");
			if(null != DATA_IDS && !"".equals(DATA_IDS)){
				String ArrayDATA_IDS[] = DATA_IDS.split(",");
				hairstyleService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, "导出Hairstyle到excel");
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			Map<String,Object> dataMap = new HashMap<String,Object>();
			List<String> titles = new ArrayList<String>();
			titles.add("发型表唯一ID");	//1
			titles.add("发型标题");	//2
			titles.add("发型发布的内容");	//3
			titles.add("发型发布人ID");	//4
			titles.add("发型发布时间");	//5
			titles.add("浏览次数");	//6
			titles.add("缩略图1路径");	//7
			titles.add("缩略图2路径");	//8
			titles.add("缩略图3路径");	//9
			dataMap.put("titles", titles);
			List<PageData> varOList = hairstyleService.listAll(pd);
			List<PageData> varList = new ArrayList<PageData>();
			for(int i=0;i<varOList.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", varOList.get(i).getString("id"));	//1
				vpd.put("var2", varOList.get(i).getString("styletitle"));	//2
				vpd.put("var3", varOList.get(i).getString("content"));	//3
				vpd.put("var4", varOList.get(i).getString("userid"));	//4
				vpd.put("var5", varOList.get(i).getString("publishtime"));	//5
				vpd.put("var6", varOList.get(i).get("viewcount").toString());	//6
				vpd.put("var7", varOList.get(i).getString("thumbnail1"));	//7
				vpd.put("var8", varOList.get(i).getString("thumbnail2"));	//8
				vpd.put("var9", varOList.get(i).getString("thumbnail3"));	//9
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
