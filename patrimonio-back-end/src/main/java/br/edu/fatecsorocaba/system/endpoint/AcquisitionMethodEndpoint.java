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
import br.edu.fatecsorocaba.system.model.AcquisitionMethod;
import br.edu.fatecsorocaba.system.repository.AcquisitionMethodRepository;

@RestController
@RequestMapping("acquisitionmethods")
public class AcquisitionMethodEndpoint {

	@Autowired
	private AcquisitionMethodRepository repository;

	@GetMapping
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> save(@RequestBody AcquisitionMethod acquisitionMethod) {
			acquisitionMethod = repository.save(acquisitionMethod);
			return new ResponseEntity<>(acquisitionMethod, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable("id") int id){
		verifyIfacquisitionMethodExists(id);
		Optional<AcquisitionMethod> acquisitionMethod = repository.findById(id);
		return new ResponseEntity<>(acquisitionMethod, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") int id){
		verifyIfacquisitionMethodExists(id);
		repository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PutMapping
	public ResponseEntity<?> update(@RequestBody AcquisitionMethod acquisitionMethod){
		verifyIfacquisitionMethodExists(acquisitionMethod.getAcquisitionMethodId());
		repository.save(acquisitionMethod);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	public void verifyIfacquisitionMethodExists(int id) {
		if(!repository.findById(id).isPresent())
			throw new ResourceNotFoundException("acquisitionMethod with ID " + id + " not found.");
	}

}
