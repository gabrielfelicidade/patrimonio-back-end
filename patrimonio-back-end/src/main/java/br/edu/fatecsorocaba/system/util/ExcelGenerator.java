package br.edu.fatecsorocaba.system.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.CellCopyPolicy;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.ResourceUtils;

import br.edu.fatecsorocaba.system.model.Patrimony;

public class ExcelGenerator {

	public static ByteArrayInputStream patrimoniesToExcel(List<Patrimony> patrimonies) throws IOException {
        File file = ResourceUtils.getFile("classpath:templates\\template.xlsx");
        try(XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file.getAbsolutePath())); ByteArrayOutputStream out = new ByteArrayOutputStream();){
        	XSSFSheet sheet = workbook.getSheetAt(0);
        	int rowIndex = 2;
		   	for (Patrimony patrimony : patrimonies) {
		   		int cellindex = 0;
	        	sheet.copyRows(rowIndex, rowIndex, rowIndex+1, new CellCopyPolicy());
		   		Row row = sheet.getRow(rowIndex++);
		   		
		   		if (patrimony.getLocation() != null) 
		   			row.getCell(cellindex).setCellValue(patrimony.getLocation().getDescription());
		   		cellindex++;
		   		//
		   		row.getCell(cellindex++).setCellValue(patrimony.getPatrimonyId());
		   		//
		   		if (patrimony.getAcquisitionProcessId() != null)
		   			row.getCell(cellindex).setCellValue(patrimony.getAcquisitionProcessId());
		   		cellindex++;
		   		//
		   		if (patrimony.getDescription() != null)
		   			row.getCell(cellindex).setCellValue(patrimony.getDescription());
		   		cellindex++;
		   		//
		   		if (patrimony.getCommercialInvoice() != null)
		   			row.getCell(cellindex).setCellValue(patrimony.getCommercialInvoice());
		   		cellindex++;
		   		//
		   		if (patrimony.getBrand() != null)
		   			row.getCell(cellindex).setCellValue(patrimony.getBrand());
		   		cellindex++;
		   		//
		   		if (patrimony.getModel() != null)
		   			row.getCell(cellindex).setCellValue(patrimony.getModel());
		   		cellindex++;
		   		//
		   		if (patrimony.getSerialNumber() != null)
		   			row.getCell(cellindex).setCellValue(patrimony.getSerialNumber());
		   		cellindex++;
		   		//
		   		if (patrimony.getAdditionalInformation() != null)
		   			row.getCell(cellindex).setCellValue(patrimony.getAdditionalInformation());
		   		cellindex++;
		   		//
		   		if (patrimony.getValue() != null)
		   			row.getCell(cellindex).setCellValue(patrimony.getValue().doubleValue());
		   		cellindex++;
		   		//
		   		if (patrimony.getAcquisitionMethod() != null)
		   			row.getCell(cellindex).setCellValue(patrimony.getAcquisitionMethod().getDescription());
		   		cellindex++;
			   
		   		row.setHeight((short)-1);
	   		}
		   	sheet.removeRow(sheet.getRow(rowIndex));
		    workbook.write(out);
		   	return new ByteArrayInputStream(out.toByteArray());
		}
	}
}