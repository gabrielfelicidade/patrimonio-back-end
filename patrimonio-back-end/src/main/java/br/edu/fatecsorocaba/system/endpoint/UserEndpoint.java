package br.edu.fatecsorocaba.system.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import br.edu.fatecsorocaba.system.model.User;
import br.edu.fatecsorocaba.system.repository.UserRepository;
import br.edu.fatecsorocaba.system.validationInterfaces.OnCreate;
import br.edu.fatecsorocaba.system.validationInterfaces.OnUpdate;

@RestController
@RequestMapping("users")
public class UserEndpoint {
	@Autowired
	private UserRepository repository;
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<>(repository.findByUserIdNot(1L), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		verifyIfuserExists(id);
		return new ResponseEntity<>(repository.findById(id).orElse(null), HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> save(@Validated(OnCreate.class) @RequestBody User user, 
			Authentication authentication) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user = repository.save(user);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> delete(@PathVariable("id") Long id, 
			Authentication authentication) {
		verifyIfuserExists(id);
		repository.deleteById(id);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@PutMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> update(@Validated(OnUpdate.class) @RequestBody User user, 
			Authentication authentication) {
		verifyIfuserExists(user.getUserId());
		if (user.getPassword() != null)
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		repository.save(user);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	public void verifyIfuserExists(Long id) {
		if (!repository.findById(id).isPresent() || id==1)
			throw new ResourceNotFoundException("User with ID " + id + " not found.");
	}
}