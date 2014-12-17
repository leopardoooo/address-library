package com.yaochen.address.dto;

public class SystemFunction {
	private Integer FunctionOID;
	private Integer UserOID;
	private String UserName;
	private Integer RoleOID;
	private String FunctionName;
	public Integer getFunctionOID() {
		return FunctionOID;
	}
	public void setFunctionOID(Integer functionOID) {
		FunctionOID = functionOID;
	}
	public Integer getUserOID() {
		return UserOID;
	}
	public void setUserOID(Integer userOID) {
		UserOID = userOID;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public Integer getRoleOID() {
		return RoleOID;
	}
	public void setRoleOID(Integer roleOID) {
		RoleOID = roleOID;
	}
	public String getFunctionName() {
		return FunctionName;
	}
	public void setFunctionName(String functionName) {
		FunctionName = functionName;
	}
}
