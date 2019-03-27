package br.edu.fatecsorocaba.system.endpoint;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import br.edu.fatecsorocaba.system.repository.LocationRepository;

@RestController
@RequestMapping("locations")
public class LocationEndpoint {

	@Autowired
	private LocationRepository repository;

	@GetMapping
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> save(@RequestBody Location location) {
			location = repository.save(location);
			return new ResponseEntity<>(location, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable("id") Long id){
		verifyIfLocationExists(id);
		Optional<Location> location = repository.findById(id);
		return new ResponseEntity<>(location, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id){
		verifyIfLocationExists(id);
		repository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PutMapping
	public ResponseEntity<?> update(@RequestBody Location location){
		verifyIfLocationExists(location.getLocationId());
		repository.save(location);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	public void verifyIfLocationExists(Long id) {
		if(!repository.findById(id).isPresent())
			throw new ResourceNotFoundException("Location with ID " + id + " not found.");
	}

}
