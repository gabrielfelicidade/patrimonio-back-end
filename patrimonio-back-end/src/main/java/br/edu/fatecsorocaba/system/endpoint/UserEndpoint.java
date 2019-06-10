package br.edu.fatecsorocaba.system.endpoint;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

import br.edu.fatecsorocaba.system.config.CustomUserDetails;
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
	@Autowired
	private LogService logService;
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
			@AuthenticationPrincipal CustomUserDetails customCustomUserDetails) {
		verifyIfuserExistsPOST(user.getUsername());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user = repository.saveAndFlush(user);
		logService.saveLog("Cadastro de Usuários", "Inserção, ID: " + user.getUserId(), customCustomUserDetails);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@Transactional
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> delete(@PathVariable("id") Long id, 
			@AuthenticationPrincipal CustomUserDetails customCustomUserDetails) {
		verifyIfuserExists(id);
		repository.deleteById(id);
		logService.saveLog("Edição de Usuários", "Exclusão, ID: " + id, customCustomUserDetails);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@Transactional
	@PutMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> update(@Validated(OnUpdate.class) @RequestBody User user, 
			@AuthenticationPrincipal CustomUserDetails customCustomUserDetails) {
		verifyIfuserExistsPUT(user.getUserId(), user.getUsername());
		User userBase = repository.findById(user.getUserId()).orElse(null);
		if (user.getPassword() != null)
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		else
			user.setPassword(userBase.getPassword());
		repository.save(user);
		logService.saveLog("Edição de Usuários", "Alteração, ID: " + user.getUserId(),
				customCustomUserDetails);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	@Transactional
	@PutMapping("/changePassword")
	@PreAuthorize("hasRole('BASIC')")
	public ResponseEntity<?> changePassword(@Validated(OnChangePassword.class) @RequestBody User user, 
			@AuthenticationPrincipal CustomUserDetails customCustomUserDetails) {
		String password = user.getPassword();
		user = repository.findByUsername(customCustomUserDetails.getUsername());
		user.setPassword(passwordEncoder.encode(password));
		repository.save(user);
		logService.saveLog("Alteração de Senha", "Alteração de Senha",
				customCustomUserDetails);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	public void verifyIfuserExists(Long id) {
		if (!repository.findById(id).isPresent())
			throw new ResourceNotFoundException("Usuário com o código " + id + " não encontrado.");
	}
	
	public void verifyIfuserExistsPOST(String username) {
		if (repository.findByUsername(username) != null)
			throw new ConstraintViolationException("Usuário \"" + username + "\" já existe.", null, "Unique");
	}
	
	public void verifyIfuserExistsPUT(Long id, String username) {
		if (repository.findByUserIdNotAndUsername(id, username) != null)
			throw new ConstraintViolationException("Já existe um usuário \"" + username + "\".", null, "Unique");
	}
}