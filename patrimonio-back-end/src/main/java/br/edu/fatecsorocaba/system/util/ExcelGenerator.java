package br.edu.fatecsorocaba.system.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.edu.fatecsorocaba.system.model.Patrimony;

public class ExcelGenerator {
 
 public static ByteArrayInputStream patrimoniesToExcel(List<Patrimony> patrimonies) throws IOException {
	   String[] COLUMNs = {"Localização", "Número de Patrimônio", "Codigo do Processo de Aquisição", "Item", "Nota Fiscal",
			   "Marca", "Modelo", "Número de Série", "Informações Complementares", "Valor",
			   "Modalidade de Aquisição"};
	   try(
			   Workbook workbook = new XSSFWorkbook();
			   ByteArrayOutputStream out = new ByteArrayOutputStream();
	   ){
		   CreationHelper createHelper = workbook.getCreationHelper();
	  
		   Sheet sheet = workbook.createSheet("patrimonios");
	  
		   Font headerFont = workbook.createFont();
		   headerFont.setBold(true);
		   headerFont.setColor(IndexedColors.BLACK.getIndex());
		   headerFont.setBold(true);
	  
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
	  
		   // Row for Header
		   Row headerRow = sheet.createRow(0);
	  
		   // Header
		   for (int col = 0; col < COLUMNs.length; col++) {
			   Cell cell = headerRow.createCell(col);
			   cell.setCellValue(COLUMNs[col]);
			   cell.setCellStyle(headerCellStyle);
		   }
	  
		   int rowIdx = 1;
		   int cellindex = 0;
		   for (Patrimony patrimony : patrimonies) {
			   Row row = sheet.createRow(rowIdx++);
			   
			   if (patrimony.getLocation() != null)
				   row.createCell(cellindex++).setCellValue(patrimony.getLocation().getDescription());
			   else
				   row.createCell(cellindex++).setCellValue("");
			   //
			   row.createCell(cellindex++).setCellValue(patrimony.getPatrimonyId());
			   //
			   if (patrimony.getAcquisitionProcessId() != null)
				   row.createCell(cellindex++).setCellValue(patrimony.getAcquisitionProcessId());
			   else
				   row.createCell(cellindex++).setCellValue("");
			   //
			   if (patrimony.getDescription() != null)
				   row.createCell(cellindex++).setCellValue(patrimony.getDescription());
			   else
				   row.createCell(cellindex++).setCellValue("");
			   //
			   if (patrimony.getCommercialInvoice() != null)
				   row.createCell(cellindex++).setCellValue(patrimony.getCommercialInvoice());
			   else
				   row.createCell(cellindex++).setCellValue("");
			   //
			   if (patrimony.getBrand() != null)
				   row.createCell(cellindex++).setCellValue(patrimony.getBrand());
			   else
				   row.createCell(cellindex++).setCellValue("");
			   //
			   if (patrimony.getModel() != null)
				   row.createCell(cellindex++).setCellValue(patrimony.getModel());
			   else
				   row.createCell(cellindex++).setCellValue("");
			   //
			   if (patrimony.getSerialNumber() != null)
				   row.createCell(cellindex++).setCellValue(patrimony.getSerialNumber());
			   else
				   row.createCell(cellindex++).setCellValue("");
			   //
			   if (patrimony.getAdditionalInformation() != null)
				   row.createCell(cellindex++).setCellValue(patrimony.getAdditionalInformation());
			   else
				   row.createCell(cellindex++).setCellValue("");
			   //
			   if (patrimony.getValue() != null)
				   row.createCell(cellindex++).setCellValue(patrimony.getValue().toString());
			   else
				   row.createCell(cellindex++).setCellValue("");
			   //
			   if (patrimony.getAcquisitionMethod() != null)
				   row.createCell(cellindex++).setCellValue(patrimony.getAcquisitionMethod().getDescription());
			   else
				   row.createCell(cellindex++).setCellValue("");
		   }
		   for (int i = 0; i < cellindex; i++) {
			   sheet.autoSizeColumn(i);
		   }
	  
		   workbook.write(out);
		   return new ByteArrayInputStream(out.toByteArray());
	   	}
	}	
}