package br.edu.fatecsorocaba.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import br.edu.fatecsorocaba.system.model.Log;
import br.edu.fatecsorocaba.system.model.User;
import br.edu.fatecsorocaba.system.repository.LogRepository;
import br.edu.fatecsorocaba.system.repository.UserRepository;

@Component
public class LogService {
	private final LogRepository repository;
	private final UserRepository userRepository;
		
	@Autowired
	public LogService(LogRepository repository, UserRepository userRepository) {
		this.repository = repository;
		this.userRepository = userRepository;
	}

	public void saveLog(String tablename, String action, String username) {
		User user = userRepository.findByUsername(username);
		Log log = new Log(tablename, action, "Nome: " + user.getName() + " Usuário: " + user.getUsername());
		repository.save(log);
	}
}
