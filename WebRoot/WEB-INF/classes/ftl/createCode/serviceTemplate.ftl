package com.Hairdressing.service.${packageName};

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.Hairdressing.dao.DaoSupport;
import com.Hairdressing.entity.Page;
import com.Hairdressing.util.PageData;


@Service("${objectNameFirstLower}Service")
public class ${objectName}Service {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/*
	* 新增
	*/
	public void save(PageData pd)throws Exception{
		dao.save("${objectName}Mapper.save", pd);
	}
	
	/*
	* 删除
	*/
	public void delete(PageData pd)throws Exception{
		dao.delete("${objectName}Mapper.delete", pd);
	}
	
	/*
	* 修改
	*/
	public void edit(PageData pd)throws Exception{
		dao.update("${objectName}Mapper.edit", pd);
	}
	
	/*
	*列表
	*/
	public List<PageData> listPage(Page page)throws Exception{
		return (List<PageData>)dao.findForList("${objectName}Mapper.datalistPage", page);
	}
	
	/*
	*列表(全部)
	*/
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("${objectName}Mapper.listAll", pd);
	}
	
	/*
	* 通过id获取数据
	*/
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("${objectName}Mapper.findById", pd);
	}
	
	/*
	* 批量删除
	*/
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("${objectName}Mapper.deleteAll", ArrayDATA_IDS);
	}
	
}

