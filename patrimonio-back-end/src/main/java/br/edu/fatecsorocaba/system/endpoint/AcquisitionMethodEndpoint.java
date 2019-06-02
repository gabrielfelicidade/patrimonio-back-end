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
import br.edu.fatecsorocaba.system.model.AcquisitionMethod;
import br.edu.fatecsorocaba.system.repository.AcquisitionMethodRepository;
import br.edu.fatecsorocaba.system.service.LogService;
import br.edu.fatecsorocaba.system.validationInterfaces.OnCreate;
import br.edu.fatecsorocaba.system.validationInterfaces.OnUpdate;

@RestController
@RequestMapping("acquisitionmethods")
public class AcquisitionMethodEndpoint {
	@Autowired
	private AcquisitionMethodRepository repository;
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
		verifyIfacquisitionMethodExists(id);
		return new ResponseEntity<>(repository.findById(id).orElse(null), HttpStatus.OK);
	}
	
	@Transactional
	@PostMapping
	@PreAuthorize("hasRole('INTERMEDIARY')")
	public ResponseEntity<?> save(@Validated(OnCreate.class) @RequestBody AcquisitionMethod acquisitionMethod,
											@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		verifyIfacquisitionMethodExistsPOST(acquisitionMethod.getDescription());
		acquisitionMethod = repository.saveAndFlush(acquisitionMethod);
		logService.saveLog("Cadastro de Métodos de Aquisição", "Inserção, ID: " + acquisitionMethod.getAcquisitionMethodId(),
				customUserDetails);
		return new ResponseEntity<>(acquisitionMethod, HttpStatus.OK);
	}
	
	@Transactional
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('INTERMEDIARY')")
	public ResponseEntity<?> delete(@PathVariable("id") Long id, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
		verifyIfacquisitionMethodExists(id);
		repository.deleteById(id);
		logService.saveLog("Edição de Métodos de Aquisição", "Exclusão, ID: " + id, customUserDetails);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	@Transactional
	@PutMapping
	@PreAuthorize("hasRole('INTERMEDIARY')")
	public ResponseEntity<?> update(@Validated(OnUpdate.class) @RequestBody AcquisitionMethod acquisitionMethod,
									@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		verifyIfacquisitionMethodExistsPUT(acquisitionMethod.getAcquisitionMethodId(), acquisitionMethod.getDescription());
		repository.save(acquisitionMethod);
		logService.saveLog("Edição de Métodos de Aquisição", "Alteração, ID: " + acquisitionMethod.getAcquisitionMethodId(),
				customUserDetails);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	public void verifyIfacquisitionMethodExists(Long id) {
		if (!repository.findById(id).isPresent())
			throw new ResourceNotFoundException("Método de aquisição com o código " + id + " não encontrado.");
	}
	
	public void verifyIfacquisitionMethodExistsPOST(String description) {
		if (repository.findByDescription(description) != null)
			throw new ConstraintViolationException("Método de aquisição com a descrição \"" + description + "\" já existe.", null, "Unique");
	}
	
	public void verifyIfacquisitionMethodExistsPUT(Long id, String description) {
		if (repository.findByAcquisitionMethodIdNotAndDescription(id, description) != null)
			throw new ConstraintViolationException("Já existe um outro método de aquisição com a descrição \"" + description + "\".", null, "Unique");
	}
}
