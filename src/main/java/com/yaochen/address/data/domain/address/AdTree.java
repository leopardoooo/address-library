/*
 * Copyright © 2014 YAOCHEN Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.domain.address;

import java.util.Date;

public class AdTree {
    /** 地址ID */
    private Integer addrId;

    /** 地址名称（对外显示） */
    private String addrName;

    /** 地址级别 */
    private Integer addrLevel;

    /** 地址类型:城市地址、农村地址 */
    private String addrType;

    /** 地址用途(一般是客户地址最后一级填写)：城市小区、工业园厂房、城市酒店、小区商铺 */
    private String addrUse;

    /** 是否留空 */
    private String isBlank;

    /** 上级地址ID */
    private Integer addrParent;

    /** 地址私有名称（对内使用） */
    private String addrPrivateName;

    /** 地址全称（含父级名称） */
    private String addrFullName;

    /** 地址代码 */
    private String addrCode;

    /**  */
    private String countyId;

    /** 创建时间 */
    private Date createTime;

    /** 创建操作员 */
    private String createOptrId;

    /** 创建流水号 */
    private Integer createDoneCode;

    /** 状态:正常、待审核 */
    private String status;

    /**  */
    private String str1;

    /**  */
    private String str2;

    /**  */
    private String str3;

    /**  */
    private String str4;

    /**  */
    private String str5;

    public Integer getAddrId() {
        return addrId;
    }

    public void setAddrId(Integer addrId) {
        this.addrId = addrId;
    }

    public String getAddrName() {
        return addrName;
    }

    public void setAddrName(String addrName) {
        this.addrName = addrName == null ? null : addrName.trim();
    }

    public Integer getAddrLevel() {
        return addrLevel;
    }

    public void setAddrLevel(Integer addrLevel) {
        this.addrLevel = addrLevel;
    }

    public String getAddrType() {
        return addrType;
    }

    public void setAddrType(String addrType) {
        this.addrType = addrType == null ? null : addrType.trim();
    }

    public String getAddrUse() {
        return addrUse;
    }

    public void setAddrUse(String addrUse) {
        this.addrUse = addrUse == null ? null : addrUse.trim();
    }

    public String getIsBlank() {
        return isBlank;
    }

    public void setIsBlank(String isBlank) {
        this.isBlank = isBlank == null ? null : isBlank.trim();
    }

    public Integer getAddrParent() {
        return addrParent;
    }

    public void setAddrParent(Integer addrParent) {
        this.addrParent = addrParent;
    }

    public String getAddrPrivateName() {
        return addrPrivateName;
    }

    public void setAddrPrivateName(String addrPrivateName) {
        this.addrPrivateName = addrPrivateName == null ? null : addrPrivateName.trim();
    }

    public String getAddrFullName() {
        return addrFullName;
    }

    public void setAddrFullName(String addrFullName) {
        this.addrFullName = addrFullName == null ? null : addrFullName.trim();
    }

    public String getAddrCode() {
        return addrCode;
    }

    public void setAddrCode(String addrCode) {
        this.addrCode = addrCode == null ? null : addrCode.trim();
    }

    public String getCountyId() {
        return countyId;
    }

    public void setCountyId(String countyId) {
        this.countyId = countyId == null ? null : countyId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateOptrId() {
        return createOptrId;
    }

    public void setCreateOptrId(String createOptrId) {
        this.createOptrId = createOptrId == null ? null : createOptrId.trim();
    }

    public Integer getCreateDoneCode() {
        return createDoneCode;
    }

    public void setCreateDoneCode(Integer createDoneCode) {
        this.createDoneCode = createDoneCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getStr1() {
        return str1;
    }

    public void setStr1(String str1) {
        this.str1 = str1 == null ? null : str1.trim();
    }

    public String getStr2() {
        return str2;
    }

    public void setStr2(String str2) {
        this.str2 = str2 == null ? null : str2.trim();
    }

    public String getStr3() {
        return str3;
    }

    public void setStr3(String str3) {
        this.str3 = str3 == null ? null : str3.trim();
    }

    public String getStr4() {
        return str4;
    }

    public void setStr4(String str4) {
        this.str4 = str4 == null ? null : str4.trim();
    }

    public String getStr5() {
        return str5;
    }

    public void setStr5(String str5) {
        this.str5 = str5 == null ? null : str5.trim();
    }
}