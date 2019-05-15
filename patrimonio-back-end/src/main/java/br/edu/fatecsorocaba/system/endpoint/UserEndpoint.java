package br.edu.fatecsorocaba.system.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

import br.edu.fatecsorocaba.system.error.ResourceNotFoundException;
import br.edu.fatecsorocaba.system.model.User;
import br.edu.fatecsorocaba.system.repository.UserRepository;
import br.edu.fatecsorocaba.system.service.LogService;
import br.edu.fatecsorocaba.system.validationInterfaces.OnChangePassword;
import br.edu.fatecsorocaba.system.validationInterfaces.OnCreate;
import br.edu.fatecsorocaba.system.validationInterfaces.OnUpdate;

@RestController
@RequestMapping("users")
public class UserEndpoint {
	@Autowired
	private UserRepository repository;
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	@Autowired
	private LogService logService;

	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		verifyIfuserExists(id);
		return new ResponseEntity<>(repository.findById(id).orElse(null), HttpStatus.OK);
	}

	@Transactional
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> save(@Validated(OnCreate.class) @RequestBody User user, 
			@AuthenticationPrincipal UserDetails userDetail) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user = repository.save(user);
		logService.saveLog("Usuário", "Inserção", userDetail.getUsername());
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@Transactional
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> delete(@PathVariable("id") Long id, 
			@AuthenticationPrincipal UserDetails userDetail) {
		verifyIfuserExists(id);
		repository.deleteById(id);
		logService.saveLog("Usuário", "Exclusão, ID: " + id, userDetail.getUsername());
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@Transactional
	@PutMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> update(@Validated(OnUpdate.class) @RequestBody User user, 
			@AuthenticationPrincipal UserDetails userDetail) {
		verifyIfuserExists(user.getUserId());
		if (user.getPassword() != null)
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		repository.save(user);
		logService.saveLog("Usuário", "Alteração, ID: " + user.getUserId(),
				userDetail.getUsername());
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	@Transactional
	@PutMapping("/changePassword")
	@PreAuthorize("hasRole('BASIC')")
	public ResponseEntity<?> changePassword(@Validated(OnChangePassword.class) @RequestBody User user, 
			@AuthenticationPrincipal UserDetails userDetail) {
		String password = user.getPassword();
		user = repository.findByUsername(userDetail.getUsername());
		user.setPassword(passwordEncoder.encode(password));
		repository.save(user);
		logService.saveLog("Usuário", "Alteração de Senha",
				userDetail.getUsername());
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	public void verifyIfuserExists(Long id) {
		if (!repository.findById(id).isPresent())
			throw new ResourceNotFoundException("User with ID " + id + " not found.");
	}
}