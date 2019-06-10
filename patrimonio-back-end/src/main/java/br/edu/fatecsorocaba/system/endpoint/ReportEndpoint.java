package br.edu.fatecsorocaba.system.endpoint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.fatecsorocaba.system.repository.LocationRepository;
import br.edu.fatecsorocaba.system.repository.PatrimonyRepository;
import br.edu.fatecsorocaba.system.service.LocationService;
import br.edu.fatecsorocaba.system.service.PatrimonyService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@RestController
@RequestMapping("reports")
public class ReportEndpoint {

	@Autowired
	private LocationRepository locationRepository;
	@Autowired
	private PatrimonyRepository patrimonyRepository;
	private LocationService locationService = new LocationService();
	private PatrimonyService patrimonyService = new PatrimonyService();

	@GetMapping("LocationsPatrimonies")
	public ResponseEntity<?> getLocationsPatrimoniesReport() {

		try {
			InputStream jrxmlInput = this.getClass().getClassLoader()
					.getResourceAsStream("reports/LocationsPatrimonies.jrxml");
			JasperDesign design = JRXmlLoader.load(jrxmlInput);
			JasperReport jasperReport = JasperCompileManager.compileReport(design);
			List<Map<String, Object>> lista = locationService
					.getLocationsPatrimoniesReportData(this.locationRepository);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(),
					new JRBeanCollectionDataSource(lista));
			JRPdfExporter pdfExporter = new JRPdfExporter();
			pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			ByteArrayOutputStream pdfReportStream = new ByteArrayOutputStream();
			pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfReportStream));
			pdfExporter.exportReport();

			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "attachment; filename=report.pdf");

			pdfReportStream.close();

			return ResponseEntity.ok().headers(headers).body(pdfReportStream.toByteArray());
		} catch (IOException | JRException e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@GetMapping("WritedOffByYearAndMonth/{year}/{month}")
	public ResponseEntity<?> getWritedOffByYearAndMonth(@PathVariable("year") int year,
			@PathVariable("month") int month) {
		try {
			InputStream jrxmlInput = this.getClass().getClassLoader()
					.getResourceAsStream("reports/PatrimoniesWritedOffByDate.jrxml");
			JasperDesign design = JRXmlLoader.load(jrxmlInput);
			JasperReport jasperReport = JasperCompileManager.compileReport(design);
			List<Map<String, Object>> lista = patrimonyService
					.getPatrimoniesWritedOffByDateReportData(this.patrimonyRepository, year, month);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("year", year);
			params.put("month", month);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params,
					new JRBeanCollectionDataSource(lista));
			JRPdfExporter pdfExporter = new JRPdfExporter();
			pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			ByteArrayOutputStream pdfReportStream = new ByteArrayOutputStream();
			pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfReportStream));
			pdfExporter.exportReport();

			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "attachment; filename=report.pdf");

			pdfReportStream.close();

			return ResponseEntity.ok().headers(headers).body(pdfReportStream.toByteArray());
		} catch (IOException | JRException e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>(null, HttpStatus.OK);
	}

}
