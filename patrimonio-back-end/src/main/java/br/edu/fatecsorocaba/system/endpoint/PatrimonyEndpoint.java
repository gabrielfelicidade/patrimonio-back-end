package br.edu.fatecsorocaba.system.endpoint;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import br.edu.fatecsorocaba.system.model.Patrimony;
import br.edu.fatecsorocaba.system.repository.PatrimonyRepository;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("patrimonies")
public class PatrimonyEndpoint {
	@Autowired
	private PatrimonyRepository repository;
	
	@GetMapping
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		verifyIfpatrinomyExists(id);
		return new ResponseEntity<>(repository.findById(id).orElse(null), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> save(@Valid @RequestBody Patrimony patrinomy) {
		return new ResponseEntity<>(repository.save(patrinomy), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		verifyIfpatrinomyExists(id);
		repository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<?> update(@Valid @RequestBody Patrimony patrinomy) {
		if (patrinomy.getPatrimonyId() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		verifyIfpatrinomyExists(patrinomy.getPatrimonyId());
		repository.save(patrinomy);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	public void verifyIfpatrinomyExists(Long id) {
		if (!repository.findById(id).isPresent())
			throw new ResourceNotFoundException("Patrinomy with ID " + id + " not found.");
	}
}
