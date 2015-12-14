package com.yaochen.address.support;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSON;
import com.easyooo.framework.common.util.Grouping;
import com.easyooo.framework.common.util.ListUtil;
import com.yaochen.address.common.CollectionHelper;
import com.yaochen.address.common.StringHelper;
import com.yaochen.address.dto.db.ColumnInfo;
import com.yaochen.address.dto.db.TableInfo;

public class DatabaseInfoHolder  implements InitializingBean,ApplicationContextAware {
	private static final Logger logger = Logger.getLogger(DatabaseInfoHolder.class);
	
	private String dbName;
	private ApplicationContext cxt;
	
	private static final Map<String, TableInfo> tables = new HashMap<String, TableInfo>();

	public static Map<String, TableInfo> getTables() {
		return tables;
	}

	public void init(List<ColumnInfo> columns) {
		if(CollectionHelper.isEmpty(columns)){
			return;
		}
		
		Grouping<String, ColumnInfo> group = new Grouping<String, ColumnInfo>() {
			public String getGroupingKey(ColumnInfo element) {
				return element.getTableName();
			}
		};
		
		Map<String, List<ColumnInfo>> map = ListUtil.grouping(columns, group);
		for (Entry<String, List<ColumnInfo>> entry : map.entrySet()) {
			String key = StringHelper.upperFirst(StringHelper.camellize(entry.getKey()));
			TableInfo table = new TableInfo(key, entry.getValue());
			tables.put(key, table);
		}
		
		logger.info("初始化完成.");
		logger.info(JSON.toJSONString(tables));
	}
	
	public static <T> TableInfo getTableInfo(Class<T> klass){
		String key = klass.getSimpleName();
		return tables.get(key);
	}
	
	public static <T> TableInfo getTableInfo(T t){
		if(t == null){
			throw new IllegalArgumentException("传入的bean不能为空.");
		}
		return getTableInfo(t.getClass());
	}

	private void customizeInit() {
		
		DruidDataSource ds = cxt.getBean(DruidDataSource.class);
		String sql = "SELECT t.TABLE_SCHEMA,t.TABLE_NAME,t.COLUMN_NAME,t.COLUMN_COMMENT from information_schema.`COLUMNS` t where t.TABLE_SCHEMA = 'address' limit 10 ";
		
		List<ColumnInfo> columns = new ArrayList<ColumnInfo>();
		
		try (DruidPooledConnection dsConn = ds.getConnection(); Connection conn = dsConn.getConnection(); PreparedStatement pst = conn.prepareStatement(sql); ResultSet rst = pst.executeQuery();) {
			while (rst.next()) {
				String columnName = rst.getString("column_name").toLowerCase();
				String comment = rst.getString("column_comment");
				String tableName = rst.getString("TABLE_NAME");
				ColumnInfo col = new ColumnInfo(columnName, comment);
				col.setTableName(tableName.toLowerCase());
				columns.add(col);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("初始化数据出错,未能获得项目相关的表信息.");
			System.exit(1);
		}
		this.init(columns);
	}
	
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDbName() {
		return dbName;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if(StringHelper.isEmpty(dbName)){
			throw new BeanCreationException("dbName is required!");
		}
		customizeInit();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.cxt = applicationContext;
	}
	
}
