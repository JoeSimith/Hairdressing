package com.Hairdressing.service.system.user;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.Hairdressing.dao.DaoSupport;
import com.Hairdressing.entity.Page;
import com.Hairdressing.entity.system.User;
import com.Hairdressing.util.PageData;

@Service("userService")
public class UserService {
	
	@Resource(name = "daoSupport")
	private DaoSupport dao;

	/*
	* 新增
	*/
	public void save(PageData pd)throws Exception{
		dao.save("UserMapper.save", pd);
	}
	
	/*
	* 删除
	*/
	public void delete(PageData pd)throws Exception{
		dao.delete("UserMapper.delete", pd);
	}
	
	/*
	* 修改
	*/
	public void edit(PageData pd)throws Exception{
		dao.update("UserMapper.edit", pd);
	}
	
	/*
	*列表
	*/
	public List<PageData> listPage(Page page)throws Exception{
		return (List<PageData>)dao.findForList("UserMapper.datalistPage", page);
	}
	
	/*
	*列表(全部)
	*/
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("UserMapper.listAll", pd);
	}
	
	/*
	* 通过id获取数据
	*/
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("UserMapper.findById", pd);
	}
	
	/*
	* 通过手机号查询用户是否存在
	*/
	public Integer testPhoneNumber(PageData pd)throws Exception{
		return (Integer)dao.findForObject("UserMapper.testPhoneNumber", pd);
	}
	
	/*
	* 批量删除
	*/
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("UserMapper.deleteAll", ArrayDATA_IDS);
	}
	/*
	 * 通过用户名密码获取数据
	 */
	public User loginUser(PageData pd)throws Exception{
		return (User)dao.findForObject("UserMapper.loginUser", pd);
	}

}
