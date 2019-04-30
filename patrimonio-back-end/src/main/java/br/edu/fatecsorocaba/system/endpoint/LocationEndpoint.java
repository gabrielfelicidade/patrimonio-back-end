package br.edu.fatecsorocaba.system.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.fatecsorocaba.system.error.ResourceNotFoundException;
import br.edu.fatecsorocaba.system.model.Location;
import br.edu.fatecsorocaba.system.validationInterfaces.OnCreate;
import br.edu.fatecsorocaba.system.validationInterfaces.OnUpdate;
import br.edu.fatecsorocaba.system.repository.LocationRepository;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("locations")
public class LocationEndpoint {
	@Autowired
	private LocationRepository repository;
	
	@GetMapping
	@PreAuthorize("hasRole('SEARCH')")
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('SEARCH')")
	public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		verifyIfLocationExists(id);
		return new ResponseEntity<>(repository.findById(id).orElse(null), HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> save(@Validated(OnCreate.class) @RequestBody Location location) {
		return new ResponseEntity<>(repository.save(location), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		verifyIfLocationExists(id);
		repository.deleteById(id);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@PutMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> update(@Validated(OnUpdate.class) @RequestBody Location location) {
		verifyIfLocationExists(location.getLocationId());
		repository.save(location);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	public void verifyIfLocationExists(Long id) {
		if (!repository.findById(id).isPresent())
			throw new ResourceNotFoundException("Location with ID " + id + " not found.");
	}
}
