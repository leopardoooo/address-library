package com.yaochen.address.dto.db;

import com.yaochen.address.common.StringHelper;

public class ColumnInfo {
	private String tableName;
	private String columnName;
	private String comment;
	private String propertyName;
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public ColumnInfo() {
		super();
	}
	public ColumnInfo(String columnName, String comment) {
		super();
		this.columnName = columnName;
		this.comment = comment;
		this.propertyName = StringHelper.camellize(columnName);
	}
	
}
