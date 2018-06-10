package com.Hairdressing.entity.system;

import java.io.Serializable;

/**
 *
 * 类名称：User.java
 * @version 1.0
 */
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	private String userId;
	private String userName;
	private String password;
	private String businessId;
	private String fxsPhone;
	private String fxsQQorVX;
	private String fxsUser;
	private String loginId;
	private String diffFlag;
	private String headSculpturePath;
	private String createDate;
	private String delFlag;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public String getFxsPhone() {
		return fxsPhone;
	}
	public void setFxsPhone(String fxsPhone) {
		this.fxsPhone = fxsPhone;
	}
	public String getFxsQQorVX() {
		return fxsQQorVX;
	}
	public void setFxsQQorVX(String fxsQQorVX) {
		this.fxsQQorVX = fxsQQorVX;
	}
	public String getFxsUser() {
		return fxsUser;
	}
	public void setFxsUser(String fxsUser) {
		this.fxsUser = fxsUser;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getDiffFlag() {
		return diffFlag;
	}
	public void setDiffFlag(String diffFlag) {
		this.diffFlag = diffFlag;
	}
	public String getHeadSculpturePath() {
		return headSculpturePath;
	}
	public void setHeadSculpturePath(String headSculpturePath) {
		this.headSculpturePath = headSculpturePath;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
}
