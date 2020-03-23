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
import br.edu.fatecsorocaba.system.model.Patrimony;
import br.edu.fatecsorocaba.system.repository.AcquisitionMethodRepository;
import br.edu.fatecsorocaba.system.repository.LocationRepository;
import br.edu.fatecsorocaba.system.repository.PatrimonyRepository;
import br.edu.fatecsorocaba.system.service.LogService;
import br.edu.fatecsorocaba.system.util.ExcelGenerator;
import br.edu.fatecsorocaba.system.validationInterfaces.OnCreate;
import br.edu.fatecsorocaba.system.validationInterfaces.OnUpdate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("patrimonies")
public class PatrimonyEndpoint {
	@Autowired
	private PatrimonyRepository repository;
	@Autowired
	private LocationRepository locationRepository;
	@Autowired
	private AcquisitionMethodRepository acquisitionMethodRepository;
	@Autowired
	private LogService logService;

	@GetMapping
	@PreAuthorize("hasRole('BASIC')")
	public ResponseEntity<?> getAll() {
		return new ResponseEntity<>(repository.findByStatusNot(0), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('BASIC')")
	public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		verifyIfpatrinomyExists(id);
		return new ResponseEntity<>(repository.findById(id).orElse(null), HttpStatus.OK);
	}

	@GetMapping("/getByStatus/{status}")
	@PreAuthorize("hasRole('BASIC')")
	public ResponseEntity<?> getAllByStatus(@PathVariable("status") int status) {
		return new ResponseEntity<>(repository.findByStatus(status), HttpStatus.OK);
	}

	@Transactional
	@PostMapping
	@PreAuthorize("hasRole('INTERMEDIARY')")
	public ResponseEntity<?> save(@Validated(OnCreate.class) @RequestBody Patrimony patrinomy,
			@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		verifyIfLocationExistsPOST(patrinomy.getPatrimonyId());
		patrinomy = repository.saveAndFlush(patrinomy);
		logService.saveLog("Cadastro de Patrimônios", "Inserção, ID: " + patrinomy.getPatrimonyId(), customUserDetails);
		return new ResponseEntity<>(patrinomy, HttpStatus.OK);
	}

	@Transactional
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('INTERMEDIARY')")
	public ResponseEntity<?> delete(@PathVariable("id") Long id,
			@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		verifyIfpatrinomyExists(id);
		repository.deleteById(id);
		logService.saveLog("Edição de Patrimônios", "Exclusão, ID: " + id, customUserDetails);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@Transactional
	@PutMapping
	@PreAuthorize("hasRole('INTERMEDIARY')")
	public ResponseEntity<?> update(@Validated(OnUpdate.class) @RequestBody Patrimony patrinomy,
			@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		verifyIfpatrinomyExists(patrinomy.getPatrimonyId());
		verifyIfEntitysExists(patrinomy);
		repository.save(patrinomy);
		logService.saveLog("Edição de Patrimônios", "Alteração, ID: " + patrinomy.getPatrimonyId(), customUserDetails);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@Transactional
	@PostMapping("/export")
	@PreAuthorize("hasRole('INTERMEDIARY')")
	public ResponseEntity<InputStreamResource> exportPatrimonies(
			@Validated(OnUpdate.class) @RequestBody List<Patrimony> patrimonies,
			@AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
		for (Patrimony patrimony : patrimonies) {
			verifyIfpatrinomyExists(patrimony.getPatrimonyId());
			verifyIfEntitysExists(patrimony);
			patrimony.setStatus(1);
			patrimony.getLocation();
			repository.save(patrimony);
		}

		ByteArrayInputStream in = new ExcelGenerator().patrimoniesToExcel(patrimonies);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=patrimonios.xlsx");

		logService.saveLog("Baixa de Patrimônios",
				"Geração de Excel/Processo Inicial de Baixa. Nº Itens: " + patrimonies.size(), customUserDetails);
		return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
	}

	@Transactional
	@PostMapping("/cancelWriteOff")
	@PreAuthorize("hasRole('INTERMEDIARY')")
	public ResponseEntity<?> cancelWriteOff(@Validated(OnUpdate.class) @RequestBody List<Patrimony> patrimonies,
			@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		for (Patrimony patrimony : patrimonies) {
			verifyIfpatrinomyExists(patrimony.getPatrimonyId());
			verifyIfEntitysExists(patrimony);
			patrimony.setStatus(2);
			repository.save(patrimony);
		}
		logService.saveLog("Baixa de Patrimônios",
				"Cancelamento de Baixa de Itens em Processo. Nº Itens: " + patrimonies.size(), customUserDetails);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@Transactional
	@PostMapping("/writeOff")
	@PreAuthorize("hasRole('INTERMEDIARY')")
	public ResponseEntity<?> writeOff(@Validated(OnUpdate.class) @RequestBody List<Patrimony> patrimonies,
			@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		for (Patrimony patrimony : patrimonies) {
			verifyIfpatrinomyExists(patrimony.getPatrimonyId());
			verifyIfEntitysExists(patrimony);
			patrimony.setStatus(0);
			patrimony.setWriteOffDate(Date.from(Instant.now()));
			repository.save(patrimony);
		}
		logService.saveLog("Baixa de Patrimônios",
				"Baixa Patrimônial/Processo Final de Baixa. Nº Itens: " + patrimonies.size(), customUserDetails);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	public void verifyIfpatrinomyExists(Long id) {
		if (!repository.findById(id).isPresent())
			throw new ResourceNotFoundException("Patrimônio com o número \"" + id + "\" não encontrado.");
	}

	public void verifyIfLocationExistsPOST(Long id) {
		if (repository.findById(id).isPresent())
			throw new ConstraintViolationException("Patrimônio de número \"" + id + "\" já existe.", null, "Unique");
	}

	public void verifyIfEntitysExists(Patrimony patrimony) {
		String message = "";
		try {
			patrimony.setAcquisitionMethod(acquisitionMethodRepository
					.findById(patrimony.getAcquisitionMethod().getAcquisitionMethodId()).orElseThrow(null));
		} catch (Exception e) {
			message += "O método de aquisição informado, com o código "
					+ patrimony.getAcquisitionMethod().getAcquisitionMethodId() + " não foi encontrado. ";
		}
		try {
			patrimony.setLocation(
					locationRepository.findById(patrimony.getLocation().getLocationId()).orElseThrow(null));
		} catch (Exception e) {
			message += "A localização informada, com o código " + patrimony.getLocation().getLocationId()
					+ " não foi encontrada.";
		}
		if (message.length() > 0) {
			throw new EntityNotFoundException(message);
		}
	}
}
