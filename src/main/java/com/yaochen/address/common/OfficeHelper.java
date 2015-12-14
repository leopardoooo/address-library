package com.yaochen.address.common;

import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.easyooo.framework.common.util.CglibUtil;


public class OfficeHelper {
	
	public static <T> HSSFWorkbook generateExcel(List<T> list ,String [] properties,String [] firstLine) throws Exception{
		
		HSSFWorkbook hwb = new HSSFWorkbook();
		
		HSSFSheet sheet = hwb.createSheet("操作日志导出数据");
		int rows = 0;
		
		if(firstLine != null && firstLine.length == properties.length){
			HSSFRow firstrow = sheet.createRow(rows); // 下标为0的行开始
			HSSFCell[] firstcell = new HSSFCell[properties.length];
			for(int index = 0;index < properties.length;index++){
				firstcell[index] = firstrow.createCell(index);
				firstcell[index].setCellValue(new HSSFRichTextString(firstLine[index]));
			}
			rows ++;
		}
		
		
		for (T t : list) {
			Object[] values = CglibUtil.getPropertyValue(t, properties);
			int index = 0;
			// 创建一行
			HSSFRow row = sheet.createRow(rows );
			for (Object val : values) {
				String str = getFieldStringValue(val);
				HSSFCell xh = row.createCell(index);
				xh.setCellValue(str);
				index ++;
			}
            // 得到要插入的每一条记录
            rows ++;
		}
		
		return hwb;
	}

	private static String getFieldStringValue(Object val) {
		if(val == null){
			return "";
		}
		if(Date.class.isAssignableFrom(val.getClass())){
			Date date = (Date) val;
			return DateHelper.format(date, DateHelper.FORMAT_TIME);
		}
		return val.toString();
	}
	
}
