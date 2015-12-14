package com.yaochen.test.wps;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Font;
import org.junit.Ignore;
import org.junit.Test;

public class ExcelTest {
	
	
	@Test
	public void testMain() throws Exception {
		        List<XlsDto> list = readXls();
		         
		        try {
		            XlsDto2Excel.xlsDto2Excel(list);
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		        for (int i = 0; i < list.size(); i++) {
		        	XlsDto xls = (XlsDto) list.get(i);
		            System.out.println(xls.getXh() + "    " + xls.getXm() + "    "
		                    + xls.getYxsmc() + "    " + xls.getKcm() + "    "
		                    + xls.getCj());
		        }
		 
	}
	
	@Test
	@Ignore
	public void testWrite() throws Exception {
		HSSFWorkbook wb = new HSSFWorkbook();  //--->创建了一个excel文件  
        HSSFSheet sheet = wb.createSheet("理财资金报表");   //--->创建了一个工作簿  
        HSSFDataFormat format= wb.createDataFormat();   //--->单元格内容格式  
        sheet.setColumnWidth((short)3, 20* 256);    //---》设置单元格宽度，因为一个单元格宽度定了那么下面多有的单元格高度都确定了所以这个方法是sheet的  
        sheet.setColumnWidth((short)4, 20* 256);    //--->第一个参数是指哪个单元格，第二个参数是单元格的宽度  
        sheet.setDefaultRowHeight((short)300);    // ---->有得时候你想设置统一单元格的高度，就用这个方法  
          
        //样式1  
        HSSFCellStyle style = wb.createCellStyle(); // 样式对象  
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平  
        //设置标题字体格式  
        Font font = wb.createFont();     
        //设置字体样式   
        font.setFontHeightInPoints((short)20);   //--->设置字体大小  
        font.setFontName("Courier New");   //---》设置字体，是什么类型例如：宋体  
/*
        font1.setItalic(true);     //--->设置是否是加粗  
		style1.setFont(font1);     //--->将字体格式加入到style1中     
        //style1.setFillForegroundColor(IndexedColors.DARK_YELLOW.getIndex());  
        //style1.setFillPattern(CellStyle.SOLID_FOREGROUND);设置单元格颜色  
        style1.setWrapText(true);   //设置是否能够换行，能够换行为true  
        style1.setBorderBottom((short)1);   //设置下划线，参数是黑线的宽度  
        style1.setBorderLeft((short)1);   //设置左边框  
        style1.setBorderRight((short)1);   //设置有边框  
        style1.setBorderTop((short)1);   //设置下边框  
        style4.setDataFormat(format.getFormat("￥#,##0"));    //--->设置为单元格内容为货币格式  
         
        style5.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));    //--->设置单元格内容为百分数格式  
          */
          
        //表格第一行  
        HSSFRow row1 = sheet.createRow(0);   //--->创建一行  
        // 四个参数分别是：起始行，起始列，结束行，结束列  
        sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) 15));  
        row1.setHeightInPoints(25);  
        HSSFCell cell1 = row1.createCell((short)0);   //--->创建一个单元格  
          
        cell1.setCellStyle(style);  
        cell1.setCellValue("总公司资金运用日报明细表（理财资金）");  
          
        //表格第二行  
        sheet.addMergedRegion(new Region(1,(short)0,1,(short)15));  
        HSSFRow row2 = sheet.createRow(1);  
        HSSFCell cell2 = row2.createCell((short)0);  
        cell2.setCellValue("报告日期："+new Date());  
//        cell2.setCellStyle(style2);  
          
        //表格第三行  
        sheet.addMergedRegion(new Region(2,(short)0,2,(short)15));  
        HSSFRow row3 = sheet.createRow(2);  
        HSSFCell cell3 = row3.createCell((short)0);  
        cell3.setCellValue("交易日期："+new Date());  
//        cell3.setCellStyle(style2);  
		
	}
	
	
	 /**
     * 读取xls文件内容
     * 
     * @return List<XlsDto>对象
     * @throws IOException
     *             输入/输出(i/o)异常
     */
    private List<XlsDto> readXls() throws IOException {
        InputStream is = new FileInputStream("pldrxkxxmb.xls");
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        XlsDto xlsDto = null;
        List<XlsDto> list = new ArrayList<XlsDto>();
        // 循环工作表Sheet
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // 循环行Row
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow == null) {
                    continue;
                }
                xlsDto = new XlsDto();
                // 循环列Cell
                // 0学号 1姓名 2学院 3课程名 4 成绩
                // for (int cellNum = 0; cellNum <=4; cellNum++) {
                HSSFCell xh = hssfRow.getCell(0);
                if (xh == null) {
                    continue;
                }
                xlsDto.setXh(getValue(xh));
                HSSFCell xm = hssfRow.getCell(1);
                if (xm == null) {
                    continue;
                }
                xlsDto.setXm(getValue(xm));
                HSSFCell yxsmc = hssfRow.getCell(2);
                if (yxsmc == null) {
                    continue;
                }
                xlsDto.setYxsmc(getValue(yxsmc));
                HSSFCell kcm = hssfRow.getCell(3);
                if (kcm == null) {
                    continue;
                }
                xlsDto.setKcm(getValue(kcm));
                HSSFCell cj = hssfRow.getCell(4);
                if (cj == null) {
                    continue;
                }
                xlsDto.setCj(Float.parseFloat(getValue(cj)));
                list.add(xlsDto);
            }
        }
        return list;
    }
 
    /**
     * 得到Excel表中的值
     * 
     * @param hssfCell
     *            Excel中的每一个格子
     * @return Excel中每一个格子中的值
     */
    @SuppressWarnings("static-access")
    private String getValue(HSSFCell hssfCell) {
        if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
            // 返回布尔类型的值
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
            // 返回数值类型的值
            return String.valueOf(hssfCell.getNumericCellValue());
        } else {
            // 返回字符串类型的值
            return String.valueOf(hssfCell.getStringCellValue());
        }
    }
	
}
