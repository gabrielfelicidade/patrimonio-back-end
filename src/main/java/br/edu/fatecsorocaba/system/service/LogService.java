package br.edu.fatecsorocaba.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.edu.fatecsorocaba.system.config.CustomUserDetails;
import br.edu.fatecsorocaba.system.model.Log;
import br.edu.fatecsorocaba.system.repository.LogRepository;

@Component
public class LogService {
	private final LogRepository repository;
		
	@Autowired
	public LogService(LogRepository repository) {
		this.repository = repository;
	}

	public void saveLog(String tablename, String action, CustomUserDetails customUserDetails) {
		Log log = new Log(tablename, action, "Nome: " + customUserDetails.getName() + " Usuário: " + customUserDetails.getUsername());
		repository.save(log);
	}
}
