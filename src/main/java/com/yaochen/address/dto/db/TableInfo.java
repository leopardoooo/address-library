package com.yaochen.address.dto.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yaochen.address.common.StringHelper;


public class TableInfo {
	private String tableName;
	private String className;
	private List<ColumnInfo> columns = new ArrayList<ColumnInfo>();
	private Map<String, ColumnInfo> columnMap = new HashMap<String, ColumnInfo>();
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public List<ColumnInfo> getColumns() {
		return columns;
	}
	public void setColumns(List<ColumnInfo> columns) {
		this.columns = columns;
	}
	public TableInfo() {
		super();
	}
	public TableInfo(String tableName, List<ColumnInfo> columns) {
		super();
		this.tableName = tableName;
		this.className = StringHelper.upperFirst(StringHelper.camellize(tableName));
		this.columns = columns;
		for (ColumnInfo col : columns) {
			String cn = col.getPropertyName();
			this.columnMap.put(cn, col);
		}
	}
	
	public ColumnInfo getColumnInfo(String propertyName){
		return this.columnMap.get(propertyName);
	}
	
}
