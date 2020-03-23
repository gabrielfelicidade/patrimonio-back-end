package br.edu.fatecsorocaba.system.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.fatecsorocaba.system.error.ResourceNotFoundException;
import br.edu.fatecsorocaba.system.repository.LogRepository;

@RestController
@RequestMapping("logs")
public class LogEndpoint {
	@Autowired
	private LogRepository repository;

	@GetMapping
	@PreAuthorize("hasRole('BASIC')")
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('BASIC')")
	public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		verifyIflogExists(id);
		return new ResponseEntity<>(repository.findById(id).orElse(null), HttpStatus.OK);
	}
	
	public void verifyIflogExists(Long id) {
		if (!repository.findById(id).isPresent())
			throw new ResourceNotFoundException("Log with ID " + id + " not found.");
	}
}
