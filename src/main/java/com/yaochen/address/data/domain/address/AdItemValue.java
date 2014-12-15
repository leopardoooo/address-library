/*
 * Copyright © 2014 YAOCHEN Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.domain.address;

public class AdItemValue {
    /**  */
    private String itemKey;

    /**  */
    private String itemValue;

    /**  */
    private String itemName;

    /**  */
    private String itemDesc;

    /**  */
    private Integer itemIdx;

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey == null ? null : itemKey.trim();
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue == null ? null : itemValue.trim();
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName == null ? null : itemName.trim();
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc == null ? null : itemDesc.trim();
    }

    public Integer getItemIdx() {
        return itemIdx;
    }

    public void setItemIdx(Integer itemIdx) {
        this.itemIdx = itemIdx;
    }
}