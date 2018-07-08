package com.Hairdressing.service.business;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.Hairdressing.dao.DaoSupport;
import com.Hairdressing.entity.Page;
import com.Hairdressing.util.PageData;


@Service("hairstyleevaluateService")
public class HairstyleevaluateService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/*
	* 新增
	*/
	public void save(PageData pd)throws Exception{
		dao.save("HairstyleevaluateMapper.save", pd);
	}
	
	/*
	* 删除
	*/
	public void delete(PageData pd)throws Exception{
		dao.delete("HairstyleevaluateMapper.delete", pd);
	}
	
	/*
	* 修改
	*/
	public void edit(PageData pd)throws Exception{
		dao.update("HairstyleevaluateMapper.edit", pd);
	}
	
	/*
	*列表
	*/
	public List<PageData> listPage(Page page)throws Exception{
		return (List<PageData>)dao.findForList("HairstyleevaluateMapper.datalistPage", page);
	}
	
	/*
	*列表(全部)
	*/
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("HairstyleevaluateMapper.listAll", pd);
	}
	
	/*
	* 通过id获取数据
	*/
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("HairstyleevaluateMapper.findById", pd);
	}
	/*
	* 通过id获取评价行数
	*/
	public int findCountById(PageData pd)throws Exception{
		return (int)dao.findForObject("HairstyleevaluateMapper.findCountById", pd);
	}
	
	/*
	* 批量删除
	*/
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("HairstyleevaluateMapper.deleteAll", ArrayDATA_IDS);
	}
	
}

