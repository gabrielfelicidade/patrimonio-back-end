package br.edu.fatecsorocaba.system.endpoint;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
		try {
			location = repository.save(location);
			return new ResponseEntity<>(location, HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.OK);
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> geById(@PathVariable("id") int id){
		try {
			Optional<Location> location = repository.findById(id);
			if(location.isPresent()) {
				return new ResponseEntity<>(location, HttpStatus.OK);
			}else {
				return new ResponseEntity<>(location, HttpStatus.NOT_FOUND);
			}
		} catch (Exception ex) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.OK);
		}
	}

}
