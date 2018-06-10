package com.Hairdressing.service.business;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.Hairdressing.dao.DaoSupport;
import com.Hairdressing.entity.Page;
import com.Hairdressing.util.PageData;


@Service("rebackService")
public class RebackService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/*
	* 新增
	*/
	public void save(PageData pd)throws Exception{
		dao.save("RebackMapper.save", pd);
	}
	
	/*
	* 删除
	*/
	public void delete(PageData pd)throws Exception{
		dao.delete("RebackMapper.delete", pd);
	}
	
	/*
	* 修改
	*/
	public void edit(PageData pd)throws Exception{
		dao.update("RebackMapper.edit", pd);
	}
	
	/*
	*列表
	*/
	public List<PageData> listPage(Page page)throws Exception{
		return (List<PageData>)dao.findForList("RebackMapper.datalistPage", page);
	}
	
	/*
	*列表(全部)
	*/
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("RebackMapper.listAll", pd);
	}
	
	/*
	* 通过id获取数据
	*/
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("RebackMapper.findById", pd);
	}
	
	/*
	* 批量删除
	*/
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("RebackMapper.deleteAll", ArrayDATA_IDS);
	}
	
}

