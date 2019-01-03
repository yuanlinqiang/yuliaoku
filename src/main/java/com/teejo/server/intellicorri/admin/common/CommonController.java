package com.teejo.server.intellicorri.admin.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teejo.server.intellicorri.admin.common.beans.CommonResult;

public class CommonController {
	
	public Gson gson = new GsonBuilder().disableHtmlEscaping().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	
	public CommonResult cr = null;
	
	//创建Excel导出模板
	public Workbook createExcelTemplate(String[] titleNameArray) throws Exception {

		Workbook wb = null;
//		InputStream is = null;
		OutputStream os = null;
		try {	
			//使用Excel模板的方式
//			ServletContext sc = ContextLoader.getCurrentWebApplicationContext().getServletContext();
//			is = sc.getResourceAsStream("/template/excel/temp.xls");
//			wb = WorkbookFactory.create(is);
			
			//不使用Excel模板
			wb = new HSSFWorkbook();
            Sheet sheet = wb.createSheet();

            //设置标题样式
			CellStyle style = wb.createCellStyle();
		    style.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
		    style.setFillPattern(FillPatternType.BIG_SPOTS);//FillPatternType.SOLID_FOREGROUND
			style.setAlignment(HorizontalAlignment.CENTER);
			style.setFillForegroundColor(IndexedColors.OLIVE_GREEN.getIndex());
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			
			Font font = wb.createFont();    
			font.setFontHeightInPoints((short) 11);    
			font.setColor(IndexedColors.WHITE.getIndex());
			style.setFont(font);//选择需要用到的字体格式  
			Row row = sheet.getRow(0);
			if (row == null) {
				row = sheet.createRow(0);
			}
			for (int i = 0; i < titleNameArray.length; i++) {
				Cell cell = row.getCell(i);
				if (cell == null) {
					cell = row.createCell(i);
				}
				cell.setCellValue(titleNameArray[i]);
				cell.setCellStyle(style);
			}	
			os = new ByteArrayOutputStream();
			wb.write(os);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			is.close();
			os.close();
		}
		return wb;
	}
	
	/**
	 * 导出Excel
	 * List list:导出内容数据
	 * String[] titleNameArray:中文标题数组
	 * String[] fieldNameArray:字段名数组
	 */
	public InputStream exportExcelContent(List data,String[] titleNameArray,String[] fieldNameArray) throws Exception {
		InputStream is = null;
		ByteArrayOutputStream os = null;
		int startRowIndex = 1,startCellIndex = 0;
		int rowIndex = startRowIndex;
		Workbook wb = createExcelTemplate(titleNameArray);
		
		try {
			Sheet sheet = wb.getSheetAt(0);
			for (int i = 0; i < data.size(); i++) {
                Map map = gson.fromJson(gson.toJson(data.get(i)),HashMap.class);//各种对象转换为HashMap
				Row row = sheet.getRow(rowIndex);
				if (row == null) {
					row = sheet.createRow(rowIndex);
				}
				for (int j = 0; j < fieldNameArray.length; j++) {
					Cell cell = row.getCell(startCellIndex + j);
					if (cell == null) {
						cell = row.createCell(startCellIndex + j);
					}
					if (fieldNameArray[j] != ""
							&& map.containsKey(fieldNameArray[j])) {
						cell.setCellValue(this.toString(map.get(fieldNameArray[j])));
					} else {
						cell.setCellValue("");
					}
				}
				rowIndex++;
			}

			os = new ByteArrayOutputStream();
			wb.write(os);
			is = new ByteArrayInputStream(os.toByteArray());
			return is;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			os.close();
			is.close();
		}
		return is;
	}
	
	/**
	 * 获取Excel内容数据 用于从Excel导入
	 * @param fieldNameArray
	 * @throws Exception
	 */
	public List<Map<String,String>> getExcelContent(MultipartFile file,String[] fieldNameArray) throws Exception {
		int startRowIndex = 1;//开始读取的行索引
		int startCellIndex = 0;//开始读取的列索引
//		int endCellIndex = fieldNameArray.length;//读取结束的列索引

		InputStream inp = file.getInputStream();
		Workbook wb = WorkbookFactory.create(inp);
      //  Sheet sheet = wb.createSheet();
		Sheet sheet = wb.getSheetAt(0);
		
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Row row = sheet.getRow(startRowIndex);
		Cell cell = row.getCell(startCellIndex);
		int rowindex = 0;
		//cell.setCellType(CellType.STRING);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		while(row!=null && cell.getStringCellValue()!=null){
			Map<String,String> map = new HashMap<String,String>();
			for(int i=0;i<fieldNameArray.length;i++){
				cell = row.getCell(startCellIndex+i);
				String cellvalue = "";
				if(cell!=null){
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cellvalue = cell.getStringCellValue();
				}
				map.put(fieldNameArray[i],cellvalue);
			}
			list.add(map);
			rowindex++;
			row = sheet.getRow(startRowIndex+rowindex);
			if(row!=null){
				cell = row.getCell(startCellIndex);
				if(cell==null){
					break;
				}else{
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					if(cell.getStringCellValue()==null || "".equals(cell.getStringCellValue())){
						break;
					}
				}
			}
		}
		return list;
	}
	
	public String toString(Object obj){
		return obj==null?"":obj.toString();
	}
	
	//提取异常信息中的关键描述内容
	public  String getPointOfException(String msg){
//		System.err.println("=======================================");
//		System.err.println(e.getMessage());
//		System.err.println("=======================================");
		if(msg!=null && msg.length()>30){
			String[] msg_arr = msg.split(": ");
			msg = msg_arr[msg_arr.length-1];
		}
		return msg;
	}
	public CommonResult getCommonResult(boolean ok){
		CommonResult cr = new CommonResult();
		cr.setOk(ok);
        return cr;
	}
	
	public CommonResult getCommonResult(boolean ok,String msg){
		CommonResult cr = new CommonResult();
		cr.setOk(ok);
		cr.setMsg(msg);
        return cr;
	}
	
	public CommonResult getCommonResult(boolean ok,String msg,Object data){
		CommonResult cr = new CommonResult();
		cr.setOk(ok);
		cr.setMsg(msg);
		cr.setData(data);
        return cr;
	}
	

    public static Map<String, Object> objectToMap(Object obj) throws Exception {      
        if(obj == null){      
            return null;      
        }     
    
        Map<String, Object> map = new HashMap<String, Object>();      
    
        Field[] declaredFields = obj.getClass().getDeclaredFields();      
        for (Field field : declaredFields) {      
            field.setAccessible(true);    
            map.put(field.getName(), field.get(obj));    
        }      
    
        return map;    
    }     
    
}
