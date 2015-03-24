package com.yaochen.address.dto;

import java.util.List;

public class UserInSession {
	private List<SystemFunction> SystemFunction;
	private String Duty;
	private String HasWork;
	private String AreaOID;
	private String AreaName;
	private String HumanName;
	private String CompanyOID;
	private String CompanyName;
	private String DepartmentOID;
	private String DepartmentName;
	private String PYCode;
	private String UserOID;
	private String UserName;
	private String Password;
	private String DelFlag;
	private String Img;
	private String Mobile;
	private String PublicMobile;
	private String OfficeTelphone;
	private String Signature;
	private String ImgUrl;
	/** 用户在本系统的角色 **/
	private Integer roleIdInSys;
	/**允许操作的最高级别**/
	private Integer maxLevelAllowed;
	
	public List<SystemFunction> getSystemFunction() {
		return SystemFunction;
	}
	public void setSystemFunction(List<SystemFunction> systemFunction) {
		SystemFunction = systemFunction;
	}
	public String getDuty() {
		return Duty;
	}
	public void setDuty(String duty) {
		Duty = duty;
	}
	public String getHasWork() {
		return HasWork;
	}
	public void setHasWork(String hasWork) {
		HasWork = hasWork;
	}
	public String getAreaOID() {
		return AreaOID;
	}
	public void setAreaOID(String areaOID) {
		AreaOID = areaOID;
	}
	public String getAreaName() {
		return AreaName;
	}
	public void setAreaName(String areaName) {
		AreaName = areaName;
	}
	public String getHumanName() {
		return HumanName;
	}
	public void setHumanName(String humanName) {
		HumanName = humanName;
	}
	public String getCompanyOID() {
		return CompanyOID;
	}
	public void setCompanyOID(String companyOID) {
		CompanyOID = companyOID;
	}
	public String getCompanyName() {
		return CompanyName;
	}
	public void setCompanyName(String companyName) {
		CompanyName = companyName;
	}
	public String getDepartmentOID() {
		return DepartmentOID;
	}
	public void setDepartmentOID(String departmentOID) {
		DepartmentOID = departmentOID;
	}
	public String getDepartmentName() {
		return DepartmentName;
	}
	public void setDepartmentName(String departmentName) {
		DepartmentName = departmentName;
	}
	public String getPYCode() {
		return PYCode;
	}
	public void setPYCode(String pYCode) {
		PYCode = pYCode;
	}
	public String getUserOID() {
		return UserOID;
	}
	public void setUserOID(String userOID) {
		UserOID = userOID;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getDelFlag() {
		return DelFlag;
	}
	public void setDelFlag(String delFlag) {
		DelFlag = delFlag;
	}
	public String getImg() {
		return Img;
	}
	public void setImg(String img) {
		Img = img;
	}
	public String getMobile() {
		return Mobile;
	}
	public void setMobile(String mobile) {
		Mobile = mobile;
	}
	public String getPublicMobile() {
		return PublicMobile;
	}
	public void setPublicMobile(String publicMobile) {
		PublicMobile = publicMobile;
	}
	public String getOfficeTelphone() {
		return OfficeTelphone;
	}
	public void setOfficeTelphone(String officeTelphone) {
		OfficeTelphone = officeTelphone;
	}
	public String getSignature() {
		return Signature;
	}
	public void setSignature(String signature) {
		Signature = signature;
	}
	public String getImgUrl() {
		return ImgUrl;
	}
	public void setImgUrl(String imgUrl) {
		ImgUrl = imgUrl;
	}
	public Integer getMaxLevelAllowed() {
		return maxLevelAllowed;
	}
	public void setMaxLevelAllowed(Integer maxLevelAllowed) {
		this.maxLevelAllowed = maxLevelAllowed;
	}
	public Integer getRoleIdInSys() {
		return roleIdInSys;
	}
	public void setRoleIdInSys(Integer roleIdInSys) {
		this.roleIdInSys = roleIdInSys;
	}
	
}
