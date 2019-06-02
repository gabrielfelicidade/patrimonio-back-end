package br.edu.fatecsorocaba.system.endpoint;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import br.edu.fatecsorocaba.system.model.Location;
import br.edu.fatecsorocaba.system.validationInterfaces.OnCreate;
import br.edu.fatecsorocaba.system.validationInterfaces.OnUpdate;
import br.edu.fatecsorocaba.system.repository.LocationRepository;
import br.edu.fatecsorocaba.system.service.LogService;

@RestController
@RequestMapping("locations")
public class LocationEndpoint {
	@Autowired
	private LocationRepository repository;
	@Autowired
	private LogService logService;
	
	@GetMapping
	@PreAuthorize("hasRole('BASIC')")
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('BASIC')")
	public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		verifyIfLocationExists(id);
		return new ResponseEntity<>(repository.findById(id).orElse(null), HttpStatus.OK);
	}

	@Transactional
	@PostMapping
	@PreAuthorize("hasRole('INTERMEDIARY')")
	public ResponseEntity<?> save(@Validated(OnCreate.class) @RequestBody Location location,
								  @AuthenticationPrincipal CustomUserDetails userDetails) {
		verifyIfLocationExistsPOST(location.getDescription());
		location = repository.saveAndFlush(location);
		logService.saveLog("Cadastro de Localizações", "Inserção, ID: " + location.getLocationId(), userDetails);
		return new ResponseEntity<>(location, HttpStatus.OK);
	}

	@Transactional
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('INTERMEDIARY')")
	public ResponseEntity<?> delete(@PathVariable("id") Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
		verifyIfLocationExists(id);
		repository.deleteById(id);
		logService.saveLog("Edição de Localizações", "Exclusão, ID: " + id, userDetails);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@Transactional
	@PutMapping
	@PreAuthorize("hasRole('INTERMEDIARY')")
	public ResponseEntity<?> update(@Validated(OnUpdate.class) @RequestBody Location location, 
									@AuthenticationPrincipal CustomUserDetails userDetails) {
		verifyIfLocationExistsPUT(location.getLocationId(), location.getDescription());
		repository.save(location);
		logService.saveLog("Edição de Localizações", "Alteração, ID: " + location.getLocationId(), userDetails);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	public void verifyIfLocationExists(Long id) {
		if (!repository.findById(id).isPresent())
			throw new ResourceNotFoundException("Localização com o código " + id + " não encontrado.");
	}
	
	public void verifyIfLocationExistsPOST(String description) {
		if (repository.findByDescription(description) != null)
			throw new ConstraintViolationException("Localização com a descrição \"" + description + "\" já existe.", null, "Unique");
	}
	
	public void verifyIfLocationExistsPUT(Long id, String description) {
		if (repository.findByLocationIdNotAndDescription(id, description) != null)
			throw new ConstraintViolationException("Já existe uma outra localização com a descrição \"" + description + "\".", null, "Unique");
	}
}
