package com.Hairdressing.util;

import antlr.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* 导出到EXCEL
* 类名称：ObjectExcelView.java
* @version 1.0
 */
public class ObjectExcelJcsjView extends AbstractExcelView{
	public String fileTile;
	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		Date date = new Date();
		String filename = "日期_" + Tools.date2Str(date, "yyyyMMddHHmmss");
		filename = URLEncoder.encode(filename, "UTF-8");
		HSSFSheet sheet;
		HSSFCell cell;
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename="+filename+".xls");
		sheet = workbook.createSheet("sheet1");

		List<String> titles = (List<String>) model.get("titles");


		List<List<String>> inputList = (List<List<String>>) model.get("inputList");

		HSSFCellStyle headerStyle = workbook.createCellStyle(); //标题样式
		headerStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		HSSFFont headerFont = workbook.createFont();	//标题字体
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headerFont.setFontHeightInPoints((short)11);
		headerStyle.setFont(headerFont);
		short width = 20,height=25*20;
		sheet.setDefaultColumnWidth(width);

		HSSFDataFormat format = workbook.createDataFormat();
		headerStyle.setDataFormat(format.getFormat("@"));

		headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);//下边框
		headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
		headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
		headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框

		//监测单位进行单元格合并
//		sheet.addMergedRegion(new CellRangeAddress(4,4,1,5));

		int inputRow = 0;
		int inputCol = 0;

		/*
		组装输入框
		 */
		for (List<String> strings : inputList) {
			inputCol = 0;
			for (String string : strings) {
				cell = getCell(sheet, inputRow, inputCol);
				if (org.apache.commons.lang.StringUtils.isBlank(string)) {
					HSSFCellStyle cellStyle1 = cell.getCellStyle();
					cellStyle1.setAlignment(HSSFCellStyle.ALIGN_LEFT);
					cell.setCellStyle(headerStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}
				setText(cell, string);
				inputCol++;
			}
			sheet.getRow(inputRow).setHeight(height);
			inputRow++;
		}

		/*
		组装行标题
		 */
		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//		inputRow += 1;
		inputCol = 0;
		for (String title : titles) {
			cell = getCell(sheet, inputRow, inputCol);
			cell.setCellStyle(headerStyle);
			setText(cell,title);
			sheet.getRow(inputRow).setHeight(height);
			inputCol++;
		}
		inputRow++;


		/*
		 组装输入列表
		 */
		HSSFCellStyle contentStyle = workbook.createCellStyle(); //内容样式
		contentStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		List<List<String>> varList = (List<List<String>>) model.get("varList");
		for (List<String> strings : varList) {
			inputCol = 0;
			for (String string : strings) {
				cell = getCell(sheet, inputRow, inputCol);
				cell.setCellStyle(headerStyle);
				setText(cell, string);
				inputCol++;
			}
			sheet.getRow(inputRow).setHeight(height);
			inputRow++;
		}


//		List<PageData> varList = (List<PageData>) model.get("varList");
//		int varCount = varList.size();
//		for(int i=0; i<varCount; i++){
//			PageData vpd = varList.get(i);
//			for(int j=0;j<len;j++){
//				String varstr = vpd.getString("var"+(j+1)) != null ? vpd.getString("var"+(j+1)) : "";
//				cell = getCell(sheet, inputRow + i+1, j);
//				cell.setCellStyle(contentStyle);
//				setText(cell,varstr);
//			}
//			inputRow++;
//			sheet.getRow(inputRow).setHeight(height);
//
//		}

	}

}
