package br.edu.fatecsorocaba.system.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
//import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.edu.fatecsorocaba.system.model.Patrimony;

public class ExcelGenerator {
	public static final short EXCEL_COLUMN_WIDTH_FACTOR = 256; 
	public static final int UNIT_OFFSET_LENGTH = 7; 
	public static final int[] UNIT_OFFSET_MAP = new int[] { 0, 36, 73, 109, 146, 182, 219 };
	
	public static short pixel2WidthUnits(int pxs) {
	    short widthUnits = (short) (EXCEL_COLUMN_WIDTH_FACTOR * (pxs / UNIT_OFFSET_LENGTH)); 
	    widthUnits += UNIT_OFFSET_MAP[(pxs % UNIT_OFFSET_LENGTH)];  
	    return widthUnits; 
	} 
 
	public static ByteArrayInputStream patrimoniesToExcel(List<Patrimony> patrimonies) throws IOException {
		String[] COLUMNs = {"Localização", "Número de Patrimônio", "Codigo do Processo de Aquisição", "Item", "Nota Fiscal",
			   "Marca", "Modelo", "Número de Série", "Informações Complementares", "Valor",
			   "Modalidade de Aquisição"};
		int[] WIDTHs = {27, 13, 9, 38, 9, 13, 14,  16, 57, 16, 13};
		try(Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();){
//		   	CreationHelper createHelper = workbook.getCreationHelper();
	  
			Sheet sheet = workbook.createSheet("patrimonios");
	  
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setColor(IndexedColors.BLACK.getIndex());
			headerFont.setFontName("Arial");
			headerFont.setFontHeightInPoints((short)10);
		   
			Font cellFont = workbook.createFont();
			cellFont.setColor(IndexedColors.BLACK.getIndex());
			cellFont.setFontName("Arial");
			cellFont.setFontHeightInPoints((short)10);
	  
			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);
			headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headerCellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
			headerCellStyle.setBorderBottom(BorderStyle.THIN);
		   	headerCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		   	headerCellStyle.setBorderLeft(BorderStyle.THIN);
		   	headerCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		   	headerCellStyle.setBorderRight(BorderStyle.THIN);
		   	headerCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		   	headerCellStyle.setBorderTop(BorderStyle.THIN);
		   	headerCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
		   
		   	CellStyle rowCellStyle = workbook.createCellStyle();
		   	rowCellStyle.setFont(cellFont);
		   	rowCellStyle.setWrapText(true);
	  
		   	// Row for Header
		   	Row headerRow = sheet.createRow(1);
		   	
		   	// Header
		   	for (int col = 0; col < COLUMNs.length; col++) {
		   		Cell cell = headerRow.createCell(col);
		   		cell.setCellValue(COLUMNs[col]);
		   		cell.setCellStyle(headerCellStyle);
		   	}
	  
		   	int rowIdx = 2;
		   	for (Patrimony patrimony : patrimonies) {
		   		int cellindex = 0;
		   		Row row = sheet.createRow(rowIdx++);
		   		
		   		if (patrimony.getLocation() != null) 
		   			row.createCell(cellindex).setCellValue(patrimony.getLocation().getDescription());
		   		else
		   			row.createCell(cellindex).setCellValue("");
		   		row.getCell(cellindex++).setCellStyle(rowCellStyle);
		   		//
		   		row.createCell(cellindex).setCellValue(patrimony.getPatrimonyId());
		   		row.getCell(cellindex++).setCellStyle(rowCellStyle);
		   		//
		   		if (patrimony.getAcquisitionProcessId() != null)
		   			row.createCell(cellindex).setCellValue(patrimony.getAcquisitionProcessId());
		   		else
		   			row.createCell(cellindex).setCellValue("");
		   		row.getCell(cellindex++).setCellStyle(rowCellStyle);
		   		//
		   		if (patrimony.getDescription() != null)
		   			row.createCell(cellindex).setCellValue(patrimony.getDescription());
		   		else
		   			row.createCell(cellindex).setCellValue("");
		   		row.getCell(cellindex++).setCellStyle(rowCellStyle);
		   		//
		   		if (patrimony.getCommercialInvoice() != null)
		   			row.createCell(cellindex).setCellValue(patrimony.getCommercialInvoice());
		   		else
		   			row.createCell(cellindex).setCellValue("");
		   		row.getCell(cellindex++).setCellStyle(rowCellStyle);
		   		//
		   		if (patrimony.getBrand() != null)
		   			row.createCell(cellindex).setCellValue(patrimony.getBrand());
		   		else
		   			row.createCell(cellindex).setCellValue("");
		   		row.getCell(cellindex++).setCellStyle(rowCellStyle);
		   		//
		   		if (patrimony.getModel() != null)
		   			row.createCell(cellindex).setCellValue(patrimony.getModel());
		   		else
		   			row.createCell(cellindex).setCellValue("");
		   		row.getCell(cellindex++).setCellStyle(rowCellStyle);
		   		//
		   		if (patrimony.getSerialNumber() != null)
		   			row.createCell(cellindex).setCellValue(patrimony.getSerialNumber());
		   		else
		   			row.createCell(cellindex).setCellValue("");
		   		row.getCell(cellindex++).setCellStyle(rowCellStyle);
		   		//
		   		if (patrimony.getAdditionalInformation() != null)
		   			row.createCell(cellindex).setCellValue(patrimony.getAdditionalInformation());
		   		else
		   			row.createCell(cellindex).setCellValue("");
		   		row.getCell(cellindex++).setCellStyle(rowCellStyle);
		   		//
		   		if (patrimony.getValue() != null)
		   			row.createCell(cellindex).setCellValue(patrimony.getValue().toString());
		   		else
		   			row.createCell(cellindex).setCellValue("");
		   		row.getCell(cellindex++).setCellStyle(rowCellStyle);
		   		//
		   		if (patrimony.getAcquisitionMethod() != null)
		   			row.createCell(cellindex).setCellValue(patrimony.getAcquisitionMethod().getDescription());
		   		else
		   			row.createCell(cellindex).setCellValue("");
		   		row.getCell(cellindex++).setCellStyle(rowCellStyle);
			   
		   		row.setHeight((short)-1);
	   		}

			   
		   	for (int i = 0; i < COLUMNs.length; i++) {
		   		sheet.setColumnWidth(i, pixel2WidthUnits(WIDTHs[i])*10);
		   	}
		   	
		   	workbook.write(out);
		   	return new ByteArrayInputStream(out.toByteArray());
	   	}
	}	
}