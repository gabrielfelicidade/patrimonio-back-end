package br.edu.fatecsorocaba.system.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.fatecsorocaba.system.error.ResourceNotFoundException;
import br.edu.fatecsorocaba.system.model.Patrimony;
import br.edu.fatecsorocaba.system.repository.PatrimonyRepository;
import br.edu.fatecsorocaba.system.util.ExcelGenerator;
import br.edu.fatecsorocaba.system.validationInterfaces.OnCreate;
import br.edu.fatecsorocaba.system.validationInterfaces.OnUpdate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("patrimonies")
public class PatrimonyEndpoint {
	@Autowired
	private PatrimonyRepository repository;
	
	@GetMapping
	@PreAuthorize("hasRole('SEARCH')")
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('SEARCH')")
	public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		verifyIfpatrinomyExists(id);
		return new ResponseEntity<>(repository.findById(id).orElse(null), HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> save(@Validated(OnCreate.class) @RequestBody Patrimony patrinomy) {
		return new ResponseEntity<>(repository.save(patrinomy), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		verifyIfpatrinomyExists(id);
		repository.deleteById(id);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	@PutMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> update(@Validated(OnUpdate.class) @RequestBody Patrimony patrinomy) {
		if (patrinomy.getPatrimonyId() == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		verifyIfpatrinomyExists(patrinomy.getPatrimonyId());
		repository.save(patrinomy);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
//	@PostMapping("/writeOff")
//	@PreAuthorize("hasRole('USER')")
//	public ResponseEntity<?> writeOff(@Validated(OnCreate.class) @RequestBody Patrimony patrinomy) {
//		return new ResponseEntity<>(repository.save(patrinomy), HttpStatus.OK);
//	}
//	
	@GetMapping("/export/patrimonies.xlsx")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<InputStreamResource> excelCustomersReport() throws IOException {
		List<Patrimony> patrimonies = repository.findAll();
    
		ByteArrayInputStream in = ExcelGenerator.patrimoniesToExcel(patrimonies);
    
		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=patrimonios.xlsx");
    
        return ResponseEntity
                  .ok()
                  .headers(headers)
                  .body(new InputStreamResource(in));
	}

	public void verifyIfpatrinomyExists(Long id) {
		if (!repository.findById(id).isPresent())
			throw new ResourceNotFoundException("Patrinomy with ID " + id + " not found.");
	}
}
