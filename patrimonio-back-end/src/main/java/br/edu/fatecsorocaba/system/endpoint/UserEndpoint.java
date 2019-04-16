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
import br.edu.fatecsorocaba.system.model.User;
import br.edu.fatecsorocaba.system.repository.UserRepository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("users")
public class UserEndpoint {
	@Autowired
	private UserRepository repository;

	@GetMapping
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		verifyIfuserExists(id);
		return new ResponseEntity<>(repository.findById(id).orElse(null), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> save(@Valid @RequestBody User user) {
		user = repository.save(user);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		verifyIfuserExists(id);
		repository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<?> update(@Valid @RequestBody User user) {
		//Antes verificar se o Id não é nulo.
		verifyIfuserExists(user.getUserId());
		repository.save(user);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	public void verifyIfuserExists(Long id) {
		if (!repository.findById(id).isPresent())
			throw new ResourceNotFoundException("User with ID " + id + " not found.");
	}

}
