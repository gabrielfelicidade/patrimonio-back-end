package br.edu.fatecsorocaba.system.endpoint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.fatecsorocaba.system.config.CustomUserDetails;
import br.edu.fatecsorocaba.system.error.ResourceNotFoundException;
import br.edu.fatecsorocaba.system.model.Location;
import br.edu.fatecsorocaba.system.validationInterfaces.OnCreate;
import br.edu.fatecsorocaba.system.validationInterfaces.OnUpdate;
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
import br.edu.fatecsorocaba.system.repository.LocationRepository;
import br.edu.fatecsorocaba.system.service.LocationService;
import br.edu.fatecsorocaba.system.service.LogService;

@RestController
@RequestMapping("locations")
public class LocationEndpoint {
	@Autowired
	private LocationRepository repository;
	@Autowired
	private LogService logService;
	private LocationService service = new LocationService();
	
	@GetMapping
	@PreAuthorize("hasRole('BASIC')")
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('BASIC')")
	public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		verifyIfLocationExists(id);
		return new ResponseEntity<>(repository.findById(id).orElse(null), HttpStatus.OK);
	}
	
	@GetMapping("/report")
	public ResponseEntity<?> getLocationsPatrimoniesReport() {

		try {
			InputStream jrxmlInput = this.getClass().getClassLoader().getResourceAsStream("reports/LocationsPatrimonies.jrxml");
			JasperDesign design = JRXmlLoader.load(jrxmlInput);
			JasperReport jasperReport = JasperCompileManager.compileReport(design);
			List<Map<String, Object>> lista = service.getLocationsPatrimoniesReport(this.repository);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), new JRBeanCollectionDataSource(lista));
			JRPdfExporter pdfExporter = new JRPdfExporter();
			pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			ByteArrayOutputStream pdfReportStream = new ByteArrayOutputStream();
			pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfReportStream));
			pdfExporter.exportReport();
			
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "attachment; filename=report.pdf");
			
			pdfReportStream.close();
			
			return ResponseEntity
	                .ok()
	                .headers(headers)
	                .body(pdfReportStream.toByteArray());
		} catch (IOException | JRException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@Transactional
	@PostMapping
	@PreAuthorize("hasRole('INTERMEDIARY')")
	public ResponseEntity<?> save(@Validated(OnCreate.class) @RequestBody Location location,
								  @AuthenticationPrincipal CustomUserDetails userDetails) {
		verifyIfLocationExistsPOST(location.getDescription());
		location = repository.saveAndFlush(location);
		logService.saveLog("Cadastro de Localizações", "Inserção, ID: " + location.getLocationId(), userDetails);
		return new ResponseEntity<>(location, HttpStatus.OK);
	}

	@Transactional
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('INTERMEDIARY')")
	public ResponseEntity<?> delete(@PathVariable("id") Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
		verifyIfLocationExists(id);
		repository.deleteById(id);
		logService.saveLog("Edição de Localizações", "Exclusão, ID: " + id, userDetails);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@Transactional
	@PutMapping
	@PreAuthorize("hasRole('INTERMEDIARY')")
	public ResponseEntity<?> update(@Validated(OnUpdate.class) @RequestBody Location location, 
									@AuthenticationPrincipal CustomUserDetails userDetails) {
		verifyIfLocationExistsPUT(location.getLocationId(), location.getDescription());
		repository.save(location);
		logService.saveLog("Edição de Localizações", "Alteração, ID: " + location.getLocationId(), userDetails);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	public void verifyIfLocationExists(Long id) {
		if (!repository.findById(id).isPresent())
			throw new ResourceNotFoundException("Localização com o código " + id + " não encontrado.");
	}
	
	public void verifyIfLocationExistsPOST(String description) {
		if (repository.findByDescription(description) != null)
			throw new ConstraintViolationException("Localização com a descrição \"" + description + "\" já existe.", null, "Unique");
	}
	
	public void verifyIfLocationExistsPUT(Long id, String description) {
		if (repository.findByLocationIdNotAndDescription(id, description) != null)
			throw new ConstraintViolationException("Já existe uma outra localização com a descrição \"" + description + "\".", null, "Unique");
	}
}
