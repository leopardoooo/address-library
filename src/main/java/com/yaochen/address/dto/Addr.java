package com.yaochen.address.dto;

import java.util.ArrayList;
import java.util.List;

import com.easyooo.framework.common.util.CglibUtil;
import com.yaochen.address.common.StringHelper;
import com.yaochen.address.data.domain.address.AdTree;

public class Addr extends AdTree {
	public List<Addr> children;
	private String district;//区
	private String region;//工业园
	private String road;//路
	private String roadNum;//路号
	private String village;//小区
	private String building ;//大厦，生产队...
	private  String street;//街道
	private String department ;//门牌号,单元
	private String houseNum ;//房号
	
	private String districtCode;//区
	private String regionCode;//工业园
	private String roadCode;//路
	private String roadNumCode;//路号
	private String villageCode;//小区
	private String buildingCode;//大厦，生产队...
	private  String streetCode;//街道
	private String departmentCode;//门牌号,单元
	private String houseNumCode;//房号
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getRoad() {
		return road;
	}
	public void setRoad(String road) {
		this.road = road;
	}
	public String getRoadNum() {
		return roadNum;
	}
	public void setRoadNum(String roadNum) {
		this.roadNum = roadNum;
	}
	public String getVillage() {
		return village;
	}
	public void setVillage(String village) {
		this.village = village;
	}
	public String getBuilding() {
		return building;
	}
	public void setBuilding(String building) {
		this.building = building;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getHouseNum() {
		return houseNum;
	}
	public void setHouseNum(String houseNum) {
		this.houseNum = houseNum;
	}
	public String getDistrictCode() {
		return districtCode;
	}
	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}
	public String getRegionCode() {
		return regionCode;
	}
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
	public String getRoadCode() {
		return roadCode;
	}
	public void setRoadCode(String roadCode) {
		this.roadCode = roadCode;
	}
	public String getRoadNumCode() {
		return roadNumCode;
	}
	public void setRoadNumCode(String roadNumCode) {
		this.roadNumCode = roadNumCode;
	}
	public String getVillageCode() {
		return villageCode;
	}
	public void setVillageCode(String villageCode) {
		this.villageCode = villageCode;
	}
	public String getBuildingCode() {
		return buildingCode;
	}
	public void setBuildingCode(String buildingCode) {
		this.buildingCode = buildingCode;
	}
	public String getStreetCode() {
		return streetCode;
	}
	public void setStreetCode(String streetCode) {
		this.streetCode = streetCode;
	}
	public String getDepartmentCode() {
		return departmentCode;
	}
	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}
	public String getHouseNumCode() {
		return houseNumCode;
	}
	public void setHouseNumCode(String houseNumCode) {
		this.houseNumCode = houseNumCode;
	}
	public Addr() {
		super();
	}
	
	public Addr(AdTree addr) {
		CglibUtil.copy(addr, this);
		String str1 = addr.getStr1();
		String addrPrivateName = addr.getAddrPrivateName();

		/*
		List<String> names = getNames(str1);
		List<String> codes = getNames(addrPrivateName);
		this.setDistrict(names.get(0));
		this.setRegion(names.get(1));
		this.setRoad(names.get(2));
		this.setRoadNum(names.get(3));
		this.setVillage(names.get(4));
		this.setBuilding(names.get(5));
		this.setStreet(names.get(6));
		this.setDepartment(names.get(7));
		this.setHouseNum(names.get(8));
		this.setDistrictCode(codes.get(1));
		this.setRegionCode(codes.get(2));
		this.setRoadCode(codes.get(3));
		this.setRoadNumCode(codes.get(4));
		this.setVillageCode(codes.get(5));
		this.setBuildingCode(codes.get(6));
		this.setStreetCode(codes.get(7));
		this.setDepartmentCode(codes.get(8));
		this.setHouseNumCode(codes.get(9));
		*/
	}
	
	private List<String> getNames(String str1) {
		List<String> list = new ArrayList<String>();
		if(StringHelper.isEmpty(str1)){
			str1 = "";
		}
		String[] split = str1.split("/");
		for (String string : split) {
			list.add(string);
		}
		int size = 32-list.size();
		while(size >0){
			list.add(null);
			size--;
		}
		return list;
	}
	public List<Addr> getChildren() {
		return children;
	}
	public void setChildren(List<Addr> children) {
		this.children = children;
	}
}
